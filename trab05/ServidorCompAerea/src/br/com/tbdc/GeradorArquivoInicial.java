package br.com.tbdc;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.saldo.Dinheiro;
import br.com.tbdc.model.voo.Voo;
import hamner.db.RecordWriter;
import hamner.db.RecordsFile;
import hamner.db.RecordsFileException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Este módulo apenas serve para gerar um arquivo com vários voos.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class GeradorArquivoInicial {
    /** Caminho do arquivo a gerar (pode ser caminho relativo) */
    private static final String FILENAME = "voo.jdb";

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

    /** Gera um grupo de voos "hardcoded" e escreve no arquivo.
     * @throws IOException caso o método adicionarVoo jogue essa exceção
     * @throws RecordsFileException caso o método adicionarVoo jogue essa exceção
     */
    private static void popularServidor() throws IOException, RecordsFileException {
        adicionarVoo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.of(2018, 12, 16), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.of(2018, 12, 16), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.of(2018, 12, 17), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.of(2018, 12, 17), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.of(2018, 12, 18), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.of(2018, 12, 18), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.of(2018, 12, 19), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.of(2018, 12, 19), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.of(2018, 12, 20), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.of(2018, 12, 20), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.of(2018, 12, 16), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.of(2018, 12, 16), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.of(2018, 12, 17), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.of(2018, 12, 17), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.of(2018, 12, 18), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.of(2018, 12, 18), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.of(2018, 12, 19), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.of(2018, 12, 19), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.of(2018, 12, 20), 100, Dinheiro.reais(new BigDecimal(100.00)));
        adicionarVoo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.of(2018, 12, 20), 100, Dinheiro.reais(new BigDecimal(100.00)));
    }

    /** Cria um novo voo com os parâmetros especificados e escreve no arquivo.
     * @param origem cidade de origem
     * @param destino cidade de destino
     * @param data data do voo
     * @param poltronasTotal número de poltronas total do voo
     * @param precoPassagem preço da passagem
     * @throws IOException caso ocorra um erro de I/O
     * @throws RecordsFileException caso já exista algum voo com o mesmo ID no arquivo
     */
    private static void adicionarVoo(Cidade origem, Cidade destino, LocalDate data, int poltronasTotal, Dinheiro precoPassagem) throws IOException, RecordsFileException {
        Voo v = new Voo(origem, destino, data, poltronasTotal, precoPassagem);

        RecordWriter rw = new RecordWriter(v.getId() + "");
        rw.writeObject(v);
        recordsFile.insertRecord(rw);
    }
}
