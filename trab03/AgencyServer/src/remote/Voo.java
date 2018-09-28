package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

/** Interface RMI para o voo.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface Voo extends Remote {
    /** Obtém o identificador do voo.
     * @return identificador do voo
     * @throws RemoteException caso ocorra erro no RMI
     */
    int obterId() throws RemoteException;

    /** Obtém o local de origem do voo.
     * @return nome do local de origem
     * @throws RemoteException caso ocorra erro no RMI
     */
    String obterOrigem() throws RemoteException;

    /** Obtém o local de destino do voo.
     * @return nome do local de destino
     * @throws RemoteException caso ocorra erro no RMI
     */
    String obterDestino() throws RemoteException;

    /** Obtém a data de partida do voo.
     * @return data de partida
     * @throws RemoteException caso ocorra erro no RMI
     */
    Calendar obterData() throws RemoteException;

    /** Obtém o número total de poltronas do voo.
     * @return número total de poltronas
     * @throws RemoteException caso ocorra erro no RMI
     */
    int obterPoltronasTotal() throws RemoteException;

    /** Obtém o número de poltronas disponíveis no voo.
     * @return número de poltronas disponíveis
     * @throws RemoteException caso ocorra erro no RMI
     */
    int obterPoltronasDisp() throws RemoteException;

    /** Realiza a compra de passagens de um voo.
     * @param numPassagens número de passagens a comprar
     * @return true, se e somente se a compra ocorreu com sucesso
     * @throws RemoteException caso ocorra erro no RMI
     */
    boolean comprar(int numPassagens) throws RemoteException;
}
