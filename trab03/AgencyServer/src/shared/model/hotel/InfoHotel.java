package shared.model.hotel;

import shared.model.cidade.Cidade;
import shared.model.saldo.Dinheiro;

/** Esta classe é uma estrutura de armazenamento de informações de um hotel.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class InfoHotel {
    /** Contagem de hotéis para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador numérico do hotel */
    private int id;

    /** Nome do hotel */
    private String nome;

    /** Cidade do hotel */
    private Cidade local;

    /** Número de quartos do hotel */
    private int numQuartos;

    /** Preço da diária */
    private Dinheiro precoDiaria;

    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do hotel
     * @return identificador do hotel
     */
    public int getId() {
        return id;
    }

    /** Retorna o nome do hotel
     * @return nome do hotel
     */
    public String getNome() {
        return nome;
    }

    /** Retorna o cidade do hotel
     * @return cidade do hotel
     */
    public Cidade getLocal() {
        return local;
    }

    /** Retorna o número de quartos do hotel
     * @return número de quartos do hotel
     */
    public int getNumQuartos() {
        return numQuartos;
    }

    /** Retorna o preço da diária do hotel
     * @return preço da diária do hotel
     */
    public Dinheiro getPrecoDiaria() {
        return precoDiaria;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão de informação de um hotel.
     * @param nome nome do hotel
     * @param local cidade do hotel
     * @param numQuartos número de quartos do hotel
     * @param precoDiaria valor da diária do hotel
     */
    public InfoHotel(String nome, Cidade local, int numQuartos, Dinheiro precoDiaria) {
        this.id = (count++);
        this.nome = nome;
        this.local = local;
        this.numQuartos = numQuartos;
        this.precoDiaria = precoDiaria;
    }

}
