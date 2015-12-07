package fr.obd2Reader.connection;



import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import fr.obd2Reader.connection.driver.BluetoothConnection;

/**
 * Object encapsuling a bluetooth connection with an ELM327 module.
 * @author Supa Kanojo Hunta
 *
 */
public class ELM327Connection {

	private BluetoothConnection btConnection;
	
	/**
	 * Default constructor for ELM327Connection.
	 */
	public ELM327Connection(){
		btConnection = new BluetoothConnection();
		connectToElm327(btConnection);
		btConnection.send("ATZ");
		System.out.println(btConnection.readUntil(">"));
		btConnection.send("ATSP0");
		System.out.println(btConnection.readUntil(">"));
	}
	
	/**
	 * Search for an ELM327 module and establish a bluetooth connection if found.
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
	
	/**
	 * Send a request to connected ELM327 module.
	 * @param cmd
	 */
	public void send(String cmd){
		btConnection.send(cmd);
	}
	
	/**
	 * Read informations from connected ELM327 module.
	 * @return
	 */
	public String read(){
		return(btConnection.readUntil(">"));
	}
	
	/**
	 * Getter for the input stream.
	 * @return
	 */
	public InputStream getInputStream(){
		return btConnection.getInputStream();
	}
	
	/**
	 * Getter for the output stream.
	 * @return
	 */
	public OutputStream getOutputStream(){
		return btConnection.getOutputStream();
	}
	
}
