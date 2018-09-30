package model.cidade;

public enum Cidade {
    PORTO_ALEGRE("Porto Alegre"),
    FLORIANOPOLIS("Florianópolis"),
    CURITIBA("Curitiba"),
    LONDRINA("Londrina"),
    FOZ_DE_IGUACU("Foz de Iguaçu"),
    SAO_PAULO("São Paulo"),
    RIO_DE_JANEIRO("Rio de Janeiro"),
    BELO_HORIZONTE("Belo Horizonte")
    ;

    private final String nome;

    Cidade(final String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
