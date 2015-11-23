package fr.obd2Reader.command.temperature;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An OBD command getting engine coolant temperature of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class EngineCoolantTemperatureCommand extends TemperatureCommand{
	
	/**
	 * Default constructor for FuelRailPressure.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public EngineCoolantTemperatureCommand(OutputStream out, InputStream in){
		super("01 05", out, in);
	}
	
}
