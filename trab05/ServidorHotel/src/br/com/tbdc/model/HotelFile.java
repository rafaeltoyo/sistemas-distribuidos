package br.com.tbdc.model;

import hamner.db.RecordReader;
import hamner.db.RecordWriter;
import hamner.db.RecordsFile;
import hamner.db.RecordsFileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HotelFile {
    /** Arquivo */
    private RecordsFile recordsFile;

    /** Lock para operações de R/W */
    private ReentrantReadWriteLock lock;

    /*------------------------------------------------------------------------*/

    /** Abre um arquivo existente e interpreta como um arquivo de hotéis.
     * @param filename caminho para o arquivo
     * @throws IOException caso ocorra um erro de I/O
     * @throws RecordsFileException caso o arquivo não exista
     */
    public HotelFile(String filename) throws IOException, RecordsFileException {
        // Tenta acessar o arquivo.
        recordsFile = new RecordsFile(filename, "rw");
        lock = new ReentrantReadWriteLock();
    }

    /*------------------------------------------------------------------------*/

    /** Obtém o maior ID de hotel contido no arquivo.
     * @return maior ID, ou -1 se não houver nenhum hotel
     */
    public int getMaiorId() {
        Enumeration<String> keys = recordsFile.enumerateKeys();

        // As chaves são sempre maiores ou iguais a 0
        int maiorKey = -1;
        while (keys.hasMoreElements()) {
            String keyStr = keys.nextElement();

            int key = Integer.parseInt(keyStr);
            if (maiorKey < key) {
                maiorKey = key;
            }
        }

        return maiorKey;
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um hotel ao arquivo.
     * O acesso ao arquivo já é sincronizado dentro da classe RecordsFile.
     * @param hotel objeto instanciado previamente
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do hotel) já exista no arquivo
     */
    public void adicionarHotel(Hotel hotel) throws IOException, RecordsFileException {
        if (hotel != null) {
            RecordWriter rw = new RecordWriter(hotel.getId() + "");
            rw.writeObject(hotel);
            recordsFile.insertRecord(rw);
        }
    }

    /*------------------------------------------------------------------------*/

    /** Lê um hotel do arquivo.
     * @param idHotel identificador do hotel
     * @return objeto hotel com todos os atributos carregados
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do hotel) não exista no arquivo
     */
    public Hotel lerHotel(int idHotel) throws IOException, RecordsFileException {
        RecordReader rr = recordsFile.readRecord(idHotel + "");

        try {
            return (Hotel) rr.readObject();
        }
        catch (ClassNotFoundException e) {
            // Não vai acontecer
            e.printStackTrace();
        }

        return null;
    }

    /*------------------------------------------------------------------------*/

    public ArrayList<Hotel> lerHoteis() throws IOException, RecordsFileException {
        ArrayList<Hotel> hoteis = new ArrayList<>();

        Enumeration<String> keys = recordsFile.enumerateKeys();

        while (keys.hasMoreElements()) {
            String keyStr = keys.nextElement();

            RecordReader rr = recordsFile.readRecord(keyStr);
            try {
                hoteis.add((Hotel) rr.readObject());
            } catch (ClassNotFoundException e) {
                // Não vai acontecer
                e.printStackTrace();
            }
        }

        return hoteis;
    }

    /*------------------------------------------------------------------------*/

    /** Atualiza as informações do hotel contidas em arquivo.
     * @param hotel objeto Hotel a atualizar
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do hotel) não exista no arquivo
     */
    public void atualizarHotel(Hotel hotel) throws IOException, RecordsFileException {
        if (hotel != null) {
            RecordWriter rw = new RecordWriter(hotel.getId() + "");
            rw.writeObject(hotel);
            recordsFile.updateRecord(rw);
        }
    }

    /*------------------------------------------------------------------------*/

    /** Remove um hotel do arquivo.
     * O acesso ao arquivo já é sincronizado dentro da classe RecordsFile.
     * @param idHotel identificador do hotel a remover
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do hotel) não exista no arquivo
     */
    public void removerHotel(int idHotel) throws IOException, RecordsFileException {
        recordsFile.deleteRecord(idHotel + "");
    }

    /*------------------------------------------------------------------------*/

    /** Obtém a trava de leitura.
     * Essa função é mantida pública para que a classe que trata das transações
     * possa obter a trava e mantê-la até que receba a resposta.
     */
    public void lockLeitura() {
        lock.readLock().lock();
    }

    /** Obtém a trava de escrita.
     * Essa função é mantida pública para que a classe que trata das transações
     * possa obter a trava e mantê-la até que receba a resposta.
     */
    public void lockEscrita() {
        lock.writeLock().lock();
    }

    /** Libera a trava de leitura.
     * Essa função é mantida pública para que a classe que trata das transações
     * possa liberar a trava quando receber a resposta.
     */
    public void unlockLeitura() {
        lock.readLock().unlock();
    }

    /** Libera a trava de escrita.
     * Essa função é mantida pública para que a classe que trata das transações
     * possa liberar a trava quando receber a resposta.
     */
    public void unlockEscrita() {
        lock.writeLock().unlock();
    }
}
