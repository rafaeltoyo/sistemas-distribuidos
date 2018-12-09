package br.com.tbdc.server;

import br.com.tbdc.rmi.InterfaceTransacao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class ServidorResposta extends UnicastRemoteObject implements InterfaceTransacao {

    private SynchronousQueue<Boolean> response = new SynchronousQueue<>();
    private int id;

    public ServidorResposta(int idTransacao) throws RemoteException {
        this.id = idTransacao;
    }

    public void resetId(int idTransacao) {
        this.id = idTransacao;
        this.response.clear();
    }

    public boolean esperar(int num_participante) {

        int contador = 0;

        do {
            try {
                // Esperar uma resposta dos participantes
                // FIXME: Colocar timeout
                if (response.poll(10, TimeUnit.SECONDS)) {
                    // Registrar resposta positiva
                    contador++;
                }
                else {
                    // Qualquer resposta negativa cancela a operação
                    return false;
                }
            } catch (NullPointerException | InterruptedException e) {
                return false;
            }
        } while (contador < num_participante);
        // Terminado o laço sem nenhum problema, todas respostas foram SIM.
        return true;
    }

    @Override
    public boolean responder(int idTransacao, boolean resposta) throws RemoteException {

        // Validar a resposta com o id de transação
        if (idTransacao == this.id) {
            // Tudo certo, podemos adicionar na fila
            response.offer(resposta);
            return true;
        }
        // Resposta não é para essa transação
        return false;
    }

}
