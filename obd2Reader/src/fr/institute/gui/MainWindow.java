package fr.institute.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import fr.institute.connection.Connection;
import fr.institute.connection.ELM327Connection;
import fr.institute.engine.RequestEngineModel;
import fr.institute.engine.RequestManager;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements MainWindowConstants{

	private Connection connection;
	private JMenu connect;
	private JMenu file;
	private JTabbedPane tabbedPanelChoice;
	private JTabbedPane tabbedPanelRecord;
	private JLabel disconnectedLabel;
	private JList<String> informationsList;
	private ArrayList<String> displayedInformations;
	private ArrayList<Boolean> recordedInformations;
	private JPanel connectedRTIPanel;
	private JPanel disconnectedRTIPanel;
	private JTextField terminalOutput;
	private JTextArea terminalInput;
	private RecordPanel recordPanel;
	private InformationPanel currentInfoPanel;
	private RequestEngineModel requestEngine;

	private class UpdateInformations implements Runnable {
		
		@Override
		public synchronized void run() {
			String name;
			float data;
			Date date;
			
			while(true){
				
				if(tabbedPanelChoice.getSelectedComponent().getName().matches("(Real time informations|Vehicle error codes)") && connect.getText().matches("disconnect")){
					for(int i=0; i<displayedInformations.size(); i++){
						if(recordedInformations.get(i)){
							name = displayedInformations.get(i);
							data = requestEngine.getUpToDateData(displayedInformations.get(i));
							date = new Date(System.currentTimeMillis());
							FileHandler.saveData(name, data, date);
						}
					}
					currentInfoPanel.updateGraphicPanel();
					currentInfoPanel.updateNumericPanel();					
				}										
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static class InfoCellRenderer implements ListCellRenderer{

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			String item = (String) value;

			JLabel cell = new JLabel();
			
			cell.setText(item);

			cell.setIconTextGap(10); 
			cell.setFont(new Font(GLOBAL_FONT_NAME,Font.PLAIN,17)); 
			cell.setForeground(Color.GREEN);

			if (isSelected) {
				cell.setBorder(new LineBorder(DARK_GREEN));
				cell.setBackground(DARK_GREEN);
			}
			else{
				cell.setBorder(new LineBorder(list.getBackground()));
				cell.setBackground(list.getBackground());
			}

			cell.setOpaque(true);
			
			return cell;
		}
		
	}
	
	private class RecordFrameActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e){
			recordPanel = new RecordPanel(displayedInformations, recordedInformations);			
		}
		
	}
	
	private class GlobalMenuListener implements MenuListener{

		@Override
		public void menuCanceled(MenuEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void menuDeselected(MenuEvent arg0) {
			
		}

		@Override
		public void menuSelected(MenuEvent arg0) {			
			String text = arg0.toString().substring(arg0.toString().indexOf("text=") + 5, arg0.toString().indexOf("]]"));
			
			switch(text){
				case MENU_CONNECT : 
					//rather than this, find an animation to put somewhere in the GUI so that the user k	now something is processing. 
					JOptionPane.showMessageDialog(null, "Trying to connect to your car...", "Connecting", JOptionPane.INFORMATION_MESSAGE);
					if(connection.connect()){
						file.setEnabled(true);
						requestEngine = new RequestManager(connection.getOutputStream(), connection.getInputStream());
						
						String[] dataArray = requestEngine.getCompatibleRequests();
						displayedInformations = new ArrayList<String>();
						recordedInformations = new ArrayList<Boolean>();
						displayedInformations.addAll(Arrays.asList(dataArray));
						for(int i=0; i<displayedInformations.size(); i++){
							recordedInformations.add(false);
						}
						informationsList.setListData(dataArray);
						
						connect.setName(MENU_DISCONNECT);
						tabbedPanelChoice.setComponentAt(0, connectedRTIPanel);
					}
					else{
						JOptionPane.showMessageDialog(null, "Connection failed :(", "Interrupted", JOptionPane.ERROR_MESSAGE);
					}
					break;
				case MENU_DISCONNECT :
					connection.disconnect();
					file.setEnabled(false);
					connect.setName(MENU_CONNECT);
					break;
			}			
		}
	}

	private class InformationsListListener implements ListSelectionListener{

		private JList<String> informationsList;
		
		public InformationsListListener(JList<String> informationsList){
			this.informationsList = informationsList;
		}
		
		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			if(!arg0.getValueIsAdjusting()){
				currentInfoPanel = new InformationPanel(informationsList.getSelectedValue());
			}
		}
		
	}
	
	private class TerminalOutputKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
				terminalInput.append(terminalOutput.getText().substring(1));
				connection.send(terminalOutput.getText().substring(1));
				terminalInput.append(connection.read() + "\n");
				terminalOutput.setText(">");
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			
		}

	}
	
	public MainWindow(){
		super(APPLICATION_NAME);
		
		connection = new ELM327Connection();
		
		customLookAndFeel();
		initiateComponents();
		initiateAspect();
		
		Thread updateInformation = new Thread(new UpdateInformations());
		updateInformation.start();
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void customLookAndFeel(){
	
		setBackground(Color.DARK_GRAY);
		
		UIManager.put("TabbedPane.highlight", Color.DARK_GRAY);
		UIManager.put("TabbedPane.light", Color.DARK_GRAY);
		UIManager.put("TabbedPane.darkShadow", Color.DARK_GRAY);
		UIManager.put("TabbedPane.shadow", Color.DARK_GRAY);
		UIManager.put("TabbedPane.selected", DARK_GREEN);
		UIManager.put("TabbedPane.focus",  DARK_GREEN);
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
		
		UIManager.put("List.selectionBackground", Color.DARK_GRAY);
		UIManager.put("List.selectionForeground", Color.GREEN);
		UIManager.put("List.focus", Color.DARK_GRAY);
		UIManager.put("List.border", NEUTRAL_DARK_GRAY_BORDER);
		
		UIManager.put("TextField.border", NEUTRAL_DARK_GRAY_BORDER);
		UIManager.put("TextField.background", DARK_GREEN);
		UIManager.put("TextField.caretForeground", DARK_GREEN);
		UIManager.put("TextField.caretBlinkRate", CARET_BLINK_RATE);
		
		UIManager.put("TextArea.border", NEUTRAL_DARK_GRAY_BORDER);
		UIManager.put("TextArea.margin", new Insets(0,0,0,0));
		
		UIManager.put("Panel.background", Color.DARK_GRAY);
		UIManager.put("Panel.border", NEUTRAL_DARK_GRAY_BORDER);
		UIManager.put("Panel.font", new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		UIManager.put("Panel.foreground", Color.GREEN);

		UIManager.put("OptionPane.background", Color.DARK_GRAY);
		UIManager.put("OptionPane.border", NEUTRAL_DARK_GRAY_BORDER);
		UIManager.put("OptionPane.buttonAreaBorder", NEUTRAL_DARK_GRAY_BORDER);
		UIManager.put("OptionPane.okButtonText", "    Ok    ");
		UIManager.put("OptionPane.buttonFont",  new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		UIManager.put("OptionPane.foreground", Color.GREEN);
		UIManager.put("OptionPane.messageFont", new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		UIManager.put("OptionPane.messageForeground", Color.GREEN);
		UIManager.put("OptionPane.messageAreaBorder", NEUTRAL_DARK_GRAY_BORDER);
		
		UIManager.put("Button.background", Color.GREEN);
		UIManager.put("Button.border", NEUTRAL_GREEN_BORDER);
		UIManager.put("Button.font", new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		UIManager.put("Button.foreground", Color.DARK_GRAY);
		UIManager.put("Button.select", Color.GREEN);
		
		UIManager.put("MenuBar.background", Color.DARK_GRAY);
		UIManager.put("MenuBar.borderColor", Color.DARK_GRAY);
		/*UIManager.put("MenuBar.darkShadow", Color.DARK_GRAY);
		UIManager.put("MenuBar.highlight", Color.DARK_GRAY);
		UIManager.put("MenuBar.shadow", Color.DARK_GRAY);*/
		
		UIManager.put("Menu.foreground", Color.GREEN);
		UIManager.put("Menu.disabledForeground", TEMPERED_GREEN);
		UIManager.put("Menu.selectionBackground", DARK_GREEN);
		UIManager.put("Menu.selectionForeground", Color.GREEN);
		UIManager.put("Menu.pressedItemF", DARK_GREEN);
		UIManager.put("Menu.pressedItemB", DARK_GREEN);
		UIManager.put("Menu.border", NEUTRAL_DARK_GRAY_BORDER);


		UIManager.put("MenuItem.disabledForeground", TEMPERED_GREEN);
		UIManager.put("MenuItem.foreground", Color.GREEN);
		UIManager.put("MenuItem.background", Color.DARK_GRAY);
		UIManager.put("MenuItem.selectionForeground", Color.GREEN);
		UIManager.put("MenuItem.selectionBackground", DARK_GREEN);
		//UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder(0,0,0,0));
		UIManager.put("MenuItem.margin", new Insets(10,10,10,10));





	}
	
	public void initiateAspect(){
		Toolkit tkt = Toolkit.getDefaultToolkit();
		Dimension itemWindowDim = tkt.getScreenSize();
		int height = (int)itemWindowDim.getHeight();
		int width = (int)itemWindowDim.getWidth();
		this.setBounds(width/2-width/4, height/2-height/4, width/2, height/2);
		
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font terminalLikeFont = Font.createFont(Font.TRUETYPE_FONT, new File(GLOBAL_FONT_FILE_PATH));
			ge.registerFont(terminalLikeFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
			
		terminalOutput.setBackground(Color.DARK_GRAY);
		terminalOutput.setCaretColor(Color.GREEN);
		terminalOutput.setForeground(Color.GREEN);
		terminalOutput.putClientProperty("caretWidth", 6);
		terminalOutput.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		terminalOutput.setBorder(NEUTRAL_DARK_GRAY_BORDER);
		
		terminalInput.setEditable(false);
		terminalInput.setBackground(Color.DARK_GRAY);
		terminalInput.setForeground(Color.GREEN);
		terminalInput.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		terminalInput.setBorder(NEUTRAL_DARK_GRAY_BORDER);
	
		tabbedPanelChoice.setOpaque(true);
		tabbedPanelChoice.setBackground(Color.DARK_GRAY);
		tabbedPanelChoice.setForeground(Color.GREEN);
		tabbedPanelChoice.setFont(new Font(GLOBAL_FONT_NAME, Font.PLAIN, 17));
		
		informationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		informationsList.setBackground(Color.DARK_GRAY);
		informationsList.setForeground(Color.GREEN);
		informationsList.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		
	}
	
	public void initiateComponents(){	
		setJMenuBar();
		add(getMainPanel());
		
	}
	
	public void setJMenuBar(){
		JMenuBar menu = new JMenuBar();
		
		file = new JMenu(MENU_FILE);
		connect = new JMenu(MENU_CONNECT);
		JMenu options = new JMenu(MENU_OPTIONS);
		connect.addMenuListener(new GlobalMenuListener());
		
		JMenuItem save = new JMenuItem(MENU_FILE_RECORD);
		save.addActionListener(new RecordFrameActionListener());
		JMenuItem language = new JMenuItem(MENU_OPTION_LANGUAGE);
		
		file.add(save);
		options.add(language);
		
		menu.add(file);
		menu.add(connect);
		menu.add(options);
		
		this.setJMenuBar(menu);		
	}
	
	public JTabbedPane getMainPanel(){		
		
		tabbedPanelChoice = new JTabbedPane();
		
		buildDisconnectedRTIPanel();
		buildConnectedRTIPanel();
		
		tabbedPanelChoice.addTab("Real time informations", disconnectedRTIPanel);
		tabbedPanelChoice.addTab("Vehicle error codes", getErrorCodesPanel());
		tabbedPanelChoice.addTab("Terminal", getTerminalPanel());
				
		return tabbedPanelChoice;
	}
	
	public void buildDisconnectedRTIPanel(){
		disconnectedRTIPanel = new JPanel(new BorderLayout());
		
		disconnectedLabel = new JLabel("Not connected to any device!");
		disconnectedLabel.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		disconnectedLabel.setForeground(Color.GREEN);
		disconnectedLabel.setHorizontalAlignment(JLabel.CENTER);
			
		//disconnected.add(empty1, BorderLayout.WEST); put a sugoi animation here. 
		disconnectedRTIPanel.add(disconnectedLabel, BorderLayout.CENTER);
		disconnectedRTIPanel.setName(REAL_TIME_INFORMATIONS_PANEL);
	}
	
	@SuppressWarnings("unchecked")
	public void buildConnectedRTIPanel(){
		connectedRTIPanel = new JPanel(new BorderLayout());
		
		String[] informations = {"              "};
		informationsList = new JList<String>(informations);
		informationsList.setCellRenderer(new InfoCellRenderer());
		
		informationsList.addListSelectionListener(new InformationsListListener(informationsList));
		
		JScrollPane infoScroll = new JScrollPane(informationsList);		
		
		currentInfoPanel = new InformationPanel("n�ant");
		
		connectedRTIPanel.add(currentInfoPanel);
		connectedRTIPanel.add(infoScroll, BorderLayout.WEST);
		connectedRTIPanel.setName(REAL_TIME_INFORMATIONS_PANEL);
	}
	
	public JPanel getErrorCodesPanel(){
		JPanel errorCodesPanel = new JPanel();
		
		
		errorCodesPanel.setName(ERROR_CODE_PANEL);
		
		return errorCodesPanel;
	}
	
	public JPanel getTerminalPanel(){
		JPanel terminal = new JPanel(new BorderLayout());
		
		terminalOutput = new JTextField(">");
		terminalOutput.addKeyListener(new TerminalOutputKeyListener());
		
		terminalInput = new JTextArea();
		JScrollPane terminalInputScroller = new JScrollPane(terminalInput);
		
		terminal.add(terminalOutput, BorderLayout.SOUTH);
		terminal.add(terminalInputScroller);
		
		terminal.setName("Terminal");
		
		return terminal;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args){
		MainWindow main = new MainWindow();
	}
	
}
