package client;

import remote.AgencyClient;
import remote.AgencyServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AgencyClientImpl extends UnicastRemoteObject implements AgencyClient {
    private AgencyServer serverRef;

    public AgencyClientImpl(AgencyServer serverRef) throws RemoteException {
        super();
        this.serverRef = serverRef;
    }

    public void notifyEvent(String str) throws RemoteException {

    }
}
