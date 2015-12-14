package fr.institute.gui;

import java.awt.Color;

import javax.swing.*;

public class InformationPanel extends JTabbedPane{

	private String name;
	
	public InformationPanel(){
		this.setBackground(Color.DARK_GRAY);
	}
	
	public InformationPanel(String name){
		this.name = name;
	}

}
