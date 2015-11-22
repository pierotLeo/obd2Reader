package fr.obd2Reader.connection;



import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import fr.obd2Reader.command.pressure.EvaporationSystemVaporPressure;
import fr.obd2Reader.command.speed.SpeedCommand;
import fr.obd2Reader.driver.BluetoothConnection;

public class ELM327Connection {

	private BluetoothConnection btConnection;
	
	public ELM327Connection(){
		btConnection = new BluetoothConnection();
		connectToElm327(btConnection);
		btConnection.send("ATZ");
		System.out.println(btConnection.readUntil(">"));
		btConnection.send("ATSP0");
		System.out.println(btConnection.readUntil(">"));
	}
	
	/**
	 * Search for ELM327 module and connect with it if found.
	 * 
	 * @param btConnection
	 */
	private void connectToElm327(BluetoothConnection btConnection){
		String url;
		
		if(btConnection.searchDevices()){
			if(btConnection.searchServices("OBDII", "1101")!=0){
				url = findRfcommUrl(btConnection.getUrls());
				if(!url.isEmpty()){
					btConnection.clientConnection(url);
				}
			}
		}
	}
	
	/**
	 * Called by the connectToElm327() method in order to find a connection url affiliated to RFCOMM protocol.
	 * 
	 * @param serviceRecord : list of services to search from.
	 * @return String : last RFCOMM url found in the list.
	 */
	private String findRfcommUrl(ArrayList<String> serviceRecord){
		String url = "";
		for(int i=0; i<serviceRecord.size(); i++){
			if(serviceRecord.get(i).matches("btspp://.*")){
				url = serviceRecord.get(i);
			}
		}
		return url;
	}
	
	public void send(String cmd){
				btConnection.send(cmd);
	}
	
	public String read(){
		return(btConnection.readUntil(">"));
	}
	
	public InputStream getInputStream(){
		return btConnection.getInputStream();
	}
	
	public OutputStream getOutputStream(){
		return btConnection.getOutputStream();
	}
	
}
