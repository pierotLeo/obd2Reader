package fr.institute.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import fr.institute.engine.RequestEngineModel;

public class InformationPanel extends JTabbedPane{

	private String name;
	private RequestView requestView;
	private RequestEngineModel requestEngine;
	private JTextArea DataTextArea;
	private XYSeries dataCurve;
	private ChartPanel xylineChartPanel;
	
	public InformationPanel(String name){
		super();
		this.name = name;
		initiate();
	}
	
	/**
	 * Fill the panel with the information returned by OBD system if compatible, else nothing but a "No data available" label
	 * @throws IOException 
	 * @throws FontFormatException 
	 */
	private void initiate(){
		this.addTab("Graphic", drawGraphicPanel());
		this.addTab("Numeric", drawNumericPanel());
		this.setOpaque(true);
		this.setBackground(Color.DARK_GRAY);
		this.setForeground(Color.GREEN);
		this.setFont(new Font("Share Tech Mono", Font.PLAIN, 15));
	}
	
	private ChartPanel drawGraphicPanel(){
		
		 JFreeChart xylineChart = ChartFactory.createXYLineChart(
		         "Graphic representation" ,
		         "Time" ,
		         "requestView.getUnit(name)" ,
		         createDataset() ,
		         PlotOrientation.VERTICAL ,
		         true , true , false);
		 xylineChart.setBackgroundPaint(Color.DARK_GRAY);
		 xylineChart.getTitle().setPaint(Color.GREEN);
		 xylineChart.getTitle().setFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 20));
		 xylineChart.getLegend().setBackgroundPaint(Color.DARK_GRAY);
		 xylineChart.getLegend().setItemPaint(Color.GREEN);
		 xylineChart.getLegend().setItemFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 
		 final XYPlot plot = xylineChart.getXYPlot( );
		 plot.setBackgroundPaint(Color.GRAY);
		 
		 ValueAxis domainAxis = plot.getDomainAxis();
		 domainAxis.setTickLabelPaint(Color.GREEN);
		 domainAxis.setTickLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 domainAxis.setLabelPaint(Color.GREEN);
		 domainAxis.setLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 plot.setDomainGridlinePaint(Color.GREEN);
		 
		 ValueAxis rangeAxis = plot.getRangeAxis();
		 rangeAxis.setTickLabelPaint(Color.GREEN);
		 rangeAxis.setTickLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 rangeAxis.setLabelPaint(Color.GREEN);
		 rangeAxis.setLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 plot.setRangeGridlinePaint(Color.GREEN);
		 
		 xylineChartPanel = new ChartPanel(xylineChart);
		 
	     XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	     renderer.setSeriesPaint( 0 , Color.GREEN );
	     renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	     plot.setRenderer(renderer);
	     
	     return xylineChartPanel;
	}
	
	private XYDataset createDataset( )
	{
		final XYSeriesCollection dataset = new XYSeriesCollection( );          
      	
      	dataCurve = new XYSeries(name);
      	dataCurve.add(0.0, 0.0);
      	dataCurve.getItemCount();
      
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
		
		return numericPanel;
	}
	
	public void updateGraphicPanel(double chrono){
		dataCurve.add(chrono, requestEngine.getUpToDateData(name));
	}

	/**
	 * Add a new data to the numeric representation's set of datas and update it.
	 * @param newData : data to update representation with.
	 */
	public void updateNumericPanel(){
		
	}	
	
	public void setName(String name){
		this.name = name;
	}

}
