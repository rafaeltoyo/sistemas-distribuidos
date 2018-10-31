package server.model.saldo;

/** Esta classe representa uma compra de um voo ou hospedagem. É utilizada para
 * execução de rollback (quando necessário) na compra simultânea de vários
 * itens.
 */
public class Reserva {
    /** Quantidade da reserva */
    private int quantidade;

    /*------------------------------------------------------------------------*/

    /** Retorna a quantidade da reserva
     * @return quantidade da reserva
     */
    public int getQuantidade() {
        return quantidade;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão.
     * @param quantidade quantidade da reserva
     */
    public Reserva(int quantidade) {
        this.quantidade = quantidade;
    }
}
