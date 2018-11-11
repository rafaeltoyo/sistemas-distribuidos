package server.webservice.xmladapters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import server.model.saldo.Dinheiro;

/** Adaptador para a classe Dinheiro para utilização em XML.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class DinheiroAdapter extends XmlAdapter<String, Dinheiro> {
    /** Parser de string para números decimais */
    private static DecimalFormat df = new DecimalFormat();
    
    /*------------------------------------------------------------------------*/

    /** Converte uma string para um objeto Dinheiro.
     * @param v string
     * @return Dinheiro
     * @throws Exception caso não seja possível fazer parse da string para
     * Dinheiro
     */
    @Override
    public Dinheiro unmarshal(String v) throws Exception {
        df.setParseBigDecimal(true);
        return Dinheiro.reais((BigDecimal) df.parse(v));
    }

    /** Converte um objeto Dinheiro para uma string.
     * @param v Dinheiro
     * @return string
     * @throws Exception caso haja algum erro na conversão para string
     */
    @Override
    public String marshal(Dinheiro v) throws Exception {
        return v.toString();
    }
}
