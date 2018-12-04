package br.com.tbdc;

import br.com.tbdc.coordenador.ServidorCoordenador;
import br.com.tbdc.data.DataStorage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    /** Porta do serviço de nomes */
    private static final int REGISTRY_PORT = 11037;

    public static void main(String[] args) {
        // testeDataStorage();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.exit(0)));

        try {
            // Cria o serviço de nomes
            Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);

            ServidorCoordenador servidor = new ServidorCoordenador(registry);
            registry.rebind("servidor_coordenador", servidor);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void testeDataStorage() {
        DataStorage d = new DataStorage("teste.txt");
        d.printData();

        d.writeData("teste1\n", true);
        d.writeData("teste2\n");

        d.printData();

        d.writeData("teste3\n");
        d.writeData("teste4\n");

        d.printData();
    }

}
