package br.com.tbdc;

import br.com.tbdc.data.DataStorage;

public class Main {

    public static void main(String[] args) {

        DataStorage d = new DataStorage("teste.txt");
        d.printData();

        d.writeData("teste1\n", true);
        d.writeData("teste2\n");

        d.printData();

        d.writeData("teste3\n");
        d.writeData("teste4\n");

        d.printData();

        System.exit(0);
    }

}
