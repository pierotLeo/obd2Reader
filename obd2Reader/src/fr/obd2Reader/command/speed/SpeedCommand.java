package fr.obd2Reader.command.speed;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.obd2Reader.command.CompatibleCommand;
import fr.obd2Reader.command.ObdCommand;

/**
 * An OBD command getting speed of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class SpeedCommand extends ObdCommand implements CompatibleCommand{
	
	/**
	 * Default constructor for FuelRailPressure.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public SpeedCommand(OutputStream out, InputStream in){
		super("01 0D", out, in);
		setUnit("km/h");
	}
	
	public void compute(){
		sendCommand();
		read();
		setData(getInBuff().get(0));
	}

	@Override
	public boolean isCompatible(ArrayList<Byte> vehicleRef) {
		return false;
	}
}
