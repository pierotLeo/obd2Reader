package fr.obd2Reader.command.pressure;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

/**
 * Mother-class of every OBD commands containing pressure type information.
 * @author Supa Kanojo Hunta
 *
 */
public abstract class PressureCommand extends ObdCommand{
	private double pressure;
	
	/**
	 * Default constructor for PressureCommand.
	 * @param command : ASCII encoded version of the command to send to OBD system.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public PressureCommand(String command, OutputStream out, InputStream in){
		super(command, out, in);
		setUnit("kPa");
	}
	
	/**
	 * Getter for pressure.
	 * @return
	 */
	public double getPressure(){
		return pressure;
	}
	
	/**
	 * Setter for pressure.
	 * @param pressure
	 */
	public void setPressure(double pressure){
		this.pressure = pressure;
	}
}
