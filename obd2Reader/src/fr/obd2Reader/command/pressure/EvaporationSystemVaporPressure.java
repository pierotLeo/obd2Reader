package fr.obd2Reader.command.pressure;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An OBD command getting evaporation system vapor pressure of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class EvaporationSystemVaporPressure extends PressureCommand{
	
	/**
	 * Default constructor for EvaporationSystemVaporPressure.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public EvaporationSystemVaporPressure(OutputStream out, InputStream in){
		super("01 32", out, in);
		setUnit("Pa");
	}
	
	public void compute(){
		sendCommand();
		read();
		setData(((getInBuff().get(0)*256) + getInBuff().get(1))/4);
	}

}
