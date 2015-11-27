package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class PercentageCommand extends ObdCommand{
		
	public PercentageCommand(String command, String name, OutputStream out, InputStream in){
		super(command, name, out, in);
		setUnit("%");
	}
	
	public void compute(){
		sendCommand();
		read();
		setData(getInBuff().get(0)*100/255);
	}
}
