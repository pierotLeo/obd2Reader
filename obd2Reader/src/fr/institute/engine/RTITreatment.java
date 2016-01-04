package fr.institute.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class RTITreatment {

private String treatment;
	
	public RTITreatment(String treatment){
		this.treatment = treatment;
	}
	
	public float compute(ArrayList<Integer> inBuff){
		String[] bracketsSplittedTreatment = treatment.split("(\\(|\\))");
		ArrayList<String> splitTreatment = new ArrayList<String>(Arrays.asList(bracketsSplittedTreatment));
		Iterator<String> itr = splitTreatment.iterator();
		
		while(itr.hasNext()){
			if(itr.next().isEmpty())
				itr.remove();
		}
		
		for(int i=0; i<splitTreatment.size(); i++){

			String[] currentOperation = splitTreatment.get(i).split(" ");
			if(currentOperation.length == 3 && currentOperation[1].matches("(\\+|\\*|\\-|/|x)")){
				for(int j = 0; j<3; j+=2){
					char operand = currentOperation[j].charAt(0);
					currentOperation[j] = String.valueOf(inBuff.get((int)operand - 65));
				}
				
				
				switch(currentOperation[1]){
					case "x":
						splitTreatment.set(i,String.valueOf(Float.valueOf(currentOperation[0]) * Float.valueOf(currentOperation[2])) + " ");
						break;
					case "*":
						splitTreatment.set(i,String.valueOf(Float.valueOf(currentOperation[0]) * Float.valueOf(currentOperation[2])) + " ");
						break;
					case "+":
						splitTreatment.set(i,String.valueOf(Float.valueOf(currentOperation[0]) + Float.valueOf(currentOperation[2])) + " ");
						break;
					case "-":
						splitTreatment.set(i,String.valueOf(Float.valueOf(currentOperation[0]) - Float.valueOf(currentOperation[2])) + " ");
						break;
					case "/":
						splitTreatment.set(i,String.valueOf(Float.valueOf(currentOperation[0]) / Float.valueOf(currentOperation[2])) + " ");
						break;
				}
			}
			String currentElement = splitTreatment.get(i);
			if(splitTreatment.get(i).trim().matches("^\\-?[0-9]*\\.[0-9]*$") && splitTreatment.size()>1){
				
				splitTreatment.set(i+1, currentElement +  splitTreatment.get(i+1));
				itr = splitTreatment.iterator();
				while(itr.hasNext()){
					if(itr.next().equals(currentElement))
						itr.remove();
				}
				i--;
			}
		}
		
		return Float.valueOf(splitTreatment.get(0));
	}
	
}
