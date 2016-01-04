package fr.institute.engine;

import fr.institute.connection.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class RequestManager implements RequestEngineModel, DirectAccessToRequestRoutageTable{

	public static final int NOT_FOUND = -1;
	
	private InputStream input;
	private OutputStream output;
	private Connection connection;
	private ArrayList<PidRequest> requestTable;
	private ArrayList<Integer> vehicleRef;
	
	//only for tests
	public RequestManager(){
		requestTable = new ArrayList<PidRequest>();
	}
	
	public RequestManager(Connection connection){
		this.connection = connection;
		this.input = connection.getInputStream();
		requestTable = new ArrayList<PidRequest>();
		vehicleRef = getVehicleRef();
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
		
		
		for(int i=1; i<=4; i++){
			request = "Vehicle compatibility - " + i ;
			System.out.println(request);
			sendRequest(request);
			readRequest(request);
			vehicleRef.addAll(requestTable.get(indexOf(requestRoutageTable.getNumberAt(request))).getBuffer());
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
			
			while(currentReadChar != '4' && currentReadChar != '>'){
				currentReadChar = (char) input.read();
				System.out.print(currentReadChar);
			}
			
			if(currentReadChar != '>'){
				for(int i=0; i<5; i++){
					currentReadChar = (char) input.read();
				}
				
				while(currentReadChar != '>'){
					if(Character.toString(currentReadChar).matches("^[a-fA-F0-9]$")){
						indexOfAnswer++;
						fullByteString += Character.toString(currentReadChar);
						if(indexOfAnswer%2 == 0){
							/*System.out.println("requestTable : " + requestTable);
							System.out.println("index : " + requestTable.get(indexOf(pidName)));
							System.out.println("Buffer : " + requestTable.get(indexOf(pidName)).getBuffer());*/
							requestTable.get(indexOf(pidName)).getBuffer().add(Integer.parseInt(fullByteString, 16));
							fullByteString = "";
						}
					}
					currentReadChar = (char) input.read();
				}
				//System.out.println();				
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
		String toSend = "";
	
		if(!requestAlreadyCreated(requestRoutageTable.getNumberAt(pidName))){
			requestTable.add(new PidRequest(requestRoutageTable.getNumberAt(pidName)));
		}
		toSend = String.format("%04X", requestRoutageTable.getNumberAt(pidName)) + "\r";
		
		connection.send(toSend);
		
		/*try {
			output.write(toSend.getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
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

	@Override
	public ArrayList<String> getErrorCodes() {
		sendRequest("Vehicle error codes");
		readRequest("Vehicle error codes");
		
		return requestRoutageTable.getErrorCodesTreatment().compute(requestTable.get(indexOf("Vehicle error codes")).getBuffer());
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

	public static void main(){
		ArrayList<PidRequest> requestTable = new ArrayList<PidRequest>();
		
	}
}
