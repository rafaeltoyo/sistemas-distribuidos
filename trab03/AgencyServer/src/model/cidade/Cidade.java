package model.cidade;

public enum Cidade {
    CURITIBA("Curitiba"),
    SAO_PAULO("São Paulo")
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
