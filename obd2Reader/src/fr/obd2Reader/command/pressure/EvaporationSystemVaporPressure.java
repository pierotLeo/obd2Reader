package fr.obd2Reader.command.pressure;

import java.io.InputStream;
import java.io.OutputStream;

public class EvaporationSystemVaporPressure extends PressureCommand{
	
	public EvaporationSystemVaporPressure(OutputStream out, InputStream in){
		super("01 32", out, in);
	}
	
	public void calculate(){
		sendCommand();
		read();
		setPressure(((getInBuff().get(0)*256) + getInBuff().get(1))/4);
	}

}
