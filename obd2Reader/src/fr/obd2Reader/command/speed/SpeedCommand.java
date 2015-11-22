package fr.obd2Reader.command.speed;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

/**
 * An OBD command getting speed of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class SpeedCommand extends ObdCommand{
	
	private float speed;
	
	/**
	 * Default constructor for FuelRailPressure.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public SpeedCommand(OutputStream out, InputStream in){
		super("01 0D", out, in);
	}
	
	public void compute(){
		sendCommand();
		read();
		speed = getInBuff().get(0);
	}
	
	/**
	 * Getter for speed.
	 * @return
	 */
	public float getSpeed(){
		return speed;
	}
}
