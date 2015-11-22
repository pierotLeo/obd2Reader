package fr.obd2Reader.command.voltage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An OBD command getting control module voltage of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class ControlModuleVoltageCommand extends VoltageCommand{

	/**
	 * Default constructor for ControlModuleVoltageCommand.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public ControlModuleVoltageCommand(OutputStream out, InputStream in){
		super("01 42", out, in);
	}
	
	public void compute(){
		sendCommand();
		read();
		setVoltage(((getInBuff().get(0) * 256) + getInBuff().get(1)) / 1000);
	}
	
}
