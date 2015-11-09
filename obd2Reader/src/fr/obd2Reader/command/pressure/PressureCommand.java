package fr.obd2Reader.command.pressure;

import fr.obd2Reader.command.ObdCommand;

public abstract class PressureCommand extends ObdCommand{
	private double pressure;
	
	public PressureCommand(String command){
		super(command);
	}
	
	public double getPressure(){
		return pressure;
	}
	
	public void setPressure(double pressure){
		this.pressure = pressure;
	}
}
