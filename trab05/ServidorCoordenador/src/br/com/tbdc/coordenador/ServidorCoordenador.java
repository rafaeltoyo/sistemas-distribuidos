package br.com.tbdc.coordenador;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.hotel.InfoHotelRet;
import br.com.tbdc.model.pacote.ConjuntoPacote;
import br.com.tbdc.model.voo.InfoVoo;
import br.com.tbdc.model.voo.TipoPassagem;
import br.com.tbdc.rmi.InterfaceCoordenador;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ServidorCoordenador implements InterfaceCoordenador {

    @Override
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo, Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numPessoas) throws RemoteException {
        return null;
    }

    @Override
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) throws RemoteException {
        return false;
    }

    @Override
    public ArrayList<InfoHotelRet> consultarHospedagens(Cidade local, LocalDate dataIni, LocalDate dataFim, int numQuartos, int numPessoas) throws RemoteException {
        return null;
    }

    @Override
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos) throws RemoteException {
        return false;
    }

    @Override
    public ConjuntoPacote consultarPacotes(Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numQuartos, int numPessoas) throws RemoteException {
        return null;
    }

    @Override
    public boolean comprarPacote(int idVooIda, int idVooVolta, int idHotel, LocalDate dataIda, LocalDate dataVolta, int numQuartos, int numPessoas) throws RemoteException {
        return false;
    }
}
