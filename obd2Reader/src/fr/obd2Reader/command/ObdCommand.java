
package fr.obd2Reader.command;

import java.io.*;
import java.util.ArrayList;

public abstract class ObdCommand {
	
	private String command;
	private ArrayList<Integer> inBuff;
	private OutputStream out;
	private InputStream in;
	
	public ObdCommand(String command, OutputStream out, InputStream in){
		this.command = command;
		this.inBuff = new ArrayList<Integer>();
		this.out = out;
		this.in = in;
	}
	
	public abstract void calculate();
	
	protected void sendCommand(){
		try {
			out.write(command.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void read(){
		try{
			//tant que le caractère lu est différent de >...
			while(!Character.toString((char)in.read()).equals(">")){
				//on ajoute l'entier lu au buffer de l'instance.
				inBuff.add(in.read());
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
	
	protected ArrayList<Integer> getInBuff(){
		return inBuff;
	}
}
