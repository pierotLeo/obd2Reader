package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Mother-class of every OBD commands containing precentage type information.
 * @author Supa Kanojo Hunta
 *
 */
public abstract class PercentageCommand extends ObdCommand{
		
	/**
	 * Default constructor for PercentageCommand. Don't even know what the fuck this is doing here since it's an abstract class though. Plus this is no default constructor at all. Nor are any of the whole program actually. Sigh.
	 * @param command
	 * @param out
	 * @param in
	 */
	public PercentageCommand(String command, OutputStream out, InputStream in){
		super(command, out, in);
		setUnit("%");
	}

	public void compute(){
		sendCommand();
		read();
		setData(getInBuff().get(0)*100/255);
	}
}
