package model.saldo;

import java.io.Serializable;
import java.util.Date;

public class Reserva implements Serializable {
    // FIXME
    // Objetos dessa classe aparecem dentro de um objeto do tipo Voo.
    // O problema é que, quando o usuário faz uma consulta de voos, objetos Voo
    // são retornados pelo servidor. Então, essa classe teria que implementar
    // Serializable, para poder ser enviada por rede.
    //
    // No entanto, para mim, não faz muito sentido que os dados de reserva de
    // outros passageiros sejam enviados para o cliente. Só interessa mesmo o
    // número de poltronas livres.
    //
    // Temos duas opções:
    // 1: Só colocar `implements Serializable` aqui, e deixar assim (solução
    // mais rápida de implementar, mas menos eficiente)
    // 2: Retirar `extends ObjComSaldo` do Voo e criar uma nova classe,
    // que extende ObjComSaldo, e tem dentro o Voo. A consulta continua
    // retornando objetos Voo, mas como o saldo está fora desses objetos, não
    // são enviados ao cliente (opção ideal)

    Date data;

    int quantidade;

    public Reserva(int quantidade) {
        this.data = new Date();
        this.quantidade = quantidade;
    }
}
