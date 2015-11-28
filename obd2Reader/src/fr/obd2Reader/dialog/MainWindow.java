package fr.obd2Reader.dialog;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import fr.obd2Reader.command.FuelSystemStatusCommand;
import fr.obd2Reader.command.VehicleCompatibility;
import fr.obd2Reader.command.pressure.EvaporationSystemVaporPressureCommand;
import fr.obd2Reader.command.pressure.FuelRailPressureCommand;
import fr.obd2Reader.command.speed.SpeedCommand;
import fr.obd2Reader.command.temperature.EngineCoolantTemperatureCommand;
import fr.obd2Reader.command.voltage.ControlModuleVoltageCommand;
import fr.obd2Reader.connection.ELM327Connection;

/**
 * Main window displayed at the user. Contains all the shit you hear me.
 * @author Supa Kanojo Hunta
 *
 */
public class MainWindow extends JFrame implements RTIPanelConstants, TerminalControllerConstants{

	private VehicleCompatibility vehicle;
	private ELM327Connection connection;
	private JTabbedPane tabbedMenu;
	private JTextField terminalOutput;
	private JTextArea terminalInput;
	private JPanel RTIPanel;
	private InformationPanel currentInfoPanel;
	private boolean currentInfoPanelIsDisplayed;
	private ArrayList<InformationPanel> customInfoPanel;
	private boolean customInfoPanelIsDisplayed;
	private Thread informationsUpdate;
	
	private class UpdateInformations implements Runnable{
		
