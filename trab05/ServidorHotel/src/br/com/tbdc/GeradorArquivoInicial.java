package br.com.tbdc;

import br.com.tbdc.model.Hotel;
import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.saldo.Dinheiro;
import hamner.db.RecordWriter;
import hamner.db.RecordsFile;
import hamner.db.RecordsFileException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Este módulo apenas serve para gerar um arquivo com vários hotéis.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class GeradorArquivoInicial {
    /** Caminho do arquivo a gerar (pode ser caminho relativo) */
    private static final String FILENAME = "hotel.jdb";

    /** Tamanho inicial do índice a alocar */
    private static final int INITIAL_SIZE = 64;

    /** Instância do arquivo */
    private static RecordsFile recordsFile;

    /** Cria um novo arquivo com os parâmetros especificados na classe.
     * Caso o arquivo já exista, não substitui o arquivo existente.
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            recordsFile = new RecordsFile(FILENAME, INITIAL_SIZE);
            popularServidor();
            recordsFile.close();
        }
        catch (RecordsFileException | IOException e) {
            e.printStackTrace();
        }
    }

    /** Gera um grupo de hotéis "hardcoded" e escreve no arquivo.
     * @throws IOException caso o método adicionarHotel jogue essa exceção
     * @throws RecordsFileException caso o método adicionarHotel jogue essa exceção
     */
    private static void popularServidor() throws IOException, RecordsFileException {
        adicionarHotel("Hotel Peru", Cidade.FLORIANOPOLIS, 20, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Pato", Cidade.FLORIANOPOLIS, 30, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Galinha", Cidade.FLORIANOPOLIS, 15, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Weeb", Cidade.CURITIBA, 20, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Noob", Cidade.CURITIBA, 30, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Bispo", Cidade.CURITIBA, 15, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Pereira", Cidade.SAO_PAULO, 20, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Madoka", Cidade.SAO_PAULO, 30, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
        adicionarHotel("Hotel Homura", Cidade.SAO_PAULO, 15, 100.00, LocalDate.of(2018, 12, 16), LocalDate.of(2018, 12, 20));
    }

    /** Cria um novo hotel com os parâmetros especificados e escreve no arquivo.
     * @param nome nome do hotel
     * @param local cidade do hotel
     * @param numQuartos número total de quartos do hotel
     * @param precoDiaria preço da diária do hotel
     * @param dataIni data de início (para instanciar hospedagens)
     * @param dataFim data de fim (para instanciar hospedagens)
     * @throws IOException caso ocorra um erro de I/O
     * @throws RecordsFileException caso já exista algum hotel com o mesmo ID no arquivo
     */
    private static void adicionarHotel(String nome, Cidade local, int numQuartos, double precoDiaria, LocalDate dataIni, LocalDate dataFim) throws IOException, RecordsFileException {
        Hotel h = new Hotel(nome, local, numQuartos, Dinheiro.reais(new BigDecimal(precoDiaria)));
        h.adicionarHospedagem(dataIni, dataFim);

        RecordWriter rw = new RecordWriter(h.getId() + "");
        rw.writeObject(h);
        recordsFile.insertRecord(rw);
    }
}
