package fr.institute.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FileHandler {

	public static double meanData(String dataName, Date from, Date until){
		Calendar start = new GregorianCalendar(), 
					end = new GregorianCalendar(),
					old = new GregorianCalendar(),
					next = new GregorianCalendar();
		start.setTime(from);
		old.setTime(from);
		end.setTime(until);
		
		double mean = -1, currentData = 0;
		long i = 0;
		int nbData = 0;
		String[] toLoadFrom = new String[3];
		String rawData;		
		
		for(i = start.getTimeInMillis(); i <= end.getTimeInMillis(); i += 24*60*60*1000){
			next.setTimeInMillis(i);
			if(next.get(Calendar.DAY_OF_MONTH) != old.get(Calendar.DAY_OF_MONTH) || i == start.getTimeInMillis()){
				String saveFileName = String.valueOf(next.get(Calendar.DAY_OF_MONTH)) + " - " + 
										String.valueOf(next.get(Calendar.MONTH)) + " - " + 
										String.valueOf(next.get(Calendar.YEAR)) + 
										".txt";
				try{
					BufferedReader fromFile = new BufferedReader(
													new InputStreamReader(
														new FileInputStream(saveFileName)
													)
												);				
					try{
						currentData = 0;
						while(true){
							rawData = fromFile.readLine();
							toLoadFrom = rawData.split(" - ");
							if(toLoadFrom[0].matches(dataName)){
								nbData++;
								currentData += Double.parseDouble(toLoadFrom[1]);
								if(mean == -1)
									mean = 0;
							}
						}
					}catch(NullPointerException e){
						mean += currentData;
					}
					
					fromFile.close();
				} catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
			
			old.setTimeInMillis(i);
		}
		
		return mean/nbData;	
	}

	public static double meanData(String dataName, Date date){
		Calendar start = new GregorianCalendar();
		start.setTime(date);
		
		double mean = -1;
		double currentData = 0;
		String[] toLoadFrom = new String[3];
		String rawData;
		int i=0;
		
		String saveFileName = String.valueOf(start.get(Calendar.DAY_OF_MONTH)) + " - " + 
				String.valueOf(start.get(Calendar.MONTH)) + " - " + 
				String.valueOf(start.get(Calendar.YEAR)) + 
				".txt";
		
		try{
			BufferedReader fromFile = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(saveFileName)));				
			try{
				while(true){
					rawData = fromFile.readLine();
					toLoadFrom = rawData.split(" - ");
					if(toLoadFrom[0].matches(dataName)){
						i++;
						currentData += Double.parseDouble(toLoadFrom[1]);	
					}
				}
			}catch(NullPointerException e){
				mean = currentData/i;										
			}
			
			fromFile.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return mean;			
		
	}
	
	public static double meanData(String dataName){
		Calendar calendar = Calendar.getInstance();
		String saveFileName = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " - " + 
								String.valueOf(calendar.get(Calendar.MONTH)) + " - " + 
								String.valueOf(calendar.get(Calendar.YEAR)) + 
								".txt";

		double mean = -1;
		double currentData = 0;
		String[] toLoadFrom = new String[3];
		String rawData;
		int i=0;
		
		try{
			BufferedReader fromFile = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(saveFileName)));
			
			try{
				while(true){
					rawData = fromFile.readLine();
					toLoadFrom = rawData.split(" - ");
					if(toLoadFrom[0].matches(dataName)){
						i++;				
						currentData += Double.parseDouble(toLoadFrom[1]);	
					}
				}
			}catch(NullPointerException e){
				mean = currentData/i;										
			}
			
			fromFile.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return mean;			
	}
	
	public static void saveData(String name, float data, Date date){
		PrintWriter toFile = null;
		Calendar calendar = Calendar.getInstance();
		String saveFileName = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " - " + 
								String.valueOf(calendar.get(Calendar.MONTH)) + " - " + 
								String.valueOf(calendar.get(Calendar.YEAR)) + 
								".txt";
		
		try{
			toFile = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFileName, true))));	
			toFile.println(name + " - " + data + " - " + date);
			toFile.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
			if(toFile != null){
				toFile.flush();
				toFile.close();
			}
		}
	}
	
}
