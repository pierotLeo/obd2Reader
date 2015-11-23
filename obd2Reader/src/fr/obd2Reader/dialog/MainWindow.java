package fr.obd2Reader.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.obd2Reader.command.VehicleCompatibility;
import fr.obd2Reader.connection.ELM327Connection;

/**
 * Main window displayed at the user. Contains all the shit you hear me.
 * @author Supa Kanojo Hunta
 *
 */
public class MainWindow extends JFrame implements RTIPanelConstants{

	private VehicleCompatibility vehicle;
	private ELM327Connection connection;
	private JTabbedPane tabbedMenu;
	private JTextField terminalOutput;
	private JTextArea terminalInput;
	
	private class InfoListListener implements ListSelectionListener{
		private JList list;
		
		public InfoListListener (JList list){
			this.list = list;
		}
		
		public void valueChanged(ListSelectionEvent e){
			if(!e.getValueIsAdjusting()){
				
				switch(list.getSelectedIndex()){
					case NO_SELECTION :
						break;
					case PRE_MADE_PANEL :
						break;
					case SPEED :
						break;
					case RPM :
						break;
					case CONSOMATION :
						break;
					case COOLANT_TEMPERATURE :
						break;
					case THROTTLE_POSITION :
						break;
					case HP :
						break;
					case FUEL_SYSTEM_STATUS :
						break;
					case EVAPORATION_SYSTEM_PRESSURE :
						break;
					case FUEL_RAIL_PRESSURE :
						break;
					case CONTROL_MODULE_VOLTAGE :
						break;
					default :
						break;
				}
			}
		}
	}
	
	private class TerminalKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int action = arg0.getKeyCode();
			
			switch(action){
				case KeyEvent.VK_ENTER:
					terminalInput.append(terminalOutput.getText() + "\n");
					
					//connection.send(terminalOutput.getText());
					//terminalInput.append(connection.read() + "\n");
					
					terminalOutput.setText("");
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * Default constructor for MainWindow.
	 */
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
	
	/**
	 * Fill the main window with a tabbed panel, containing 3 features for now.
	 */
	private void initiate(){
		connection = new ELM327Connection();
		add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	/**
	 * Build the big fat ass-booty of main window's tabbed panel.
	 * @return
	 */
	private JTabbedPane getCenterPanel(){
		tabbedMenu = new JTabbedPane();
		
		tabbedMenu.addTab("Real time informations", getRTIPanel());
		tabbedMenu.addTab("Vehicle error codes", getErrorCodesPanel());
		tabbedMenu.addTab("Terminal", getTerminalPanel());
		
		return tabbedMenu;
	}
	
	/**
	 * Build the real time informations panel.
	 * @return
	 */
	private JPanel getRTIPanel(){
		JPanel RTIPanel = new JPanel(new BorderLayout());
		
		vehicle = new VehicleCompatibility(connection.getOutputStream(), connection.getInputStream());
		
		String[] informations = {	
			"Pre-made panel", 
			"Speed", 
			"RPM", 
			"Consomation", 
			"Coolant temperature", 
			"Throttle position", 
			"HP", 
			"Fuel system status",
			"Evaporation system pressure",
			"Fuel rail pressure",
			"Control module voltage"};
		JList<String> infoList = new JList<String>(informations);
		infoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		infoList.addListSelectionListener(new InfoListListener(infoList));

		JScrollPane infoScroll = new JScrollPane(infoList);
		
		
		RTIPanel.add(infoScroll, BorderLayout.WEST);
		
		return RTIPanel;
	}
	
	/**
	 * Build the error codes panel.
	 * @return
	 */
	private JPanel getErrorCodesPanel(){
		JPanel errorCodesPanel = new JPanel();
		
		
		
		return errorCodesPanel;
	}
	
	/**
	 * Build the terminal panel.
	 * @return
	 */
	private JPanel getTerminalPanel(){		
		JPanel terminal = new JPanel(new BorderLayout());
		
		terminalOutput = new JTextField();
		terminalOutput.addKeyListener(new TerminalOutputKeyListener());
		
		terminalInput = new JTextArea();
		terminalInput.setEditable(false);
		JScrollPane terminalInputScroller = new JScrollPane(terminalInput);
		
		terminal.add(terminalOutput, BorderLayout.SOUTH);
		terminal.add(terminalInputScroller);
		
		return terminal;
	}
	
	public static void main(String[] args){
		MainWindow main = new MainWindow();
	}
}
