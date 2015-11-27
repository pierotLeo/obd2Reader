package fr.obd2Reader.command.temperature;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.obd2Reader.command.CompatibleCommand;


public class EngineCoolantTemperatureCommand extends TemperatureCommand implements CompatibleCommand{
	
	public EngineCoolantTemperatureCommand(OutputStream out, InputStream in){
		super("01 05", "Engine Coolant Temperature", out, in);
	}
	
	public void compute(){
		sendCommand();
		read();
		setData((float)(getInBuff().get(0)-40));
	}

	@Override
	public boolean isCompatible(ArrayList<Byte> vehicleRef) {
		return false;
	}
	
}
