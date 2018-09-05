package app.message;

import app.agent.MulticastPeer;
import app.agent.Peer;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Message {

    protected String type;
    protected MulticastPeer sender;
    protected Peer receiver;
    protected String signature;
    protected long timestamp;

    private final static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /*------------------------------------------------------------------------*/

    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[2 * bytes.length];

        for (int i = 0; i < bytes.length; ++i) {
            int v = bytes[i] & 0xFF;
            hexChars[2 * i] = HEX_ARRAY[v >>> 4];
            hexChars[2 * i + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] hexStringToBytes(String str) {
        int len = str.length();
        byte[] bytes = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) (
                    (Character.digit(str.charAt(i), 16) << 4)
                            + Character.digit(str.charAt(i + 1), 16)
            );
        }

        return bytes;
    }

    /*------------------------------------------------------------------------*/

    public Message(String type, MulticastPeer sender) {
        this.type = type;
        this.sender = sender;
        this.timestamp = System.currentTimeMillis();
    }

    public JSONObject getJSON() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Criar o JSON
        JSONObject jsonMsg = new JSONObject();

        //  Adicionar tipo da mensagem
        jsonMsg.put("type", type);

        // Adicionar informações do usuário
        jsonMsg.put("sid", sender.getId());
        jsonMsg.put("publickey", bytesToHexString(sender.getPublicKey().getEncoded()));

        // Adicionar informações do destinatário na mensagem
        if (receiver != null) {
            jsonMsg.put("rid", receiver.getId());
        }
        // Adicionar informações de "assinatura" na mensagem
        if (signature != null && !signature.isEmpty()) {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, sender.getPrivateKey());

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signature.getBytes());

            jsonMsg.put("auth", bytesToHexString(cipher.doFinal(md.digest())));
        }

        jsonMsg.put("timestamp", timestamp);

        return jsonMsg;
    }

    public void setReceiver(Peer receiver) {
        this.receiver = receiver;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public byte[] getBytes() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return getJSON().toString().getBytes();
    }

    /*------------------------------------------------------------------------*/

    public long getTimestamp() {
        return timestamp;
    }

    /*------------------------------------------------------------------------*/
}
