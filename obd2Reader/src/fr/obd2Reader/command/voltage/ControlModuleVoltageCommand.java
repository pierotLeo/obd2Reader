package fr.obd2Reader.command.voltage;

import java.io.InputStream;
import java.io.OutputStream;

public class ControlModuleVoltageCommand extends VoltageCommand{

	public ControlModuleVoltageCommand(OutputStream out, InputStream in){
		super("01 42", out, in);
	}
	
	public void calculate(){
		sendCommand();
		read();
		setVoltage(((getInBuff().get(0) * 256) + getInBuff().get(1)) / 1000);
	}
	
}
