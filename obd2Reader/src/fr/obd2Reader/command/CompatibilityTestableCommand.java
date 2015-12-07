package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public abstract class CompatibilityTestableCommand extends ObdCommand{

	public CompatibilityTestableCommand(String command, String name, OutputStream out, InputStream in){
		super(command, name, out, in);
	}
	
	public static boolean isCompatible(ArrayList<Short> vehicleRef, int pid){
		boolean compatible = false;
		
		if(pid/4 < vehicleRef.size()){
			short offSet = (short) ((8-pid%8)%8),
				 bitField = (short)vehicleRef.get(pid/8),
				 bitMask = (short) (0b1 << offSet);
			compatible = (bitField & bitMask) >> offSet == 1;
		 }
		
		return compatible;
	};
	
}
