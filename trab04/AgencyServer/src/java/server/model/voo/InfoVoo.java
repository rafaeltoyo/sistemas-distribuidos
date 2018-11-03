package server.model.voo;

import java.io.Serializable;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import server.model.cidade.Cidade;
import server.model.saldo.Dinheiro;
import server.webservice.DinheiroAdapter;
import server.webservice.LocalDateAdapter;

/** Esta classe é uma estrutura de armazenamento de informações de um voo.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
@XmlRootElement
public class InfoVoo implements Serializable {
    /** Contagem de voos para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador do voo */
    @XmlAttribute
    private int id;

    /** Local de origem (partida) */
    @XmlElement
    private Cidade origem;

    /** Local de destino (chegada) */
    @XmlElement
    private Cidade destino;

    /** Data do voo */
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate data;

    /** Preço da passagem */
    @XmlElement
    @XmlJavaTypeAdapter(value = DinheiroAdapter.class)
    private Dinheiro precoPassagem;

    /** Poltronas disponíveis */
    public int poltronasDisp;

    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do voo
     * @return identificador do voo
     */
    public int getId() {
        return id;
    }

    /** Retorna a cidade de origem do voo
     * @return cidade de origem do voo
     */
    public Cidade getOrigem() {
        return origem;
    }

    /** Retorna a cidade de destino do voo
     * @return cidade de destino do voo
     */
    public Cidade getDestino() {
        return destino;
    }

    /** Retorna a data do voo
     * @return data do voo
     */
    public LocalDate getData() {
        return data;
    }

    /** Retorna o preço da passagem
     * @return preço da passagem
     */
    public Dinheiro getPrecoPassagem() {
        return precoPassagem;
    }

    /*------------------------------------------------------------------------*/
    
    public InfoVoo() {
        this.id = (count++);
        this.origem = null;
        this.destino = null;
        this.data = null;
        this.precoPassagem = null;
        this.poltronasDisp = 0;
    }
    
    /*------------------------------------------------------------------------*/

    /** Construtor para um novo voo.
     * Obtém um identificador com auto-incremento.
     * Considera que todas as poltronas da aeronave estão disponíveis para
     * compra.
     * @param origem local de origem (partida)
     * @param destino local de destino (chegada)
     * @param data data de partida
     * @param poltronasDisp número de poltronas disponíveis
     * @param precoPassagem valor da passagem
     */
    public InfoVoo(Cidade origem, Cidade destino, LocalDate data,
                   int poltronasDisp, Dinheiro precoPassagem) {
        this.id = (count++);
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.precoPassagem = precoPassagem;
        this.poltronasDisp = poltronasDisp;
    }
    
}
