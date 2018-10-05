package model.hotel;

import model.cidade.Cidade;
import model.saldo.Reserva;

import java.time.LocalDate;
import java.util.HashMap;

public class Hotel {
    private InfoHotel infoHotel;

    private HashMap<LocalDate, Hospedagem> hospedagens = new HashMap<>();

    /*------------------------------------------------------------------------*/

    public InfoHotel getInfoHotel() {
        return infoHotel;
    }

    /*------------------------------------------------------------------------*/

    public Hotel(String nome, Cidade local, int numQuartos) {
        infoHotel = new InfoHotel(nome, local, numQuartos);
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

    public Hospedagem getHospedagemData(LocalDate data) {
        return hospedagens.get(data);
    }

    /*------------------------------------------------------------------------*/

    public boolean reservar(LocalDate dataIni, LocalDate dataFim, int numQuartos) {
        // FIXME: será que tem que por synchronized em tudo isso?
        HashMap<Hospedagem, Reserva> arrayReservas = new HashMap<>();
        LocalDate data = LocalDate.of(dataIni.getYear(), dataIni.getMonth(), dataIni.getDayOfMonth());

        while (!data.isAfter(dataFim)) {
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

    private void desfazerReserva(HashMap<Hospedagem, Reserva> arrayReservas) {
        // FIXME: será que tem que por synchronized em tudo isso?
        for (HashMap.Entry<Hospedagem, Reserva> par : arrayReservas.entrySet()) {
            Hospedagem hosp = par.getKey();
            Reserva r = par.getValue();

            hosp.estornar(r);
        }
    }
}
