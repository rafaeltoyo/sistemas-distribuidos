package shared.model.hotel;

import shared.model.cidade.Cidade;
import shared.model.saldo.Dinheiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Classe que representa as informações de um hotel a serem enviadas como
 * resposta a um cliente.
 * Objetos dessa classe apenas são instanciados com o intuito de serem enviados
 * para os clientes.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class InfoHotelRet implements Serializable {
    /** Identificador numérico do hotel */
    private int id;

    /** Nome do hotel */
    private String nome;

    /** Cidade do hotel */
    private Cidade local;

    /** Número total de quartos do hotel */
    private int numQuartos;

    /** Número de quartos disponíveis no período informado */
    private int quartosDisponiveis;

    /** Data de entrada */
    private LocalDate dataEntrada;

    /** Número de diárias */
    private long numDiarias;

    /** Preço da diária */
    private Dinheiro precoDiaria;

    /** Preço total */
    private Dinheiro precoTotal;

    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do hotel.
     * @return identificador numérico do hotel
     */
    public int getId() {
        return id;
    }

    /** Retorna o nome do hotel.
     * @return nome do hotel
     */
    public String getNome() {
        return nome;
    }

    /** Retorna a cidade do hotel
     * @return cidade do hotel
     */
    public Cidade getLocal() {
        return local;
    }

    /** Retorna o número total de quartos do hotel.
     * @return número de quartos do hotel
     */
    public int getNumQuartos() {
        return numQuartos;
    }

    /** Retorna o número de quartos disponíveis do hotel para o período
     * requisitado pelo cliente.
     * @return número de quartos disponíveis
     */
    public int getQuartosDisponiveis() {
        return quartosDisponiveis;
    }

    /** Retorna a data de entrada do hotel para o período requisitado pelo
     * cliente.
     * @return data de entrada
     */
    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    /** Retorna o número de diárias calculado para o período requisitado pelo
     * cliente.
     * @return número de diárias
     */
    public long getNumDiarias() {
        return numDiarias;
    }

    /** Retorna o preço da diária do hotel
     * @return preço da diária do hotel
     */
    public Dinheiro getPrecoDiaria() {
        return precoDiaria;
    }

    /** Retorna o preço total para o número de diárias requisitado
     * @return preço total da estada
     */
    public Dinheiro getPrecoTotal() {
        return precoTotal;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão.
     * @param infoHotel informações do hotel
     * @param quartosDisponiveis número de quartos disponíveis no período
     * @param dataEntrada data de início do período
     * @param dataSaida data de fim do período
     */
    public InfoHotelRet(InfoHotel infoHotel, int quartosDisponiveis,
                        LocalDate dataEntrada, LocalDate dataSaida, int numQuartosAComprar) {
        this.id = infoHotel.getId();
        this.nome = infoHotel.getNome();
        this.local = infoHotel.getLocal();
        this.numQuartos = infoHotel.getNumQuartos();
        this.quartosDisponiveis = quartosDisponiveis;
        this.dataEntrada = dataEntrada;
        this.numDiarias = ChronoUnit.DAYS.between(dataEntrada, dataSaida);
        this.precoDiaria = infoHotel.getPrecoDiaria();
        this.precoTotal = Dinheiro.reais(this.precoDiaria.getQuantia().multiply(BigDecimal.valueOf(this.numDiarias * numQuartosAComprar)));
    }
}
