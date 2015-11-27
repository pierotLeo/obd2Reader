package fr.obd2Reader.dialog;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
	private XYSeries dataCurve;
	private ChartPanel test;
	
	public InformationPanel(VehicleCompatibility vehicle, String name){
		super();
		this.vehicle = vehicle;
		this.name = name;
		initiate();
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
		//if(!checkCompatibility())
			//add(getDisplayNoData());
		//else
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
	
	private ChartPanel drawGraphicPanel(){
		 JFreeChart xylineChart = ChartFactory.createXYLineChart(
		         "Graphic representation" ,
		         "Time" ,
		         "command.getUnit()" ,
		         createDataset() ,
		         PlotOrientation.VERTICAL ,
		         true , true , false);
		 test = new ChartPanel(xylineChart);
		 
		 final XYPlot plot = xylineChart.getXYPlot( );
	     XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	     renderer.setSeriesPaint( 0 , Color.GREEN );
	     renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	     plot.setRenderer( renderer );
	     
	     return test;
	}
	
	private XYDataset createDataset( )
	{
		final XYSeriesCollection dataset = new XYSeriesCollection( );          
      	
      	dataCurve = new XYSeries(name);
      	dataCurve.add(0.0, 0.0);
      
      	dataset.addSeries( dataCurve );
      	return dataset;
	}
	
	
	/**
	 * Build numeric representation of the information.
	 * @return
	 */
	private JPanel drawNumericPanel(){
		JPanel numericPanel = new JPanel(new BorderLayout());
		
		DataTextArea = new JTextArea();
		
		numericPanel.add(DataTextArea, BorderLayout.CENTER);
		//numericPanel.add(new JTextArea(command.getUnit()),BorderLayout.SOUTH);
		
		return numericPanel;
	}
	
	public void updateGraphicPanel(float newData, double chrono){
		dataCurve.add(newData, chrono);
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
		this.name = command.getName();
	}
	
	public void setName(String name){
		this.name = name;
	}
}