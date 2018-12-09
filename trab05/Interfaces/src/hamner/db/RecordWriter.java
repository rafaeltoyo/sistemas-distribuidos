package hamner.db;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class RecordWriter {
    protected String key;
    protected DbByteArrayOutputStream out;
    protected ObjectOutputStream objOut;

    public String getKey() {
        return key;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public ObjectOutputStream getObjectOutputStream() throws IOException {
        if (objOut == null) {
            objOut = new ObjectOutputStream(out);
        }
        return objOut;
    }

    public int getDataLength() {
        return out.size();
    }

    public RecordWriter(String key) {
        this.key = key;
        out = new DbByteArrayOutputStream();
    }

    public void writeObject(Object o) throws IOException {
        getObjectOutputStream().writeObject(o);
        getObjectOutputStream().flush();
    }

    public void writeTo(OutputStream str) throws IOException {
        out.writeTo(str);
    }
}
