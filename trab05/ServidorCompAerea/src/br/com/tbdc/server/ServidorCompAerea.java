package br.com.tbdc.server;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.saldo.Reserva;
import br.com.tbdc.model.voo.InfoVoo;
import br.com.tbdc.model.voo.TipoPassagem;
import br.com.tbdc.model.voo.Voo;
import br.com.tbdc.rmi.InterfacePassagens;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ServidorCompAerea implements InterfacePassagens {

    private ArrayList<Voo> voos = new ArrayList<>();

    /**
     * Adiciona um voo à lista de voos do servidor, busca registros de
     * interesse que são atendidos por esse voo e envia notificação aos
     * respectivos clientes.
     *
     * @param voo voo já instanciado e inicializado
     */
    public void adicionarVoo(Voo voo) {
        voos.add(voo);
    }

    /*------------------------------------------------------------------------*/

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo, Cidade origem, Cidade destino, LocalDate dataIda,
                                                 LocalDate dataVolta, int numPessoas) throws RemoteException {
        // Checagem de parâmetros
        if (tipo == null)
            throw new RemoteException("Tipo da passagem não pode ser nulo.");
        if (origem == null)
            throw new RemoteException("Cidade de origem não pode ser nula.");
        if (destino == null)
            throw new RemoteException("Cidade de destino não pode ser nula.");
        if (dataIda == null)
            throw new RemoteException("Data de ida não pode ser nula.");
        if (dataVolta == null && tipo == TipoPassagem.IDA_E_VOLTA)
            throw new RemoteException("Data de volta não pode ser nula.");
        if (numPessoas <= 0)
            throw new RemoteException("É esperado número de pessoas maior ou igual a 1.");

        // Armazenar o resultado da consulta
        ArrayList<InfoVoo> result = new ArrayList<>();

        // Para todos os voos armazenados no servidor ...
        for (Voo voo : voos) {
            // Adiciona voos de ida
            if (validarConsulta(voo, tipo, origem, destino, dataIda, numPessoas)) {
                result.add(voo.getInfoVoo());
            }
            // Adiciona voos de volta
            if (tipo == TipoPassagem.IDA_E_VOLTA) {
                if (validarConsulta(voo, tipo, destino, origem, dataVolta, numPessoas)) {
                    result.add(voo.getInfoVoo());
                }
            }
        }
        return result;
    }

    private boolean validarConsulta(Voo voo, TipoPassagem tipo, Cidade origem, Cidade destino, LocalDate data, int numPessoas) {
        return origem.equals(voo.getOrigem()) &&
                destino.equals(voo.getDestino()) &&
                data.equals(voo.getData()) &&
                numPessoas <= voo.getPoltronasDisp();
    }

    /*------------------------------------------------------------------------*/

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) throws RemoteException {
        if (tipo == TipoPassagem.IDA_E_VOLTA) {
            return comprarPassagensIdaEVolta(idVooIda, idVooVolta, numPessoas);
        }
        return comprarPassagensSomenteIda(idVooIda, numPessoas);
    }

    /**
     * Tenta efetuar compra de passagem de ida.
     * Chamada pela função comprarPassagens.
     *
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

    /**
     * Tenta efetuar compra de passagens de ida e volta.
     * Chamada pela função comprarPassagens.
     *
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
}
