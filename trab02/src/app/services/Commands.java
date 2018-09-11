package app.services;

import app.message.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Commands {

    public String allowHash;
    public String denyHash;
    public String leaveHash;

    /*------------------------------------------------------------------------*/

    private static Commands ourInstance = new Commands();

    public static Commands getInstance() {
        return ourInstance;
    }

    private Commands() {
        allowHash = "";
        denyHash = "";
        leaveHash = "";
    }

    /*------------------------------------------------------------------------*/

    public void init() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update("allow".getBytes());
        allowHash = Message.bytesToHexString(md.digest());

        md.update("deny".getBytes());
        denyHash = Message.bytesToHexString(md.digest());

        md.update("leave".getBytes());
        leaveHash = Message.bytesToHexString(md.digest());
    }

    /*------------------------------------------------------------------------*/
}
