package fr.institute.engine;

import java.util.ArrayList;

public class ErrorCodesTreatment {
	
	/**
	 * Build a list of the encountered error codes contained in the parameter.
	 * @param inBuff
	 * @return
	 */
	public ArrayList<String> compute(ArrayList<Integer> inBuff){

		ArrayList<String> troubleCodes = new ArrayList<String>();
		String code = null;
		for( int i = 0; i < 3; i++){

			switch((inBuff.get(2*i + 1) & 0xC0)){
				case (byte) 0x00: 	code = "P"; break;
				case (byte) 0x40:	code = "C"; break;
				case (byte) 0x80:	code = "B"; break;
				case (byte) 0xC0:	code = "U"; break;
			}
			
			switch((inBuff.get(2*i + 1) & 0x30)){
				case (byte) 0x00: code += "0"; break;
				case (byte) 0x10: code += "1"; break;
				case (byte) 0x20: code += "2"; break;
				case (byte) 0x30: code += "3"; break;
			}
			
			code += (char)(inBuff.get(2*i + 1) & 0x0F);
			code += (char)(inBuff.get(2*i + 2) & 0xF0);
			code += (char)(inBuff.get(2*i + 2) & 0x0F);
		
			if(!code.equals("P0000")){
				troubleCodes.add(code);
			}
			code = null;
		}
		return troubleCodes;
	}
}
