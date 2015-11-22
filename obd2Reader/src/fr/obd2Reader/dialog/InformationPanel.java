package fr.obd2Reader.dialog;

import java.awt.BorderLayout;

import javax.swing.*;

import fr.obd2Reader.command.CompatibleCommand;
import fr.obd2Reader.command.VehicleCompatibility;
import fr.obd2Reader.command.ObdCommand;

/**
 * Panel containing one information of the vehicle.
 * @author Supa Kanojo Hunta
 *
 */
public class InformationPanel extends JPanel{

	private String name;
	private CompatibleCommand command;
	private VehicleCompatibility vehicle;
	
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
		
		displayMode.addTab("Graphic", getGraphPanel());
		displayMode.addTab("Numeric", getNumericPanel());

		return displayMode;
	}
	
	/**
	 * Build graphic representation of the information.
	 * @return
	 */
	private JPanel getGraphPanel(){
		JPanel graphicPanel = new JPanel();
		
		
		
		return graphicPanel;
	}
	
	/**
	 * Build numeric representation of the information.
	 * @return
	 */
	private JPanel getNumericPanel(){
		JPanel numericPanel = new JPanel();
		
		
		
		return numericPanel;
	}
	
}
