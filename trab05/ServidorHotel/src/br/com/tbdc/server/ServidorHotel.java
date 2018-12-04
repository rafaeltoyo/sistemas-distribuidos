package br.com.tbdc.server;

import br.com.tbdc.controller.ControladorHotel;
import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.hotel.InfoHotelRet;
import br.com.tbdc.rmi.InterfaceHospedagens;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

/** Classe utilizada para a comunicação entre este servidor (Hotel) com o
 * servidor Coordenador, via RMI.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ServidorHotel extends UnicastRemoteObject implements InterfaceHospedagens {

    /** Construtor em branco. */
    public ServidorHotel() throws RemoteException {
        super();
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public ArrayList<InfoHotelRet> consultarHospedagens(Cidade local, LocalDate dataIni, LocalDate dataFim, int numQuartos, int numPessoas) throws RemoteException {
        // Checagem de parâmetros
        if (local == null)
            throw new RemoteException("Cidade não pode ser nula.");
        if (dataIni == null)
            throw new RemoteException("Data de início não pode ser nula.");
        if (dataFim == null)
            throw new RemoteException("Data de fim não pode ser nula.");
        if (!dataFim.isAfter(dataIni))
            throw new RemoteException("Data de fim deve ser posterior à data de início.");
        if (numQuartos <= 0)
            throw new RemoteException("É esperado número de quartos maior ou igual a 1.");
        if (numPessoas <= 0)
            throw new RemoteException("É esperado número de pessoas maior ou igual a 1.");

        return ControladorHotel.getInstance().consultarHospedagens(local, dataIni, dataFim, numQuartos, numPessoas);
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos) throws RemoteException {
        // Checagem de parâmetros
        if (idHotel < 0)
            throw new RemoteException("O identificador do hotel deve ser maior ou igual a 1.");
        if (dataIni == null)
            throw new RemoteException("Data de início não pode ser nula.");
        if (dataFim == null)
            throw new RemoteException("Data de fim não pode ser nula.");
        if (!dataFim.isAfter(dataIni))
            throw new RemoteException("Data de fim deve ser posterior à data de início.");
        if (numQuartos <= 0)
            throw new RemoteException("É esperado número de quartos maior ou igual a 1.");

        return ControladorHotel.getInstance().comprarHospedagem(idHotel, dataIni, dataFim, numQuartos);
    }
}
