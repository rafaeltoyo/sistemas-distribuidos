package br.com.tbdc.model.voo;

import hamner.db.RecordReader;
import hamner.db.RecordWriter;
import hamner.db.RecordsFile;
import hamner.db.RecordsFileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VooFile {
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
    public VooFile(String filename) throws IOException, RecordsFileException {
        // Tenta acessar o arquivo.
        recordsFile = new RecordsFile(filename, "rw");
        lock = new ReentrantReadWriteLock();
    }

    /*------------------------------------------------------------------------*/

    /** Obtém o maior ID de voo contido no arquivo.
     * @return maior ID, ou -1 se não houver nenhum voo
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

    /** Adiciona um voo ao arquivo.
     * O acesso ao arquivo já é sincronizado dentro da classe RecordsFile.
     * @param voo objeto instanciado previamente
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do voo) já exista no arquivo
     */
    public void adicionarVoo(Voo voo) throws IOException, RecordsFileException {
        if (voo != null) {
            RecordWriter rw = new RecordWriter(voo.getId() + "");
            rw.writeObject(voo);
            recordsFile.insertRecord(rw);
        }
    }

    /*------------------------------------------------------------------------*/

    /** Lê um voo do arquivo.
     * @param idVoo identificador do voo
     * @return objeto voo com todos os atributos carregados
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do voo) não exista no arquivo
     */
    public Voo lerVoo(int idVoo) throws IOException, RecordsFileException {
        RecordReader rr = recordsFile.readRecord(idVoo + "");

        try {
            return (Voo) rr.readObject();
        }
        catch (ClassNotFoundException e) {
            // Não vai acontecer
            e.printStackTrace();
        }

        return null;
    }

    /*------------------------------------------------------------------------*/

    public ArrayList<Voo> lerVoos() throws IOException, RecordsFileException {
        ArrayList<Voo> voos = new ArrayList<>();

        Enumeration<String> keys = recordsFile.enumerateKeys();

        while (keys.hasMoreElements()) {
            String keyStr = keys.nextElement();

            RecordReader rr = recordsFile.readRecord(keyStr);
            try {
                voos.add((Voo) rr.readObject());
            } catch (ClassNotFoundException e) {
                // Não vai acontecer
                e.printStackTrace();
            }
        }

        return voos;
    }

    /*------------------------------------------------------------------------*/

    /** Atualiza as informações do voo contidas em arquivo.
     * @param voo objeto Voo a atualizar
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do voo) não exista no arquivo
     */
    public void atualizarVoo(Voo voo) throws IOException, RecordsFileException {
        if (voo != null) {
            RecordWriter rw = new RecordWriter(voo.getId() + "");
            rw.writeObject(voo);
            recordsFile.updateRecord(rw);
        }
    }

    /*------------------------------------------------------------------------*/

    /** Remove um voo do arquivo.
     * O acesso ao arquivo já é sincronizado dentro da classe RecordsFile.
     * @param idVoo identificador do voo a remover
     * @throws IOException caso ocorra algum erro de I/O
     * @throws RecordsFileException caso a chave (ID do voo) não exista no arquivo
     */
    public void removerVoo(int idVoo) throws IOException, RecordsFileException {
        recordsFile.deleteRecord(idVoo + "");
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
