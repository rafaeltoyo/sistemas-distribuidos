package model;

import model.saldo.Reserva;

public class Pacote {

    private static int count = 0;

    private int id;

    private Passagem passagem;

    private Hospedagem hospedagem;

    public Pacote(Passagem passagem, Hospedagem hospedagem) {
        this(count++, passagem, hospedagem);
    }

    public Pacote(int id, Passagem passagem, Hospedagem hospedagem) {
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
        Reserva reservaPassagem = passagem.reservar(numPessoas);
        if (reservaPassagem == null) {
            return false;
        }
        boolean temHospedagem = hospedagem.reservar(numPessoas) != null;
        if (!temHospedagem) {
            passagem.estornar(reservaPassagem);
            return false;
        }
        return true;
    }
}
