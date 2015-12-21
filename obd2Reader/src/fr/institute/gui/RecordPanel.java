package fr.institute.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class RecordPanel extends JFrame implements MainWindowConstants{
	private JButton b1 = new JButton("Start record");
	private JButton b2 = new JButton("Stop record");
	private ArrayList <JCheckBox> recordedInformations;
	private ArrayList <JCheckBox> nonRecordedInformations;
	private ArrayList<String> displayedInformations;
	private ArrayList<Boolean> recordedInformationsBoolean;
	private JPanel recordPanel;

	public RecordPanel(ArrayList<String> displayedInformations, ArrayList<Boolean> recordedInformationsBoolean){
		this.displayedInformations = displayedInformations;
		this.recordedInformationsBoolean = recordedInformationsBoolean;
		this.setTitle(MENU_FILE_RECORD);
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		recordPanel = new JPanel(new BorderLayout());
		if(this.displayedInformations != null){
			for(int i=0; i<this.displayedInformations.size(); i++){
				if(this.recordedInformationsBoolean.get(i))
					recordedInformations.add(new JCheckBox(this.displayedInformations.get(i)));
				else
					nonRecordedInformations.add(new JCheckBox(this.displayedInformations.get(i)));
			}
		}
		b1.addActionListener(new StartButtonActionListener());
		b2.addActionListener(new StopButtonActionListener());
		recordPanel.add(b1, BorderLayout.SOUTH);
		recordPanel.add(b2, BorderLayout.NORTH);
		if(recordedInformations != null && nonRecordedInformations != null){
			for(int i=0; i<recordedInformations.size(); i++)
				recordPanel.add(recordedInformations.get(i), BorderLayout.NORTH);
			for(int i=0; i< nonRecordedInformations.size();i++)
				recordPanel.add(nonRecordedInformations.get(i), BorderLayout.CENTER);
		}
		this.setContentPane(recordPanel);
		this.setVisible(true);
	}
	
	public ArrayList<Boolean> getRecordedInformationsBoolean(){
		return this.recordedInformationsBoolean;
	}
	
	public class StartButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(nonRecordedInformations != null){
				for(int i=0; i<nonRecordedInformations.size(); i++){
					if(nonRecordedInformations.get(i).isSelected()){
						recordedInformations.add(nonRecordedInformations.get(i));
						for(int j=0; j<displayedInformations.size(); j++){
							if(displayedInformations.get(j) == nonRecordedInformations.get(j).getName()){
								recordedInformationsBoolean.set(j, true);
							}
						}
						nonRecordedInformations.remove(i);
					}
				}
				for(int i=0; i<recordedInformations.size(); i++)
					recordPanel.remove(recordedInformations.get(i));
				for(int i=0; i< nonRecordedInformations.size();i++)
					recordPanel.remove(nonRecordedInformations.get(i));
				for(int i=0; i<recordedInformations.size(); i++)
					recordPanel.add(recordedInformations.get(i), BorderLayout.NORTH);
				for(int i=0; i< nonRecordedInformations.size();i++)
					recordPanel.add(nonRecordedInformations.get(i), BorderLayout.CENTER);
			}
		}
	}
	
	public class StopButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(recordedInformations != null){
				for(int i=0; i<recordedInformations.size(); i++){
					if(recordedInformations.get(i).isSelected()){
						nonRecordedInformations.add(recordedInformations.get(i));
						for(int j=0; j<displayedInformations.size(); j++){
							if(displayedInformations.get(j) == recordedInformations.get(j).getName()){
								recordedInformationsBoolean.set(j, false);
							}
						}
						recordedInformations.remove(i);
					}
				}
				for(int i=0; i<recordedInformations.size(); i++)
					recordPanel.remove(recordedInformations.get(i));
				for(int i=0; i< nonRecordedInformations.size();i++)
					recordPanel.remove(nonRecordedInformations.get(i));
				for(int i=0; i<recordedInformations.size(); i++)
					recordPanel.add(recordedInformations.get(i), BorderLayout.NORTH);
				for(int i=0; i< nonRecordedInformations.size();i++)
					recordPanel.add(nonRecordedInformations.get(i), BorderLayout.CENTER);
			}
		}
	}	
}