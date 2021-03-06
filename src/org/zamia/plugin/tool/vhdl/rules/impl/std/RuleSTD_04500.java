package org.zamia.plugin.tool.vhdl.rules.impl.std;

import java.util.List;

import org.w3c.dom.Element;
import org.zamia.SourceLocation;
import org.zamia.ZamiaProject;
import org.zamia.plugin.tool.vhdl.ClockSource;
import org.zamia.plugin.tool.vhdl.EntityException;
import org.zamia.plugin.tool.vhdl.ListClockSource;
import org.zamia.plugin.tool.vhdl.ReportFile;
import org.zamia.plugin.tool.vhdl.manager.ClockSignalSourceManager;
import org.zamia.plugin.tool.vhdl.rules.RuleE;
import org.zamia.plugin.tool.vhdl.rules.RuleResult;
import org.zamia.plugin.tool.vhdl.rules.impl.Rule;
import org.zamia.plugin.tool.vhdl.rules.impl.RuleManager;
import org.zamia.plugin.tool.vhdl.rules.impl.SonarQubeRule;
import org.zamia.util.Pair;
import org.zamia.vhdl.ast.VHDLNode;

/*
 * Clock Reassignment.
 * Do not reassign a clock in a concurrent statement.
 * No parameters.
 */
public class RuleSTD_04500 extends Rule {

	public RuleSTD_04500() {
		super(RuleE.STD_04500);
	}
	
	public Pair<Integer, RuleResult> Launch(ZamiaProject zPrj, String ruleId, ParameterSource parameterSource) {

		initializeRule(parameterSource, ruleId);
		
		//// Make the clock source list.
		
		ListClockSource listClockSource;
		try {
			listClockSource = ClockSignalSourceManager.getClockSourceSignal();
		} catch (EntityException e) {
			LogNeedBuild();
			return new Pair<Integer, RuleResult> (RuleManager.NO_BUILD, null);
		}

		//// Write report
		
		Pair<Integer, RuleResult> result = null;
		
		ReportFile reportFile = new ReportFile(this);
		if (reportFile.initialize()) {
			for (ClockSource clockSource : listClockSource.getListClockSource()) {
				if (clockSource.checkAffectation()) {
					String entityId = clockSource.getEntity();
					String architectureId = clockSource.getArchitecture();
					SourceLocation location = clockSource.getSignalDeclaration().getLocation();
					Element info = reportFile.addViolation(location, entityId, architectureId);
					
					String sourceTag = clockSource.getTag();
					reportFile.addElement(ReportFile.TAG_SOURCE_TAG, sourceTag, info); 
					String clockId = clockSource.toString();
					reportFile.addElement(ReportFile.TAG_CLOCK, clockId, info); 
					String signalType = clockSource.getType();
					reportFile.addElement(ReportFile.TAG_SIGNAL_TYPE, signalType, info); 

					List<String> clkNameSrc = clockSource.getSignalDeclaration().getListOperand();
					
					reportFile.addSonarTags(info, SonarQubeRule.SONAR_ERROR_STD_04500, new Object[] {clkNameSrc.get(0), clockId}, SonarQubeRule.SONAR_MSG_STD_04500, new Object[] {clockId, clkNameSrc.get(0)});
				}
			}
			
			result = reportFile.save();
		}
		
		return result;
	}
}
