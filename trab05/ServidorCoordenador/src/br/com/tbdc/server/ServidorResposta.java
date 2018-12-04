package br.com.tbdc.server;

import br.com.tbdc.rmi.InterfaceTransacao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.SynchronousQueue;

public class ServidorResposta extends UnicastRemoteObject implements InterfaceTransacao {

    private SynchronousQueue<Boolean> response = new SynchronousQueue<>();
    private int id;

    public ServidorResposta(int idTransacao) throws RemoteException {
        this.id = idTransacao;
    }

    public boolean esperar(int num_participante) {

        // Para cada participante ...
        for (int i = 0; i < num_participante; i++) {

            boolean quero;

            try {
                // Esperar uma resposta
                quero = response.take();
            } catch (InterruptedException e) {
                return false;
            }

            // Qualquer resposta negativa cancela a operacao
            if (!quero) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean responder(int idTransacao, boolean resposta) throws RemoteException {
        response.offer(resposta);

        return false;
    }

}
