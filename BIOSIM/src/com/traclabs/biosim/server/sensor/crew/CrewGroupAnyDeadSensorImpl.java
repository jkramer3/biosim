package biosim.server.sensor.crew;

import biosim.server.sensor.framework.*;
import biosim.idl.sensor.crew.*;
import biosim.idl.framework.*;

public class CrewGroupAnyDeadSensorImpl extends CrewGroupSensorImpl implements CrewGroupAnyDeadSensorOperations{
	public CrewGroupAnyDeadSensorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected void gatherData(){
		boolean preFilteredBooleanValue = getInput().anyDead();
		if (preFilteredBooleanValue)
			myValue = randomFilter(1);
		else
			myValue = randomFilter(0);
	}
	
	public float getMax(){
		return 1f;
	}
	
	protected void notifyListeners(){
	}
	
	protected BioModule getInputModule(){
		return (BioModule)(getInput());
	}
}