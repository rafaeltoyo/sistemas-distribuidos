package br.com.tbdc.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataStorage {

    private final static String DEFAULT_DIR = '.' + File.separator + "storage";

    private File file;

    public DataStorage(String filename) {
        this(filename, DEFAULT_DIR);
    }

    public DataStorage(String filename, String path) {
        file = new File(path + File.separator + filename);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException ignored) {
        }
    }

    public synchronized void printData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String text = null;
            System.out.println("File: " + file.getAbsolutePath());
            while ((text = reader.readLine()) != null) {
                System.out.println(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeData(String data) {
        this.writeData(data, false);
    }

    public synchronized void writeData(String data, boolean reset) {
        if (file.canWrite()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, !reset))) {
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
