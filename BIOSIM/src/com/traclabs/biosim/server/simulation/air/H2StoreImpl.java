package biosim.server.simulation.air;

import biosim.idl.simulation.air.*;
import biosim.server.simulation.framework.*;
/**
 * The H2 Store Implementation.  Used by the AirRS to store excess H2 for the crew.
 * Not really used right now.
 *
 * @author    Scott Bell
 */
public class H2StoreImpl extends StoreImpl implements H2StoreOperations {
	public H2StoreImpl(int pID){
		super(pID);
	}
	/**
	* Returns the name of this module (H2Store)
	* @return the name of this module
	*/
	public String getModuleName(){
		return "H2Store"+getID();
	}
}
