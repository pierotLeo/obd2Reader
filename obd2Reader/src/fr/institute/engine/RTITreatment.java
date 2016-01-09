package fr.institute.engine;

import java.util.ArrayList;

import com.japisoft.formula.Formula;
import com.japisoft.formula.node.EvaluateException;

public class RTITreatment {

private String operation;
	
	public RTITreatment(String operation){
		this.operation = operation;
	}
	
	/**
	 * 
	 * @param buffer
	 * @return
	 * @throws EvaluateException 
	 */
	public Double compute(ArrayList<Integer> buffer) throws EvaluateException{
		Formula f = new Formula(operation);
		
		for(int i=0; i<operation.length(); i++){
			if(Character.toString(operation.charAt(i)).matches("[A-Z]")){
				f.setSymbolValue(Character.toString(operation.charAt(i)), buffer.get((int)operation.charAt(i) - 65));
			}
		}
		
		Double d = f.evaluate().getDoubleValue();
		
		return d;
	}
	
}
