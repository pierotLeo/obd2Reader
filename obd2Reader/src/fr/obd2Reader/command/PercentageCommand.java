package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class PercentageCommand extends ObdCommand{
	
	private float percentage;
	
	public PercentageCommand(String command, OutputStream out, InputStream in){
		super(command, out, in);
	}
	
	public void compute(){
		sendCommand();
		read();
		percentage = getInBuff().get(0)*100/255;
	}
}
