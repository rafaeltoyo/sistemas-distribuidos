package hamner.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DbByteArrayOutputStream extends ByteArrayOutputStream {
    public DbByteArrayOutputStream() {
        super();
    }

    public DbByteArrayOutputStream(int size) {
        super(size);
    }

    @Override
    public synchronized void writeTo(OutputStream dstr) throws IOException {
        byte[] data = super.buf;
        int length = super.size();
        dstr.write(data, 0, length);
    }
}
