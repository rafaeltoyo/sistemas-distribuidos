package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgencyClient extends Remote {
    void notifyEvent(String str) throws RemoteException;
}
