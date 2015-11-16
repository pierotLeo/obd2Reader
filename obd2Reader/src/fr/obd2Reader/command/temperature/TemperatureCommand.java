package fr.obd2Reader.command.temperature;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

public class TemperatureCommand extends ObdCommand{
	private float temperature;
	
	public TemperatureCommand(String command, OutputStream out, InputStream in){
		super(command, out, in);
	}
	
	public void calculate(){
		sendCommand();
		read();
		temperature = getInBuff().get(0) - 40;
	}
	
	protected float getTemperature(){
		return temperature;
	}
	
	protected void setTemperature(float temperature){
		this.temperature = temperature;
	}
}
