package model;

import model.saldo.Reserva;
import model.saldo.Saldo;

import java.util.Date;

public class Passagem {

    private static int count = 0;

    private int id;

    private TipoPassagem tipo;

    private int origem;

    private int destino;

    private Date dataIda;

    private Date dataVolta;

    Saldo numPessoas;

    public Passagem(TipoPassagem tipo, int origem, int destino, Date dataIda, Date dataVolta, int numPessoas) {
        this(count++, tipo, origem, destino, dataIda, dataVolta, numPessoas);
    }

    public Passagem(int id, TipoPassagem tipo, int origem, int destino, Date dataIda, Date dataVolta, int numPessoas) {
        this.id = id;
        this.tipo = tipo;
        this.origem = origem;
        this.destino = destino;
        this.dataIda = dataIda;
        this.dataVolta = dataVolta;
        this.numPessoas = new Saldo(numPessoas);
    }

    public int getId() {
        return id;
    }

    public boolean reservar(int numPessoas) {
        synchronized (this.numPessoas) {
            return this.numPessoas.pegarSaldo(numPessoas) != null;
        }
    }
}
