
package fr.obd2Reader.command;

import java.io.*;
import java.util.ArrayList;

/**
 * Mother-class of an OBD command. 
 * @author Supa Kanojo Hunta
 *
 */
public abstract class ObdCommand {
	
	private String command;
	private ArrayList<Byte> inBuff;
	private OutputStream out;
	private InputStream in;
	
	/**
	 * Constructor for ObdCommand.
	 * @param command : ASCII encoded version of the command to send to OBD system.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public ObdCommand(String command, OutputStream out, InputStream in){
		this.command = command;
		this.inBuff = new ArrayList<Byte>();
		this.out = out;
		this.in = in;
	}
	
	//to rework. Numerous commands should be imploded into one string, separated by a specific character. Then split up and send separately.
	/**
	 * Constructor for ObdCommand. Used when an OBDCommand must send numerous request to be fulfilled.
	 * @param out : OutputStream of a pre-established connection. Used to Write information to connection's other end.
	 * @param in : InputStream of a pre-established connection. Used to read information from connection's other end.
	 */
	public ObdCommand(OutputStream out, InputStream in){
		this.inBuff = new ArrayList<Byte>();
		this.out = out;
		this.in = in;
	}
	
	/**
	 * Decode sent information from OBD system before storing it.
	 */
	public abstract void compute();
	
	/**
	 * Send a request to OBD system.
	 */
	protected void sendCommand(){
		try {
			out.write((command+"\r").getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//As specified above, to rework. This is probably where the string should be split up.
	/**
	 * Send a request to OBD system. Used when numerous requests are required.
	 * @param cmd :  request to send to OBD system.
	 */
	protected void sendCommand(String cmd){
		try{
			out.write((cmd + "\r").getBytes());
			out.flush();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Read information from OBD system and store it into a byte buffer.
	 */
	protected void read(){
		try{
			char currentRead = ' ';
			while(!Character.toString(currentRead).equals(">")){
				currentRead = (char)in.read();
				inBuff.add(Byte.parseByte(Character.toString(currentRead), 16));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter for the input stream.
	 * @return 
	 */
	public InputStream getInputStream(){
		return in;
	}
	
	/**
	 * Getter for the output stream.
	 * @return
	 */
	public OutputStream getOutputStream(){
		return out;
	}
	
	/**
	 * Getter for input stream buffer.
	 * @return
	 */
	public ArrayList<Byte> getInBuff(){
		return inBuff;
	}
}
