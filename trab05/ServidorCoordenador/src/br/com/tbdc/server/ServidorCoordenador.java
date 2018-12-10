package br.com.tbdc.server;

import br.com.tbdc.data.Transaction;
import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.hotel.InfoHotelRet;
import br.com.tbdc.model.pacote.ConjuntoPacote;
import br.com.tbdc.model.voo.InfoVoo;
import br.com.tbdc.model.voo.TipoPassagem;
import br.com.tbdc.rmi.InterfaceCoordenador;
import br.com.tbdc.rmi.InterfaceHospedagens;
import br.com.tbdc.rmi.InterfacePassagens;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * {@inheritDoc}
 */
public class ServidorCoordenador extends UnicastRemoteObject implements InterfaceCoordenador {

    /**
     * Serviço de nomes
     */
    private Registry registry;

    /**
     * =================================================================================================================
     * Construtor único.
     * =================================================================================================================
     *
     * @param registry serviço de nomes
     * @throws RemoteException caso ocorra erro no RMI
     */
    public ServidorCoordenador(Registry registry) throws RemoteException {
        super();
        this.registry = registry;
        TransactionController.getInstance();
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    @Override
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo, Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numPessoas) throws RemoteException {
        try {
            /* A razão para fazer lookup toda vez é que caso o servidor da
             * companhia aérea sofra um crash e volte, fará um rebind no serviço
             * de nomes, e a referência antiga torna-se inválida. */
            InterfacePassagens servidorCompAerea = (InterfacePassagens) registry.lookup("servidor_comp_aerea");
            return servidorCompAerea.consultarPassagens(tipo, origem, destino, dataIda, dataVolta, numPessoas);
        }//
        catch (NotBoundException e) {
            return new ArrayList<>();
        }
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    @Override
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) throws RemoteException {
        try {
            InterfacePassagens servidorCompAerea = (InterfacePassagens) registry.lookup("servidor_comp_aerea");
            return servidorCompAerea.comprarPassagens(tipo, idVooIda, idVooVolta, numPessoas);
        }//
        catch (NotBoundException e) {
            return false;
        }
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    @Override
    public ArrayList<InfoHotelRet> consultarHospedagens(Cidade local, LocalDate dataIni, LocalDate dataFim, int numQuartos, int numPessoas) throws RemoteException {
        try {
            InterfaceHospedagens servidorHotel = (InterfaceHospedagens) registry.lookup("servidor_hotel");
            return servidorHotel.consultarHospedagens(local, dataIni, dataFim, numQuartos, numPessoas);
        }//
        catch (NotBoundException e) {
            return new ArrayList<>();
        }
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    @Override
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni,
                                     LocalDate dataFim, int numQuartos) throws RemoteException {
        try {
            InterfaceHospedagens servidorHotel = (InterfaceHospedagens) registry.lookup("servidor_hotel");
            return servidorHotel.comprarHospedagem(idHotel, dataIni, dataFim, numQuartos);
        }//
        catch (NotBoundException e) {
            return false;
        }
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    public ConjuntoPacote consultarPacotes(Cidade origem, Cidade destino,
                                           LocalDate dataIda, LocalDate dataVolta, int numQuartos,
                                           int numPessoas) throws RemoteException {
        // Referência para os participantes
        InterfacePassagens servidorCompAerea;
        InterfaceHospedagens servidorHotel;

        try {
            // Pegar os participantes pelo registro de nomes
            servidorCompAerea = (InterfacePassagens) registry.lookup("servidor_comp_aerea");
            servidorHotel = (InterfaceHospedagens) registry.lookup("servidor_hotel");
        }//
        catch (NotBoundException e) {
            return new ConjuntoPacote();
        }

        // Obtém todos os voos
        ArrayList<InfoVoo> voos = servidorCompAerea.consultarPassagens(TipoPassagem.IDA_E_VOLTA, origem, destino, dataIda, dataVolta, numPessoas);

        // Obtém as informações de hotéis
        ArrayList<InfoHotelRet> hosps = servidorHotel.consultarHospedagens(destino, dataIda, dataVolta, numQuartos, numPessoas);

        ConjuntoPacote conjuntoPacote = new ConjuntoPacote();
        if (!voos.isEmpty() && !hosps.isEmpty()) {

            // Pegar todos Voos
            for (InfoVoo v : voos) {
                // Ida
                if (v.getOrigem() == origem) {
                    conjuntoPacote.adicionarVooIda(v);
                }
                // Volta
                else {
                    conjuntoPacote.adicionarVooVolta(v);
                }
            }

            // Pegar todos Hoteis
            for (InfoHotelRet ihr : hosps) {
                conjuntoPacote.adicionarHospedagem(ihr);
            }
        }

        return conjuntoPacote;
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    @Override
    public boolean comprarPacote(int idVooIda, int idVooVolta, int idHotel, LocalDate dataIda, LocalDate dataVolta, int numQuartos, int numPessoas) throws RemoteException {

        // Referência para os participantes
        InterfacePassagens servidorCompAerea;
        InterfaceHospedagens servidorHotel;

        try {
            // Pegar os participantes pelo registro de nomes
            servidorCompAerea = (InterfacePassagens) registry.lookup("servidor_comp_aerea");
            servidorHotel = (InterfaceHospedagens) registry.lookup("servidor_hotel");
        }//
        catch (NotBoundException e) {
            return false;
        }

        // Inicio da transação
        int id = TransactionController.getInstance().beginTransaction();

        // Criar um serviço para esperar as resposta
        ServidorResposta service = new ServidorResposta(id);

        // Iniciar o processo de comprar Pacote
        // Enviar pergunta ao servidor de passagens
        servidorCompAerea.comprarPacote(TipoPassagem.IDA_E_VOLTA, idVooIda, idVooVolta, numPessoas, service);
        // Enviar pergunta ao servidor de hospedagens
        servidorHotel.comprarPacote(idHotel, dataIda, dataVolta, numQuartos, service);

        // Jogar transação para provisória
        TransactionController.getInstance().prepare(id);

        // Esperar por duas respostas
        if (service.esperar(2)) {

            // Criar outro serviço para esperar as resposta
            ServidorResposta serviceCommit = new ServidorResposta(id);

            // Confirmar transação localmente
            TransactionController.getInstance().commit(id);

            // Confirmar transação para os participantes
            servidorCompAerea.efetivarPacote(serviceCommit);
            servidorHotel.efetivarPacote(serviceCommit);

            // Esperar todos efetivarem com sucesso
            serviceCommit.esperar(2);
            return true;
        }//
        else {

            // Criar outro serviço para esperar as resposta
            ServidorResposta serviceRollback = new ServidorResposta(id);

            // Confirmar transação localmente
            TransactionController.getInstance().rollback(id);

            // Confirmar transação para os participantes
            servidorCompAerea.abortarPacote(serviceRollback);
            servidorHotel.abortarPacote(serviceRollback);

            // Esperar todos efetivarem com sucesso
            serviceRollback.esperar(2);
            return false;
        }
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    @Override
    public Transaction.Status consultarStatusTransacao(int idTransacao) throws RemoteException {
        Transaction.Status s = TransactionController.getInstance().getTransactionStatus(idTransacao);
        return ((s != null)? s : Transaction.Status.ABORTED);
    }
}
