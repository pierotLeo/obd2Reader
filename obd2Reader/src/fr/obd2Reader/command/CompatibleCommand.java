package fr.obd2Reader.command;

import java.util.ArrayList;

/**
 * The interface for testing compatibility of a PID request with the system in use. 
 * @author Supa Kanojo Hunta
 *
 */
public interface CompatibleCommand {

	/**
	 * Test compatibility. 
	 * @param vehicleRef : Compatibility reference of the system in use. The byte encoding current command compatibility's must be active (1) to be understood by the vehicle's OBD system.
	 * @return boolean : true if the command is compatible with the system, else false.
	 */
	public boolean isCompatible(ArrayList<Byte> vehicleRef);
	public void compute();
	public String getUnit();
}
