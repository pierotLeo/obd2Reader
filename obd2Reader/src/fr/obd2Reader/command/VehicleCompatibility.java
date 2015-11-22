package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * OBD command getting mode 01 PIDs compatibility of the system in use.
 * @author Supa Kanojo Hunta
 *
 */
public class VehicleCompatibility extends ObdCommand{

	/**
	 * Default constructor for VehicleCompatibility.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public VehicleCompatibility(OutputStream out, InputStream in){
		super(out, in);
	}
	
	public void compute(){
		for(int i = 0; i < 5; i++){
			sendCommand("01" + i*2 + "0");
			read();
		}
		
		for(int i=4; i>=0; i--){
			for(int j=0; j<2; j++){
				getInBuff().remove(6*i);				
			}
		}
	}
	
}
