package br.com.tbdc.rmi;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.hotel.InfoHotelRet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * =====================================================================================================================
 * Interface RMI para o servidor de hospedagens.
 * =====================================================================================================================
 *
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface InterfaceHospedagens extends Remote {

    /**
     * =================================================================================================================
     * Retorna um mapa cujas chaves são os hotéis e os valores são listas de hospedagens (data + número de quartos
     * disponíveis), com base nos parâmetros fornecidos.
     * =================================================================================================================
     *
     * @param local      cidade do hotel
     * @param dataIni    data de chegada (primeira diária)
     * @param dataFim    data de saída (não é inclusa no resultado)
     * @param numQuartos número de quartos desejados
     * @param numPessoas número de pessoas (total, não por quarto)
     * @return mapa com informações de hotel e hospedagem
     * @throws RemoteException caso ocorra erro no RMI
     */
    ArrayList<InfoHotelRet> consultarHospedagens(Cidade local,
                                                 LocalDate dataIni,
                                                 LocalDate dataFim,
                                                 int numQuartos,
                                                 int numPessoas) throws RemoteException;

    /**
     * =================================================================================================================
     * Tenta comprar hospedagem em um hotel para todas as noites entre as datas informadas.
     * =================================================================================================================
     *
     * @param idHotel    identificador do hotel
     * @param dataIni    data de entrada
     * @param dataFim    data de saída (não é incluída na reserva, ou seja, é feita reserva somente até o dia anterior
     *                   à data de saída)
     * @param numQuartos número de quartos desejados
     * @return true se e somente se a compra for bem sucedida
     * @throws RemoteException caso ocorra erro no RMI
     */
    boolean comprarHospedagem(int idHotel,
                              LocalDate dataIni,
                              LocalDate dataFim,
                              int numQuartos) throws RemoteException;

    boolean comprarPacote(int idHotel,
                          LocalDate dataIni,
                          LocalDate dataFim,
                          int numQuartos,
                          InterfaceTransacao coordenador) throws RemoteException;

    boolean efetivarPacote(InterfaceTransacao coordenador) throws RemoteException;

    boolean abortarPacote(InterfaceTransacao coordenador) throws RemoteException;

}
