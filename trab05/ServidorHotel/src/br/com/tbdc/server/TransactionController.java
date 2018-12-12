package br.com.tbdc.server;

import br.com.tbdc.controller.ControladorHotel;
import br.com.tbdc.data.DataStorage;
import br.com.tbdc.data.Transaction;
import br.com.tbdc.rmi.InterfaceCoordenador;

import java.io.File;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

public class TransactionController {

    /**
     * Instância única do controlador
     */
    private static TransactionController ourInstance;
    /**
     * Espelho em memória dos dados do arquivo de Log
     */
    private final HashMap<Integer, Transaction> transactions;
    /**
     * Classe responsável por abrir e gerenciar o arquivo de Log de transações
     */
    private DataStorage log;
    /**
     * Controle do próximo ID de uma nova transação
     */
    private int currentId;
    /**
     * Referência ao coordenador para solicitar status de transações
     */
    private InterfaceCoordenador coordStatus;

    /**
     * =================================================================================================================
     * Construtor genérico (privado)
     * =================================================================================================================
     */
    private TransactionController() {
        this.currentId = 0;
        this.transactions = new HashMap<>();

        this.log = new DataStorage("transactions");
        log.readData(this::resumeTransactions);

        Collection<Transaction> c = this.transactions.values();
        for (Transaction t : c) {
            if (t.getStatus() != Transaction.Status.CONFIRMED && t.getStatus() != Transaction.Status.ABORTED) {
                // Se a transação estiver no estado ACTIVE, não foi enviada confirmação ao coordenador.
                // Portanto, certamente a transação foi abortada.
                if (t.getStatus() == Transaction.Status.ACTIVE) {
                    this.rollback(t.getId());
                }
                else {
                    attemptTransactionRecovery(t.getId());
                }
            }
        }
    }

    /**
     * =================================================================================================================
     * Resgatar a instância do controlador (singleton)
     * =================================================================================================================
     *
     * @return Referência para a instância única do controlador
     */
    public static synchronized TransactionController getInstance() {
        if (ourInstance == null) {
            ourInstance = new TransactionController();
        }
        return ourInstance;
    }

    /**
     * =================================================================================================================
     * Iniciar uma transação solicitada pelo servidor
     * =================================================================================================================
     *
     * @param idTransaction ID da transação iniciada
     * @param idHotel       ID do Hotel
     * @param dataIni       Data de entrada
     * @param dataFim       Data de saída
     * @param qnt           Quantidade de diárias em cada um dos dias
     * @return ID da transação iniciada
     */
    public int acceptTransaction(int idTransaction, int idHotel, LocalDate dataIni, LocalDate dataFim, int qnt) {
        Transaction t;
        synchronized (transactions) {
            t = new Transaction(idTransaction,
                    Integer.toString(idHotel) + " " + dataIni.toString() + " " + dataFim.toString() + " " + Integer.toString(qnt),
                    LocalDateTime.now(),
                    Transaction.Status.ACTIVE);
            this.transactions.put(t.getId(), t);
            log.writeData(t.toString());
        }
        return t.getId();
    }

