package model;

import model.hotel.InfoHotel;
import model.voo.Passagem;

public class Pacote {

    private static int count = 0;

    private int id;

    private Passagem passagem;

    private InfoHotel hospedagem;

    public Pacote(Passagem passagem, InfoHotel hospedagem) {
        this(count++, passagem, hospedagem);
    }

    public Pacote(int id, Passagem passagem, InfoHotel hospedagem) {
        this.id = id;
        this.passagem = passagem;
        this.hospedagem = hospedagem;
    }

    /**
     *
     * @return Id do Pacote
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param numPessoas
     * @return Sucesso da compra
     */
    public boolean comprar(int numPessoas) {
        /* TODO
        Reserva reservaPassagem = passagem.reservar(numPessoas);
        if (reservaPassagem == null) {
            return false;
        }
        boolean temHospedagem = hospedagem.reservar(numPessoas) != null;
        if (!temHospedagem) {
            passagem.estornar(reservaPassagem);
            return false;
        }*/
        return true;
    }
}
