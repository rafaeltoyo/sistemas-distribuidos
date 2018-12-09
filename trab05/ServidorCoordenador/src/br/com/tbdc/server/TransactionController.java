package br.com.tbdc.server;

import br.com.tbdc.data.DataStorage;
import br.com.tbdc.data.Transaction;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

public class TransactionController {

    private static TransactionController ourInstance;
    private DataStorage log;
    private final HashMap<Integer, Transaction> transactions;
    private int currentId;

    private TransactionController() {
        this.currentId = 0;
        this.transactions = new HashMap<>();

        this.log = new DataStorage("transactions");
        log.readData(this::resumeTransactions);

        Collection<Transaction> c = this.transactions.values();
        for (Transaction t : c) {
            if (t.getStatus() != Transaction.Status.CONFIRMED && t.getStatus() != Transaction.Status.ABORTED) {
                this.rollback(t.getId());
            }
        }
    }

    public static synchronized TransactionController getInstance() {
        if (ourInstance == null) {
            ourInstance = new TransactionController();
        }
        return ourInstance;
    }

    public int beginTransaction() {
        Transaction t;
        synchronized (transactions) {
            t = new Transaction(++currentId, "", LocalDateTime.now(), Transaction.Status.ACTIVE);
            this.transactions.put(t.getId(), t);
            log.writeData(t.toString());
        }
        return t.getId();
    }

    /**
     * Método para passar uma Transação de ATIVA para PENDENTE
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
     * Método para passar uma Transação de PENDENTE para ABORTADA
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
     * Método para passar uma Transação de PENDENTE para CONFIRMADA
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
     * Método para converter uma linha lida do arquivo de Log para um objeto Transação e armazena-lo corretamente.
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
}
