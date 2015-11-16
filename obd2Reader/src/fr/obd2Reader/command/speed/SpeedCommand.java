package fr.obd2Reader.command.speed;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

public class SpeedCommand extends ObdCommand{
	
	private float speed;
	
	public SpeedCommand(OutputStream out, InputStream in){
		super("01 0D", out, in);
	}
	
	public void calculate(){
		sendCommand();
		read();
		speed = getInBuff().get(0);
	}
	
	public float getSpeed(){
		return speed;
	}
}
