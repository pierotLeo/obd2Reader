package fr.obd2Reader.command.voltage;

public class ControlModuleVoltageCommand extends VoltageCommand{

	public ControlModuleVoltageCommand(){
		super("01 42");
	}
	
	protected void calculate(){
		setVoltage(((getInBuff().get(0) * 256) + getInBuff().get(1)) / 1000);
	}
	
}
