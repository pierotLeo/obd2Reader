package fr.obd2Reader.dialog;

import java.awt.BorderLayout;

import javax.swing.*;

import fr.obd2Reader.command.CompatibleCommand;
import fr.obd2Reader.command.VehicleCompatibility;
import fr.obd2Reader.command.ObdCommand;
import fr.obd2Reader.connection.ELM327Connection;

/**
 * Panel containing one information of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class InformationPanel extends JPanel{

	private String name;
	private CompatibleCommand command;
	private VehicleCompatibility vehicle;
	private JTextArea DataTextArea;
	
	public InformationPanel(VehicleCompatibility vehicle){
		this.vehicle = vehicle;
	}
	
	/**
	 * Constructor for InformationPanel.
	 * @param name : Name of the contained information to display to the user.
	 * @param command : Command affiliated with the information.
	 * @param vehicle : PID's compatibility of the vehicle. 
	 */
	public InformationPanel(String name, CompatibleCommand command, VehicleCompatibility vehicle){
		super();
		this.name = name;
		this.command = command;
		this.vehicle = vehicle;
		initiate();
	}
	
	//might be better to test that lower into command classes hierarchy. Don't really know where for now though.
	/**
	 * Test compatibility of the information with the vehicle.
	 * @return
	 */
	private boolean checkCompatibility(){
		vehicle.compute();
		return command.isCompatible(vehicle.getInBuff());
	}
	
	/**
	 * Fill the panel with the information returned by OBD system if compatible, else nothing but a "No data available" label
	 */
	private void initiate(){
		if(!checkCompatibility())
			add(getDisplayNoData(), BorderLayout.CENTER);
		else
			add(getDisplayMode(), BorderLayout.CENTER);
	}
	
	//...might...be...useless as shit. Just a possibility.
	/**
	 * Build a "No data available" label.
	 * @return
	 */
	private JLabel getDisplayNoData(){
		JLabel noData = new JLabel("No data available");
		
		return noData;
	}
	
	/**
	 * Build a tabbed pane containing two representations of the information : numeric and graphic.
	 * @return
	 */
	private JTabbedPane getDisplayMode(){
		JTabbedPane displayMode = new JTabbedPane();
		
		displayMode.addTab("Graphic", drawGraphicPanel());
		displayMode.addTab("Numeric", drawNumericPanel());

		return displayMode;
	}
	
	/**
	 * Build graphic representation of the information.
	 * @return
	 */
	private JPanel drawGraphicPanel(){
		JPanel graphicPanel = new JPanel();
		
		
		
		return graphicPanel;
	}
	
	/**
	 * Add a new data to the graphic representation's set of datas and update it.
	 * @param newData : data to update representation with.
	 */
	public void updateGraphicPanel(float newData){
		
	}
	
	/**
	 * Build numeric representation of the information.
	 * @return
	 */
	private JPanel drawNumericPanel(){
		JPanel numericPanel = new JPanel(new BorderLayout());
		
		DataTextArea = new JTextArea();
		
		numericPanel.add(DataTextArea, BorderLayout.CENTER);
		numericPanel.add(new JTextArea(command.getUnit()),BorderLayout.SOUTH);
		
		return numericPanel;
	}

	/**
	 * Add a new data to the numeric representation's set of datas and update it.
	 * @param newData : data to update representation with.
	 */
	public void updateNumericPanel(float newData){
		DataTextArea.setText(String.valueOf(newData));
	}
	
	/**
	 * Getter for the command.
	 * @return
	 */
	public CompatibleCommand getCommand(){
		return command;
	}
	
	public void setCommand(CompatibleCommand command){
		this.command = command;
	}
	
	public void setName(String name){
		this.name = name;
	}
}
