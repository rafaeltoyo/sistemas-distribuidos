package hamner.db;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Enumeration;

public abstract class BaseRecordsFile {

    protected static final int FILE_HEADERS_REGION_LENGTH = 16;

    protected static final int RECORD_HEADER_LENGTH = 16;

    protected static final int MAX_KEY_LENGTH = 64;

    protected static final int INDEX_ENTRY_LENGTH = MAX_KEY_LENGTH + RECORD_HEADER_LENGTH;

    protected static final long NUM_RECORDS_HEADER_LOCATION = 0;

    protected static final long DATA_START_HEADER_LOCATION = 4;

    /*------------------------------------------------------------------------*/

    /** Arquivo. */
    private RandomAccessFile file;

    /** Ponteiro para o local do arquivo onde começam os registros. */
    protected long dataStartPointer;

    /*------------------------------------------------------------------------*/

    protected long getFileLength() throws IOException {
        return file.length();
    }

    protected void setFileLength(long length) throws IOException {
        file.setLength(length);
    }

    /*------------------------------------------------------------------------*/

    /** Cria um novo arquivo e inicializa os headers apropriados.
     * É alocado espaço suficiente no índice para o tamanho inicial especificado.
     * @param filename nome do arquivo
     * @param initialSize tamanho inicial do arquivo
     * @throws IOException caso a criação do arquivo falhe
     * @throws RecordsFileException caso o arquivo já exista
     */
    protected BaseRecordsFile(String filename, int initialSize) throws IOException, RecordsFileException {
        File f = new File(filename);
        if (f.exists()) {
            throw new RecordsFileException("O arquivo já existe: " + filename);
        }

        file = new RandomAccessFile(f, "rw");
        dataStartPointer = indexPositionToKeyFp(initialSize);
        setFileLength(dataStartPointer);
        writeNumRecordsHeader(0);
        writeDataStartPointerHeader(dataStartPointer);
    }

