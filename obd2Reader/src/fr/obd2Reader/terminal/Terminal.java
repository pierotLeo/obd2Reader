package fr.obd2Reader.terminal;

import java.util.ArrayList;
import java.util.Scanner;

import fr.obd2Reader.bluetooth.BluetoothConnection;

public class Terminal {

	BluetoothConnection btConnection;
	
	public Terminal(){
		btConnection = new BluetoothConnection();
		connectToElm327(btConnection);
	}
	
	/**
	 * Search for ELM327 module and connect with it if found.
	 * 
	 * @param btConnection
	 */
	private void connectToElm327(BluetoothConnection btConnection){
		String url;
		
		if(btConnection.searchDevices()){
			if(btConnection.searchServices("OBDII", 0x0100)!=0){
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
	 * Initiate the communication with the user.
	 */
	public void menu(){
		Scanner sc = new Scanner(System.in);
		String input = "";
		System.out.println("Bienvenue sur OBD2-reader. Entrez ce que vous souhaitez envoyer au module ELM327, ou \"quitter\" pour quitter le programme.");
		while(!input.equals("Quitter") && !input.equals("quitter")){
			if(!input.isEmpty()){
				btConnection.send(input);
				System.out.println(btConnection.readUntil(">"));
			}
			input = sc.nextLine();
		}
		sc.close();
		
	}
	
	public static void main(String[] args) {
		Terminal t = new Terminal();
		t.menu();
	}

}
