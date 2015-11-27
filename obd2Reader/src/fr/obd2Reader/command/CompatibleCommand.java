package fr.obd2Reader.command;

import java.util.ArrayList;

public interface CompatibleCommand{

	public boolean isCompatible(ArrayList<Byte> vehicleRef);
	public void compute();
	public String getUnit();
	public float getData();
	public String getName();
}
