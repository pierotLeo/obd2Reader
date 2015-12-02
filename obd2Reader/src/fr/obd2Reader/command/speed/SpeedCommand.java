package fr.obd2Reader.command.speed;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.obd2Reader.command.CompatibilityTestableCommand;
import fr.obd2Reader.command.ObdCommand;

public class SpeedCommand extends CompatibilityTestableCommand{
	
	public SpeedCommand(OutputStream out, InputStream in){
		super("01 0D", "Speed", out, in);
		setUnit("km/h");
	}
	
	public void compute(){
		sendCommand();
		read();
		setData(getInBuff().get(2));
	}

}
