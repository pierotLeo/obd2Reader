package fr.institute.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import fr.institute.engine.RequestEngineModel;

@SuppressWarnings("serial")
public class InformationPanel extends JTabbedPane{

	public static final int MAX_TIME_RANGE = 10;
	public static final int DISPLAY_OFFSET = 1;
	
	private String name;
	//private RequestView requestView;
	private RequestEngineModel requestEngine; 
	private ValueAxis domainAxis;
	private ValueAxis rangeAxis;
	private XYSeries dataCurve;
	private JFreeChart chart;
	private DefaultValueDataset dataSet;
	private ChartPanel xylineChartPanel;
	private long clock;
	
	public InformationPanel(String name, RequestEngineModel requestEngine){
		super();
		this.name = name;
		this.requestEngine = requestEngine;
		this.clock = System.currentTimeMillis();
		initiate();
	}
	
	/**
	 * Fill the panel with the information returned by OBD system if compatible, else nothing but a "No data available" label
	 * @throws IOException 
	 * @throws FontFormatException  
	 */
	private void initiate(){
		this.removeAll();	
		this.addTab("Graphic", drawGraphicPanel());
		this.addTab("Numeric", drawNumericPanel());
		this.setOpaque(true);
		this.setBackground(Color.DARK_GRAY);
		this.setForeground(Color.GREEN);
		this.setFont(new Font("Share Tech Mono", Font.PLAIN, 15));
	}
	
	private ChartPanel drawGraphicPanel(){
		
		String unit = "";
		
		if(requestEngine != null)
			unit = requestEngine.getUnit(name);
		
		 JFreeChart xylineChart = ChartFactory.createXYLineChart(
		         "Graphic representation" ,
		         "Time" ,
		         unit ,
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
		 
		 domainAxis = plot.getDomainAxis();
		 domainAxis.setTickLabelPaint(Color.GREEN);
		 domainAxis.setTickLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 domainAxis.setLabelPaint(Color.GREEN);
		 domainAxis.setLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 domainAxis.setRange(new Range(0,MAX_TIME_RANGE));
		 
		 plot.setDomainGridlinePaint(Color.GREEN);
		 
		 rangeAxis = plot.getRangeAxis();
		 rangeAxis.setTickLabelPaint(Color.GREEN);
		 rangeAxis.setTickLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 rangeAxis.setLabelPaint(Color.GREEN);
		 rangeAxis.setLabelFont(new Font("Share Tech Mono", Font.TRUETYPE_FONT, 15));
		 rangeAxis.setRange(new Range(0,100));
		
		 plot.setRangeGridlinePaint(Color.GREEN);
		 
		
		 xylineChartPanel = new ChartPanel(xylineChart);
		 
	     XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
	     renderer.setSeriesPaint( 0 , Color.GREEN );
	     renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	     renderer.setSeriesShapesVisible(0, false);
	     
	     return xylineChartPanel;
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
     * Creates a sample chart.
     *
     * @param dataset  a dataset.
     *
     * @return The chart.
     */
    private JFreeChart createChart(ValueDataset dataset) {
        MeterPlot plot = new MeterPlot(dataset);
        plot.addInterval(new MeterInterval("High", new Range(80.0, 100.0)));
        plot.setDialOutlinePaint(Color.white);
        
        String unit = "";
        if(requestEngine != null)
			unit = requestEngine.getUnit(name);
		
        
        chart = new JFreeChart(name + "\n" + unit,
                JFreeChart.DEFAULT_TITLE_FONT, plot, false);
       
        return chart;
    }
    
    private JPanel drawNumericPanel() {
        dataSet = new DefaultValueDataset(0.0);
        JFreeChart chart = createChart(dataSet);
        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add(new ChartPanel(chart));
        return panel;
    }
	
	public void updateGraphicPanel(){
		String unit = "";
		if(requestEngine != null)
			unit = requestEngine.getUnit(name);
		
		double chrono = (float)((System.currentTimeMillis() - clock)/10)/100;
		
		if(!unit.equals(rangeAxis.getLabel()))
			rangeAxis.setLabel(unit);
		
		if(!rangeAxis.isAutoRange() && !domainAxis.isAutoRange()){
			rangeAxis.setAutoRange(true);
			domainAxis.setAutoRange(true);
		}
		
		dataCurve.add(chrono, requestEngine.getUpToDateData(name));
		if(chrono > MAX_TIME_RANGE){
			domainAxis.setLowerBound(chrono - MAX_TIME_RANGE);
			domainAxis.setUpperBound(chrono + DISPLAY_OFFSET);
		}
	}

	/**
	 * Add a new data to the numeric representation's set of data and update it.
	 * @param newData : data to update representation with.
	 */
	public void updateNumericPanel(){
		dataSet.setValue(requestEngine.getUpToDateData(name));
	}	
	
	public void randomUpdateGraphicPanel(){
		String unit = "";
		if(requestEngine != null)
			unit = requestEngine.getUnit(name);
		
		double chrono = (float)((System.currentTimeMillis() - clock)/10)/100;
		
		
		if(!unit.equals(rangeAxis.getLabel()))
			rangeAxis.setLabel(unit);
		
		if(!rangeAxis.isAutoRange() && !domainAxis.isAutoRange()){
			rangeAxis.setAutoRange(true);
			domainAxis.setAutoRange(true);
		}
		
		Random rand = new Random();
		
		dataCurve.add(chrono, Math.abs(rand.nextInt()%100));
		if(chrono >= MAX_TIME_RANGE){
			domainAxis.setLowerBound(chrono - MAX_TIME_RANGE);
			domainAxis.setUpperBound(chrono + DISPLAY_OFFSET);
		}
	}
	
	public void specificUpdateGraphicPanel(int data){
		String unit = "";
		if(requestEngine != null)
			unit = requestEngine.getUnit(name);
		
		double chrono = (float)((System.currentTimeMillis() - clock)/10)/100;
		
		
		if(!unit.equals(rangeAxis.getLabel()))
			rangeAxis.setLabel(unit);
		
		if(!rangeAxis.isAutoRange() && !domainAxis.isAutoRange()){
			rangeAxis.setAutoRange(true);
			domainAxis.setAutoRange(true);
		}
				
		dataCurve.add(chrono, data);
		if(chrono >= MAX_TIME_RANGE){
			domainAxis.setLowerBound(chrono - MAX_TIME_RANGE);
			domainAxis.setUpperBound(chrono + DISPLAY_OFFSET);
		}
	}
	
	public void changeInformationTo(String name){
		setName(name);
		/*dataCurve.clear();
		domainAxis.setLabel(name);
		chart.setTitle(name);*/
		initiate();
		clock = System.currentTimeMillis();
	}

	public String getName(){
		return name;
	}
	
	
	public void setName(String name){
		this.name = name;
	}
	
	public long getClock(){
		return this.clock;
	}

	public RequestEngineModel getRequestEngine(){
		return requestEngine;
	}
	
	public void setRequestEngine(RequestEngineModel requestEngine){
		this.requestEngine = requestEngine;
	}
	
}
