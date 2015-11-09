package fr.obd2Reader.command.temperature;


public class EngineCoolantTemperatureCommand extends TemperatureCommand{
	
	public EngineCoolantTemperatureCommand(){
		super("01 05");
	}
	
	protected void calculate(){
		setTemperature((float)(getInBuff().get(0)-40));
	}
	
}
