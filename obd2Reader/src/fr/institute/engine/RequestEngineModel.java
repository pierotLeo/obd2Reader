package fr.institute.engine;

import java.util.ArrayList;

public interface RequestEngineModel {

	public float getUpToDateData(String pidName);
	public String[] getCompatibleRequests();
	public ArrayList<String> getErrorCodes();
}
