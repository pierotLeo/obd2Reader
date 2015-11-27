package fr.obd2Reader.command.voltage;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

public abstract class VoltageCommand extends ObdCommand{
	
	public VoltageCommand(String command, String name, OutputStream out, InputStream in){
		super(command, name, out, in);
		setUnit("V");
	}
	
}
