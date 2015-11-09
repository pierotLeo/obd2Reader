package fr.obd2Reader.command.pressure;

public class EvaporationSystemVaporPressure extends PressureCommand{
	
	public EvaporationSystemVaporPressure(){
		super("01 32");
	}
	
	protected void calculate(){
		setPressure(((getInBuff().get(0)*256) + getInBuff().get(1))/4);
	}

}
