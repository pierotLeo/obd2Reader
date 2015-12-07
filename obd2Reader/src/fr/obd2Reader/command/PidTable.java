package fr.obd2Reader.command;

import java.util.ArrayList;

public class PidTable extends ArrayList<PidCell>{

	public PidTable(){
		this.add(
				new PidCell(0x0104, "Calculated engine load value", new Treatment("(A x 100)/ 255")));
		this.add(
				new PidCell(0x0105, "Engine coolant temperature", new Treatment("A - 40")));
		this.add(
				new PidCell(0x0106, "Short term fuel % trim - Bank 1", new Treatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x0107, "Long term fuel % trim - Bank 1", new Treatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x0108, "Short term fuel % trim - Bank 2", new Treatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x0109, "Long term fuel % trim - Bank 2", new Treatment("((A - 128)x 100)/ 28")));
		this.add(
				new PidCell(0x010A, "Fuel pressure", new Treatment("A * 3")));
		this.add(
				new PidCell(0x010B, "Intake manifold absolute pressure", new Treatment("A")));
		this.add(
				new PidCell(0x010C, "Engine RPM", new Treatment("((A x 256)+ B)/ 4")));
		this.add(
				new PidCell(0x010D, "Vehicle speed", new Treatment("A")));
		this.add(
				new PidCell(0x010E, "Timing advance", new Treatment("(A - 128)/ 2")));
		this.add(
				new PidCell(0x010F, "Intake air temperature", new Treatment("A - 40")));
		this.add(
				new PidCell(0x0110, "MAF air flow rate", new Treatment("((A x 256)+ B)/ 100")));
		this.add(
				new PidCell(0x0111, "Throttle position", new Treatment("(A x 100)/ 255")));
		this.add(
				new PidCell(0x011F, "Run time since engine start", new Treatment("(A x 256)+ B")));
		this.add(
				new PidCell(0x0121, "Distance traveled with malfunction indicator lamp on", new Treatment("(A x 256)+ B")));
		this.add(
				new PidCell(0x0122, "Fuel rail pressure (relative to manifold vacuum)", new Treatment("((A x 256)+ B)x 0.079")));
		this.add(
				new PidCell(0x0123, "Fuel rail pressure (diesel or gazoline direct inject)", new Treatment("((A x 256)+ B)x 10")));
	}
	
	public String getNameAt(int pidNumber){
		String pidName = "";
		for(int i=0; i<size(); i++){
			if(get(i).getPidNumber() == pidNumber){
				pidName = get(i).getPidName();
				break;
			}			
		}
		
		return pidName;
	
	}
	
	public Treatment getTreatmentAt(int pidNumber){
		Treatment pidTreatment = null;
		for(int i=0; i<size(); i++){
			if(get(i).getPidNumber() == pidNumber){
				pidTreatment = get(i).getTreatment();
				break;
			}			
		}
		
		return pidTreatment;
	}
	
	public boolean contains(int pidNumber){
		boolean pidNumberExists = false;
		
		for(int i=0; i<size(); i++){
			if(get(i).getPidNumber() == pidNumber){
				pidNumberExists = true;
			}
		}
		
		return pidNumberExists;
	}
	
}
