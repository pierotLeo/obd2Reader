package fr.institute.gui;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AnalysisFrame extends JFrame{
	private String[] m_infoList;
	
	
	public AnalysisFrame(String[] p_infoList){
		m_infoList = p_infoList;
	}
	
	public JPanel getMainPanel(){
		JPanel mainPanel = new JPanel();
	
		JLabel choseDataLabel = new JLabel("Chose the data you want to analyze :");
		JComboBox<String> infoComboBox = new JComboBox<String>(m_infoList);
		
		JLabel choseTimeRestraintLabel = new JLabel("Chose the time restraint for the analysis :");
		
		
		return mainPanel;
	}
}

