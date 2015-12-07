package fr.obd2Reader.command.temperature;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

public class TemperatureCommand extends ObdCommand{
	
	public TemperatureCommand(String command, String name, OutputStream out, InputStream in){
		super(command, name, out, in);
		setUnit("°C");
	}
	
	public void compute(){
		sendCommand();
		read();
		setData(getInBuff().get(0) - 40);
	}

}
