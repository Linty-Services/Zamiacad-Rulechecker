package org.zamia.plugin.tool.vhdl.tools;

import java.util.ArrayList;
import java.util.List;

import org.zamia.ZamiaProject;
import org.zamia.plugin.tool.vhdl.manager.ReportManager.ParameterSource;
import org.zamia.plugin.tool.vhdl.rules.RuleStruct;
import org.zamia.plugin.tool.vhdl.rules.RuleTypeE;
import org.zamia.plugin.tool.vhdl.rules.StatusE;

public class ToolService {

	private static ToolService instance;
	
	 private ToolService() {
	        
	    }
	 
	 public static synchronized ToolService getInstance() {
	        if (instance == null) {
	            instance = new ToolService();
	        }

	        return instance;
	    }
	 
	 /**
	  * find rule in handbook defined in file rc_config.xml
	  * @param zPrj
	  * @return
	  */
	public List<RuleStruct> findAllTools(ZamiaProject zPrj) {
		
		List<RuleStruct> listRules = new ArrayList<RuleStruct>();
		
		for(ToolE tool : ToolE.values()) { 
	        RuleTypeE type = RuleTypeE.IDE;
	        String parameter = tool.isParam()? "Yes" : "No";
	        String enable = "Implemented";
	        String status = StatusE.NOT_EXECUTED.toString();
            ParameterSource p = parameter.equals("Yes")? tool.getParameterSource(): ParameterSource.RULE_CHECKER;
	        listRules.add(new RuleStruct(tool.getIdReq(), tool.getIdReq(), tool.getRuleName(), type.toString(), parameter, p == null? ParameterSource.RULE_CHECKER: p, enable, tool.isSelected(), status, "", true, ""));
	
	     }
		return listRules;
	}
	
}

