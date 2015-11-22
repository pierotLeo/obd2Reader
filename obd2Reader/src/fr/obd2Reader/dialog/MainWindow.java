package fr.obd2Reader.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import fr.obd2Reader.command.VehicleCompatibility;
import fr.obd2Reader.connection.ELM327Connection;

public class MainWindow extends JFrame{

	private VehicleCompatibility vehicle;
	private ELM327Connection connection;
	private JTabbedPane tabbedMenu;
	private JTextArea terminal;
	
	public MainWindow(){
		super("On-Board Diagnostic Reader");
		Toolkit tkt = Toolkit.getDefaultToolkit();
		Dimension itemWindowDim = tkt.getScreenSize();
		int height = (int)itemWindowDim.getHeight();
		int width = (int)itemWindowDim.getWidth();
		this.setBounds(width/2-width/4, height/2-height/4, width/2, height/2);
		initiate();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initiate(){
		connection = new ELM327Connection();
		add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	private JTabbedPane getCenterPanel(){
		tabbedMenu = new JTabbedPane();
		
		tabbedMenu.addTab("Real time informations", getRTIPanel());
		tabbedMenu.addTab("Vehicle error codes", getErrorCodesPanel());
		tabbedMenu.addTab("Terminal", getTerminalPanel());
		
		return tabbedMenu;
	}
	
	private JPanel getRTIPanel(){
		JPanel RTIPanel = new JPanel();
		
		vehicle = new VehicleCompatibility(connection.getOutputStream(), connection.getInputStream());
		/*vertical scrolling list. Contains following InformationPanel: 
		  	-premade panel (premade set of informations, like speed, rpm and consommation)
		  	-speed
		  	-rpm
		  	-consommation
		  	-coolant temperature
		  	-throttle posisition
		  	-HP 
			-fuel system status
			-Evaporation system pressure
			-fuel rail pressure
			-control module voltage
		*/
		
		return RTIPanel;
	}
	
	private JPanel getErrorCodesPanel(){
		JPanel errorCodesPanel = new JPanel();
		
		
		
		return errorCodesPanel;
	}
	
	
	private JTextArea getTerminalPanel(){		
		terminal = new JTextArea();
		
		
		
		return terminal;
	}
	
	public static void main(String[] args){
		MainWindow main = new MainWindow();
	}
}
