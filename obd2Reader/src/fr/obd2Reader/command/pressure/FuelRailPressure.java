package fr.obd2Reader.command.pressure;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An OBD command getting fuel rail pressure of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class FuelRailPressure extends PressureCommand{

	/**
	 * Default constructor for FuelRailPressure.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public FuelRailPressure(OutputStream out, InputStream in){
		super("01 22", out, in);
	}
	
	public void compute(){
		sendCommand();
		read();
		setData((float)(((getInBuff().get(0) * 256) + getInBuff().get(1)) * 0.079));
	}
	
}
