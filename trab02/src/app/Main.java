/*============================================================================*/
/* Main.java                                                                  */
/*                                                                            */
/* TOP-LEVEL MAIN                                                             */
/*============================================================================*/
/* Autores: Rafael Hideo Toyomoto e Victor Barpp Gomes                        */
/*                                                                            */
/* 2018-08-23                                                                 */
/*============================================================================*/
// Este é apenas o ponto de entrada da aplicação.
/*============================================================================*/

package app;

import peer.MulticastPeer;

import java.io.IOException;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

/*============================================================================*/

/**
 * A classe Main é apenas o ponto de entrada da aplicação. Cria um objeto da
 * classe MulticastPeer e chama seu método run.
 *
 * @since 2018-08-23
 */
public class Main {

    /**
     * Método que é executado ao iniciar a aplicação.
     *
     * @param args argumentos de linha de comando.
     */
    public static void main(String[] args) {
        MulticastPeer peer = null;

        try {
            peer = new MulticastPeer();
            peer.run();
        }
        catch (SocketException e) {
            System.err.println("Socket: " + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("IO: " + e.getMessage());
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println("Security: " + e.getMessage());
        }
        finally {
            if (peer != null) peer.close();
        }
    }
}

/*============================================================================*/
