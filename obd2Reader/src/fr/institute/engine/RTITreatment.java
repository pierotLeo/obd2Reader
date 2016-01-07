package fr.institute.engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RTITreatment {

private String operation;
	
	public RTITreatment(String operation){
		this.operation = operation;
	}
	
	/**
	 * Split the input into a Array of Strings with the desired regular expression.Doesn't keep the separator into the Strings.
	 * @param input
	 * @param regex
	 * @param includeSeparator
	 * @return
	 */
	public String[] split(String input, String regex){
		return split(input, regex, false);
	}
	
	/**
	 * Split the input into a Array of Strings with the desired regular expression. Allow you to precise if you want to include the separator into the Strings or not.
	 * @param input
	 * @param regex
	 * @param includeSeparator
	 * @return
	 */
	public String [] split(String input, String regex, boolean includeSeparator){
		Matcher m = Pattern.compile(regex).matcher(input);
		ArrayList<String> matchList = new ArrayList<String>();
		int index = 0;
		while(m.find()) {
			matchList.add( input.substring(index, m.start()) );
			if (includeSeparator) {
				matchList.add( m.group() );
			}
			index = m.end();
		}
		if (index==0) {
			return new String[] {input};
		}
		if (index < input.length()) {
			matchList.add( input.substring(index) );
		}
		return matchList.toArray(new String[matchList.size()]);
	}
	
	/**
	 * Transform the desired operation to its postfix form.
	 * @param operation
	 * @return
	 */
	public ArrayList<String> infixToPostfix(String operation){
		operation = operation.replaceAll("(\\(|\\))", "");
		ArrayList<String> tabPostfix = new ArrayList<String>();
		String tab[] = split(operation, "(\\+|-|\\*|\\/)", true);
		Heap<String> pile = new Heap<String>();
		for(String s : tab){
			if(s.matches("(\\+|-|\\*|\\/)")){
				if(pile.isEmpty()){
					pile.stack(s);
				}else{
					if(s.matches("(\\+|-)") && pile.getTop().matches("(\\*|\\/)")){
						while(!pile.isEmpty()){
							tabPostfix.add(pile.unStack());
						}
						pile.stack(s);
					}else{
						pile.stack(s);
					}
				}
			}else{
				tabPostfix.add(s);
			}
		}
		while(!pile.isEmpty()){
			tabPostfix.add(pile.unStack());
		}
		return tabPostfix;
	}
	
	/**
	 * Check if the desired string can be switch to a numeric value.
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	/**
	 * Compute a postfix formed operation.
	 * @param tabPostfix
	 * @return
	 */
	public double calculPostfix(ArrayList<String> tabPostfix){
		double a;
		double b;
		double valeur = 0;
		Heap<String> pile = new Heap<String>();
		String courant;
		System.out.println(tabPostfix);
		for(int i = 0; i < tabPostfix.size(); i++){
			courant = tabPostfix.get(i);
			if(isNumeric(courant)){
				pile.stack(courant);
			}else{
				b = Double.parseDouble(pile.unStack());
				a = Double.parseDouble(pile.unStack());
				switch(courant){
					case "+": valeur = a + b; break;
					case "-": valeur = a - b; break;
					case "*": valeur = a * b; break;
					case "/": valeur = a / b; break;
				}
				pile.stack(Double.toString(valeur));
			}
		}
		return Double.parseDouble(pile.unStack());
	}
	
	/**
	 * Build coordinates of the highest priority bracket.
	 * @param operation
	 * @return
	 */
	public Point priorityBracket(String operation){
		Heap<Integer> pile = new Heap<Integer>();
		ArrayList<Point> aLPoint = new ArrayList<Point>();
		Point smaller = new Point();
		
		for(int i = 0; i < operation.length(); i++){
			if(operation.charAt(i) == '('){
				pile.stack(i);
			} else if(operation.charAt(i) == ')'){
				aLPoint.add(new Point(pile.unStack(), i));
			}
		}
		if(!aLPoint.isEmpty()){
			smaller = aLPoint.get(0);
		
			for(Point p : aLPoint){
				if(p.getY() - p.getX() < smaller.getY() - smaller.getX()){
					smaller = p;
				}
			}
		return smaller;
		}
		return null;
	}
	
	/**
	 * Escape meta characters contained in the diesired String.
	 * @param s
	 * @return
	 */
	public String stringToRegex(String s){
		char [] tabSpecialChar = { '+', '*', '/', '.', '(', ')', '|', '[', ']', '^', '$', '#', '!', '{', '}', '?'};
		String regex = "";
		for(int i = 0; i < s.length(); i++){
			for(char c : tabSpecialChar){
				if(s.charAt(i) == c){
					regex += "\\\\";
				}
			}
			regex += s.charAt(i);
		}
		return regex;
	}
	
	/**
	 * 
	 * @param buffer
	 * @return
	 */
	public float compute(ArrayList<Integer> buffer){
		operation = operation.trim();
		Matcher matcher;
		Pattern patternBracket = Pattern.compile("(\\(|\\))");
		
		for(int i=0; i<operation.length(); i++){
			if(Character.toString(operation.charAt(i)).matches("[A-Z]")){
				operation = operation.replace(operation.charAt(i), String.valueOf(buffer.get((int)operation.charAt(i) - 65)).charAt(0));
				System.out.println(operation);
			}
		}
		
		do{
			matcher = patternBracket.matcher(operation);
			Point pB = priorityBracket(operation);
			if(pB != null){
				String subString = operation.substring(pB.x, pB.y+1);				
				operation = operation.replace(subString, Double.toString(calculPostfix(infixToPostfix(subString))));
			}
			//System.out.println(stringToRegex(subString));
			//System.out.println(formule.replaceAll(stringToRegex(subString), Double.toString(calculPostfix(infixToPostfix(subString)))));
			//formule = formule.replaceAll(stringToRegex(subString), Double.toString(calculPostfix(infixToPostfix(subString))));
		}while(matcher.find());
		
		return Float.parseFloat(operation);
	}
	
	/*public float compute(ArrayList<Integer> inBuff){
		
		System.out.print("Traitement buffer de : ");
		for(int current : inBuff){
			System.out.print(current + " - ");
		}
		
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
	}*/
	
	public static void main(String[] args){
		ArrayList<Integer> inBuff = new ArrayList<Integer>();
		inBuff.add(00);
		RTITreatment treatment = new RTITreatment("(-2*100)");
		System.out.println("010D41 02 03".substring("010D41 02 03".indexOf("010D")+"010D".length()));
	}
	
}
