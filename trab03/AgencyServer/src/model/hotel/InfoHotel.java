package model.hotel;

import model.cidade.Cidade;

import java.io.Serializable;

public class InfoHotel implements Serializable {
    /** Contagem de hotéis para o auto-incremento do identificador */
    private static int count = 0;

    private int id;

    private String nome;

    private Cidade local;

    private int numQuartos;

    /*------------------------------------------------------------------------*/

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Cidade getLocal() {
        return local;
    }

    public int getNumQuartos() {
        return numQuartos;
    }

    /*------------------------------------------------------------------------*/

    public InfoHotel(String nome, Cidade local, int numQuartos) {
        this.id = (count++);
        this.nome = nome;
        this.local = local;
        this.numQuartos = numQuartos;
    }

}
