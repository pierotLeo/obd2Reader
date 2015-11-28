package fr.obd2Reader.dialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.obd2Reader.connection.ELM327Connection;

public class TerminalController implements TerminalControllerConstants{

	private ELM327Connection connection;
	private String input;
	
	public TerminalController(ELM327Connection connection){
		this.connection = connection;
	}
	
	public TerminalController(){
		
	}
	
	public boolean isCompatible(int pid){
		boolean isCompatible = false;
		
		//waaaaaah, mais comment c'est dur en fait.
		//a régler en fonction du mode et tout. Beaucoup travail.
		
		return isCompatible;
	}
	
	public String getIsCompatibleMan(){
		String man = "isCompatible [PID]\n"
					+ "PID : PID request to test.\n";
		
		return man;
	}
	
	public int analyzeInput(String msg){
		
		Pattern man = Pattern.compile("^man ");
		Pattern isCompatible = Pattern.compile("^isCompatible [0-9]{2|4}");
		
		if(man.matcher(msg).find()){
			
			String manualTo = msg.substring(FOLLOWING_MAN);
			
			if(manualTo.matches("isCompatible"))
				return IS_COMPATIBLE_MAN;
			else
				return NO_SUCH_COMMAND;
		}
		else
			return MISC_REQUEST;
		
	}
	
	public void send(String msg){
		
		connection.send(msg);
	}
	
	public String read(){
		String controllerAnswer = "";
		
		String moduleAnswer = connection.read();

		if(moduleAnswer.contains("?"))
			controllerAnswer = "Request not understood. To make sure the request you want to make is compatible with the system, use the isCompatible command (man isCompatible).\n";
		else
			controllerAnswer = moduleAnswer;
		
		return controllerAnswer;
	}
}
