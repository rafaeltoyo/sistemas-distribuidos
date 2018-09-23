package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgencyServer extends Remote {
    String getWirow() throws RemoteException;
}
