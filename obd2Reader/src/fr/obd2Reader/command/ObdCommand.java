package fr.obd2Reader.command;

import java.io.*;

public abstract class ObdCommand {
	
	//rendre la lecture et l'�criture interne � chaque objet ObdCommand
	public void sendCommand(OutputStream out){
		
	}
	
	public String read(InputStream in){
		String inBuf = "";
		return inBuf;
	}
}
