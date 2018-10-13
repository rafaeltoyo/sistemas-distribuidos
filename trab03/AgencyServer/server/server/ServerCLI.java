package server;

import model.cidade.Cidade;
import model.hotel.Hotel;
import model.voo.Voo;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Representa a interface de linha de comando do servidor.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ServerCLI {
    /** Porta do serviço de nomes */
    private static final int NAMING_SERVICE_PORT = 11037;

    /** Instância da classe servidor */
    private static AgencyServerImpl agencyServerImpl;

    /*------------------------------------------------------------------------*/

    /** Inicializa o serviço de nomes e a aplicação servidor.
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        try {
            Registry namingServiceRef = LocateRegistry.createRegistry(
                    NAMING_SERVICE_PORT);

            agencyServerImpl = new AgencyServerImpl();
            namingServiceRef.bind("server", agencyServerImpl);

            System.out.println("/*---------------------*/\n" +
                    "/* Servidor da agência */\n" +
                    "/*---------------------*/\n");
            mostrarMenu();

            Scanner scanner = new Scanner(System.in);

            boolean rodando = true;
            while (rodando) {
                System.out.print(">>> ");
                String comando = scanner.nextLine();
                if (comando.startsWith("-add")) {
                    processarComando(comando);
                }
                else if (comando.startsWith("-quit")) {
                    rodando = false;
                    System.out.println("Finalizando...");
                }
                else if (comando.startsWith("-help")) {
                    mostrarMenu();
                }
                else {
                    System.out.println("Comando inválido.");
                }
            }
        }
        catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.exit(0);
    }

    /*------------------------------------------------------------------------*/

    /** Mostra o menu da interface de linha de comando. */
    private static void mostrarMenu() {
        System.out.println("Comandos:\n" +
                "  -add voo <origem> <destino> <data> <num_poltronas>\n" +
                "    Adiciona um novo voo ao sistema.\n" +
                "    <origem>: cidade de origem, entre aspas\n" +
                "    <destino>: cidade de destino, entre aspas\n" +
                "    <data>: data do voo, em formato AAAA-MM-DD\n" +
                "    <num_poltronas>: número de poltronas do avião\n" +
                "  -add hotel <nome> <cidade> <num_quartos>\n" +
                "    Adiciona um novo hotel ao sistema.\n" +
                "    <nome>: nome do hotel, entre aspas\n" +
                "    <cidade>: cidade do hotel, entre aspas\n" +
                "    <num_quartos>: número de quartos do hotel\n" +
                "  -add hospedagem <id_hotel> <data_ini> <data_fim>\n" +
                "    Adiciona hospedagem a um hotel existente em um range de datas.\n" +
                "    <id_hotel>: identificador numérico do hotel\n" +
                "    <data_ini>: primeira data do intervalo, em formato AAAA-MM-DD\n" +
                "    <data_fim>: última data do intervalo, em formato AAAA-MM-DD\n" +
                "  -quit\n" +
                "    Finaliza o servidor.\n" +
                "  -help\n" +
                "    Mostra esta lista de comandos novamente.\n");
    }

    /*------------------------------------------------------------------------*/

    /** Processa um comando que inicia com -add
     * @param comando string que contém o comando
     */
    private static void processarComando(String comando) {
        // Separa a string em tokens usando regex
        ArrayList<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(comando);
        while (m.find()) {
            tokens.add(m.group(1).replace("\"", ""));
        }

        // Chama a função adequada
        boolean deuboa = false;
        if (tokens.size() == 6 && tokens.get(1).equals("voo")) {
            deuboa = adicionarVoo(tokens.get(2), tokens.get(3), tokens.get(4), tokens.get(5));
        }
        else if (tokens.size() == 5 && tokens.get(1).equals("hotel")) {
            deuboa = adicionarHotel(tokens.get(2), tokens.get(3), tokens.get(4));
        }
        else if (tokens.size() == 5 && tokens.get(1).equals("hospedagem")) {
            deuboa = adicionarHospedagem(tokens.get(2), tokens.get(3), tokens.get(4));
        }
        else {
            System.out.println("Comando ou argumentos inválidos.");
            return;
        }

        if (!deuboa) {
            System.out.println("Falha ao executar o comando. Verifique os argumentos.");
        }
    }

    /*------------------------------------------------------------------------*/

    /** Processa um comando -add voo.
     * Cria um novo voo e adiciona-o à lista de voos do servidor.
     * @param origemStr string da cidade de origem
     * @param destinoStr string da cidade de destino
     * @param dataStr string da data do voo em formato AAAA-MM-DD
     * @param numPoltronasStr string do número de poltronas do avião
     * @return true se e somente se o voo foi adicionado com sucesso
     */
    private static boolean adicionarVoo(String origemStr, String destinoStr, String dataStr, String numPoltronasStr) {
        Cidade origem = null;
        Cidade destino = null;

        // Converte os campos <origem> e <destino> para Cidade
        for (Cidade c : Cidade.values()) {
            if (c.toString().equals(origemStr)) {
                origem = c;
            }
            else if (c.toString().equals(destinoStr)) {
                destino = c;
            }
            if (origem != null && destino != null) {
                break;
            }
        }
        if (origem == null || destino == null) {
            return false;
        }

        // Converte o campo <data> para LocalDate
        LocalDate data;
        try {
            data = LocalDate.parse(dataStr);
        }
        catch (DateTimeParseException e) {
            return false;
        }

        // Converte o campo <num_poltronas> para int
        int numPoltronas;
        try {
            numPoltronas = Integer.parseInt(numPoltronasStr);
        }
        catch (NumberFormatException e) {
            return false;
        }

        // Agora deu boa
        Voo voo = new Voo(origem, destino, data, numPoltronas);
        agencyServerImpl.adicionarVoo(voo);

        System.out.println("Voo adicionado:\n" +
                "  Id: " + voo.getId() + "\n" +
                "  Origem: " + voo.getOrigem() + "\n" +
                "  Destino: " + voo.getDestino() + "\n" +
                "  Data: " + voo.getData() + "\n" +
                "  Número de poltronas: " + voo.getPoltronasDisp() + "\n");

        return true;
    }

    /*------------------------------------------------------------------------*/

    /** Processa um comando -add hotel.
     * Cria um novo hotel e adiciona-o à lista de hotéis do servidor.
     * @param nomeStr string do nome do hotel
     * @param cidadeStr string da cidade do hotel
     * @param numQuartosStr string do número de quartos
     * @return true se e somente se o hotel foi adicionado com sucesso
     */
    private static boolean adicionarHotel(String nomeStr, String cidadeStr, String numQuartosStr) {
        if (nomeStr == null) {
            return false;
        }

        // Converte o campo <cidade> para Cidade
        Cidade cidade = null;
        for (Cidade c : Cidade.values()) {
            if (c.toString().equals(cidadeStr)) {
                cidade = c;
                break;
            }
        }
        if (cidade == null) {
            return false;
        }

        // Converte o campo <num_quartos> para int
        int numQuartos;
        try {
            numQuartos = Integer.parseInt(numQuartosStr);
        }
        catch (NumberFormatException e) {
            return false;
        }

        // Agora deu boa
        Hotel hotel = new Hotel(nomeStr, cidade, numQuartos);
        agencyServerImpl.adicionarHotel(hotel);

        System.out.println("Hotel adicionado:\n" +
                "  Id: " + hotel.getId() + "\n" +
                "  Nome: " + hotel.getNome() + "\n" +
                "  Número de quartos: " + hotel.getInfoHotel().getNumQuartos() + "\n" +
                "Obs: adicione hospedagens ao hotel usando o comando -add hospedagem\n");

        return true;
    }

    /*------------------------------------------------------------------------*/

    /** Processa um comando -add hospedagem.
     * Cria novas hospedagens para um hotel existente no período informado.
     * Atenção: sobreescreve hospedagens que porventura já existam dentro do período.
     * @param idHotelStr string do identificador numérico do hotel
     * @param dataIniStr string da data de início do período em formato AAAA-MM-DD
     * @param dataFimStr string da data de fim do período em formato AAAA-MM-DD
     * @return true se e somente se as hospedagens foram adicionadas com sucesso
     */
    private static boolean adicionarHospedagem(String idHotelStr, String dataIniStr, String dataFimStr) {
        // Converte o campo <id_hotel> para int
        int idHotel;
        try {
            idHotel = Integer.parseInt(idHotelStr);
        }
        catch (NumberFormatException e) {
            return false;
        }

        // Converte o campo <data_ini> para LocalDate
        LocalDate dataIni;
        try {
            dataIni = LocalDate.parse(dataIniStr);
        }
        catch (DateTimeParseException e) {
            return false;
        }

        // Converte o campo <data_ini> para LocalDate
        LocalDate dataFim;
        try {
            dataFim = LocalDate.parse(dataFimStr);
        }
        catch (DateTimeParseException e) {
            return false;
        }

        // Agora deu boa
        agencyServerImpl.adicionarHospedagem(idHotel, dataIni, dataFim);

        System.out.println("Hospedagens adicionadas ao Hotel " + idHotelStr +
                " entre as datas " + dataIniStr + " e " + dataFimStr);

        return true;
    }
}
