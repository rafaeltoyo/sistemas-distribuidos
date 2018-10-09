package model.voo;

import model.cidade.Cidade;

import java.io.Serializable;
import java.time.LocalDate;

public class InfoVoo implements Serializable {
    /** Contagem de voos para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador do voo */
    private int id;

    /** Local de origem (partida) */
    private Cidade origem;

    /** Local de destino (chegada) */
    private Cidade destino;

    /** Data do voo */
    private LocalDate data;

    /** Poltronas disponíveis */
    public int poltronasDisp;

    /*------------------------------------------------------------------------*/

    public int getId() {
        return id;
    }

    public Cidade getOrigem() {
        return origem;
    }

    public Cidade getDestino() {
        return destino;
    }

    public LocalDate getData() {
        return data;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor para um novo voo.
     * Obtém um identificador com auto-incremento.
     * Considera que todas as poltronas da aeronave estão disponíveis para
     * compra.
     * @param origem local de origem (partida)
     * @param destino local de destino (chegada)
     * @param data data de partida
     */
    public InfoVoo(Cidade origem, Cidade destino, LocalDate data,
                   int poltronasDisp) {
        this.id = (count++);
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.poltronasDisp = poltronasDisp;
    }
}
