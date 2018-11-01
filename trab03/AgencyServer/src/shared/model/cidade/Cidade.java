package shared.model.cidade;

/** Representa uma cidade.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public enum Cidade {
    /** Porto Alegre - RS */
    PORTO_ALEGRE("Porto Alegre"),

    /** Florianópolis - SC */
    FLORIANOPOLIS("Florianópolis"),

    /** Curitiba - PR */
    CURITIBA("Curitiba"),

    /** Londrina - PR */
    LONDRINA("Londrina"),

    /** Foz do Iguaçu - PR */
    FOZ_DO_IGUACU("Foz do Iguaçu"),

    /** São Paulo - SP */
    SAO_PAULO("São Paulo"),

    /** Rio de Janeiro - RJ */
    RIO_DE_JANEIRO("Rio de Janeiro"),

    /** Belo Horizonte - MG */
    BELO_HORIZONTE("Belo Horizonte")
    ;

    /*------------------------------------------------------------------------*/

    /** Nome da cidade */
    private final String nome;

    /*------------------------------------------------------------------------*/

    /** Construtor para armazenar uma String com o nome da cidade.
     * @param nome nome da cidade
     */
    Cidade(final String nome) {
        this.nome = nome;
    }

    /*------------------------------------------------------------------------*/

    /** Sobrecarga do método toString para retornar a string fornecida no
     * construtor.
     * @return nome da cidade
     */
    @Override
    public String toString() {
        return this.nome;
    }
}
