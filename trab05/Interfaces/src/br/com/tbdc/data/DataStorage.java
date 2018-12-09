package br.com.tbdc.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Function;

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

    public synchronized void readData(Function<String, Boolean> parser) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String text = null;
            while ((text = reader.readLine()) != null) {
                if (!parser.apply(text)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean writeData(String data) {
        return this.writeData(data, false);
    }

    public synchronized boolean writeData(String data, boolean reset) {
        if (file.canWrite()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, !reset))) {
                writer.write(data);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
