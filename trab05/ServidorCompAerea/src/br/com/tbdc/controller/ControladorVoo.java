package br.com.tbdc.controller;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.saldo.Reserva;
import br.com.tbdc.model.voo.InfoVoo;
import br.com.tbdc.model.voo.TipoPassagem;
import br.com.tbdc.model.voo.Voo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Esta classe é um singleton que controla todos os voos do servidor por meio
 * de métodos CRUD.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ControladorVoo {
    /** Instância do singleton */
    private static final ControladorVoo instance = new ControladorVoo();

    /** Lista de voos em memória */
    private List<Voo> voos;

    /*------------------------------------------------------------------------*/

    /** Construtor privado sem argumentos que apenas inicializa a lista de voos.
     */
    private ControladorVoo() {
        voos = new ArrayList<>();
    }

    /** Retorna a instância do singleton.
     * @return instância do singleton
     */
    public static synchronized ControladorVoo getInstance() {
        return instance;
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um voo à lista de voos do servidor.
     * @param voo voo já instanciado e inicializado
     */
    public void adicionarVoo(Voo voo) {
        voos.add(voo);
    }

    /*------------------------------------------------------------------------*/

    /** Retorna uma lista de passagens que atendem aos atributos fornecidos nos
     * parâmetros.
     * @param tipo somente ida ou ida e volta
     * @param origem local de origem do voo
     * @param destino local de destino do voo
     * @param dataIda data do voo de ida
     * @param dataVolta data do voo de volta, caso o tipo seja ida e volta
     * @param numPessoas número de passagens desejadas
     * @return lista de passagens aéreas disponíveis que atendem aos parâmetros
     */
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo, Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numPessoas) {
        // Armazenar o resultado da consulta
        ArrayList<InfoVoo> result = new ArrayList<>();

        // Para todos os voos armazenados no servidor ...
        for (Voo voo : voos) {
            // Adiciona voos de ida
            if (validarConsulta(voo, origem, destino, dataIda, numPessoas)) {
                result.add(voo.getInfoVoo());
            }
            // Adiciona voos de volta
            if (tipo == TipoPassagem.IDA_E_VOLTA) {
                if (validarConsulta(voo, destino, origem, dataVolta, numPessoas)) {
                    result.add(voo.getInfoVoo());
                }
            }
        }
        return result;
    }

    /** Confere se um voo possui atributos iguais aos parâmetros fornecidos.
     * @param voo voo que será testado
     * @param origem parâmetro de local de origem
     * @param destino parâmetro de local de destino
     * @param data parâmetro de data
     * @param numPessoas parâmetro de número de pessoas
     * @return true se e somente se o voo se enquadra a todos os parâmetros
     */
    private boolean validarConsulta(Voo voo, Cidade origem, Cidade destino, LocalDate data, int numPessoas) {
        return origem.equals(voo.getOrigem()) &&
                destino.equals(voo.getDestino()) &&
                data.equals(voo.getData()) &&
                numPessoas <= voo.getPoltronasDisp();
    }

    /*------------------------------------------------------------------------*/

    /** Tenta comprar passagens de um voo de ida e opcionalmente de um voo de
     * volta. Se o parâmetro tipo for IDA_E_VOLTA, primeiro tenta-se comprar as
     * passagens de ida e depois as passagens de volta. Caso seja efetuada a
     * reserva das passagens de ida, mas ocorra falha na reserva das passagens
     * de volta, a reserva da passagem de ida é revertida (não é efetuada a sua
     * compra).
     * @param tipo       SOMENTE_IDA ou IDA_E_VOLTA
     * @param idVooIda   identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida
     */
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) {
        if (tipo == TipoPassagem.IDA_E_VOLTA) {
            return comprarPassagensIdaEVolta(idVooIda, idVooVolta, numPessoas);
        }
        return comprarPassagensSomenteIda(idVooIda, numPessoas);
    }

    /** Tenta efetuar compra de passagem de ida.
     * Chamada pela função comprarPassagens.
     * @param idVoo      identificador do voo de ida
     * @param numPessoas número de passagens a adquirir
     * @return true se e somente se a compra for bem sucedida
     */
    private boolean comprarPassagensSomenteIda(int idVoo, int numPessoas) {
        Voo voo = null;

        // Busca o voo
        for (Voo v : voos) {
            if (v.getId() == idVoo) {
                voo = v;
                break;
            }
        }
        // Retorna false se não encontrou o voo
        // Retorna true se conseguir comprar o número desejado, e false caso contrário
        return voo != null && voo.reservar(numPessoas) != null;
    }

    /** Tenta efetuar compra de passagens de ida e volta.
     * Chamada pela função comprarPassagens.
     * @param idVooIda   identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida para ambos os voos
     */
    private boolean comprarPassagensIdaEVolta(int idVooIda, int idVooVolta, int numPessoas) {
        Voo vooIda = null;
        Voo vooVolta = null;

        // Busca os voos
        for (Voo v : voos) {
            if (v.getId() == idVooIda) {
                vooIda = v;
            } else if (v.getId() == idVooVolta) {
                vooVolta = v;
            }
            if (vooIda != null && vooVolta != null) {
                break;
            }
        }

        // Retorna false se não encontrou algum dos voos
        if (vooIda == null || vooVolta == null) {
            return false;
        }

        Reserva reservaVooIda = vooIda.reservar(numPessoas);
        if (reservaVooIda != null) {
            if (vooVolta.reservar(numPessoas) != null) {
                // Retorna true se conseguir comprar ida e volta
                return true;
            }
            // Caso não consiga comprar a volta,
            // faz rollback na compra do voo de ida e retorna false
            vooIda.estornar(reservaVooIda);
        }
        // Retorna false se não conseguir comprar o número desejado para a ida
        return false;
    }

    /*------------------------------------------------------------------------*/

}
