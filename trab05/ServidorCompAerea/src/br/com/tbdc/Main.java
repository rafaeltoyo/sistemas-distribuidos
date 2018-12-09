package br.com.tbdc;

import br.com.tbdc.controller.ControladorVoo;
import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.voo.Voo;
import br.com.tbdc.server.ServidorCompAerea;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;

public class Main {
    /** Porta do serviço de nomes */
    private static final int REGISTRY_PORT = 11037;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.exit(0)));

        try {
            Registry registry = LocateRegistry.getRegistry(REGISTRY_PORT);

            ServidorCompAerea servidor = new ServidorCompAerea();
            registry.rebind("servidor_comp_aerea", servidor);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
