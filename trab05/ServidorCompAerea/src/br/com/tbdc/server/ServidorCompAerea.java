package br.com.tbdc.server;

import br.com.tbdc.controller.ControladorVoo;
import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.voo.InfoVoo;
import br.com.tbdc.model.voo.TipoPassagem;
import br.com.tbdc.rmi.InterfacePassagens;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

/** Classe utilizada para a comunicação entre este servidor (Companhia Aérea)
 * com o servidor Coordenador, via RMI.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ServidorCompAerea extends UnicastRemoteObject implements InterfacePassagens {

    public ServidorCompAerea() throws RemoteException {
        super();
    }

    /*------------------------------------------------------------------------*/

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo, Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numPessoas) throws RemoteException {
        // Checagem de parâmetros
        if (tipo == null)
            throw new RemoteException("Tipo da passagem não pode ser nulo.");
        if (origem == null)
            throw new RemoteException("Cidade de origem não pode ser nula.");
        if (destino == null)
            throw new RemoteException("Cidade de destino não pode ser nula.");
        if (dataIda == null)
            throw new RemoteException("Data de ida não pode ser nula.");
        if (dataVolta == null && tipo == TipoPassagem.IDA_E_VOLTA)
            throw new RemoteException("Data de volta não pode ser nula.");
        if (numPessoas <= 0)
            throw new RemoteException("É esperado número de pessoas maior ou igual a 1.");

        return ControladorVoo.getInstance().consultarPassagens(tipo, origem, destino, dataIda, dataVolta, numPessoas);
    }

    /*------------------------------------------------------------------------*/

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) throws RemoteException {
        // Checagem de parâmetros
        if (tipo == null)
            throw new RemoteException("Tipo da passagem não pode ser nulo.");
        if (idVooIda < 0)
            throw new RemoteException("Identificador do voo de ida deve ser maior ou igual a 0.");
        if (tipo == TipoPassagem.IDA_E_VOLTA && idVooVolta < 0)
            throw new RemoteException("Identificador do voo de volta deve ser maior ou igual a 0.");
        if (numPessoas < 1)
            throw new RemoteException("É esperado número de pessoas maior ou igual a 1.");

        return ControladorVoo.getInstance().comprarPassagens(tipo, idVooIda, idVooVolta, numPessoas);
    }

}
