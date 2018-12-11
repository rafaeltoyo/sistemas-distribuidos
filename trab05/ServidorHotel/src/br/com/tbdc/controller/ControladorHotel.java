package br.com.tbdc.controller;

import br.com.tbdc.model.Hospedagem;
import br.com.tbdc.model.Hotel;
import br.com.tbdc.model.HotelFile;
import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.hotel.InfoHotelRet;
import br.com.tbdc.rmi.InterfaceTransacao;
import br.com.tbdc.server.TransactionController;
import hamner.db.RecordsFileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

/** Esta classe é um singleton que controla todos os hotéis do servidor por meio
 * de métodos CRUD.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ControladorHotel {
    /** Instância do singleton */
    private static final ControladorHotel instance = new ControladorHotel();

    /** Arquivo de hotéis */
    private HotelFile hoteis;

    /*------------------------------------------------------------------------*/

    /** Construtor privado sem argumentos que apenas inicializa a lista de voos.
     */
    private ControladorHotel() {
        try {
            hoteis = new HotelFile("hotel.jdb");
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
    }

    /** Retorna a instância do singleton.
     * @return instância do singleton
     */
    public static synchronized ControladorHotel getInstance() {
        return instance;
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um hotel à lista de hotéis do servidor
     * @param hotel hotel já instanciado e inicializado
     */
    public void adicionarHotel(Hotel hotel) {
        hoteis.lockEscrita();
        try {
            hoteis.adicionarHotel(hotel);
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            hoteis.unlockEscrita();
        }
    }

    /*------------------------------------------------------------------------*/

    /** Permite adicionar novas hospedagens (datas) a um hotel já existente, a
     * partir de seu identificador.
     * @param idHotel identificador do hotel
     * @param dataIni data de início do período
     * @param dataFim data de fim do período (também é incluída no intervalo)
     */
    public void adicionarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim) {
        hoteis.lockEscrita();
        try {
            Hotel h = hoteis.lerHotel(idHotel);
            h.adicionarHospedagem(dataIni, dataFim);
            hoteis.atualizarHotel(h);
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            hoteis.unlockEscrita();
        }
    }

    /*------------------------------------------------------------------------*/

    /** Retorna um vetor de informações de hotéis que atendem aos parâmetros
     * fornecidos.
     * @param local cidade do hotel
     * @param dataIni data de chegada (primeira diária)
     * @param dataFim data de saída (não é inclusa no resultado)
     * @param numQuartos número de quartos desejados
     * @param numPessoas número de pessoas (total, não por quarto)
     * @return vetor com informações de hotel e hospedagem
     */
    public ArrayList<InfoHotelRet> consultarHospedagens(Cidade local, LocalDate dataIni, LocalDate dataFim, int numQuartos, int numPessoas) {
        // Cria um vetor de InfoHotelRet para enviar ao cliente
        ArrayList<InfoHotelRet> result = new ArrayList<>();

        hoteis.lockLeitura();
        try {
            // Pega todos os hotéis
            ArrayList<Hotel> todosHoteis = hoteis.lerHoteis();

            for (Hotel h : todosHoteis) {
                // Pula hotéis em outras cidades
                if (!local.equals(h.getLocal())) {
                    continue;
                }

                // Inicia a contagem de número de quartos disponíveis no número
                // máximo de quartos do hotel
                int quartosDisp = h.getInfoHotel().getNumQuartos();

                // Flag usada depois do loop para adicionar um hotel ou não à lista
                // de retorno
                boolean podeReceber = true;

                // Considera-se que o cliente sai na data de volta.
                // Portanto, não são incluídas hospedagens para o dia de volta.
                LocalDate data = dataIni.plusDays(0);
                while (data.isBefore(dataFim)) {
                    Hospedagem hosp = h.getHospedagemData(data);
                    if (hosp == null) {
                        // Deu ruim, esse hotel não está oferecendo hospedagem em
                        // um dos dias do período
                        podeReceber = false;
                        break;
                    }

                    int hospQuartosDisp = hosp.getQuartosDisp();
                    if (hospQuartosDisp < numPessoas) {
                        // Deu ruim, esse hotel não pode receber o cliente em todos
                        // os dias do período
                        podeReceber = false;
                        break;
                    }

                    if (hospQuartosDisp < quartosDisp) {
                        // Há um dia mais lotado, atualiza o número
                        quartosDisp = hospQuartosDisp;
                    }

                    // FIXME: vamos só ignorar o número de pessoas?

                    data = data.plusDays(1);
                }

                // Apenas envia o hotel se tiver vagas em todos os dias do período
                if (podeReceber) {
                    InfoHotelRet ihr = new InfoHotelRet(h.getInfoHotel(),
                            quartosDisp, dataIni, dataFim, numQuartos);
                    result.add(ihr);
                }
            }
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            hoteis.unlockLeitura();
        }

        return result;
    }

    /*------------------------------------------------------------------------*/

    /** Tenta comprar hospedagem em um hotel para todas as noites entre as datas informadas.
     *
     * @param idHotel identificador do hotel
     * @param dataIni data de entrada
     * @param dataFim data de saída (não é incluída na reserva, ou seja, é feita
     *                reserva somente até o dia anterior à data de saída)
     * @param numQuartos número de quartos desejados
     * @return true se e somente se a compra for bem sucedida */
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos) {
        hoteis.lockEscrita();

        try {
            ArrayList<Hotel> todosHoteis = hoteis.lerHoteis();

            // Busca o hotel
            Hotel hotel = null;
            for (Hotel h : todosHoteis) {
                if (h.getId() == idHotel) {
                    hotel = h;
                    break;
                }
            }

            if (hotel == null) {
                // Hotel não existe
                return false;
            }

            // Faz a reserva, se possível, e retorna true se bem sucedido
            if (hotel.reservar(dataIni, dataFim, numQuartos)) {
                hoteis.atualizarHotel(hotel);
                return true;
            }
            return false;
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            hoteis.unlockEscrita();
        }

        return false;
    }

    /*------------------------------------------------------------------------*/

    /** Inicia uma nova thread para tratar a fase 1 do protocolo "two-phase
     * commit": a preparação.
     * @param idHotel identificador do hotel
     * @param dataIni data de início da hospedagem
     * @param dataFim data de fim da hospedagem
     * @param numQuartos número de quartos a adquirir
     * @param coordenador interface à qual responder
     * @return true, sempre
     */
    public boolean prepararCompraPacote(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos, InterfaceTransacao coordenador) {
        System.out.println("start prepararCompraPacote");

        Thread separateThread = new Thread(() -> doPrepararCompraPacote(idHotel, dataIni, dataFim, numQuartos, coordenador));
        separateThread.run();

        System.out.println("end prepararCompraPacote");
        return true;
    }

    /** Executa realmente a fase 1 do protocolo "two-phase commit".
     * Obtém a lock de escrita, tenta executar e cria um arquivo temporário.
     * Responde ao coordenador por meio da interface fornecida como parâmetro.
     * ATENÇÃO: mantém a lock de escrita! Ela apenas é liberada ao receber um
     * commit ou rollback do coordenador.
     * @param idHotel identificador do hotel
     * @param dataIni data de início da hospedagem
     * @param dataFim data de fim da hospedagem
     * @param numQuartos número de quartos a adquirir
     * @param coordenador interface à qual responder
     */
    private void doPrepararCompraPacote(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos, InterfaceTransacao coordenador) {
        // Travar o arquivo de Hoteis
        hoteis.lockEscrita();

        try {
            // Resgatar o ID da Transação do Coordenador
            int idTransacao = coordenador.getIdTransacao();
            // Registrar a transação como ATIVADA
            TransactionController.getInstance().acceptTransaction(idTransacao, idHotel, dataIni, dataFim, numQuartos);

            // Montar o arquivo temp para armazenar o objeto com a transação efetivada provisoriamente
            String tempFilename = "temp" + File.separator + idTransacao + ".tmp";

            // Busca os hoteis (Validar a possibilidade da transação)
            Hotel hotel = null;
            ArrayList<Hotel> todosHoteis = hoteis.lerHoteis();
            for (Hotel h : todosHoteis) {
                if (h.getId() == idHotel) {
                    hotel = h;
                    break;
                }
            }

            // Foi encontrado o Hotel
            if (hotel != null) {

                // Faz a reserva, se possível, e responde true se bem sucedido
                if (hotel.reservar(dataIni, dataFim, numQuartos)) {

                    // Arquivo temporário
                    FileOutputStream fileOut = new FileOutputStream(tempFilename);
                    ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                    objectOut.writeObject(hotel);
                    objectOut.close();

                    // Efetivar a transação de forma provisória
                    TransactionController.getInstance().prepare(idTransacao);

                    coordenador.responder(idTransacao, true);
                    return;
                }
                // Caso não consiga reservar todos os dias no Hotel
            }
            // Caso não consiga encontrar o Hotel

            // Cancelar a transação
            TransactionController.getInstance().rollback(idTransacao);
            // Enviar resposta NEGATIVA
            coordenador.responder(idTransacao, false);
        }
        catch (RecordsFileException | IOException e) {
            e.printStackTrace();
        }
    }

    /*------------------------------------------------------------------------*/

    /** Inicia uma nova thread para tratar a fase 2 do protocolo "two-phase
     * commit": o commit.
     * @param coordenador interface à qual responder
     * @return true, sempre
     */
    public boolean efetivarCompraPacote(InterfaceTransacao coordenador) {
        System.out.println("start efetivarCompraPacote");
        Thread separateThread = new Thread(() -> doEfetivarCompraPacote(coordenador));
        separateThread.run();

        return true;
    }

    /** Executa realmente a fase 2 do protocolo "two-phase commit".
     * Escreve o arquivo temporário no arquivo definitivo e libera a lock de
     * escrita.
     * Responde ao coordenador por meio da interface fornecida como parâmetro.
     * @param coordenador interface à qual responder
     */
    private void doEfetivarCompraPacote(InterfaceTransacao coordenador) {
        try {
            int idTransacao = coordenador.getIdTransacao();
            String tempFilename = "temp" + File.separator + idTransacao + ".tmp";

            FileInputStream fileIn = new FileInputStream(tempFilename);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Hotel hotel = (Hotel) objectIn.readObject();

            hoteis.atualizarHotel(hotel);
            coordenador.responder(idTransacao, true);

            TransactionController.getInstance().commit(idTransacao);

            File f = new File(tempFilename);
            f.delete();
        }
        catch (IOException | ClassNotFoundException | RecordsFileException e) {
            // ???
            e.printStackTrace();
        }
        finally {
            // TODO
            // Caso isso aqui não funcione, vamos ter que fazer a thread que
            // pegou a lock dormir e notificar ela aqui.
            System.out.println("releasing write lock");
            hoteis.unlockEscrita();
            System.out.println("released? ");
        }
    }

    /*------------------------------------------------------------------------*/

    /** Inicia uma nova thread para tratar a fase 2 do protocolo "two-phase
     * commit": o rollback.
     * @param coordenador interface à qual responder
     * @return true, sempre
     */
    public boolean abortarCompraPacote(InterfaceTransacao coordenador) {
        Thread separateThread = new Thread(() -> doAbortarCompraPacote(coordenador));
        separateThread.run();

        return true;
    }

    /** Executa realmente a fase 2 do protocolo "two-phase commit".
     * Apenas deleta o arquivo temporário no arquivo definitivo e libera a lock
     * de escrita.
     * Responde ao coordenador por meio da interface fornecida como parâmetro.
     * @param coordenador interface à qual responder
     */
    private void doAbortarCompraPacote(InterfaceTransacao coordenador) {
        try {
            int idTransacao = coordenador.getIdTransacao();
            String tempFilename = "temp" + File.separator + idTransacao + ".tmp";

            coordenador.responder(idTransacao, true);

            TransactionController.getInstance().rollback(idTransacao);

            File f = new File(tempFilename);
            f.delete();
        }
        catch (RemoteException e) {
            // ???
            e.printStackTrace();
        }
        finally {
            // TODO
            // Caso isso aqui não funcione, vamos ter que fazer a thread que
            // pegou a lock dormir e notificar ela aqui.
            hoteis.unlockEscrita();
        }
    }

}
