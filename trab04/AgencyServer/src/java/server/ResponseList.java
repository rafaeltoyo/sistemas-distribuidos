package server;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/** Classe wrapper de List para retorno em REST.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
@XmlRootElement
public class ResponseList {
    /** Lista de objetos */
    private List<Object> list;
    
    /*------------------------------------------------------------------------*/

    /** Retorna a lista de objetos.
     * @return lista de objetos
     */
    public List<Object> getList() {
        return list;
    }
    
    /*------------------------------------------------------------------------*/

    /** Coloca uma lista de objetos.
     * @param list lista de objetos
     */
    public void setList(List<Object> list) {
        this.list = list;
    }
    
}
