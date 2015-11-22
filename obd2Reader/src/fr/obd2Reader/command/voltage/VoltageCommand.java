package fr.obd2Reader.command.voltage;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

/**
 * Mother-class of every OBD commands containing voltage type information.
 * @author Supa Kanojo Hunta
 *
 */
public abstract class VoltageCommand extends ObdCommand{

	private double voltage;
	
	/**
	 * Default constructor for PressureCommand.
	 * @param command : ASCII encoded version of the command to send to OBD system.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public VoltageCommand(String command, OutputStream out, InputStream in){
		super(command, out, in);
	}

	/**
	 * Getter for voltage.
	 * @return
	 */
	public double getVoltage(){
		return voltage;
	}
	
	/**
	 * Setter for voltage.
	 * @param voltage
	 */
	public void setVoltage(double voltage){
		this.voltage = voltage;
	}

	
}
