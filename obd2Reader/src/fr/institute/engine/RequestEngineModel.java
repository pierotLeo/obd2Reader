package fr.institute.engine;

import java.util.ArrayList;

import com.japisoft.formula.node.EvaluateException;

public interface RequestEngineModel {

	public double getUpToDateData(String pidName) throws EvaluateException;
	public String getUnit(String pidName);
	public String[] getCompatibleRequests();
	public ArrayList<String> getErrorCodes();
}
