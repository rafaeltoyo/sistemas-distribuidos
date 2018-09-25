package model;

import model.saldo.ObjComSaldo;
import model.saldo.Reserva;
import model.saldo.Saldo;

import java.util.Date;

public class Passagem extends ObjComSaldo {

    private static int count = 0;

    private int id;

    private TipoPassagem tipo;

    private int origem;

    private int destino;

    private Date dataIda;

    private Date dataVolta;

    public Passagem(TipoPassagem tipo, int origem, int destino, Date dataIda, Date dataVolta, int numPessoas) {
        this(count++, tipo, origem, destino, dataIda, dataVolta, numPessoas);
    }

    public Passagem(int id, TipoPassagem tipo, int origem, int destino, Date dataIda, Date dataVolta, int numPessoas) {
        super(numPessoas);
        this.id = id;
        this.tipo = tipo;
        this.origem = origem;
        this.destino = destino;
        this.dataIda = dataIda;
        this.dataVolta = dataVolta;

    }

    public int getId() {
        return id;
    }
}
