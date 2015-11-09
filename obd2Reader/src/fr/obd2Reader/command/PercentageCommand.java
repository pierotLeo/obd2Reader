package fr.obd2Reader.command;

public abstract class PercentageCommand extends ObdCommand{
	
	private float percentage;
	
	public PercentageCommand(String command){
		super(command);
	}
	
	protected void calculate(){
		percentage = getInBuff().get(0)*100/255;
	}
}
