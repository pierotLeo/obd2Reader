
package fr.obd2Reader.command;

import java.io.*;
import java.util.ArrayList;

public abstract class ObdCommand {
	
	private String command;
	protected int[][] compatibility;
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
	
	//public abstract boolean isCompatible();
	
	private void buildCompatibility(){
		try {
			out.write(("0100\r").getBytes());
			out.flush();
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 8; j++){
					compatibility[i][j] = Integer.parseInt(Integer.toBinaryString(in.read()).substring(j, j+1));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void sendCommand(){
		try {
			out.write((command + "\r").getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void read(){
		try{
			while(!Character.toString((char)in.read()).equals(">")){
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
