package fr.obd2Reader.command;

public class FuelSystemStatusCommand extends ObdCommand{

	private String[] status;
	
	public FuelSystemStatusCommand(){
		super("01 03");
		status = new String[2];
	}
	
	protected void calculate(){
		for(int i = 0; i<2; i++){
			switch(getInBuff().get(i)){
				case 1 : status[i] = "Open loop due to insufficient engine temperature"; break;
				case 2 : status[i] = "Closed loop, using oxygen sensor feedback to determine fuel mix"; break;
				case 4 : status[i] = " Open loop due to engine load OR fuel cut due to deceleration"; break;
				case 8 : status[i] = "Open loop due to system failure"; break;
				case 16 : status[i] = "Closed loop, using at least one oxygen sensor but there is a fault in the feedback system"; break;
				default : status[i] = "Invalid answer"; break;
			}
		}
	}
}
