package fr.obd2Reader.command.error_codes;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.obd2Reader.command.ObdCommand;

public class GetErrorCodesCommand extends ObdCommand{

	private ArrayList<String> errorCodes;
	
	public GetErrorCodesCommand(OutputStream out, InputStream in){
		super("04", "Error Codes", out, in);
		errorCodes = new ArrayList<String>();
	}
	
	@Override
	public void compute() {
		sendCommand();
		read();
		int codeCount = getInBuff().size()/6; 
	}

}
