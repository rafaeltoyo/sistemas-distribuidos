package hamner.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** Classe utilitária que estende ByteArrayOutputStream para escrever o buffer
 * sem realocá-lo.
 * @author Derek Hamner
 */
public class DbByteArrayOutputStream extends ByteArrayOutputStream {
    /** Construtor padrão. O buffer inicial é de 32 bytes, mas pode aumentar se
     * necessário. */
    public DbByteArrayOutputStream() {
        super();
    }

    /** Construtor com especificação de tamanho de buffer inicial.
     * @param size tamanho inicial do buffer */
    public DbByteArrayOutputStream(int size) {
        super(size);
    }

    /*------------------------------------------------------------------------*/

    /** Escreve o conteúdo completo desse objeto para o OutputStream passado
     * como parâmetro.
     * @param dstr output stream
     * @throws IOException caso ocorra um erro de I/O
     */
    @Override
    public synchronized void writeTo(OutputStream dstr) throws IOException {
        byte[] data = super.buf;
        int length = super.size();
        dstr.write(data, 0, length);
    }
}
