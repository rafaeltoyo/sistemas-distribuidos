package model;

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

    public int getId() {
        return id;
    }
}
