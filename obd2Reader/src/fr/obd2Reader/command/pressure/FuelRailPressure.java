package fr.obd2Reader.command.pressure;

public class FuelRailPressure extends PressureCommand{

	public FuelRailPressure(){
		super("01 22");
	}
	
	protected void calculate(){
		setPressure(((getInBuff().get(0) * 256) + getInBuff().get(1)) * 0.079);
	}
	
}