    /**
     * =================================================================================================================
     * Método para passar uma Transação de ATIVA para PENDENTE
     * =================================================================================================================
     *
     * @param id ID da Transação a ser modificada
     * @return Sucesso da operação
     */
    public boolean prepare(int id) {
        synchronized (transactions) {

            // Buscar a transação pelo ID
            Transaction current = transactions.get(id);
            if (current != null) {

                // Modificar Status da transação
                Transaction next = transactions.get(id).prepare();

                // Checar sucesso da mudança e escrita no Log
                if (next != null && log.writeData(next.toString())) {
                    // Atualizar a referência para a nova transação
                    transactions.put(id, next);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * =================================================================================================================
     * Método para passar uma Transação NÃO CONFIRMADA para ABORTADA
     * =================================================================================================================
     *
     * @param id ID da Transação a ser modificada
     * @return Sucesso da operação
     */
    public boolean rollback(int id) {
        synchronized (transactions) {

            // Buscar a transação pelo ID
            Transaction current = transactions.get(id);
            if (current != null) {

                // Modificar Status da transação
                Transaction next = transactions.get(id).rollback();

                // Checar sucesso da mudança e escrita no Log
                if (next != null && log.writeData(next.toString())) {
                    // Atualizar a referência para a nova transação
                    transactions.put(id, next);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * =================================================================================================================
     * Método para passar uma Transação de PENDENTE para CONFIRMADA
     * =================================================================================================================
     *
     * @param id ID da Transação a ser modificada
     * @return Sucesso da operação
     */
    public boolean commit(int id) {
        synchronized (transactions) {

            // Buscar a transação pelo ID
            Transaction current = transactions.get(id);
            if (current != null) {

                // Modificar Status da transação
                Transaction next = transactions.get(id).commit();

                // Checar sucesso da mudança e escrita no Log
                if (next != null && log.writeData(next.toString())) {
                    // Atualizar a referência para a nova transação
                    transactions.put(id, next);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * =================================================================================================================
     * Método para converter uma linha lida do arquivo de Log para um objeto Transação e armazena-lo corretamente.
     * =================================================================================================================
     *
     * @param s Linha do arquivo de Log
     * @return Sucesso da operação
     */
    private Boolean resumeTransactions(String s) {

        // Converter a string para um objeto Transação
        Transaction loaded = Transaction.load(s);

        // Validar sucesso da operação
        if (loaded != null) {

            // Lock no array de Transações
            synchronized (transactions) {

                // Tentar encontrar uma Transação de mesmo ID da Transação parseada
                Transaction current = this.transactions.get(loaded.getId());

                // Se encontrar:
                if (current != null) {
                    // 1) Tentar juntar os dados das duas
                    current.merge(loaded);
                }
                // Se não encontrar, apenas armazena a transação parseada
                else {
                    this.transactions.put(loaded.getId(), loaded);
                }
            }

            // Atualizar ponteiro do ID a ser utilizado para novas transações
            this.currentId = loaded.getId() > this.currentId ? loaded.getId() : this.currentId;
        }

        // Continuar o parser -> True
        return true;
    }

    /**
     * =================================================================================================================
     * Método para consultar o Status de uma transação
     * =================================================================================================================
     *
     * @param id ID da transação
     * @return Status da transação solicitada
     */
    public Transaction.Status getTransactionStatus(int id) {

        synchronized (this.transactions) {
            Transaction t = this.transactions.get(id);
            if (t != null) {
                return t.getStatus();
            }
            return null;
        }
    }

    /**
     * =================================================================================================================
     * Método para salvar a referência do coordenador para solicitação de status
     * =================================================================================================================
     *
     * @param coordStatus referência ao coordenador
     */
    public void setCoordStatus(InterfaceCoordenador coordStatus) {
        this.coordStatus = coordStatus;
    }

    /**
     * =================================================================================================================
     * Método que tenta recuperar uma transação em estado WAITING a partir dos arquivos temporários
     * =================================================================================================================
     *
     * @param transactionId identificador da transação a tentar recuperar
     */
    private void attemptTransactionRecovery(int transactionId) {
        // Verifica se o arquivo existe
        File tempFile = new File("temp" + File.separator + transactionId + ".tmp");
        if (tempFile.isFile()) {
            try {
                // Se a transação foi confirmada pelo coordenador, recupera o objeto e dá commit
                if (coordStatus.consultarStatusTransacao(transactionId) == Transaction.Status.CONFIRMED) {
                    ControladorHotel.getInstance().recuperarCompraPacote(transactionId);
                    this.commit(transactionId);
                }
                else {
                    // Senão, apenas apaga o arquivo temporário e marca como abortada.
                    this.rollback(transactionId);
                    tempFile.delete();
                }
            }
            catch (RemoteException e) {
                // ???
                this.rollback(transactionId);
                tempFile.delete();
            }
        }
    }

    // =================================================================================================================
}
