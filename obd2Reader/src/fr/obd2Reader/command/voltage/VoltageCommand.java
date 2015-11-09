package fr.obd2Reader.command.voltage;

import fr.obd2Reader.command.ObdCommand;

public abstract class VoltageCommand extends ObdCommand{

	private double voltage;
	
	public VoltageCommand(String command){
		super(command);
	}
	
	public void setVoltage(double voltage){
		this.voltage = voltage;
	}

	public double getVoltage(){
		return voltage;
	}
	
}
