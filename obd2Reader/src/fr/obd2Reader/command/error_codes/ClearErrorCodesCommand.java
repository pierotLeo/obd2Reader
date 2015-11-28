package fr.obd2Reader.command.error_codes;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

public class ClearErrorCodesCommand extends ObdCommand{
	
	public ClearErrorCodesCommand(OutputStream out, InputStream in){
		super("04", "Clear Error Codes", out, in);
	}
	
	@Override
	public void compute() {
		sendCommand();
		read();
	}
}
