
package fr.obd2Reader.command;

import java.io.*;
import java.util.ArrayList;

public abstract class ObdCommand {
	
	private String command;
	private String name;
	private String unit;
	private float data; 
	private ArrayList<Short> inBuff;
	private OutputStream out;
	private InputStream in;
	
	public ObdCommand(){
		
	}
	
	public ObdCommand(String command, String name, OutputStream out, InputStream in){
		this.command = command;
		this.name = name;
		this.inBuff = new ArrayList<Short>();
		this.out = out;
		this.in = in;
	}
	
	public ObdCommand(OutputStream out, InputStream in){
		this.inBuff = new ArrayList<Short>();
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
			String fullByteString = "";
			char currentReadChar = ' ';
			int indexOfAnswer = 0;
			while(!Character.toString(currentReadChar).equals(">")){
				currentReadChar = (char)in.read();
				if(currentReadChar == 'S'){
					for(int i = 0; i < 10; i++)
						currentReadChar = (char)in.read();
				}
				if(Character.toString(currentReadChar).matches("^[a-fA-F0-9]$")){
					indexOfAnswer++;
					fullByteString += Character.toString(currentReadChar);
					if(indexOfAnswer%2 == 0 && indexOfAnswer != 0){
						inBuff.add(Short.parseShort(fullByteString, 16));
						fullByteString = "";
					}
				}
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
	
	public ArrayList<Short> getInBuff(){
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
