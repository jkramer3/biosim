package biosim.server.environment;

import biosim.server.framework.*;
import biosim.idl.environment.*;
/**
 * The Sim Environment Server.  Creates an instance of the Sim Environment and registers it with the nameserver.
 *
 * @author    Scott Bell
 */

public class SimEnvironmentServer extends GenericServer{
	
	/**
	* Instantiates the server and binds it to the name server.
	* @param args aren't used for anything
	*/
	public static void main(String args[]) {
		SimEnvironmentServer myServer = new SimEnvironmentServer();
		SimEnvironmentImpl mySimEnvironment = new SimEnvironmentImpl(0);
		myServer.registerServerAndRun(new SimEnvironmentPOATie(mySimEnvironment), mySimEnvironment.getModuleName());
	}
}

