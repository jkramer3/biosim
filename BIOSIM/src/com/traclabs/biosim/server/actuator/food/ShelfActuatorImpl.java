package biosim.server.actuator.food;

import biosim.server.actuator.framework.*;
import biosim.idl.actuator.food.*;
import biosim.idl.framework.*;
import biosim.idl.simulation.food.*;

public abstract class ShelfActuatorImpl extends GenericActuatorImpl implements ShelfActuatorOperations{
	protected Shelf myShelf;
	
	public ShelfActuatorImpl(int pID, String pName){
		super(pID, pName);
	}

	public void setOutput(BiomassRS pBiomassRS, int shelfIndex){
		myShelf = pBiomassRS.getShelf(shelfIndex);
	}
	
	public Shelf getOutput(){
		return myShelf;
	}
	
	public BioModule getOutputModule(){
		return myShelf.getBiomassRS();
	}
}
