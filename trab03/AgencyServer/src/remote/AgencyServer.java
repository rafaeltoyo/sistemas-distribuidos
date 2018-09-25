package remote;

import model.Hospedagem;
import model.Pacote;
import model.Passagem;
import model.eventos.Evento;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface AgencyServer extends Remote {

    ArrayList<Passagem> consultarPassagens() throws RemoteException;

    boolean comprarPassagem(Passagem passagem) throws RemoteException;

    ArrayList<Hospedagem> consultarHospedagens() throws RemoteException;

    boolean comprarHospedagem(Hospedagem hospedagem) throws RemoteException;

    ArrayList<Pacote> consultarPacotes() throws RemoteException;

    boolean comprarPacote(Pacote pacote) throws RemoteException;

    boolean registraEvento(Evento evento) throws RemoteException;

    boolean removerEvento(Evento evento) throws RemoteException;

}
