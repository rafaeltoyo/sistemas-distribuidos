package br.com.tbdc.data;

import java.time.LocalDateTime;

/**
 * Representação de uma Transação
 */
public class Transaction {

    /**
     * ID da Transação
     */
    private int id;

    /**
     * Conteúdo da Transação
     */
    private String content;

    /**
     * Timestamp da Transação
     */
    private LocalDateTime timestamp;

    /**
     * Status da Transação
     */
    private Transaction.Status status;

    /**
     * Construtor sem ID
     *
     * @param content   Conteúdo da Transação
     * @param timestamp Timestamp da Transação
     * @param status    Status da Transação
     */
    public Transaction(String content, LocalDateTime timestamp, Status status) {
        this.id = 0;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }

    /**
     * Construtor genético
     *
     * @param id        ID da Transação
     * @param content   Conteúdo da Transação
     * @param timestamp Timestamp da Transação
     * @param status    Status da Transação
     */
    public Transaction(int id, String content, LocalDateTime timestamp, Status status) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }

    /**
     * Converte uma linha lida do arquivo de Log de Transações para um objeto Transação
     *
     * @param s String lida do arquivo de Log
     * @return Objeto com os dados da Transação lida
     */
    public static Transaction load(String s) {
        String regex = "Transaction\\{" +
                "id='([0-9]+)'," +
                "content='([a-zA-Z0-9 \\-]*)'," +
                "timestamp='(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3})'," +
                "status='([A-Z]+)'" +
                "\\}";

        if (s.matches(regex)) {
            int id = Integer.decode(s.replaceAll(regex, "$1"));
            String content = s.replaceAll(regex, "$2");
            LocalDateTime date = LocalDateTime.parse(s.replaceAll(regex, "$3"));
            Status status = Status.valueOf(s.replaceAll(regex, "$4"));
            return new Transaction(id, content, date, status);
        }
        return null;
    }

    /**
     * Transforma a Transação em String para escrita no arquivo de Log
     *
     * @return String da Transação
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + Integer.toString(id) + "'," +
                "content='" + content + "'," +
                "timestamp='" + timestamp.toString() + "'," +
                "status='" + status.name() + "'}\n";
    }

    /**
     * Retorna o ID da Transação
     *
     * @return ID da Transação
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o conteúdo da Transação
     *
     * @return Conteúdo da Transação
     */
    public String getContent() {
        return content;
    }

    /**
     * Retorna o timestamp da Transação
     *
     * @return Timestamp da Transação
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Retorna o status da Transação
     *
     * @return Status da Transação
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Método para passar uma transação ativa (ACTIVE) para pendente (WAITING).
     *
     * @return Sucesso na mudança do status (Condições da Transação para tal mudança eram válidas)
     */
    public Transaction prepare() {
        if (this.status == Status.ACTIVE) {
            return new Transaction(this.id, this.content, LocalDateTime.now(), Status.WAITING);
        }
        return null;
    }

    /**
     * Método para passar uma transação ativa (ACTIVE) para pendente (WAITING).
     *
     * @return Sucesso na mudança do status (Condições da Transação para tal mudança eram válidas)
     */
    public Transaction commit() {
        if (this.status == Status.WAITING) {
            return new Transaction(this.id, this.content, LocalDateTime.now(), Status.CONFIRMED);
        }
        return null;
    }

    /**
     * Método para passar uma transação ativa (ACTIVE) para pendente (WAITING).
     *
     * @return Sucesso na mudança do status (Condições da Transação para tal mudança eram válidas)
     */
    public Transaction rollback() {
        if (this.status == Status.ACTIVE || this.status == Status.WAITING) {
            return new Transaction(this.id, this.content, LocalDateTime.now(), Status.ABORTED);
        }
        return null;
    }

    /**
     * Consumir outros objetos Transações de mesmo ID desse (Usado pelo Parser de Log) e juntar os dados.
     * @param target Objeto de possível redundância
     * @return Se a atualização (consumir) foi realizada
     */
    public boolean merge(Transaction target) {
        // Alvo passado vazio ou não é a mesma transação (mesmo ID)
        if (target == null || target.id != this.id) return false;

        // Verificar Timestamp das Transações
        if (this.timestamp.isAfter(target.timestamp)) {
            // Target tem um timestamp anterior ao atual
            return false;
        }


        // Verificar consistência dos Status
        switch (this.status) {

            case ACTIVE:
                // Transação ativa só modificará para não ativa
                if (target.status == Status.ACTIVE) break;

                this.content = target.content;
                this.timestamp = target.timestamp;
                this.status = target.status;
                return true;

            case WAITING:
                // Transação pendente só modificará para confirmada ou abortada
                if (target.status == Status.ACTIVE || target.status == Status.WAITING) break;

                this.content = target.content;
                this.timestamp = target.timestamp;
                this.status = target.status;
                return true;

            case CONFIRMED:
                break;

            case ABORTED:
                break;

        }
        return false;
    }

    /**
     * Enum dos Status válidos para uma Transação
     */
    public enum Status {

        /* Transação ativa */
        ACTIVE,

        /* Transação pendente */
        WAITING,

        /* Transação confirmada */
        CONFIRMED,

        /* Transação abortada */
        ABORTED
    }
}
