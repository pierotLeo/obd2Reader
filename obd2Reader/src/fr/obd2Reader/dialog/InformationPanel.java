package fr.obd2Reader.dialog;

import java.awt.BorderLayout;

import javax.swing.*;

import fr.obd2Reader.command.CompatibleCommand;
import fr.obd2Reader.command.VehicleCompatibility;
import fr.obd2Reader.command.ObdCommand;

public class InformationPanel extends JPanel{

	private String name;
	private CompatibleCommand command;
	private VehicleCompatibility vehicle;
	
	public InformationPanel(String name, CompatibleCommand command, VehicleCompatibility vehicle){
		super();
		this.name = name;
		this.command = command;
		this.vehicle = vehicle;
		initiate();
	}
	
	private boolean checkCompatibility(){
		vehicle.compute();
		return command.isCompatible(vehicle.getInBuff());
	}
	
	private void initiate(){
		if(!checkCompatibility())
			add(getDisplayNoData(), BorderLayout.CENTER);
		else
			add(getDisplayMode(), BorderLayout.CENTER);
	}
	
	private JLabel getDisplayNoData(){
		JLabel noData = new JLabel("No data available");
		
		return noData;
	}
	
	private JTabbedPane getDisplayMode(){
		JTabbedPane displayMode = new JTabbedPane();
		
		displayMode.addTab("Graphic", getGraphPanel());
		displayMode.addTab("Numeric", getNumericPanel());

		return displayMode;
	}
	
	private JPanel getGraphPanel(){
		JPanel graphicPanel = new JPanel();
		
		
		
		return graphicPanel;
	}
	
	private JPanel getNumericPanel(){
		JPanel numericPanel = new JPanel();
		
		
		
		return numericPanel;
	}
	
}
