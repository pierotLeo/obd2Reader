
package fr.obd2Reader.command;

import java.io.*;
import java.util.ArrayList;

public abstract class ObdCommand {
	
	private String command;
	private String name;
	private String unit;
	private float data;
	private ArrayList<Byte> inBuff;
	private OutputStream out;
	private InputStream in;
	
	public ObdCommand(){
		
	}
	
	public ObdCommand(String command, String name, OutputStream out, InputStream in){
		this.command = command;
		this.name = name;
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
	
	public void sendCommand(){
		try {
			out.write((command+"\r").getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCommand(String cmd){
		try{
			out.write((cmd + "\r").getBytes());
			out.flush();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void read(){
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
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public ArrayList<Byte> getInBuff(){
		return inBuff;
	}
	
	public String getUnit(){
		return unit;
	}
	
	public void setUnit(String unit){
		this.unit = unit;
	}
	
	public float getData(){
		return data;
	}
	
	public void setData(float data){
		this.data = data;
	}
	
	public String toString(){
		return data + " " + unit;
	}
}
