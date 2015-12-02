package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public abstract class CompatibilityTestableCommand extends ObdCommand{

	public CompatibilityTestableCommand(String command, String name, OutputStream out, InputStream in){
		super(command, name, out, in);
	}
	
	public static boolean isCompatible(ArrayList<Byte> vehicleRef, int pid){
		boolean compatible = false;
		
		if(pid/4 < vehicleRef.size()){
			byte offSet = (byte) ((4-pid%4)%4),
				 bitField = vehicleRef.get(pid/4),
				 bitMask = (byte) (0b1 << offSet);
			compatible = (bitField & bitMask) >> offSet == 1;
		 }
		
		return compatible;
	};

	public static void main(){
	
			byte bitField = 0xB,
				 bitMask = (byte) ((4-2%4)%4);
			bitMask = (byte) (0b1 << bitMask);
			System.out.println(bitMask);
			
	}
	
}
