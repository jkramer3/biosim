package com.traclabs.biosim.server.simulation.power;

import com.traclabs.biosim.idl.simulation.power.PowerPSPOATie;
import com.traclabs.biosim.server.framework.GenericServer;

/**
 * The Power PS Server. Creates an instance of the Power PS and registers it
 * with the nameserver.
 * 
 * @author Scott Bell
 */

public class PowerPSServer extends GenericServer {

    /**
     * Instantiates the server and binds it to the name server.
     * 
     * @param args
     *            aren't used for anything
     */
    public static void main(String args[]) {
        PowerPSServer myServer = new PowerPSServer();
        PowerPSImpl myPowerPS = new SolarPowerPS(GenericServer
                .getIDfromArgs(args), GenericServer.getNamefromArgs(args));
        myServer.registerServerAndRun(new PowerPSPOATie(myPowerPS), myPowerPS
                .getModuleName(), myPowerPS.getID());
    }
}

