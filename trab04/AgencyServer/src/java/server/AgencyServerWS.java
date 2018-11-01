/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import server.model.hotel.Hotel;
import server.model.voo.Voo;
import shared.model.cidade.Cidade;
import shared.model.saldo.Dinheiro;
import shared.model.voo.InfoVoo;
import shared.model.voo.TipoPassagem;

/**
 * REST Web Service
 *
 * @author Victor
 */
@Path("server")
public class AgencyServerWS {
    
    /*========================================================================*/
    /*========================================================================*/
    // Coisas autogeradas

    @Context
    private UriInfo context;

    /**
     * Retrieves representation of an instance of server.AgencyServerWS
     * @return an instance of java.lang.String
     */
    @GET
    //@Path("/get")
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        return "{}";
        //throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of AgencyServerWS
     * @param content representation for the resource
     */
    @PUT
    //@Path("/put")
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    /*========================================================================*/
    /*========================================================================*/
    
    /** Instância da classe servidor */
    private static AgencyServerImpl agencyServerImpl = new AgencyServerImpl();
    
    /** Parser de string para números decimais */
    private static DecimalFormat df = new DecimalFormat();
    
    /*------------------------------------------------------------------------*/
    
    /** Cria uma nova instância de AgencyServerWS */
    public AgencyServerWS() {
    }
    
    /*------------------------------------------------------------------------*/
    
    @GET
    @Path("/consultar_passagens")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarPassagens(
            //@QueryParam("tipo_passagem") TipoPassagem tipo,
            //@QueryParam("origem") Cidade origem,
            //@QueryParam("destino") Cidade destino,
            //@QueryParam("data_ida") String dataIdaStr,
            //@QueryParam("data_volta") String dataVoltaStr,
            @QueryParam("num_pessoas") int numPessoas) {
        
        try {
            TipoPassagem tipo = TipoPassagem.IDA_E_VOLTA;
            Cidade origem = Cidade.CURITIBA;
            Cidade destino = Cidade.SAO_PAULO;
            String dataIdaStr = "2018-10-16";
            String dataVoltaStr = "2018-10-18";

            LocalDate dataIda = LocalDate.parse(dataIdaStr);
            LocalDate dataVolta = LocalDate.parse(dataVoltaStr);

            /*
            List<Object> list = (List) agencyServerImpl.consultarPassagens(tipo, origem, destino, dataIda, dataVolta, numPessoas);

            ResponseList ret = new ResponseList();
            ret.setList(list);
            
            return ret;
            */
            
            
            List<InfoVoo> list = agencyServerImpl.consultarPassagens(tipo, origem, destino, dataIda, dataVolta, numPessoas);
            GenericEntity<List<InfoVoo>> entity = new GenericEntity<List<InfoVoo>>(list) {};
            
            return Response.ok(entity).build();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /*------------------------------------------------------------------------*/
    
}
