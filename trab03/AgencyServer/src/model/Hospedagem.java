package model;

import model.saldo.Saldo;

import java.util.Date;

public class Hospedagem {

    private static int count = 0;

    private int id;

    private int destino;

    private Date dataEntrada;

    private Date dataSaida;

    private int numQuartos;

    Saldo numPessoas;

    public Hospedagem(int destino, Date dataEntrada, Date dataSaida, int numQuartos, int numPessoas) {
        this(count++, destino, dataEntrada, dataSaida, numQuartos, numPessoas);
    }

    public Hospedagem(int id, int destino, Date dataEntrada, Date dataSaida, int numQuartos, int numPessoas) {
        this.id = id;
        this.destino = destino;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.numQuartos = numQuartos;
        this.numPessoas = new Saldo(numPessoas);
    }

    public int getId() {
        return id;
    }
}
