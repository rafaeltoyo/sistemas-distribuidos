package br.com.tbdc.controller;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.saldo.Reserva;
import br.com.tbdc.model.voo.InfoVoo;
import br.com.tbdc.model.voo.TipoPassagem;
import br.com.tbdc.model.voo.Voo;
import br.com.tbdc.model.voo.VooFile;
import br.com.tbdc.rmi.InterfaceTransacao;
import br.com.tbdc.server.TransactionController;
import hamner.db.RecordsFileException;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

/** Esta classe é um singleton que controla todos os voos do servidor por meio
 * de métodos CRUD.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ControladorVoo {
    /** Instância do singleton */
    private static final ControladorVoo instance = new ControladorVoo();

    /** Lista de voos em memória */
    private VooFile voos;

    /*------------------------------------------------------------------------*/

    /** Construtor privado sem argumentos que apenas inicializa a lista de voos.
     */
    private ControladorVoo() {
        try {
            voos = new VooFile("voo.jdb");
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
    }

    /** Retorna a instância do singleton.
     * @return instância do singleton
     */
    public static synchronized ControladorVoo getInstance() {
        return instance;
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um voo à lista de voos do servidor.
     * @param voo voo já instanciado e inicializado
     */
    public void adicionarVoo(Voo voo) {
        voos.lockEscrita();
        try {
            voos.adicionarVoo(voo);
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            voos.unlockEscrita();
        }
    }

    /*------------------------------------------------------------------------*/

    /** Retorna uma lista de passagens que atendem aos atributos fornecidos nos
     * parâmetros.
     * @param tipo somente ida ou ida e volta
     * @param origem local de origem do voo
     * @param destino local de destino do voo
     * @param dataIda data do voo de ida
     * @param dataVolta data do voo de volta, caso o tipo seja ida e volta
     * @param numPessoas número de passagens desejadas
     * @return lista de passagens aéreas disponíveis que atendem aos parâmetros
     */
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo, Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numPessoas) {
        // Armazenar o resultado da consulta
        ArrayList<InfoVoo> result = new ArrayList<>();

        voos.lockLeitura();
        try {
            ArrayList<Voo> todosVoos = voos.lerVoos();

            // Para todos os voos armazenados no servidor ...
            for (Voo voo : todosVoos) {
                // Adiciona voos de ida
                if (validarConsulta(voo, origem, destino, dataIda, numPessoas)) {
                    result.add(voo.getInfoVoo());
                }
                // Adiciona voos de volta
                if (tipo == TipoPassagem.IDA_E_VOLTA) {
                    if (validarConsulta(voo, destino, origem, dataVolta, numPessoas)) {
                        result.add(voo.getInfoVoo());
                    }
                }
            }
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            voos.unlockLeitura();
        }

        return result;
    }

    /** Confere se um voo possui atributos iguais aos parâmetros fornecidos.
     * @param voo voo que será testado
     * @param origem parâmetro de local de origem
     * @param destino parâmetro de local de destino
     * @param data parâmetro de data
     * @param numPessoas parâmetro de número de pessoas
     * @return true se e somente se o voo se enquadra a todos os parâmetros
     */
    private boolean validarConsulta(Voo voo, Cidade origem, Cidade destino, LocalDate data, int numPessoas) {
        return origem.equals(voo.getOrigem()) &&
                destino.equals(voo.getDestino()) &&
                data.equals(voo.getData()) &&
                numPessoas <= voo.getPoltronasDisp();
    }

    /*------------------------------------------------------------------------*/

    /** Tenta comprar passagens de um voo de ida e opcionalmente de um voo de
     * volta. Se o parâmetro tipo for IDA_E_VOLTA, primeiro tenta-se comprar as
     * passagens de ida e depois as passagens de volta. Caso seja efetuada a
     * reserva das passagens de ida, mas ocorra falha na reserva das passagens
     * de volta, a reserva da passagem de ida é revertida (não é efetuada a sua
     * compra).
     * @param tipo       SOMENTE_IDA ou IDA_E_VOLTA
     * @param idVooIda   identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida
     */
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) {
        if (tipo == TipoPassagem.IDA_E_VOLTA) {
            return comprarPassagensIdaEVolta(idVooIda, idVooVolta, numPessoas);
        }
        return comprarPassagensSomenteIda(idVooIda, numPessoas);
    }

    /** Tenta efetuar compra de passagem de ida.
     * Chamada pela função comprarPassagens.
     * @param idVoo      identificador do voo de ida
     * @param numPessoas número de passagens a adquirir
     * @return true se e somente se a compra for bem sucedida
     */
    private boolean comprarPassagensSomenteIda(int idVoo, int numPessoas) {
        voos.lockEscrita();

        try {
            ArrayList<Voo> todosVoos = voos.lerVoos();

            // Busca o voo
            Voo voo = null;
            for (Voo v : todosVoos) {
                if (v.getId() == idVoo) {
                    voo = v;
                    break;
                }
            }

            if (voo == null) {
                // Voo não existe
                return false;
            }

            // Retorna false se não encontrou o voo
            // Retorna true se conseguir comprar o número desejado, e false caso contrário
            if (voo.reservar(numPessoas) != null) {
                voos.atualizarVoo(voo);
                return true;
            }
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            voos.unlockEscrita();
        }

        return false;
    }

    /** Tenta efetuar compra de passagens de ida e volta.
     * Chamada pela função comprarPassagens.
     * @param idVooIda   identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida para ambos os voos
     */
    private boolean comprarPassagensIdaEVolta(int idVooIda, int idVooVolta, int numPessoas) {
        voos.lockEscrita();

        try {
            ArrayList<Voo> todosVoos = voos.lerVoos();

            // Busca os voos
            Voo vooIda = null;
            Voo vooVolta = null;
            for (Voo v : todosVoos) {
                if (v.getId() == idVooIda) {
                    vooIda = v;
                } else if (v.getId() == idVooVolta) {
                    vooVolta = v;
                }
                if (vooIda != null && vooVolta != null) {
                    break;
                }
            }

            // Retorna false se não encontrou algum dos voos
            if (vooIda == null || vooVolta == null) {
                return false;
            }

            Reserva reservaVooIda = vooIda.reservar(numPessoas);
            if (reservaVooIda != null) {
                if (vooVolta.reservar(numPessoas) != null) {
                    // Retorna true se conseguir comprar ida e volta
                    voos.atualizarVoo(vooIda);
                    voos.atualizarVoo(vooVolta);
                    return true;
                }
                // Caso não consiga comprar a volta,
                // faz rollback na compra do voo de ida e retorna false
                vooIda.estornar(reservaVooIda);
            }
        }
        catch (IOException | RecordsFileException e) {
            e.printStackTrace();
        }
        finally {
            voos.unlockEscrita();
        }

        return false;
    }

    /*------------------------------------------------------------------------*/

    public boolean prepararCompraPacote(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas, InterfaceTransacao coordenador) {
        Thread separateThread = new Thread(() -> doPrepararCompraPacote(tipo, idVooIda, idVooVolta, numPessoas, coordenador));
        separateThread.run();

        return true;
    }

    private void doPrepararCompraPacote(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas, InterfaceTransacao coordenador) {
        // Travar o arquivo de Voos
        voos.lockEscrita();

        try {
            // Resgatar o ID da Transação do Coordenador
            int idTransacao = coordenador.getIdTransacao();
            // Registrar a transação como ATIVADA
            TransactionController.getInstance().acceptTransaction(idTransacao, idVooIda, idVooVolta, numPessoas);

            // Montar o arquivo temp para armazenar o objeto com a transação efetivada provisoriamente
            String tempFilename = "temp" + File.separator + idTransacao + ".tmp";

            // Busca os voos (Validar a possibilidade da transação)
            Voo vooIda = null;
            Voo vooVolta = null;
            ArrayList<Voo> todosVoos = voos.lerVoos();
            for (Voo v : todosVoos) {

                // Voo de Ida
                if (v.getId() == idVooIda) {
                    vooIda = v;
                }
                // Voo de Volta
                else if (v.getId() == idVooVolta) {
                    vooVolta = v;
                }

                // Os dois voos já foram encontrados, parar
                if (vooIda != null && vooVolta != null) break;
            }

            // Foram encontrados os Voos de Ida e Volta solicitado
            if (vooIda != null && vooVolta != null) {

                // Tentar reservar o Voo de Ida
                Reserva reservaVooIda = vooIda.reservar(numPessoas);
                if (reservaVooIda != null) {

                    // Se TRUE, Tentar reservar o Voo de Volta
                    if (vooVolta.reservar(numPessoas) != null) {

                        // Responde true se conseguir comprar ida e volta
                        // Arquivo temporário
                        FileOutputStream fileOut = new FileOutputStream(tempFilename);
                        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                        objectOut.writeObject(new Pair<>(vooIda, vooVolta));
                        objectOut.close();

                        // Efetivar a transação de forma provisória
                        TransactionController.getInstance().prepare(idTransacao);

                        coordenador.responder(idTransacao, true);
                        return;
                    }
                    // Caso não consiga comprar a volta [...]
                    vooIda.estornar(reservaVooIda); // Rollback na compra do Voo de Ida
                }
                // Caso não consiga comprar a ida [...]
            }
            // Caso não consiga encontrar os Voos de Ida e Volta [...]

            // Cancelar a transação
            TransactionController.getInstance().rollback(idTransacao);
            // Enviar resposta NEGATIVA
            coordenador.responder(idTransacao, false);
        }
        catch (IOException | RecordsFileException e) {
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
            Pair<Voo, Voo> parVoos = (Pair<Voo, Voo>) objectIn.readObject();

            Voo vooIda = parVoos.getKey();
            Voo vooVolta = parVoos.getValue();

            voos.atualizarVoo(vooIda);
            voos.atualizarVoo(vooVolta);
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
            voos.unlockEscrita();
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
            voos.unlockEscrita();
        }
    }

}
