package fr.institute.engine;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RequestRoutageTable extends ArrayList<PidCell>{

	private ErrorCodesTreatment errorCodesTreatment;
	
	public RequestRoutageTable(){
		errorCodesTreatment = new ErrorCodesTreatment();
		
		this.add(
				new PidCell(0x0100, "Vehicle compatibility - 1", "", new RTITreatment("")));
		this.add(
				new PidCell(0x0120, "Vehicle compatibility - 2", "", new RTITreatment("")));
		this.add(
				new PidCell(0x0140, "Vehicle compatibility - 3", "", new RTITreatment("")));
		this.add(
				new PidCell(0x0160, "Vehicle compatibility - 4", "", new RTITreatment("")));
		this.add(
				new PidCell(0x0104, "Calculated engine load value", "%", new RTITreatment("(A x 100)/ 255")));
		this.add(
				new PidCell(0x0105, "Engine coolant temperature", "°C", new RTITreatment("A - 40")));
		this.add(
				new PidCell(0x0106, "Short term fuel % trim - Bank 1", "%", new RTITreatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x0107, "Long term fuel % trim - Bank 1", "%", new RTITreatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x0108, "Short term fuel % trim - Bank 2", "%", new RTITreatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x0109, "Long term fuel % trim - Bank 2", "%", new RTITreatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x010A, "Fuel pressure", "kPa", new RTITreatment("A * 3")));
		this.add(
				new PidCell(0x010B, "Intake manifold absolute pressure", "kPa", new RTITreatment("A")));
		this.add(
				new PidCell(0x010C, "Engine RPM", "rpm", new RTITreatment("((A x 256)+ B)/ 4")));
		this.add(
				new PidCell(0x010D, "Vehicle speed", "km/h", new RTITreatment("A")));
		this.add(
				new PidCell(0x010E, "Timing advance", "", new RTITreatment("(A - 128)/ 2")));
		this.add(
				new PidCell(0x010F, "Intake air temperature", "", new RTITreatment("A - 40")));
		this.add(
				new PidCell(0x0110, "MAF air flow rate", "", new RTITreatment("((A x 256)+ B)/ 100")));
		this.add(
				new PidCell(0x0111, "Throttle position", "", new RTITreatment("(A x 100)/ 255")));
		this.add(
				new PidCell(0x011F, "Run time since engine start", "", new RTITreatment("(A x 256)+ B")));
		this.add(
				new PidCell(0x0121, "Distance traveled with malfunction indicator lamp on", "", new RTITreatment("(A x 256)+ B")));
		this.add(
				new PidCell(0x0122, "Fuel rail pressure (relative to manifold vacuum)", "", new RTITreatment("((A x 256)+ B)x 0.079")));
		this.add(
				new PidCell(0x0123, "Fuel rail pressure (diesel or gazoline direct inject)", "", new RTITreatment("((A x 256)+ B)x 10")));
		
	}
	
	public String getNameAt(int pidNumber){
		String pidName = "";
		for(int i=0; i<size(); i++){
			if(get(i).getPidId() == pidNumber){
				pidName = get(i).getPidName();
				break;
			}	
		}
		
		return pidName;	
	}
	
	public ErrorCodesTreatment getErrorCodesTreatment(){
		return errorCodesTreatment;
	}
	
	
	public int getNumberAt(String pidName){
		int pidNumber = -1;
		for(int i=0; i<size(); i++){
			if(get(i).getPidName().equals(pidName)){
				pidNumber = get(i).getPidId();
				break;
			}			
		}
		
		return pidNumber;
	}
	
	public RTITreatment getTreatmentAt(int pidNumber){
		RTITreatment pidTreatment = null;
		for(int i=0; i<size(); i++){
			if(get(i).getPidId() == pidNumber){
				pidTreatment = get(i).getTreatment();
				break;
			}			
		}
		
		return pidTreatment;
	}
	
	public RTITreatment getTreatmentAt(String pidName){
		RTITreatment pidTreatment = null;
		for(int i=0; i<size(); i++){
			if(get(i).getPidName().equals(pidName)){
				pidTreatment = get(i).getTreatment();
				break;
			}			
		}
		
		return pidTreatment;
	}
	
	public boolean contains(int pidNumber){
		boolean pidNumberExists = false;
		
		for(int i=0; i<size(); i++){
			if(get(i).getPidId() == pidNumber){
				pidNumberExists = true;
			}
		}
		
		return pidNumberExists;
	}
	
}
