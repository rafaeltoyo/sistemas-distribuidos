package server.webservice.xmladapters;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/** Adaptador para a classe LocalDate para utilização em XML.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    
    /** Converte uma string para um objeto LocalDate.
     * @param v string
     * @return LocalDate
     * @throws Exception caso não seja possível fazer parse da string para
     * LocalDate
     */
    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    /** Converte um objeto LocalDate para uma string.
     * @param v LocalDate
     * @return string
     * @throws Exception caso haja algum erro na conversão para string
     */
    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
    
}
