
package fr.obd2Reader.command;

import java.io.*;
import java.util.ArrayList;

public abstract class ObdCommand {
	
	private String command;
	private ArrayList<Byte> inBuff;
	private OutputStream out;
	private InputStream in;
	
	public ObdCommand(String command, OutputStream out, InputStream in){
		this.command = command;
		this.inBuff = new ArrayList<Byte>();
		this.out = out;
		this.in = in;
	}
	
	public ObdCommand(OutputStream out, InputStream in){
		this.inBuff = new ArrayList<Byte>();
		this.out = out;
		this.in = in;
	}
	
	public abstract void compute();
	
	protected void sendCommand(){
		try {
			out.write((command+"\r").getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void sendCommand(String cmd){
		try{
			out.write((cmd + "\r").getBytes());
			out.flush();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
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
	
	public InputStream getIn(){
		return in;
	}
	
	public OutputStream getOut(){
		return out;
	}
	
	public ArrayList<Byte> getInBuff(){
		return inBuff;
	}
}
