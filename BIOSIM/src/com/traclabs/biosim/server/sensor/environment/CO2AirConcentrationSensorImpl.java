package biosim.server.sensor.environment;

import biosim.server.sensor.framework.*;
import biosim.idl.sensor.environment.*;
import biosim.idl.simulation.environment.*;

public class CO2AirLevelSensorImpl extends EnvironmentSensorImpl implements CO2AirLevelSensorOperations{
	public CO2AirLevelSensorImpl(int pID){
		super(pID);
	}

	protected void gatherData(){
		float preFilteredValue = getInput().getCO2Level();
		myValue = randomFilter(preFilteredValue);
	}
	
	protected void notifyListeners(){
	}
	
	/**
	* Returns the name of this module (CO2AirLevelSensor)
	* @return the name of the module
	*/
	public String getModuleName(){
		return "CO2AirLevelSensor"+getID();
	}
}