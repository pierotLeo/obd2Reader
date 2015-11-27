package fr.obd2Reader.command.voltage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.obd2Reader.command.CompatibleCommand;

public class ControlModuleVoltageCommand extends VoltageCommand implements CompatibleCommand{

	public ControlModuleVoltageCommand(OutputStream out, InputStream in){
		super("01 42", "Control Module Voltage", out, in);
	}
	
	public void compute(){
		sendCommand();
		read();
		setData(((getInBuff().get(0) * 256) + getInBuff().get(1)) / 1000);
	}

	@Override
	public boolean isCompatible(ArrayList<Byte> vehicleRef) {
		return false;
	}
	
}
