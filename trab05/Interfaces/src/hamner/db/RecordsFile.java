package hamner.db;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class RecordsFile extends BaseRecordsFile {
    /** Índice em memória */
    protected Hashtable memIndex;

    /*------------------------------------------------------------------------*/

    /** Cria um novo arquivo.
     * @param filename nome de arquivo
     * @param initialSize espaço inicial alocado para o índice
     * @throws IOException
     * @throws RecordsFileException
     */
    public RecordsFile(String filename, int initialSize) throws IOException, RecordsFileException {
        super(filename, initialSize);
        memIndex = new Hashtable(initialSize);
    }

    /** Abre um arquivo existente e inicializa o índice em memória.
     * @param filename nome do arquivo
     * @param accessFlags "r" ou "rw", como definido na RandomAccessFile
     * @throws IOException
     * @throws RecordsFileException
     */
    public RecordsFile(String filename, String accessFlags) throws IOException, RecordsFileException {
        super(filename, accessFlags);

        int numRecords = readNumRecordsHeader();
        memIndex = new Hashtable(numRecords);

        for (int i = 0; i < numRecords; ++i) {
            String key = readKeyFromIndex(i);
            RecordHeader header = readRecordHeaderFromIndex(i);
            header.setIndexPosition(i);
            memIndex.put(key, header);
        }
    }

    /*------------------------------------------------------------------------*/

    @Override
    public Enumeration enumerateKeys() {
        return memIndex.keys();
    }

    @Override
    public int getNumRecords() {
        return memIndex.size();
    }

    @Override
    public synchronized boolean recordExists(String key) {
        return memIndex.containsKey(key);
    }

    /*------------------------------------------------------------------------*/

    @Override
    protected RecordHeader keyToRecordHeader(String key) throws RecordsFileException {
        RecordHeader header = (RecordHeader) memIndex.get(key);
        if (header == null) {
            throw new RecordsFileException("Chave não encontrada: " + key);
        }

        return header;
    }

    /*------------------------------------------------------------------------*/

    @Override
    protected RecordHeader allocateRecord(String key, int dataLength) throws IOException, RecordsFileException {
        RecordHeader newRecord = null;

        // Varre o arquivo em busca de espaço livre. Caso ache, aloca lá.
        Enumeration e = memIndex.elements();
        while (e.hasMoreElements()) {
            RecordHeader next = (RecordHeader) e.nextElement();

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

    @Override
    protected void addEntryToIndex(String key, RecordHeader newRecord, int currentNumRecords) throws IOException, RecordsFileException {
        super.addEntryToIndex(key, newRecord, currentNumRecords);
        memIndex.put(key, newRecord);
    }

    @Override
    protected void deleteEntryFromIndex(String key, RecordHeader header, int currentNumRecords) throws IOException, RecordsFileException {
        super.deleteEntryFromIndex(key, header, currentNumRecords);
        RecordHeader deleted = (RecordHeader) memIndex.remove(key);
    }

    /*------------------------------------------------------------------------*/

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