    /** Abre um arquivo já existente e inicializa o ponteiro.
     * @param filename nome do arquivo
     * @param accessFlags "r" ou "rw", como definido na RandomAccessFile
     * @throws IOException caso a abertura do arquivo falhe
     * @throws RecordsFileException caso o arquivo não exista
     */
    protected BaseRecordsFile(String filename, String accessFlags) throws IOException, RecordsFileException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new RecordsFileException("O arquivo não existe: " + filename);
        }

        file = new RandomAccessFile(f, accessFlags);
        dataStartPointer = readDataStartHeader();
    }

    /*------------------------------------------------------------------------*/

    /** Retorna o cabeçalho para uma determinada chave.
     * @param key chave do registro
     * @return cabeçalho do registro
     */
    protected abstract RecordHeader keyToRecordHeader(String key) throws RecordsFileException;

    /*------------------------------------------------------------------------*/

    /** Aloca espaço para um novo registro.
     * @param key chave do registro
     * @param dataLength tamanho em bytes do registro
     * @return cabeçalho do novo registro
     */
    protected abstract RecordHeader allocateRecord(String key, int dataLength) throws IOException, RecordsFileException;

    /*------------------------------------------------------------------------*/

    /** Retorna o cabeçalho do registro apontado pelo ponteiro especificado.
     * @param targetFp ponteiro
     * @return cabeçalho do registro
     */
    protected abstract RecordHeader getRecordAt(long targetFp);

    /*------------------------------------------------------------------------*/

    /** Volta o ponteiro para a posição do header de número de registros e lê
     * seu valor.
     * @return valor do header de número de registros
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected int readNumRecordsHeader() throws IOException {
        file.seek(NUM_RECORDS_HEADER_LOCATION);
        return file.readInt();
    }

    /** Volta o ponteiro para a posição do header de número de registros e
     * escreve um novo valor.
     * @param numRecords novo número de registros
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected void writeNumRecordsHeader(int numRecords) throws IOException {
        file.seek(NUM_RECORDS_HEADER_LOCATION);
        file.writeInt(numRecords);
    }

    /*------------------------------------------------------------------------*/

    /** Move o ponteiro para a posição do header de início dos dados e lê seu
     * valor.
     * @return valor do header de início dos dados
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected long readDataStartHeader() throws IOException {
        file.seek(DATA_START_HEADER_LOCATION);
        return file.readLong();
    }

    /** Volta o ponteiro para a posição do header de início dos dados e escreve
     * um novo valor.
     * @param dataStartPointer novo ponteiro
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected void writeDataStartPointerHeader(long dataStartPointer) throws IOException {
        file.seek(DATA_START_HEADER_LOCATION);
        file.writeLong(dataStartPointer);
    }

    /*------------------------------------------------------------------------*/

    /** Retorna um ponteiro para o primeiro byte da chave localizada na posição
     * de índice especificada.
     * @param pos posição do índice
     * @return ponteiro para o primeiro byte da chave
     */
    protected long indexPositionToKeyFp(int pos) {
        return FILE_HEADERS_REGION_LENGTH + (INDEX_ENTRY_LENGTH * pos);
    }

    /** Lê a chave contida na posição pos do índice.
     * @param pos posição do índice
     * @return chave
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected String readKeyFromIndex(int pos) throws IOException {
        file.seek(indexPositionToKeyFp(pos));
        return file.readUTF();
    }

    /*------------------------------------------------------------------------*/

    /** Retorna um ponteiro para o primeiro byte dos headers de registro
     * localizados na posição de índice especificada.
     * @param pos posição do índice
     * @return ponteiro para o primeiro byte dos headers de registro
     */
    protected long indexPositionToRecordHeaderFp(int pos) {
        return indexPositionToKeyFp(pos) + MAX_KEY_LENGTH;
    }

    /** Lê os headers de registro contidos na posição pos do índice.
     * @param pos posição do índice
     * @return headers de registro
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected RecordHeader readRecordHeaderFromIndex(int pos) throws IOException {
        file.seek(indexPositionToRecordHeaderFp(pos));
        return RecordHeader.readHeader(file);
    }

    /** Escreve um header de registro na posição de índice contida no objeto.
     * @param header header de registro
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected void writeRecordHeaderToIndex(RecordHeader header) throws IOException {
        file.seek(indexPositionToRecordHeaderFp(header.indexPosition));
        header.write(file);
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona uma nova entrada no final do índice.
     * Assume que a função insureIndexSpace() foi chamada antes.
     * @param key nova chave
     * @param newRecord novo registro
     * @param currentNumRecords número atual de registros
     * @throws IOException caso não seja possível fazer seek no arquivo
     * @throws RecordsFileException caso a chave seja maior que o tamanho permitido
     */
    protected void addEntryToIndex(String key, RecordHeader newRecord, int currentNumRecords) throws IOException, RecordsFileException {
        DbByteArrayOutputStream temp = new DbByteArrayOutputStream(MAX_KEY_LENGTH);
        (new DataOutputStream(temp)).writeUTF(key);
        if (temp.size() > MAX_KEY_LENGTH) {
            throw new RecordsFileException("A chave é maior do que o tamanho permitido (" + MAX_KEY_LENGTH + " bytes)");
        }

        file.seek(indexPositionToKeyFp(currentNumRecords));
        temp.writeTo(new FileOutputStream(file.getFD()));

        file.seek(indexPositionToRecordHeaderFp(currentNumRecords));
        newRecord.write(file);

        newRecord.setIndexPosition(currentNumRecords);
        writeNumRecordsHeader(currentNumRecords + 1);
    }

    /** Remove um registro do índice.
     * Pega o último registro do índice e coloca no lugar do registro removido.
     * @param key chave a remover
     * @param header header de registro a remover
     * @param currentNumRecords número atual de registros
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected void deleteEntryFromIndex(String key, RecordHeader header, int currentNumRecords) throws IOException, RecordsFileException {
        if (header.indexPosition != currentNumRecords - 1) {
            String lastKey = readKeyFromIndex(currentNumRecords - 1);
            RecordHeader last = keyToRecordHeader(lastKey);
            last.setIndexPosition(header.indexPosition);

            file.seek(indexPositionToKeyFp(last.indexPosition));
            file.writeUTF(lastKey);

            file.seek(indexPositionToRecordHeaderFp(last.indexPosition));
            last.write(file);
        }

        writeNumRecordsHeader(currentNumRecords - 1);
    }

    /*------------------------------------------------------------------------*/

    /** Lê os dados de um registro a partir do valor de sua chave.
     * @param key chave do registro
     * @return dados do registro
     * @throws IOException caso não seja possível fazer seek no arquivo
     * @throws RecordsFileException
     */
    protected byte[] readRecordData(String key) throws IOException, RecordsFileException {
        return readRecordData(keyToRecordHeader(key));
    }

    /** Lê os dados de um registro a partir do seu cabeçalho.
     * @param header cabeçalho do registro
     * @return dados do registro
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    protected byte[] readRecordData(RecordHeader header) throws IOException {
        byte[] buf = new byte[header.dataCount];

        file.seek(header.dataPointer);
        file.readFully(buf);

        return buf;
    }

    /*------------------------------------------------------------------------*/

    /** Altera os dados de um registro.
     * O valor de tamanho de dados contido no header do registro é atualizado,
     * mas não é escrito no arquivo.
     * @param header cabeçalho do registro
     * @param rw
     * @throws IOException caso não seja possível fazer seek no arquivo
     * @throws RecordsFileException caso os novos dados não caibam no espaço
     * alocado para o registro
     */
    protected void writeRecordData(RecordHeader header, RecordWriter rw) throws IOException, RecordsFileException {
        if (rw.getDataLength() > header.dataCapacity) {
            throw new RecordsFileException("Os dados são maiores que o espaço disponível no registro.");
        }

        header.dataCount = rw.getDataLength();

        file.seek(header.dataPointer);
        rw.writeTo(new FileOutputStream(file.getFD()));
    }

    /** Altera os dados de um registro.
     * O valor de tamanho de dados contido no header do registro é atualizado,
     * mas não é escrito no arquivo.
     * @param header cabeçalho do registro
     * @param data novos dados do registro
     * @throws IOException caso não seja possível fazer seek no arquivo
     * @throws RecordsFileException caso os novos dados não caibam no espaço
     * alocado para o registro
     */
    protected void writeRecordData(RecordHeader header, byte[] data) throws IOException, RecordsFileException {
        if (data.length > header.dataCapacity) {
            throw new RecordsFileException("Os dados são maiores que o espaço disponível no registro.");
        }

        header.dataCount = data.length;

        file.seek(header.dataPointer);
        file.write(data, 0, data.length);
    }

    /*------------------------------------------------------------------------*/

    protected void insureIndexSpace(int requiredNumRecords) throws IOException, RecordsFileException {
        int currentNumRecords = getNumRecords();

        long endIndexPointer = indexPositionToKeyFp(requiredNumRecords);
        if (endIndexPointer > getFileLength() && currentNumRecords == 0) {
            // Se não há nenhum registro e precisar expandir o arquivo, só fazer.
            setFileLength(endIndexPointer);
            dataStartPointer = endIndexPointer;
            writeDataStartPointerHeader(dataStartPointer);
            return;
        }

        // Vai jogando o primeiro registro pro final até dar espaço suficiente
        // nos headers.
        while (endIndexPointer > dataStartPointer) {
            RecordHeader first = getRecordAt(dataStartPointer);
            byte[] data = readRecordData(first);

            first.dataPointer = getFileLength();
            first.dataCapacity = data.length;

            setFileLength(first.dataPointer + data.length);
            writeRecordData(first, data);
            writeRecordHeaderToIndex(first);

            dataStartPointer += first.dataCapacity;
            writeDataStartPointerHeader(dataStartPointer);
        }
    }

    /*------------------------------------------------------------------------*/

    /** Retorna uma enumeração das chaves de todos os registros do arquivo.
     * @return chaves de todos os registros do arquivo
     */
    public abstract Enumeration enumerateKeys();

    /*------------------------------------------------------------------------*/

    /** Retorna o número de registros do arquivo.
     * @return número de registros do arquivo
     */
    public abstract int getNumRecords();

    /*------------------------------------------------------------------------*/

    /** Determina se há um registro com a chave especificada.
     * @param key chave
     * @return true se e somente se o registro existe no arquivo
     */
    public abstract boolean recordExists(String key);

    /*------------------------------------------------------------------------*/

    /** Insere um novo registro no arquivo.
     * @param rw writer
     * @throws IOException caso não seja possível fazer seek no arquivo
     */
    public synchronized void insertRecord(RecordWriter rw) throws IOException, RecordsFileException {
        String key = rw.getKey();
        if (recordExists(key)) {
            throw new RecordsFileException("A chave já existe: " + key);
        }

        insureIndexSpace(getNumRecords() + 1);
        RecordHeader newRecord = allocateRecord(key, rw.getDataLength());

        writeRecordData(newRecord, rw);
        addEntryToIndex(key, newRecord, getNumRecords());
    }

    /** Lê um registro do arquivo.
     * @param key chave do registro
     * @return registro
     * @throws IOException caso não seja possível fazer seek no arquivo
     * @throws RecordsFileException caso o registro não exista
     */
    public synchronized RecordReader readRecord(String key) throws IOException, RecordsFileException {
        byte[] data = readRecordData(key);
        return new RecordReader(key, data);
    }

    /** Atualiza o valor de um registro no arquivo.
     * @param rw
     * @throws IOException
     */
    public synchronized void updateRecord(RecordWriter rw) throws IOException, RecordsFileException {
        RecordHeader header = keyToRecordHeader(rw.getKey());

        if (rw.getDataLength() > header.dataCapacity) {
            deleteRecord(rw.getKey());
            insertRecord(rw);
        }
        else {
            writeRecordData(header, rw);
            writeRecordHeaderToIndex(header);
        }
    }

    /** Remove um registro do arquivo.
     * @param key chave do registro
     * @throws IOException caso não seja possível fazer seek no arquivo
     * @throws RecordsFileException
     */
    public synchronized void deleteRecord(String key) throws IOException, RecordsFileException {
        RecordHeader recordToDelete = keyToRecordHeader(key);

        int currentNumRecords = getNumRecords();
        if (getFileLength() == recordToDelete.dataPointer + recordToDelete.dataCapacity) {
            // Estamos removendo o último registro. Reduzir o tamanho do arquivo.
            setFileLength(recordToDelete.dataPointer);
        }
        else {
            RecordHeader previous = getRecordAt(recordToDelete.dataPointer - 1);

            if (previous != null) {
                // O registro anterior "ganha" o espaço do registro removido.
                previous.dataCapacity += recordToDelete.dataCapacity;
                writeRecordHeaderToIndex(previous);
            }
            else {
                // Estamos removendo o primeiro registro.
                // O registro posterior "ganha" o espaço do registro removido.
                RecordHeader secondRecord = getRecordAt(recordToDelete.dataPointer + (long) recordToDelete.dataCapacity);

                byte[] data = readRecordData(secondRecord);

                secondRecord.dataPointer = recordToDelete.dataPointer;
                secondRecord.dataCapacity += recordToDelete.dataCapacity;

                writeRecordData(secondRecord, data);
                writeRecordHeaderToIndex(secondRecord);
            }
        }

        deleteEntryFromIndex(key, recordToDelete, currentNumRecords);
    }

    /*------------------------------------------------------------------------*/

    /** Fecha o arquivo.
     * @throws IOException caso não seja possível fechar a RandomAccessFile
     * @throws RecordsFileException
     */
    public synchronized void close() throws IOException, RecordsFileException {
        try {
            file.close();
        }
        finally {
            file = null;
        }
    }

}
