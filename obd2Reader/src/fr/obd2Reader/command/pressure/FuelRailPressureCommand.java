package fr.obd2Reader.command.pressure;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.obd2Reader.command.CompatibleCommand;

public class FuelRailPressureCommand extends PressureCommand implements CompatibleCommand{

	public FuelRailPressureCommand(OutputStream out, InputStream in){
		super("01 22", "Fuel Rail Pressure", out, in);
	}
	
	public void compute(){
		sendCommand();
		read();
		setData((float)(((getInBuff().get(0) * 256) + getInBuff().get(1)) * 0.079));
	}

	@Override
	public boolean isCompatible(ArrayList<Byte> vehicleRef) {
		return false;
	}
	
}
