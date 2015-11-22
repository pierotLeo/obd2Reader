package fr.obd2Reader.command;

import java.util.ArrayList;

public interface CompatibleCommand {

	public abstract boolean isCompatible(ArrayList<Byte> vehicleRef);
	
}
