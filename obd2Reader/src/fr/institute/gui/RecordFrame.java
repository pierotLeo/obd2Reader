package fr.institute.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class RecordFrame extends JFrame{
	
	private ArrayList<String> m_names;
	private ArrayList<Boolean> m_record;
	private ArrayList<JCheckBox> recordedBox;
	private ArrayList<JCheckBox> nonRecordedBox;
	private JPanel recordedList;
	private JPanel nonRecordedList;
	
	public RecordFrame(ArrayList<String> p_names, ArrayList<Boolean> p_record){
		super();
		m_names = p_names;
		m_record = p_record;
		
		
		
		add(getMainPanel());
	
		Toolkit tkt = Toolkit.getDefaultToolkit();
		Dimension itemWindowDim = tkt.getScreenSize();
		int height = (int)itemWindowDim.getHeight();
		int width = (int)itemWindowDim.getWidth();
		setBounds(width/2-width/4, height/2-height/4, width/2, height/2);
	
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JPanel getMainPanel(){
		JPanel mainPanel = new JPanel(new GridLayout(2,1));
		
		mainPanel.add(getRecordedPanel());
		mainPanel.add(getNonRecordedPanel());
		mainPanel.add(new JPanel());
		
		return mainPanel;
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
		
		
		for(int i=0; i<m_names.size(); i++){
			if(m_record.get(i)==recorded){
				infoTypeList.add(m_names.get(i));
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
					}					
					break;
				case "stop record":
					for(JCheckBox selectedRec : selectedIndex(false)){
						selectedRec.setSelected(false);
						nonRecordedBox.remove(selectedRec);
						nonRecordedList.remove(selectedRec);
						recordedBox.add(selectedRec);
						recordedList.add(selectedRec);
					}
			}
			recordedList.revalidate();
			recordedList.repaint();
			nonRecordedList.revalidate();
			nonRecordedList.repaint();
		}
		
	}
	
	public static void main(String[] args){
		ArrayList<String> infosList = new ArrayList<String>();
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
		
		RecordFrame test = new RecordFrame(infosList, is_recorded);
	
	}
	
}
