package fr.institute.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

import fr.institute.connection.Connection;
import fr.institute.connection.ELM327Connection;
import fr.institute.engine.RequestEngineModel;
import fr.institute.engine.RequestManager;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements MainWindowConstants{

	private Connection connection;
	private RequestEngineModel requestEngine;

	private JMenu connect;
	private JMenu file;
	private JTabbedPane tabbedPanelChoice;

	private JPanel RTIPanel;
	private ArrayList<String> displayedInformations;
	private ArrayList<Boolean> recordedInformations;
	private JList<String> informationsList;
	private JPanel errorCodesPanel;
	private JTextArea errorCodesArea;
	private JPanel terminal;
	private JTextField terminalOutput;
	private JTextArea terminalInput;
	private JPanel dashboard;
	private JPanel disconnectedPanel;
	
	private InformationPanel currentInfoPanel;
	
	private class aScrollBar extends  JScrollBar{
		public aScrollBar(){
			super();
			setUI(new CustomedScrollBarUI());
		}
	}
	
	private class CustomedScrollBarUI extends BasicScrollBarUI{
		protected void paintTrack (Graphics g, JComponent c, Rectangle trackBounds){
			
		}
		
		protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds){
			
			BufferedImage thumbImg;
			try {
				thumbImg = ImageIO.read(new File("vault_boy_icon.png"));
				
				if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
					return;
				}
				g.translate(thumbBounds.x, thumbBounds.y);
				g.drawRect(0, 0, thumbBounds.width - 2, thumbBounds.height - 1);
				AffineTransform transform = AffineTransform.getScaleInstance((double) thumbBounds.width
						/ thumbImg.getWidth(null), (double) thumbBounds.height / thumbImg.getHeight(null));
				((Graphics2D) g).drawImage(thumbImg, transform, null);
				g.translate(-thumbBounds.x, -thumbBounds.y);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class RecordFrame extends JFrame{
		
		private ArrayList<JCheckBox> recordedBox;
		private ArrayList<JCheckBox> nonRecordedBox;
		private JPanel recordedList;
		private JPanel nonRecordedList;
		
		//for test only
		public RecordFrame(ArrayList<String> p_names, ArrayList<Boolean> p_record){
			super();
			
			displayedInformations = p_names;
			recordedInformations = p_record;
		
			add(getMainPanel());
			
			Toolkit tkt = Toolkit.getDefaultToolkit();
			Dimension itemWindowDim = tkt.getScreenSize();
			int height = (int)itemWindowDim.getHeight();
			int width = (int)itemWindowDim.getWidth();
			setBounds(width/2-width/4, height/2-height/4, width/2, height/2);
		
			setVisible(true);
		}

		public RecordFrame(){
			super();
			
			add(getMainPanel());
		
			Toolkit tkt = Toolkit.getDefaultToolkit();
			Dimension itemWindowDim = tkt.getScreenSize();
			int height = (int)itemWindowDim.getHeight();
			int width = (int)itemWindowDim.getWidth();
			setBounds(width/2-width/4, height/2-height/4, width/2, height/2);
		
			setVisible(true);
		}
		
		public JPanel getMainPanel(){
			JPanel mainPanel = new JPanel(new GridLayout(2,1));
			
			mainPanel.add(getRecordedPanel());
			mainPanel.add(getNonRecordedPanel());
			mainPanel.add(getHistogramPanel());
			
			return mainPanel;
		}

		public JPanel getHistogramPanel(){
			JPanel histogramPanel = new JPanel();
			
			
			
			return histogramPanel;
		}
		
		public JPanel getNonRecordedPanel(){
			JPanel nonRecordedPanel = new JPanel(new BorderLayout());
			JPanel buttonPanel = new JPanel(new GridLayout(1,3));
			
			nonRecordedList = new JPanel();
			nonRecordedList.setLayout(new BoxLayout(nonRecordedList, BoxLayout.Y_AXIS));
			nonRecordedBox = new ArrayList<JCheckBox>();
			JButton fill1 = new JButton();
			JButton fill2 = new JButton();
			fill1.setVisible(false);
			fill2.setVisible(false);
			JButton stopRecordButton = new JButton("stop record");
			stopRecordButton.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 14));
			stopRecordButton.addActionListener(new RecordButtonListener());	
					
			JCheckBox currentInfoBox;
			for(String infoName : getInfoType(false)){
				currentInfoBox = new JCheckBox(infoName);
				nonRecordedBox.add(currentInfoBox);
				nonRecordedList.add(currentInfoBox);
			}
					
			JScrollPane scroll = new JScrollPane(nonRecordedList);
			
			buttonPanel.add(fill1);
			buttonPanel.add(stopRecordButton);
			buttonPanel.add(fill2);
			
			nonRecordedPanel.add(scroll, BorderLayout.CENTER);
			nonRecordedPanel.add(buttonPanel, BorderLayout.SOUTH);
			
			return nonRecordedPanel;
		}
		
		public JPanel getRecordedPanel(){
			JPanel recordedPanel = new JPanel(new BorderLayout());
			JPanel buttonPanel = new JPanel(new GridLayout(1,3));
			
			recordedList = new JPanel();
			recordedList.setLayout(new BoxLayout(recordedList, BoxLayout.Y_AXIS));
			recordedBox = new ArrayList<JCheckBox>();
			JButton fill1 = new JButton();
			JButton fill2 = new JButton();
			fill1.setVisible(false);
			fill2.setVisible(false);
			JButton startRecordButton = new JButton("start record");
			startRecordButton.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 14));
			startRecordButton.addActionListener(new RecordButtonListener());	
					
			JCheckBox currentInfoBox;
			for(String infoName : getInfoType(true)){
				currentInfoBox = new JCheckBox(infoName);
				recordedBox.add(currentInfoBox);
				recordedList.add(currentInfoBox);
			}
					
			JScrollPane scroll = new JScrollPane(recordedList);
			buttonPanel.add(fill1);
			buttonPanel.add(startRecordButton);
			buttonPanel.add(fill2);
			
			
			recordedPanel.add(scroll, BorderLayout.CENTER);
			recordedPanel.add(buttonPanel, BorderLayout.SOUTH);
			
			
			return recordedPanel;
		}
		
		private ArrayList<String> getInfoType(boolean recorded){
			ArrayList<String> infoTypeList = new ArrayList<String>();
			
			
			for(int i=0; i<displayedInformations.size(); i++){
				if(recordedInformations.get(i)==recorded){
					infoTypeList.add(displayedInformations.get(i));
				}
			}
			
			return infoTypeList;
		}
		
		public ArrayList<JCheckBox> selectedIndex(boolean recorded){
			ArrayList<JCheckBox> selectedIndex = new ArrayList<JCheckBox>();
			
			if(recorded){
				for(int i=0; i<recordedBox.size(); i++){
					if(recordedBox.get(i).isSelected()){
						selectedIndex.add(recordedBox.get(i));
					}
				}
			}
			else{
				for(int i=0; i<nonRecordedBox.size(); i++){
					if(nonRecordedBox.get(i).isSelected()){
						selectedIndex.add(nonRecordedBox.get(i));
					}
				}
			}
			
			return selectedIndex;
		}
		
		private class RecordButtonListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch(arg0.getActionCommand()){
					case "start record":
						for(JCheckBox selectedRec : selectedIndex(true)){
							selectedRec.setSelected(false);
							recordedBox.remove(selectedRec);
							recordedList.remove(selectedRec);
							nonRecordedBox.add(selectedRec);
							nonRecordedList.add(selectedRec);
							
							recordedInformations.set(displayedInformations.indexOf(selectedRec.getText()), true);
						}					
						break;
					case "stop record":
						for(JCheckBox selectedRec : selectedIndex(false)){
							selectedRec.setSelected(false);
							nonRecordedBox.remove(selectedRec);
							nonRecordedList.remove(selectedRec);
							recordedBox.add(selectedRec);
							recordedList.add(selectedRec);
							
							recordedInformations.set(displayedInformations.indexOf(selectedRec.getText()), false);
						}
				}
				recordedList.revalidate();
				recordedList.repaint();
				nonRecordedList.revalidate();
				nonRecordedList.repaint();
			}
			
		}
	}

	private class UpdateInformations implements Runnable {
		public static final int SECOND = 1000;
		
		@Override
		public synchronized void run() {
			String name;
			float data;
			Date date;
			int msDelay = 100;
			
			while(true){
				if(tabbedPanelChoice.getSelectedComponent().getName().matches("(Real time informations|Vehicle error codes|Dashboard)") && connect.getText().matches("disconnect")){
					for(int i=0; i<displayedInformations.size(); i++){
						if(recordedInformations.get(i)){
							name =displayedInformations.get(i);
							data = requestEngine.getUpToDateData(displayedInformations.get(i));
							date = new Date(System.currentTimeMillis());
							FileHandler.saveData(name, data, date);
						}
					}
					
					currentInfoPanel.updateNumericPanel();					
				}										
				currentInfoPanel.randomUpdateGraphicPanel(SECOND / msDelay);
				try {
					wait(msDelay);
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
	
	private class GlobalMenuListener implements MenuListener{

		@Override
		public void menuCanceled(MenuEvent arg0) {

		}

		@Override
		public void menuDeselected(MenuEvent arg0) {
			
		}

		@Override
		public void menuSelected(MenuEvent arg0) {			
			String text = arg0.toString().substring(arg0.toString().indexOf("text=") + 5, arg0.toString().indexOf("]]"));
			
			switch(text){
				case MENU_CONNECT : 
					//rather than this, find an animation to put somewhere in the GUI so that the user now something is processing. 
					JOptionPane.showMessageDialog(null, "We will try to connect to your car. Press ok to continue.", "Connecting", JOptionPane.INFORMATION_MESSAGE);
					if(connection.connect()){
						file.setEnabled(true);
						System.out.println("dataArray");
						requestEngine = new RequestManager(connection);
						
						String[] dataArray = requestEngine.getCompatibleRequests();
						displayedInformations = new ArrayList<String>();
						recordedInformations = new ArrayList<Boolean>();
						displayedInformations.addAll(Arrays.asList(dataArray));
						for(int i=0; i<displayedInformations.size(); i++){
							recordedInformations.add(false);
						}
						informationsList.setListData(dataArray);
						
						connect.setName(MENU_DISCONNECT);
						
						tabbedPanelChoice.setComponentAt(0, dashboard);
						tabbedPanelChoice.setComponentAt(1, RTIPanel);
						tabbedPanelChoice.setComponentAt(2, errorCodesPanel);
						tabbedPanelChoice.setComponentAt(3, terminal);
					}
					else{
						JOptionPane.showMessageDialog(null, "Connection failed :(", "Interrupted", JOptionPane.ERROR_MESSAGE);
					}
					break;
				case MENU_DISCONNECT :
					connection.disconnect();
					file.setEnabled(false);
					connect.setName(MENU_CONNECT);
					
					for(int i=0; i<tabbedPanelChoice.getTabCount(); i++){
						tabbedPanelChoice.setComponentAt(i, disconnectedPanel);
					}
					
					break;
			}			
		}
	}
	
	private class refreshErrorButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			refreshErrorCodesArea();
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
		
		UIManager.put("CheckBox.background", Color.DARK_GRAY);
		UIManager.put("CheckBox.foreground", Color.GREEN);
		UIManager.put("CheckBox.border", NEUTRAL_DARK_GRAY_BORDER);
		UIManager.put("CheckBox.focus", Color.DARK_GRAY);
		UIManager.put("CheckBox.font", new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 14));
		
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
		setBounds(0, 0, width, height);
		
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
		
		errorCodesArea.setEditable(false);
		errorCodesArea.setBackground(Color.DARK_GRAY);
		errorCodesArea.setForeground(Color.GREEN);
		errorCodesArea.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		errorCodesArea.setBorder(NEUTRAL_DARK_GRAY_BORDER);
		
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
		file.setEnabled(false);
		connect.addMenuListener(new GlobalMenuListener());
		
		JMenuItem save = new JMenuItem(MENU_FILE_RECORD);
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//For test only
				/*ArrayList<String> infosList = new ArrayList<String>();
				ArrayList<Boolean> is_recorded = new ArrayList<Boolean>();
				
				for(int i=0; i<30; i++){
					infosList.add("info " + i);
				}
				for(int i=0; i<2; i++){
					if(i%2 == 0){
						for(int j=0; j<15; j++){
							is_recorded.add(true);
						}
					}
					else{
						for(int j=0; j<15; j++){
							is_recorded.add(false);
						}
					}
				}
				new RecordFrame(infosList, is_recorded);*/
				//end test part
				new RecordFrame();
			}
			
		});
		
		file.add(save);
		
		menu.add(file);
		menu.add(connect);
		
		this.setJMenuBar(menu);		
	}
	
	
	public JTabbedPane getMainPanel(){		
		
		tabbedPanelChoice = new JTabbedPane();
		
		buildDashboardPanel();
		buildRTIPanel();
		buildErrorCodesPanel();
		buildTerminalPanel();
		
		tabbedPanelChoice.addTab("Dashboard", disconnectedPanel());
		tabbedPanelChoice.addTab("Real time informations", disconnectedPanel());
		tabbedPanelChoice.addTab("Vehicle error codes", disconnectedPanel());
		tabbedPanelChoice.addTab("Terminal", disconnectedPanel());
		
				
		return tabbedPanelChoice;
	}
	
	public JPanel disconnectedPanel(){
		 JPanel disconnectedPanel = new JPanel(new BorderLayout());
		
		JLabel disconnectedLabel = new JLabel("Not connected to any device!");
		disconnectedLabel.setFont(new Font(GLOBAL_FONT_NAME, Font.TRUETYPE_FONT, 17));
		disconnectedLabel.setForeground(Color.GREEN);
		disconnectedLabel.setHorizontalAlignment(JLabel.CENTER);
			
		//disconnected.add(empty1, BorderLayout.WEST); put a sugoi animation here. 
		disconnectedPanel.add(disconnectedLabel, BorderLayout.CENTER);
		disconnectedPanel.setName(REAL_TIME_INFORMATIONS_PANEL);
		
		return disconnectedPanel;
	}
	
	@SuppressWarnings("unchecked")
	public void buildRTIPanel(){
		RTIPanel = new JPanel(new BorderLayout());
		
		String[] informations = {"              "};
		informationsList = new JList<String>(informations);
		informationsList.setCellRenderer(new InfoCellRenderer());
		
		informationsList.addListSelectionListener(new InformationsListListener(informationsList));
		
		JScrollPane infoScroll = new JScrollPane(informationsList);		
		
		currentInfoPanel = new InformationPanel("néant");
		
		RTIPanel.add(currentInfoPanel);
		RTIPanel.add(infoScroll, BorderLayout.WEST);
		RTIPanel.setName(REAL_TIME_INFORMATIONS_PANEL);
	}
	
	public void refreshErrorCodesArea(){
		if(requestEngine != null){
			errorCodesArea.setText("");
			for(String currentCode : requestEngine.getErrorCodes())
				errorCodesArea.append(currentCode);
		
		}
		if(errorCodesArea.getText().isEmpty() || errorCodesArea.getText().matches("No error code to display"))
			errorCodesArea.setText("No error code to display");
		
	}
	public JPanel buildErrorCodesPanel(){
		errorCodesPanel = new JPanel(new BorderLayout());
		errorCodesArea = new JTextArea();
		
		JScrollPane errorCodesAreaScroller = new JScrollPane(errorCodesArea);
				
		JPanel southPanel = new JPanel(new GridLayout(1,3));
		JButton refresh = new JButton("refresh");
		JButton empty1 = new JButton();
		empty1.setVisible(false);
		JButton empty2 = new JButton();
		empty2.setVisible(false);
		
		refresh.addActionListener(new refreshErrorButtonListener());
		southPanel.add(refresh);
		southPanel.add(empty1);
		southPanel.add(empty2);
		
		errorCodesPanel.add(errorCodesAreaScroller, BorderLayout.CENTER);
		errorCodesPanel.add(southPanel, BorderLayout.SOUTH);
		errorCodesPanel.setName(ERROR_CODE_PANEL);
		
		return errorCodesPanel;
	}
	
	public JPanel buildTerminalPanel(){
		terminal = new JPanel(new BorderLayout());
		
		terminalOutput = new JTextField(">");
		terminalOutput.addKeyListener(new TerminalOutputKeyListener());
		
		terminalInput = new JTextArea();
		JScrollPane terminalInputScroller = new JScrollPane(terminalInput);
		
		terminal.add(terminalOutput, BorderLayout.SOUTH);
		terminal.add(terminalInputScroller);
		
		terminal.setName("Terminal");
		
		return terminal;
	}
	
	public JPanel buildDashboardPanel(){
		dashboard = new JPanel (new GridLayout(2, 3));
		
		dashboard.add(new InformationPanel("Engine fuel rate"));
		dashboard.add(new InformationPanel("Vehicle Speed"));
		dashboard.add(new InformationPanel("Calculated engine load value"));
		dashboard.add(new InformationPanel("Engine oil temperature"));
		dashboard.add(new InformationPanel("Engine RPM"));
		dashboard.add(new InformationPanel("Engine coolant temperature"));
		
		dashboard.setName("Dashboard");
		
		return dashboard;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args){
		MainWindow main = new MainWindow();
	}
	
}
