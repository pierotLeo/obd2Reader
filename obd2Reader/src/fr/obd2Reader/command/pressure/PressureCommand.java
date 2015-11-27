package fr.obd2Reader.command.pressure;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

public abstract class PressureCommand extends ObdCommand{
	private double pressure;
	
	public PressureCommand(String command, String name, OutputStream out, InputStream in){
		super(command, name, out, in);
	}
	
	public double getPressure(){
		return pressure;
	}
	
	public void setPressure(double pressure){
		this.pressure = pressure;
	}
}
