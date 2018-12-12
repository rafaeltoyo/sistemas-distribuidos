package br.com.tbdc.server;

import br.com.tbdc.rmi.InterfaceTransacao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * {@inheritDoc}
 * <p>
 * Servidor Resposta: Simula o servidor coordenador aguardando resposta para os participantes, mas é um serviço de
 * monitoramento das respostas para o servidor coordenador utilizar.
 */
public class ServidorResposta extends UnicastRemoteObject implements InterfaceTransacao {

    /**
     * Fila responsável por receber as resposta
     */
    private LinkedBlockingQueue<Boolean> response = new LinkedBlockingQueue<>();

    /**
     * ID da transação monitorada
     */
    private int id;

    /**
     * =================================================================================================================
     * Construtor genérico
     * =================================================================================================================
     *
     * @param idTransacao ID da transação que receberá as respostas
     * @throws RemoteException caso ocorra erro no RMI
     */
    public ServidorResposta(int idTransacao) throws RemoteException {
        this.id = idTransacao;
    }

    public void resetId(int idTransacao) {
        this.id = idTransacao;
        this.response.clear();
    }

    public int getIdTransacao() throws RemoteException {
        return id;
    }

    /**
     * =================================================================================================================
     * Esperar todas respostas solicitadas
     * =================================================================================================================
     *
     * @param num_participante Número de resposta para receber
     * @return Caso todas respostas forem SIM (True) retornar True, caso contrário retorna False
     */
    public boolean esperar(int num_participante) {

        int contador = 0;

        do {
            try {
                // Esperar uma resposta dos participantes
                // FIXME: Colocar timeout
                if (response.poll(10, TimeUnit.SECONDS)) {
                    // Registrar resposta positiva
                    contador++;
                    System.out.println("counter is " + contador);
                }//
                else {
                    // Qualquer resposta negativa cancela a operação
                    return false;
                }
            }//
            catch (NullPointerException | InterruptedException e) {
                return false;
            }
        } while (contador < num_participante);
        // Terminado o laço sem nenhum problema, todas respostas foram SIM.
        return true;
    }

    /**
     * =================================================================================================================
     * {@inheritDoc}
     * =================================================================================================================
     */
    @Override
    public boolean responder(int idTransacao, boolean resposta) throws RemoteException {
        System.out.println("got a response: " + resposta);

        // Validar a resposta com o id de transação
        if (idTransacao == this.id) {
            // Tudo certo, podemos adicionar na fila
            System.out.println("adding to queue");
            try {
                response.put(resposta);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        // Resposta não é para essa transação
        System.out.println("not adding to queue");
        return false;
    }

}
