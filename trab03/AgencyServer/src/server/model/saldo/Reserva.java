package server.model.saldo;

/**
 * Classe para controle de rollback da compra simultânea de vários itens
 */
public class Reserva {

    /** Quantidade da reserva */
    private int quantidade;

    /**
     * Retorna a quantidade da reserva
     * @return Quantidade da reserva
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Construtor padrão
     * @param quantidade Quantidade da reserva
     */
    public Reserva(int quantidade) {
        this.quantidade = quantidade;
    }
}
