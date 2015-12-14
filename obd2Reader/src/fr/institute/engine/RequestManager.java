package fr.institute.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class RequestManager implements RequestEngineModel, DirectAccessToRequestRoutageTable{

	public static final int NOT_FOUND = -1;
	
	private InputStream input;
	private OutputStream output;
	private ArrayList<PidRequest> requestTable;
	private ArrayList<Integer> vehicleRef;
	
	//only for tests
	public RequestManager(){
		
	}
	
	public RequestManager(OutputStream output, InputStream input){
		this.output  = output;
		this.input = input;
		requestTable = new ArrayList<PidRequest>();
		vehicleRef = getVehicleRef();
	}
	
	public int indexOf(String pidName){
		int index = NOT_FOUND;
		
		for(int i=0; i<requestTable.size(); i++){
			if(requestTable.get(i).getPidId() == requestRoutageTable.getNumberAt(pidName)){
				index = i;
			}
		}
		
		return index;
	}
	
	public int indexOf(int pidId){
	int index = NOT_FOUND;
		
		for(int i=0; i<requestTable.size(); i++){
			if(requestTable.get(i).getPidId() == pidId){
				index = i;
			}
		}
		
		return index;	
	}
	
	public ArrayList<Integer> getVehicleRef(){
		ArrayList<Integer> vehicleRef = new ArrayList<Integer>();
		String request;
		
		for(int i=0; i<4; i++){
			request = "01" + 2*i + "0";
			sendRequest(request);
			readRequest(request);
			vehicleRef.addAll(requestTable.get(indexOf(Integer.parseInt(request,16))).getBuffer());
		}
		
		return vehicleRef;
	}
	
	public boolean isCompatible(int request){
		boolean compatible = false;
		int pid = 0xFF & request;
		
		if(pid/8 < vehicleRef.size()){
			short offSet = (short) ((8-pid%8)%8),
				 bitMask = (short) (0b1 << offSet);
			int bitField = vehicleRef.get(pid/8);
			compatible =((bitField & bitMask) >> offSet) == 1;
		 }
		
		return compatible;
	}
	
	public void readRequest(String pidName){
		try{
			String fullByteString = "";
			char currentReadChar = ' ';
			int indexOfAnswer = 0;
			while(!Character.toString(currentReadChar).equals("4")){
				currentReadChar = (char) input.read();
			}
			
			for(int i=0; i<5; i++){
				currentReadChar = (char) input.read();
			}

			while(!Character.toString(currentReadChar).equals(">")){
				
				if(Character.toString(currentReadChar).matches("^[a-fA-F0-9]$")){
					indexOfAnswer++;
					fullByteString += Character.toString(currentReadChar);
					if(indexOfAnswer%2 == 0){
						requestTable.get(indexOf(pidName)).getBuffer().add(Integer.parseInt(fullByteString, 16));
						fullByteString = "";
					}
				}
				currentReadChar = (char) input.read();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean requestAlreadyCreated(int pidNumber){
		boolean exists = false;
		for(int i=0; i<requestTable.size(); i++){
			if(requestTable.get(i).getPidId() == pidNumber)
				exists = true;
		}
		
		return exists;
	}
		
	public void sendRequest(String pidName){
		if(!requestAlreadyCreated(requestRoutageTable.getNumberAt(pidName))){
			requestTable.add(new PidRequest(requestRoutageTable.getNumberAt(pidName)));
		}
		
		try {
			output.write((String.format("%04X", requestRoutageTable.getNumberAt(pidName))+"\r").getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String[] getCompatibleRequests() {
		ArrayList<String> availableCommandsList = new ArrayList<String>();
		
		for(int i = 0; i < requestRoutageTable.size(); i++){
			if(isCompatible(requestRoutageTable.get(i).getPidId()))
					availableCommandsList.add(requestRoutageTable.get(i).getPidName());
		}
		
		String[] availableCommands = availableCommandsList.toArray(new String[availableCommandsList.size()]);
		
		return availableCommands;
	}
	
	@Override
	public float getUpToDateData(String pidName) {
		sendRequest(pidName);
		readRequest(pidName);
		return requestRoutageTable.getTreatmentAt(pidName).compute(requestTable.get(indexOf(pidName)).getBuffer());
	}

	
	public InputStream getInput() {
		return input;
	}

	
	public void setInput(InputStream input) {
		this.input = input;
	}
	

	public OutputStream getOutput() {
		return output;
	}
	

	public void setOutput(OutputStream output) {
		this.output = output;
	}
	

	public ArrayList<PidRequest> getRequestTable() {
		return requestTable;
	}
	

	public void setRequestTable(ArrayList<PidRequest> requestTable) {
		this.requestTable = requestTable;
	}
	

	public void setVehicleRef(ArrayList<Integer> vehicleRef) {
		this.vehicleRef = vehicleRef;
	}
	
}
