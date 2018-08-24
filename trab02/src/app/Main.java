/*============================================================================*/
/* Main.java                                                                  */
/*                                                                            */
/* TOP-LEVEL MAIN                                                             */
/*============================================================================*/
/* Autor: Victor Barpp Gomes                                                  */
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

public class Main {

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
