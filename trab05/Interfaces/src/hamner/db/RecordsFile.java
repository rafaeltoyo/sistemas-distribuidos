package hamner.db;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class RecordsFile extends BaseRecordsFile {
    /** Índice em memória */
    protected Hashtable<String, RecordHeader> memIndex;

    /*------------------------------------------------------------------------*/

    /** Cria um novo arquivo e inicializa os headers apropriados.
     * É alocado espaço suficiente no índice para o tamanho inicial especificado.
     * Também é inicializado um hashtable em memória.
     * @param filename nome do arquivo
     * @param initialSize tamanho inicial do arquivo
     * @throws IOException caso ocorra um erro de I/O
     * @throws RecordsFileException caso o arquivo já exista
     */
    public RecordsFile(String filename, int initialSize) throws IOException, RecordsFileException {
        super(filename, initialSize);
        memIndex = new Hashtable<>(initialSize);
    }

    /** Abre um arquivo já existente.
     * É também inicializado um hashtable em memória com os dados existentes.
     * @param filename nome do arquivo
     * @param accessFlags "r" ou "rw", como definido na RandomAccessFile
     * @throws IOException caso a abertura do arquivo falhe
     * @throws RecordsFileException caso o arquivo não exista
     */
    public RecordsFile(String filename, String accessFlags) throws IOException, RecordsFileException {
        super(filename, accessFlags);

        int numRecords = readNumRecordsHeader();
        memIndex = new Hashtable<>(numRecords);

        for (int i = 0; i < numRecords; ++i) {
            String key = readKeyFromIndex(i);
            RecordHeader header = readRecordHeaderFromIndex(i);
            header.setIndexPosition(i);
            memIndex.put(key, header);
        }
    }

    /*------------------------------------------------------------------------*/

    /** Retorna um conjunto com chaves de todos os registros do arquivo, a
     * partir do índice em memória.
     * @return chaves de todos os registros do arquivo
     */
    @Override
    public Enumeration<String> enumerateKeys() {
        return memIndex.keys();
    }

    /** Retorna o número de registros do arquivo, a partir do índice em memória.
     * @return número de registros do arquivo
     */
    @Override
    public int getNumRecords() {
        return memIndex.size();
    }

    /** Determina se há um registro com a chave especificada, a partir do índice
     * em memória.
     * @param key chave
     * @return true se e somente se o registro existe no arquivo
     */
    @Override
    public synchronized boolean recordExists(String key) {
        return memIndex.containsKey(key);
    }

    /*------------------------------------------------------------------------*/

    /** Retorna o cabeçalho para uma determinada chave, a partir do índice em
     * memória.
     * @param key chave do registro
     * @return cabeçalho do registro
     * @throws RecordsFileException caso a chave não exista
     */
    @Override
    protected RecordHeader keyToRecordHeader(String key) throws RecordsFileException {
        RecordHeader header = memIndex.get(key);
        if (header == null) {
            throw new RecordsFileException("Chave não encontrada: " + key);
        }

        return header;
    }

    /*------------------------------------------------------------------------*/

    /** Aloca espaço para um novo registro.
     * @param key chave do registro
     * @param dataLength tamanho em bytes do registro
     * @return cabeçalho do novo registro
     * @throws IOException caso ocorra um erro de I/O
     * @throws RecordsFileException caso a chave já exista
     */
    @Override
    protected RecordHeader allocateRecord(String key, int dataLength) throws IOException, RecordsFileException {
        RecordHeader newRecord = null;

        // Varre o arquivo em busca de espaço livre. Caso ache, aloca lá.
        Enumeration<RecordHeader> e = memIndex.elements();
        while (e.hasMoreElements()) {
            RecordHeader next = e.nextElement();

            int free = next.getFreeSpace();
            if (dataLength <= next.getFreeSpace()) {
                newRecord = next.split();
                writeRecordHeaderToIndex(next);
                break;
            }
        }

        // Se não achar, aloca no final.
        if (newRecord == null) {
            long endPointer = getFileLength();
            setFileLength(endPointer + dataLength);
            newRecord = new RecordHeader(endPointer, dataLength);
        }

        return newRecord;
    }

    /*------------------------------------------------------------------------*/

    /** Retorna o cabeçalho do registro apontado pelo ponteiro especificado.
     * @param targetFp ponteiro
     * @return cabeçalho do registro
     */
    @Override
    protected RecordHeader getRecordAt(long targetFp) {
        // Varre o arquivo em busca do registro
        Enumeration e = memIndex.elements();
        while (e.hasMoreElements()) {
            RecordHeader next = (RecordHeader) e.nextElement();

            // Se o ponteiro estiver entre o início e o fim dos dados, achou
            if (targetFp >= next.dataPointer && targetFp < next.dataPointer + (long) next.dataCapacity) {
                return next;
            }
        }

        // Se não achar, retorna null
        return null;
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona uma nova entrada no final do índice.
     * Assume que a função insureIndexSpace() foi chamada antes.
     * Atualiza o índice em memória.
     * @param key nova chave
     * @param newRecord novo registro
     * @param currentNumRecords número atual de registros
     * @throws IOException caso ocorra um erro de I/O
     * @throws RecordsFileException caso a chave seja maior que o tamanho permitido
     */
    @Override
    protected void addEntryToIndex(String key, RecordHeader newRecord, int currentNumRecords) throws IOException, RecordsFileException {
        super.addEntryToIndex(key, newRecord, currentNumRecords);
        memIndex.put(key, newRecord);
    }

    /** Remove um registro do índice.
     * Pega o último registro do índice e coloca no lugar do registro removido.
     * Atualiza o índice em memória.
     * @param key chave a remover
     * @param header header de registro a remover
     * @param currentNumRecords número atual de registros
     * @throws IOException caso ocorra um erro de I/O
     * @throws RecordsFileException caso ocorra erro ao pegar o último registro
     */
    @Override
    protected void deleteEntryFromIndex(String key, RecordHeader header, int currentNumRecords) throws IOException, RecordsFileException {
        super.deleteEntryFromIndex(key, header, currentNumRecords);
        RecordHeader deleted = memIndex.remove(key);
    }

    /*------------------------------------------------------------------------*/

    /** Fecha o arquivo.
     * Também limpa o índice em memória.
     * @throws IOException caso não seja possível fechar a RandomAccessFile
     * @throws RecordsFileException ???
     */
    @Override
    public synchronized void close() throws IOException, RecordsFileException {
        try {
            super.close();
        }
        finally {
            memIndex.clear();
            memIndex = null;
        }
    }
}
