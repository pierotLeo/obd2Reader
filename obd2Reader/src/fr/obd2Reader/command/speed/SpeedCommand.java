package fr.obd2Reader.command.speed;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.obd2Reader.command.CompatibleCommand;
import fr.obd2Reader.command.ObdCommand;

public class SpeedCommand extends ObdCommand implements CompatibleCommand{
		
	public SpeedCommand(OutputStream out, InputStream in){
		super("01 0D", "Speed", out, in);
		setUnit("km/h");
	}
	
	public boolean isCompatible(ArrayList<Byte> vehicleRef){
		return ((vehicleRef.get(3) & 8) == 8);
	}
	
	public void compute(){
		sendCommand();
		read();
		setData(getInBuff().get(0));
	}

}
