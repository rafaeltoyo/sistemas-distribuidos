/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.webservice;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import server.AgencyServerImpl;
import server.model.cidade.Cidade;
import server.model.hotel.InfoHotelRet;
import server.model.pacote.ConjuntoPacote;
import server.model.voo.InfoVoo;
import server.model.voo.TipoPassagem;

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
    @Produces(MediaType.APPLICATION_XML)
    public Response consultarPassagens(
            @QueryParam("tipo_passagem") TipoPassagem tipo,
            @QueryParam("origem") Cidade origem,
            @QueryParam("destino") Cidade destino,
            @QueryParam("data_ida") String dataIdaStr,
            @QueryParam("data_volta") String dataVoltaStr,
            @QueryParam("num_pessoas") int numPessoas) {
        
        try {
            LocalDate dataIda = LocalDate.parse(dataIdaStr);
            LocalDate dataVolta;
            if (tipo == TipoPassagem.IDA_E_VOLTA) {
                dataVolta = LocalDate.parse(dataVoltaStr);
            }
            else {
                dataVolta = null;
            }
            
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
    
    // FIXME: Alterar para PUT
    @GET
    @Path("/comprar_passagens")
    @Produces(MediaType.TEXT_PLAIN)
    public String comprarPassagens(
            @QueryParam("tipo_passagem") TipoPassagem tipo,
            @QueryParam("id_voo_ida") int idVooIda,
            @QueryParam("id_voo_volta") int idVooVolta,
            @QueryParam("num_pessoas") int numPessoas) {
        
        try {
            boolean success = agencyServerImpl.comprarPassagens(tipo, idVooIda, idVooVolta, numPessoas);
            
            if (success) {
                return Boolean.TRUE.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE.toString();
    }
    
    /*------------------------------------------------------------------------*/
    
    @GET
    @Path("/consultar_hospedagens")
    @Produces(MediaType.APPLICATION_XML)
    public Response consultarHospedagens(
            @QueryParam("local") Cidade local,
            @QueryParam("data_ini") String dataIniStr,
            @QueryParam("data_fim") String dataFimStr,
            @QueryParam("num_quartos") int numQuartos,
            @QueryParam("num_pessoas") int numPessoas) {
        
        try {
            LocalDate dataIni = LocalDate.parse(dataIniStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);
            
            List<InfoHotelRet> list = agencyServerImpl.consultarHospedagens(local, dataIni, dataFim, numQuartos, numPessoas);
            GenericEntity<List<InfoHotelRet>> entity = new GenericEntity<List<InfoHotelRet>>(list) {};
            
            return Response.ok(entity).build();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /*------------------------------------------------------------------------*/
    
    // FIXME: Alterar para PUT
    @GET
    @Path("/comprar_hospedagens")
    @Produces(MediaType.TEXT_PLAIN)
    public String comprarHospedagem(
            @QueryParam("id_hotel") int idHotel,
            @QueryParam("data_ini") String dataIniStr,
            @QueryParam("data_fim") String dataFimStr,
            @QueryParam("num_quartos") int numQuartos) {
        
        try {
            LocalDate dataIni = LocalDate.parse(dataIniStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);
            
            boolean success = agencyServerImpl.comprarHospedagem(idHotel, dataIni, dataFim, numQuartos);
            
            if (success) {
                return Boolean.TRUE.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE.toString();
    }
    
    /*------------------------------------------------------------------------*/
    
    @GET
    @Path("/consultar_pacotes")
    @Produces(MediaType.APPLICATION_XML)
    public Response consultarPacotes(
            @QueryParam("origem") Cidade origem,
            @QueryParam("destino") Cidade destino,
            @QueryParam("data_ida") String dataIdaStr,
            @QueryParam("data_volta") String dataVoltaStr,
            @QueryParam("num_quartos") int numQuartos,
            @QueryParam("num_pessoas") int numPessoas) {
        
        try {
            LocalDate dataIda = LocalDate.parse(dataIdaStr);
            LocalDate dataVolta = LocalDate.parse(dataVoltaStr);
            
            ConjuntoPacote list = agencyServerImpl.consultarPacotes(origem, destino, dataIda, dataVolta, numQuartos, numPessoas);
            GenericEntity<ConjuntoPacote> entity = new GenericEntity<ConjuntoPacote>(list) {};
            
            return Response.ok(entity).build();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /*------------------------------------------------------------------------*/
    
    // FIXME: Alterar para PUT
    @GET
    @Path("/comprar_pacote")
    @Produces(MediaType.TEXT_PLAIN)
    public String comprarPacote(
            @QueryParam("id_voo_ida") int idVooIda,
            @QueryParam("id_voo_volta") int idVooVolta,
            @QueryParam("id_hotel") int idHotel,
            @QueryParam("data_ida") String dataIdaStr,
            @QueryParam("data_volta") String dataVoltaStr,
            @QueryParam("num_quartos") int numQuartos,
            @QueryParam("num_pessoas") int numPessoas) {
        
        try {
            LocalDate dataIda = LocalDate.parse(dataIdaStr);
            LocalDate dataVolta = LocalDate.parse(dataVoltaStr);
            
            boolean success = agencyServerImpl.comprarPacote(idVooIda, idVooVolta, idHotel, dataIda, dataVolta, numQuartos, numPessoas);
            
            if (success) {
                return Boolean.TRUE.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE.toString();
    }
}
