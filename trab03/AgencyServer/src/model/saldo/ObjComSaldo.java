package model.saldo;

abstract public class ObjComSaldo {

    Saldo saldo;

    public ObjComSaldo(int numPessoas) {
        this.saldo = new Saldo(numPessoas);
    }

    public Reserva reservar(int numPessoas) {
        synchronized (saldo) {
            return saldo.pegarSaldo(numPessoas);
        }
    }

    public boolean estornar(Reserva reserva) {
        synchronized (saldo) {
            return saldo.estornar(reserva);
        }
    }
}
