package fr.obd2Reader.command.pressure;

import fr.obd2Reader.command.ObdCommand;

public abstract class PressureCommand extends ObdCommand{
	private int pressure;
	
	public PressureCommand(String command){
		super(command);
	}
	
	public int getPressure(){
		return pressure;
	}
	
	public void setPressure(int pressure){
		this.pressure = pressure;
	}
}
