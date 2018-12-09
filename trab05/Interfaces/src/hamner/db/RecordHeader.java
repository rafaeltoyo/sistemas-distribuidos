package hamner.db;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class RecordHeader {
    protected long dataPointer;
    protected int dataCount;
    protected int dataCapacity;
    protected int indexPosition;

    protected int getIndexPosition() {
        return indexPosition;
    }

    protected int getDataCapacity() {
        return dataCapacity;
    }

    protected int getFreeSpace() {
        return dataCapacity - dataCount;
    }

    protected void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }

    protected RecordHeader() {
    }

    protected RecordHeader(long dataPointer, int dataCapacity) {
        if (dataCapacity < 1) {
            throw new IllegalArgumentException("Tamanho incorreto: " + dataCapacity);
        }

        this.dataPointer = dataPointer;
        this.dataCapacity = dataCapacity;
        this.dataCount = 0;
    }

    protected static RecordHeader readHeader(DataInput in) throws IOException {
        RecordHeader r = new RecordHeader();
        r.read(in);
        return r;
    }

    protected void read(DataInput in) throws IOException {
        dataPointer = in.readLong();
        dataCapacity = in.readInt();
        dataCount = in.readInt();
    }

    protected void write(DataOutput out) throws IOException {
        out.writeLong(dataPointer);
        out.writeInt(dataCapacity);
        out.writeInt(dataCount);
    }

    protected RecordHeader split() throws RecordsFileException {
        long newFp = dataPointer + (long) dataCount;
        RecordHeader newRecord = new RecordHeader(newFp, getFreeSpace());
        dataCapacity = dataCount;
        return newRecord;
    }
}
