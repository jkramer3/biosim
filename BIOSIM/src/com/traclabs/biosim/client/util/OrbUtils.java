package com.traclabs.biosim.client.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.framework.BioModuleHelper;

/**
 * The OrbUtils class provides basic CORBA utilities to server components
 * 
 * @author Scott Bell
 */

public class OrbUtils {
    //Flag to make sure OrbUtils only runs initialize once
    private static boolean initializeOrbRunOnce = false;

    //The root POA for transformation methods and other things
    private static POA myRootPOA = null;

    //The server ORB used resolving references
    private static ORB myOrb = null;

    //The root biosim naming context reference
    private static NamingContextExt myBiosimNamingContext = null;

    private static NamingContextExt myRootContext = null;

    private static Properties myORBProperties;

    /**
     * Shouldn't be called (everything static!)
     */
    private OrbUtils() {
    }

    public static void setORB(ORB pORB) {
        myOrb = pORB;
    }

    public static void setRootContext(NamingContextExt pRootContext) {
        myRootContext = pRootContext;
    }

    public static void setRootPOA(POA pRootPOA) {
        myRootPOA = pRootPOA;
    }

    /**
     * Returns the ORB
     * 
     * @return the ORB
     */
    public static ORB getORB() {
        initialize();
        return myOrb;
    }

    /**
     * Returns the root POA
     * 
     * @return the root POA
     */
    public static POA getRootPOA() {
        initialize();
        return myRootPOA;
    }

    /**
     * Returns the naming context associated with this ID
     * 
     * @return the naming context
     */
    public static NamingContextExt getNamingContext(int pID) {
        initialize();
        NamingContextExt idContext = null;
        try {
            idContext = NamingContextExtHelper.narrow(myBiosimNamingContext
                    .resolve_str("" + pID));
        } catch (Exception e) {
        }
        return idContext;
    }

    /**
     * Forces OrbUtils to retrieve the RootPoa and Naming Service again on next
     * request.
     */
    public static void resetInit() {
        initializeOrbRunOnce = false;
    }

    /**
     * Done only once, this method initializes the ORB, resolves the root POA,
     * and grabs the naming context.
     */
    private static void initialize() {
        while (!initializeLoop()) {
            sleepAwhile();
        }
    }

    /**
     * Done only once, this method initializes the ORB, resolves the root POA,
     * and grabs the naming context.
     */
    private static boolean initializeLoop() {
        if (initializeOrbRunOnce)
            return true;
        try {
            String[] nullArgs = null;
            // create and initialize the ORB
            if (myOrb == null)
                myOrb = ORB.init(nullArgs, myORBProperties);

            // get reference to rootpoa & activate the POAManager
            if (myRootPOA == null) {
                myRootPOA = POAHelper.narrow(myOrb
                        .resolve_initial_references("RootPOA"));
                myRootPOA.the_POAManager().activate();
            }
            if (myRootContext == null)
                myRootContext = NamingContextExtHelper.narrow(myOrb
                        .resolve_initial_references("NameService"));
            NamingContextExt comContext = NamingContextExtHelper
                    .narrow(myRootContext.resolve_str("com"));
            NamingContextExt traclabsContext = NamingContextExtHelper
                    .narrow(comContext.resolve_str("traclabs"));
            myBiosimNamingContext = NamingContextExtHelper
                    .narrow(traclabsContext.resolve_str("biosim"));
            initializeOrbRunOnce = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public static BioModule getBioModule(int pID, String pModuleName) {
        BioModule module = null;
        try{
            module = BioModuleHelper.narrow(getNamingContext(pID).resolve_str(
                pModuleName));
        }
        catch (Exception e){
            Logger.getLogger(OrbUtils.class).info(
                    "(id="+pID+") Had problems getting module:"+pModuleName+" "+ e);
        }
        return module;
    }

    /**
     * Sleeps for a few seconds. Used when we can't find the naming service and
     * need to poll again after a few seconds.
     */
    public static void sleepAwhile() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

}