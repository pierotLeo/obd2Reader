package fr.obd2Reader.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * An OBD command getting engine fuel rate of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class EngineFuelRate extends ObdCommand implements CompatibleCommand{

	/**
	 * Default Constructor for EngineFuelRate.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public EngineFuelRate(OutputStream out, InputStream in){
		super("01 5E", out, in);
		setUnit("L/h");
	}
	
	public boolean isCompatible(ArrayList<Byte> vehicleRef){
		return ((vehicleRef.get(8) & 4) == 4);
	}
	
	public void compute(){
		sendCommand();
		read();
		setData((float)(((getInBuff().get(0)*256) + getInBuff().get(1))*0.05));
	}
}
