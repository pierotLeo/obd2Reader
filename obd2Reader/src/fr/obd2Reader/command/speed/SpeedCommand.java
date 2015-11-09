package fr.obd2Reader.command.speed;

import fr.obd2Reader.command.ObdCommand;

public class SpeedCommand extends ObdCommand{
	
	private float speed;
	
	public SpeedCommand(){
		super("01 0D");
	}
	
	protected void calculate(){
		speed = getInBuff().get(0);
	}
}
