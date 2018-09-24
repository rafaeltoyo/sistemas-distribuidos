package server;

import model.Hospedagem;
import model.Pacote;
import model.Passagem;
import model.eventos.Evento;
import remote.AgencyServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class AgencyServerImpl extends UnicastRemoteObject implements AgencyServer {

    private ArrayList<Passagem> passagens = new ArrayList<>();

    private ArrayList<Hospedagem> hospedagens = new ArrayList<>();

    private ArrayList<Pacote> pacotes = new ArrayList<>();

    public AgencyServerImpl() throws RemoteException {
        super();
    }

    @Override
    public ArrayList<Passagem> consultarPassagens() throws RemoteException {
        return passagens;
    }

    @Override
    public boolean comprarPassagem(Passagem passagem) throws RemoteException {
        return false;
    }

    @Override
    public ArrayList<Hospedagem> consultarHospedagens() throws RemoteException {
        return hospedagens;
    }

    @Override
    public boolean comprarHospedagem(Hospedagem hospedagem) throws RemoteException {
        return false;
    }

    @Override
    public ArrayList<Pacote> consultarPacotes() throws RemoteException {
        return pacotes;
    }

    @Override
    public synchronized boolean comprarPacote(Pacote pacote) throws RemoteException {
        pacote = pacotes.get(pacote.getId());
        pacote.comprar();
        return true;
    }

    @Override
    public boolean registraEvento(Evento evento) throws RemoteException {
        return false;
    }

    @Override
    public boolean removerEvento(Evento evento) throws RemoteException {
        return false;
    }

    public void criarPassagem(Passagem passagem) {
        passagens.add(passagem.getId(), passagem);
    }

    public void criarHospedagem(Hospedagem hospedagem) {
        hospedagens.add(hospedagem.getId(), hospedagem);
    }

    public void criarPacote(Pacote pacote) {
        pacotes.add(pacote.getId(), pacote);
    }
}
