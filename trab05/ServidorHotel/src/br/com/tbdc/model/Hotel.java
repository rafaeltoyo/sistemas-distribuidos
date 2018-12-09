package br.com.tbdc.model;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.hotel.InfoHotel;
import br.com.tbdc.model.saldo.Dinheiro;
import br.com.tbdc.model.saldo.Reserva;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

/** Representa um hotel.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Hotel implements Serializable {
    /** Informações do hotel */
    private InfoHotel infoHotel;

    /** Mapeamento entre datas e hospedagens */
    private HashMap<LocalDate, Hospedagem> hospedagens = new HashMap<>();

    /*------------------------------------------------------------------------*/

    /** Retorna as informações do hotel.
     * @return informações do hotel
     */
    public InfoHotel getInfoHotel() {
        return infoHotel;
    }

    /** Retorna o identificador do hotel.
     * @return identificador do hotel
     */
    public int getId() {
        return infoHotel.getId();
    }

    /** Retorna o nome do hotel.
     * @return nome do hotel
     */
    public String getNome() {
        return infoHotel.getNome();
    }

    /** Retorna a cidade do hotel.
     * @return cidade do hotel
     */
    public Cidade getLocal() {
        return infoHotel.getLocal();
    }

    /** Retorna o preço da diária do hotel.
     * @return preço da diária
     */
    public Dinheiro getPrecoDiaria() {
        return infoHotel.getPrecoDiaria();
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão de um hotel.
     * @param nome nome do hotel
     * @param local cidade do hotel
     * @param numQuartos número total de quartos do hotel
     * @param precoDiaria valor da diária
     */
    public Hotel(String nome, Cidade local, int numQuartos, Dinheiro precoDiaria) {
        infoHotel = new InfoHotel(nome, local, numQuartos, precoDiaria);
    }

    /*------------------------------------------------------------------------*/

    /** Instancia hospedagens entre um período especificado por dataIni e
     * dataFim (inclusive)
     * @param dataIni data de início do período
     * @param dataFim data de fim do período (também é incluída no intervalo)
     */
    public void adicionarHospedagem(LocalDate dataIni, LocalDate dataFim) {
        LocalDate data = LocalDate.of(dataIni.getYear(), dataIni.getMonth(), dataIni.getDayOfMonth());

        while (!data.isAfter(dataFim)) {
            adicionarHospedagem(data);
            data = data.plusDays(1);
        }
    }

    /** Instancia a hospedagem em uma data específica
     * @param data data a adicionar na lista de hospedagens
     */
    private void adicionarHospedagem(LocalDate data) {
        if (!hospedagens.containsKey(data)) {
            hospedagens.put(data, new Hospedagem(data, infoHotel.getNumQuartos()));
        }
    }

    /*------------------------------------------------------------------------*/

    /** Retorna o objeto Hospedagem associado a uma data específica.
     * @param data data da hospedagem
     * @return o objeto Hospedagem, ou null, se não existir.
     */
    public Hospedagem getHospedagemData(LocalDate data) {
        return hospedagens.get(data);
    }

    /*------------------------------------------------------------------------*/

    /** Faz uma reserva de quartos no hotel durante um período de dias entre
     * dataIni e dataFim (exclusive).
     * @param dataIni primeiro dia da reserva
     * @param dataFim dia de saída (não é incluído no período de reserva)
     * @param numQuartos número de quartos desejados
     * @return true se e somente se a reserva foi bem sucedida
     */
    public boolean reservar(LocalDate dataIni, LocalDate dataFim, int numQuartos) {
        synchronized (hospedagens) {
            HashMap<Hospedagem, Reserva> arrayReservas = new HashMap<>();
            LocalDate data = LocalDate.of(dataIni.getYear(), dataIni.getMonth(), dataIni.getDayOfMonth());

            while (data.isBefore(dataFim)) {
                // Pega a hospedagem do dia
                Hospedagem hosp = hospedagens.get(data);
                if (hosp == null) {
                    // Se não achar, faz rollback de tudo
                    desfazerReserva(arrayReservas);
                    return false;
                }

                // Faz uma reserva na hospedagem
                Reserva r = hosp.reservar(numQuartos);
                if (r == null) {
                    // Se falhar (não tem mais vagas) faz rollback de tudo
                    desfazerReserva(arrayReservas);
                    return false;
                }

                arrayReservas.put(hosp, r);

                data = data.plusDays(1);
            }

            return true;
        }
    }

    /** Função interna para fazer "rollback" de uma reserva de quartos, caso
     * ocorra algum problema (falta de quartos, ou inexistência de um objeto
     * Hospedagem para alguma data)
     * @param arrayReservas mapeamento entre hospedagem e reserva, criado pela
     *                      função reservar
     */
    private void desfazerReserva(HashMap<Hospedagem, Reserva> arrayReservas) {
        for (HashMap.Entry<Hospedagem, Reserva> par : arrayReservas.entrySet()) {
            Hospedagem hosp = par.getKey();
            Reserva r = par.getValue();

            hosp.estornar(r);
        }
    }
}