		public synchronized void run(){
			double time = 0;
			while(true){
				try {
					if(currentInfoPanelIsDisplayed){
						currentInfoPanel.getCommand().compute();
						currentInfoPanel.updateNumericPanel(currentInfoPanel.getCommand().getData());
						currentInfoPanel.updateGraphicPanel(currentInfoPanel.getCommand().getData(), time);
						wait(250);
						time += 0.25;
					}
					else if(customInfoPanelIsDisplayed){
						for(int i = 0; i < customInfoPanel.size(); i++){
							customInfoPanel.get(i).getCommand().compute();
							customInfoPanel.get(i).updateNumericPanel(customInfoPanel.get(i).getCommand().getData());
							customInfoPanel.get(i).updateGraphicPanel(customInfoPanel.get(i).getCommand().getData(), time);
						}
						wait(250);
						time += 0.25;
					}
					else
						informationsUpdate.interrupt();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class InfoListListener implements ListSelectionListener{
		private JList<String> list;
		
		public InfoListListener (JList<String> list){
			this.list = list;
		}
		
		public void valueChanged(ListSelectionEvent e){
			if(!e.getValueIsAdjusting()){
				
				switch(list.getSelectedIndex()){
					case NO_SELECTION :
						currentInfoPanelIsDisplayed = false;
						customInfoPanelIsDisplayed = false;
						break;
					default :
						RTIPanel.add(currentInfoPanel);
						
						if(informationsUpdate.isInterrupted() || !informationsUpdate.isAlive())
							informationsUpdate.start();
						
						switch(list.getSelectedIndex()){
						
							case PRE_MADE_PANEL :
								currentInfoPanelIsDisplayed = false;
								customInfoPanelIsDisplayed = true;
								break;
							default :
								currentInfoPanelIsDisplayed = true;
								customInfoPanelIsDisplayed = false;
								
								switch(list.getSelectedIndex()){						
									case SPEED :
										currentInfoPanel.setCommand(new SpeedCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									case RPM :
										currentInfoPanel.setCommand(new RPMCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									case CONSOMATION :
										currentInfoPanel.setCommand(new ConsommationCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									case COOLANT_TEMPERATURE :
										currentInfoPanel.setCommand(new EngineCoolantTemperatureCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									case THROTTLE_POSITION :
										currentInfoPanel.setCommand(new ThrottlePositionCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									case HP :
										currentInfoPanel.setCommand(new HPCommand(connection.getOutputStream(), connection.getInputStream()))
										break;
										//Should not exist in this form. Does not have a float type data /!\ Find something or kick it back to hell.
										/*
									case FUEL_SYSTEM_STATUS :
										currentInfoPanel.setCommand(new FuelSystemStatusCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
										*/
									case EVAPORATION_SYSTEM_PRESSURE :
										currentInfoPanel.setCommand(new EvaporationSystemVaporPressureCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									case FUEL_RAIL_PRESSURE :
										currentInfoPanel.setCommand(new FuelRailPressureCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									case CONTROL_MODULE_VOLTAGE :
										currentInfoPanel.setCommand(new ControlModuleVoltageCommand(connection.getOutputStream(), connection.getInputStream()));
										break;
									default :
										currentInfoPanelIsDisplayed = false;
								}	
								break;	
						}
				}
			}
		}
	}
	
	private class TerminalOutputKeyListener implements KeyListener{

		private TerminalController terminalController;
		
		public TerminalOutputKeyListener(TerminalController terminalController){
			super();
			this.terminalController = terminalController;
		}
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			int action = arg0.getKeyCode();
			
			switch(action){
				case KeyEvent.VK_ENTER:
					terminalInput.append(terminalOutput.getText() + "\n");
					
					/*switch(terminalController.analyzeInput(request)){
					case MISC_REQUEST:
						terminalController.send(request);
						terminalInput.append(terminalController.read() + "\n");
						break;
					case IS_COMPATIBLE_MAN:
						terminalInput.append(terminalController.getIsCompatibleMan() + "\n");
						break;
				}*/
					
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
		UIManagerCustom();
		initiate();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void UIManagerCustom(){
		this.setBackground(Color.DARK_GRAY);
		Color darkGreen = new Color(50,100,50);
		UIManager.put("TabbedPane.highlight", Color.DARK_GRAY);
		UIManager.put("TabbedPane.light", Color.DARK_GRAY);
		UIManager.put("TabbedPane.darkShadow", Color.DARK_GRAY);
		UIManager.put("TabbedPane.shadow", Color.DARK_GRAY);
		UIManager.put("TabbedPane.selected", darkGreen);
		UIManager.put("TabbedPane.focus",  darkGreen);
		UIManager.put("TabbedPane.borderHightlightColor", Color.DARK_GRAY);
		UIManager.put("TabbedPane.contentAreaColor", Color.DARK_GRAY);
		UIManager.put("TabbedPane.background", Color.DARK_GRAY);
		UIManager.put("TabbedPane.selectHighlight", Color.DARK_GRAY);
		UIManager.put("TabbedPane.selectedLight", Color.DARK_GRAY);
		UIManager.put("TabbedPane.tabAreaBackground", Color.DARK_GRAY);
		UIManager.put("TabbedPane.unselectedBackground", Color.DARK_GRAY);
		UIManager.put("TabbedPane.unselectedTabBackground", Color.DARK_GRAY);
		UIManager.put("TabbedPane.unselectedTabHighlight", Color.DARK_GRAY);
		UIManager.put("TabbedPane.unselectedTabShadow", Color.DARK_GRAY);
		
		UIManager.put("List.selectionBackground", darkGreen);
		UIManager.put("List.selectionForeground", Color.GREEN);
		UIManager.put("List.focus", Color.DARK_GRAY);
		
		UIManager.put("TextField.border", BorderFactory.createMatteBorder(1, 1, 1, 1,Color.DARK_GRAY));
		UIManager.put("TextField.background", darkGreen);
		UIManager.put("TextField.caretForeground", Color.GREEN);
		UIManager.put("TextField.caretBlinkRate", 1000);
		
		UIManager.put("TextArea.border", BorderFactory.createMatteBorder(1, 1, 1, 1,Color.DARK_GRAY));
		
	}
	
	/**
	 * Fill the main window with a tabbed panel, containing 3 features for now.
	 */
	private void initiate(){
		//connection = new ELM327Connection();		

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font terminalLikeFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fr/obd2Reader/dialog/ShareTechMono_Regular.ttf"));
			ge.registerFont(terminalLikeFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
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
		tabbedMenu.setOpaque(true);
		tabbedMenu.setBackground(Color.DARK_GRAY);
		tabbedMenu.setForeground(Color.GREEN);
		tabbedMenu.setFont(new Font("Share Tech Mono", Font.PLAIN, 15));
		
		return tabbedMenu;
	}
	
	/**
	 * Build the real time informations panel.
	 * @return
	 */
	private JPanel getRTIPanel(){
		RTIPanel = new JPanel(new BorderLayout());
		
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
		infoList.setBackground(Color.DARK_GRAY);
		infoList.setForeground(Color.GREEN);
		infoList.setFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		
		//infoList.addListSelectionListener(new InfoListListener(infoList));
		
		JScrollPane infoScroll = new JScrollPane(infoList);		
		
		vehicle = new VehicleCompatibility();//connection.getOutputStream(), connection.getInputStream());
		currentInfoPanel = new InformationPanel(vehicle, "Current Displayed Information");
		
		RTIPanel.add(currentInfoPanel);
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
		//TerminalController terminalController = new TerminalController(connection);
		TerminalController terminalController = new TerminalController();

		
		terminalOutput = new JTextField();
		terminalOutput.addKeyListener(new TerminalOutputKeyListener(terminalController));
		terminalOutput.setForeground(Color.GREEN);
		terminalOutput.putClientProperty("caretWidth", 7);
		terminalOutput.setFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		
		terminalInput = new JTextArea();
		terminalInput.setEditable(false);
		terminalInput.setBackground(Color.DARK_GRAY);
		terminalInput.setForeground(Color.GREEN);
		terminalInput.setFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		JScrollPane terminalInputScroller = new JScrollPane(terminalInput);
		
		terminal.add(terminalOutput, BorderLayout.SOUTH);
		terminal.add(terminalInputScroller);
		
		return terminal;
	}
	
	public static void main(String[] args){
		MainWindow main = new MainWindow();
	}

}
