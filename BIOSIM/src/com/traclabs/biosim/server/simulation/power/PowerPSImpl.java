package biosim.server.power;

import biosim.idl.power.*;
import biosim.server.util.*;

public class PowerPSImpl extends PowerPSPOA {
	private float currentPowerProduced = 0f;
	private boolean hasCollectedReferences = false;
	private PowerStore myPowerStore;

	public void tick(){
		currentPowerProduced = 100f;
		collectReferences();
		myPowerStore.addPower(100f);
	}
	
	private void collectReferences(){
		try{
			if (!hasCollectedReferences){
				myPowerStore = PowerStoreHelper.narrow(OrbUtils.getNCRef().resolve_str("PowerStore"));
				hasCollectedReferences = true;
			}
		}
		catch (org.omg.CORBA.UserException e){
			e.printStackTrace(System.out);
		}
	}
	
	public  float getPowerProduced(){
		return currentPowerProduced;
	}
	
	public String getModuleName(){
		return "PowerPS";
	}
}
