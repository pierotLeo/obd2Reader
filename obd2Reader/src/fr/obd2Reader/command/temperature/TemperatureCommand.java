package fr.obd2Reader.command.temperature;

import fr.obd2Reader.command.ObdCommand;

public class TemperatureCommand extends ObdCommand{
	private float temperature;
	
	public TemperatureCommand(String command){
		super(command);
	}
	
	protected void calculate(){
		temperature = getInBuff().get(0) - 40;
	}
	
	protected float getTemperature(){
		return temperature;
	}
	
	protected void setTemperature(float temperature){
		this.temperature = temperature;
	}
}
