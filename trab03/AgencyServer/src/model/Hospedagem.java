package model;

import model.saldo.ObjComSaldo;
import model.saldo.Saldo;

import java.util.Date;

public class Hospedagem extends ObjComSaldo {

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
        super(numPessoas);
        this.id = id;
        this.destino = destino;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.numQuartos = numQuartos;
    }

    public int getId() {
        return id;
    }
}
