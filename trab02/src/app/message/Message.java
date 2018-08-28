package app.message;

public abstract class Message {
    protected byte[] msg;

    private final static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public byte[] getBytes() {
        return msg;
    }

    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[2 * bytes.length];

        for (int i = 0; i < bytes.length; ++i) {
            int v = bytes[i] & 0xFF;
            hexChars[2*i]     = HEX_ARRAY[v >>> 4];
            hexChars[2*i + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] hexStringToBytes(String str){
        int len = str.length();
        byte[] bytes = new byte[len/2];

        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) (
                    (Character.digit(str.charAt(i), 16) << 4)
                    + Character.digit(str.charAt(i+1), 16)
            );
        }

        return bytes;
    }
}
