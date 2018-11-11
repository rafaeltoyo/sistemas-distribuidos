/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.webservice;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import server.model.saldo.Dinheiro;

/**
 *
 * @author Victor
 */
public class DinheiroAdapter extends XmlAdapter<String, Dinheiro> {
    
    /** Parser de string para números decimais */
    private static DecimalFormat df = new DecimalFormat();

    @Override
    public Dinheiro unmarshal(String v) throws Exception {
        df.setParseBigDecimal(true);
        return Dinheiro.reais((BigDecimal) df.parse(v));
    }

    @Override
    public String marshal(Dinheiro v) throws Exception {
        return v.toString();
    }
    
}
