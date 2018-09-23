package server;

import remote.AgencyServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AgencyServerImpl extends UnicastRemoteObject implements AgencyServer {
    public AgencyServerImpl() throws RemoteException {
        super();
    }

    public String getWirow() {
        return "Wirow";
    }
}
