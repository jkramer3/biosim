package biosim.server.food;

import biosim.server.framework.*;
import biosim.idl.food.*;
/**
 * The Food Store Server.  Creates an instance of the Food Processor and registers it with the nameserver.
 *
 * @author    Scott Bell
 */

public class FoodStoreServer extends GenericServer{
	
	/**
	* Instantiates the server and binds it to the name server.
	* @param args aren't used for anything
	*/
	public static void main(String args[]) {
		FoodStoreServer myServer = new FoodStoreServer();
		FoodStoreImpl myFoodStoreImpl = new FoodStoreImpl(0);
		myServer.registerServerAndRun(new FoodStorePOATie(myFoodStoreImpl), myFoodStoreImpl.getModuleName());
	}
}


