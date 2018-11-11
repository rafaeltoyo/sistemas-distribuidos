package server.model.saldo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/** Representa quantias em dinheiro.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Dinheiro implements Serializable {
    /** Moeda brasileira */
    private static final Currency BRL = Currency.getInstance("BRL");

    /** Arredondamento padrão */
    private static final RoundingMode ARREDONDAMENTO_PADRAO = RoundingMode.HALF_EVEN;

    /*------------------------------------------------------------------------*/

    /** Quantia constante */
    private final BigDecimal quantia;

    /** Moeda constante */
    private final Currency moeda;

    /*------------------------------------------------------------------------*/

    /** Retorna a quantia em dinheiro.
     * @return quantia em dinheiro
     */
    public BigDecimal getQuantia() {
        return quantia;
    }

    /** Retorna a moeda (ex: BRL).
     * @return moeda
     */
    public Currency getMoeda() {
        return moeda;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor privado com especificação de todos os atributos (não utiliza
     * atributos padrão da classe).
     * @param quantia quantia em dinheiro
     * @param moeda moeda (ex: BRL)
     * @param arredondamento modo de arredondamento do BigDecimal
     */
    private Dinheiro(BigDecimal quantia, Currency moeda, RoundingMode arredondamento) {
        this.moeda = moeda;
        this.quantia = quantia.setScale(moeda.getDefaultFractionDigits(), arredondamento);
    }

    /** Construtor que utiliza o modo de arredondamento padrão (HALF_EVEN).
     * @param quantia quantia em dinheiro
     * @param moeda moeda (ex: BRL)
     */
    private Dinheiro(BigDecimal quantia, Currency moeda) {
        this(quantia, moeda, ARREDONDAMENTO_PADRAO);
    }

    /*------------------------------------------------------------------------*/

    /** Cria um objeto Dinheiro com a quantia desejada em reais.
     * @param quantia quantia em reais
     * @return objeto instanciado
     */
    public static Dinheiro reais(BigDecimal quantia) {
        return new Dinheiro(quantia, BRL);
    }

    /*------------------------------------------------------------------------*/

    /** Retorna uma string printável para a quantia em dinheiro.
     * @return string que representa a quantia
     */
    @Override
    public String toString() {
        return getMoeda().getSymbol() + " " + getQuantia();
    }

    /*------------------------------------------------------------------------*/

    /** Compara este Dinheiro com o Dinheiro especificado. É um wrapper para o
     * método compareTo() da classe BigDecimal.
     * @param val objeto Dinheiro com o qual se faz a comparação
     * @return -1, 0, ou 1, se esse Dnheiro numericamente menor que, igual a, ou
     * maior que val.
     */
    public int compareTo(Dinheiro val) {
        return this.getQuantia().compareTo(val.getQuantia());
    }
}
