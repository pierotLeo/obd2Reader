package fr.obd2Reader.dialog;
import java.util.ArrayList;
import java.util.Scanner;

import fr.obd2Reader.command.pressure.EvaporationSystemVaporPressure;
import fr.obd2Reader.command.speed.SpeedCommand;
import fr.obd2Reader.driver.BluetoothConnection;

/**
 * Useless piece of crap that eventually work. Sometimes.
 * @author Supa Kanojo Hunta
 *
 */
public class Terminal {

	private BluetoothConnection btConnection;
	private Scanner sc;
	
	public Terminal(){
		sc = new Scanner(System.in);
		btConnection = new BluetoothConnection();
		connectToElm327(btConnection);
		btConnection.send("ATZ");
		System.out.println(btConnection.readUntil(">"));
		btConnection.send("AT0");
		System.out.println(btConnection.readUntil(">"));
		btConnection.send("ATL0");
		System.out.println(btConnection.readUntil(">"));
		btConnection.send("ATE0");
		System.out.println(btConnection.readUntil(">"));
		btConnection.send("ATSThh");
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
	
	public final static void clearConsole()
	{
		String clr = "";
		for(int i=0; i<70; i++){
			clr+="\n";
		}
		System.out.println(clr);
	}
	
	public void directRequestTerminal(){
		String input = "";
		System.out.println("Entrez ce que vous souhaitez envoyer au module ELM327, ou \"quitter\" pour retourner au menu.");
		while(!input.equals("Quitter") && !input.equals("quitter")){
			if(!input.isEmpty()){
				btConnection.send(input);
				System.out.println(btConnection.readUntil(">"));
			}
			input = sc.nextLine();
		}
		clearConsole();
		menu();
	}
	
	/**
	 * Open a terminal
	 */
	public void premadeRequestTerminal(){
		int input = 0;
		System.out.println("Quel information souhaitez vous récupérer?\n\t"
				+ "				1-Vitesse actuelle.\n\t"
				+ "				2-Vitesse sur les dix prochaines secondes.\n\t"
				+ "				3-Pression de la vapeur des système d'évaporation actuelle.\n\t"
				+ "				4-Retourner au menu.");
		while(input != 4){
			input = sc.nextInt();
			if(input != 0){
				switch(input){
					case 1:
						SpeedCommand speed = new SpeedCommand(btConnection.getOutputStream(), btConnection.getInputStream());
						speed.compute();
						System.out.println("Vitesse = " + speed.getSpeed() + " km/h");
						break;
					case 2:
						SpeedCommand speedP = new SpeedCommand(btConnection.getOutputStream(), btConnection.getInputStream());
						for(int  i = 0; i < 10; i++){
							try {
								speedP.compute();
								System.out.println("Vitesse = " + speedP.getSpeed() + " km/h");
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						break;
					case 3:
						EvaporationSystemVaporPressure esvp = new EvaporationSystemVaporPressure(btConnection.getOutputStream(), btConnection.getInputStream());
						esvp.compute();
						System.out.println("Pression de la vapeur des système d'évaporation = " + esvp.getPressure() + " Pa.");
						break;
					case 4:
						break;
					default :
						clearConsole();
						System.out.println("Choisissez une requête parmi celles proposées.");
						break;

				}
			}
		}
		clearConsole();
		menu();
	}
	
	/**
	 * Initiate the communication with the user.
	 */
	public void menu(){
		int input = 0;
		System.out.println("Bienvenue sur la version test d'OBD2-reader. Comment souhaitez-vous communiquer avec le module?\n\t1-Requêtes personnalisées.\n\t2-Requêtes pré-construites.\n\t3-quitter.");
		while(input!=3){
			input = sc.nextInt();
			switch(input){
				case 1 :
					clearConsole();
					directRequestTerminal(); 
					break;
				case 2 : 
					clearConsole();
					premadeRequestTerminal();
					break;
				case 3:
					break;
				default :
					clearConsole();
					System.out.println("Choisissez une option valide.");
					break;
			}
		}		
	}
	
	public static void main(String[] args) {
		Terminal t = new Terminal();
		t.menu();
	}
}
