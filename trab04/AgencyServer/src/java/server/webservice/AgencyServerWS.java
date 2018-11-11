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
import server.webservice.compra.CompraHospedagem;
import server.webservice.compra.CompraPacote;
import server.webservice.compra.CompraPassagem;

/**
 * REST Web Service
 *
 * @author Victor
 */
@Path("server")
public class AgencyServerWS {
    /** Atributo autogerado. */
    @Context
    private UriInfo context;

    /** Método autogerado.
     * Retrieves representation of an instance of server.AgencyServerWS
     * @return an instance of java.lang.String
     */
    @GET
    //@Path("/get")
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }

    /** Método autogerado.
     * PUT method for updating or creating an instance of AgencyServerWS
     * @param content representation for the resource
     */
    @PUT
    //@Path("/put")
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    /*------------------------------------------------------------------------*/
    
    /** Instância da classe servidor */
    private static AgencyServerImpl agencyServerImpl = new AgencyServerImpl();
    
    /** Parser de string para números decimais */
    private static DecimalFormat df = new DecimalFormat();
    
    /*------------------------------------------------------------------------*/
    
    /** Cria uma nova instância de AgencyServerWS */
    public AgencyServerWS() {
    }
    
    /*------------------------------------------------------------------------*/
    
    /** Retorna uma lista de passagens que atendem aos atributos fornecidos
     * nos parâmetros.
     * @param tipo somente ida ou ida e volta
     * @param origem local de origem do voo
     * @param destino local de destino do voo
     * @param dataIda data do voo de ida
     * @param dataVolta data do voo de volta, caso o tipo seja ida e volta
     * @param numPessoas número de passagens desejadas
     * @return lista de passagens aéreas disponíveis que atendem aos parâmetros
     */
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
            //e.printStackTrace();
        }
        return null;
    }
    
    /*------------------------------------------------------------------------*/
    
    /** Realiza a compra de passagens.
     * O formato do XML esperado é o seguinte:
     * <pre>
     * {@code 
     * <?xml version="1.0" encoding="UTF-8"?> 
     *     <compraPassagem> 
     *         <tipo>IDA_E_VOLTA</tipo> 
     *         <idVooIda>4</idVooIda> 
     *         <idVooVolta>9</idVooVolta> 
     *         <numPessoas>1</numPessoas> 
     *     </compraPassagem> 
     * }
     * </pre>
     * @param cp objeto que contém os parâmetros de compra
     * @return "true" se houve sucesso, "false" caso contrário
     */
    @PUT
    @Path("/comprar_passagens")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String comprarPassagens(CompraPassagem cp) {
        try {
            boolean success = agencyServerImpl.comprarPassagens(cp.getTipo(), cp.getIdVooIda(), cp.getIdVooVolta(), cp.getNumPessoas());
            
            if (success) {
                return Boolean.TRUE.toString();
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        return Boolean.FALSE.toString();
    }
    
    /*------------------------------------------------------------------------*/
    
    /** Retorna as informações de hotéis que atendem aos parâmetros fornecidos.
     * @param local cidade do hotel
     * @param dataIni data de chegada (primeira diária)
     * @param dataFim data de saída (não é inclusa no resultado)
     * @param numQuartos número de quartos desejados
     * @param numPessoas número de pessoas (total, não por quarto)
     * @return mapa com informações de hotel e hospedagem
     */
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
            //e.printStackTrace();
        }
        return null;
    }
    
    /*------------------------------------------------------------------------*/
    
    /** Realiza a compra de hospedagem.
     * O formato do XML esperado é o seguinte:
     * <pre>
     * {@code 
     * <?xml version="1.0" encoding="UTF-8"?> 
     *     <compraHospedagem> 
     *         <idHotel>1</idHotel> 
     *         <dataIni>2018-10-18</dataIni> 
     *         <dataFim>2018-10-20</dataFim> 
     *         <numQuartos>1</numQuartos> 
     *     </compraHospedagem> 
     * }
     * </pre>
     * @param ch objeto que contém os parâmetros de compra
     * @return "true" se houve sucesso, "false" caso contrário
     */
    @PUT
    @Path("/comprar_hospedagens")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String comprarHospedagem(CompraHospedagem ch) {
        
        try {
            boolean success = agencyServerImpl.comprarHospedagem(ch.getIdHotel(), ch.getDataIni(), ch.getDataFim(), ch.getNumQuartos());
            
            if (success) {
                return Boolean.TRUE.toString();
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        return Boolean.FALSE.toString();
    }
    
    /*------------------------------------------------------------------------*/

    /** Faz uma consulta de voos e hotéis para os dados fornecidos, e retorna os
     * resultados.
     * No servidor, não existem pacotes explícitos, apenas voos e hotéis.
     * @param origem local de origem do voo
     * @param destino local de destino do voo e cidade do hotel
     * @param dataIda data do voo de ida e de chegada no hotel
     * @param dataVolta data do voo de volta e de saída do hotel (não é incluída
     *                  reserva de hotel para a data de saída)
     * @param numQuartos número de quartos de hotel desejados
     * @param numPessoas número de passagens desejadas
     * @return conjunto de voos de ida, voos de volta e hospedagens que atendem
     * os dados fornecidos
     */
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
            //e.printStackTrace();
        }
        return null;
    }
    
    /*------------------------------------------------------------------------*/
    
    /** Realiza a compra de um pacote.
     * O formato do XML esperado é o seguinte:
     * <pre>
     * {@code 
     * <?xml version="1.0" encoding="UTF-8"?> 
     *     <compraPassagem> 
     *         <idVooIda>4</idVooIda> 
     *         <idVooVolta>9</idVooVolta> 
     *         <idHotel>1</idHotel> 
     *         <dataIda>2018-10-18</dataIda> 
     *         <dataVolta>2018-10-20</dataVolta> 
     *         <numQuartos>1</numQuartos> 
     *         <numPessoas>1</numPessoas> 
     *     </compraPassagem> 
     * }
     * </pre>
     * @param cp objeto que contém os parâmetros de compra
     * @return "true" se houve sucesso, "false" caso contrário
     */
    @PUT
    @Path("/comprar_pacote")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String comprarPacote(CompraPacote cp) {
        
        try {
            boolean success = agencyServerImpl.comprarPacote(cp.getIdVooIda(), cp.getIdVooVolta(), cp.getIdHotel(), cp.getDataIda(), cp.getDataVolta(), cp.getNumQuartos(), cp.getNumPessoas());
            
            if (success) {
                return Boolean.TRUE.toString();
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        return Boolean.FALSE.toString();
    }
}
