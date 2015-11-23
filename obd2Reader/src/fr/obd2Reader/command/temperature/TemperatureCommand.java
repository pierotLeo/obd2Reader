package fr.obd2Reader.command.temperature;

import java.io.InputStream;
import java.io.OutputStream;

import fr.obd2Reader.command.ObdCommand;

/**
 * Mother-class of every OBD commands containing temperature type information.
 * @author Supa Kanojo Hunta
 *
 */
public class TemperatureCommand extends ObdCommand{
	
	private float temperature;
	
	/**
	 * Default constructor for PressureCommand.
	 * @param command : ASCII encoded version of the command to send to OBD system.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public TemperatureCommand(String command, OutputStream out, InputStream in){
		super(command, out, in);
		setUnit("°C");
	}
	
	public void compute(){
		sendCommand();
		read();
		temperature = getInBuff().get(0) - 40;
	}
	
	/**
	 * Getter for temperature.
	 * @return
	 */
	protected float getTemperature(){
		return temperature;
	}
	
	/**
	 * Setter for temperature.
	 * @param temperature
	 */
	protected void setTemperature(float temperature){
		this.temperature = temperature;
	}
}
