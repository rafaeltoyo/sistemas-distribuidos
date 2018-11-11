package server.model.hotel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import server.model.cidade.Cidade;
import server.model.saldo.Dinheiro;
import server.webservice.xmladapters.DinheiroAdapter;
import server.webservice.xmladapters.LocalDateAdapter;

/** Classe que representa as informações de um hotel a serem enviadas como
 * resposta a um cliente.
 * Objetos dessa classe apenas são instanciados com o intuito de serem enviados
 * para os clientes.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
@XmlRootElement
public class InfoHotelRet implements Serializable {
    /** Identificador numérico do hotel */
    @XmlAttribute
    private int id;

    /** Nome do hotel */
    @XmlElement
    private String nome;

    /** Cidade do hotel */
    @XmlElement
    private Cidade local;

    /** Número total de quartos do hotel */
    @XmlElement
    private int numQuartos;

    /** Número de quartos disponíveis no período informado */
    @XmlElement
    private int quartosDisponiveis;

    /** Data de entrada */
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataEntrada;

    /** Número de diárias */
    @XmlElement
    private long numDiarias;

    /** Preço da diária */
    @XmlElement
    @XmlJavaTypeAdapter(value = DinheiroAdapter.class)
    private Dinheiro precoDiaria;

    /** Preço total */
    @XmlElement
    @XmlJavaTypeAdapter(value = DinheiroAdapter.class)
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

    /** Construtor sem argumentos para permitir que o objeto seja retornado
     * pelos métodos do web service como XML.
     * Inicializa todos os atributos com valores inválidos.
     */
    public InfoHotelRet() {
        this.id = -1;
        this.nome = null;
        this.local = null;
        this.numQuartos = -1;
        this.quartosDisponiveis = -1;
        this.dataEntrada = null;
        this.numDiarias = -1;
        this.precoDiaria = null;
        this.precoTotal = null;
    }

    /** Construtor padrão.
     * @param infoHotel informações do hotel
     * @param quartosDisponiveis número de quartos disponíveis no período
     * @param dataEntrada data de início do período
     * @param dataSaida data de fim do período
     * @param numQuartosAComprar número de quartos que o cliente deseja comprar
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
