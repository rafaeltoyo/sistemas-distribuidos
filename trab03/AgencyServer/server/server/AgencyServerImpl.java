package server;

import model.cidade.Cidade;
import model.evento.Evento;
import model.evento.Interesse;
import model.evento.InteresseHotel;
import model.evento.InteresseVoo;
import model.evento.ListaInteresseHotel;
import model.evento.ListaInteresseVoo;
import model.hotel.Hospedagem;
import model.hotel.Hotel;
import model.hotel.InfoHotelRet;
import model.mensagem.Mensagem;
import model.pacote.ConjuntoPacote;
import model.saldo.Reserva;
import model.voo.InfoVoo;
import model.voo.TipoPassagem;
import model.voo.Voo;
import remote.AgencyClient;
import remote.AgencyServer;
import server.controller.EventoController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Representa o servidor da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyServerImpl extends UnicastRemoteObject
        implements AgencyServer {
    /** Lista de voos disponíveis */
    private ArrayList<Voo> voos = new ArrayList<>();

    /** Lista de hotéis cadastrados */
    private ArrayList<Hotel> hoteis = new ArrayList<>();

    /** Banco de registros de interesse em voos */
    private ListaInteresseVoo interessesVoo = new ListaInteresseVoo();

    /** Banco de registros de interesse em hotéis */
    private ListaInteresseHotel interessesHotel = new ListaInteresseHotel();

    /*------------------------------------------------------------------------*/

    /** Construtor único.
     * @throws RemoteException caso ocorra erro no RMI
     */
    public AgencyServerImpl() throws RemoteException {
        super();
    }

    /*------------------------------------------------------------------------*/

    /** Verifica se existe hotel cadastrado em uma cidade
     * @param cidade cidade
     * @return true se e somente se existe hotel cadastrado na cidade
     */
    private boolean existeHotel(Cidade cidade) {
        for (Hotel h : hoteis) {
            if (h.getLocal() == cidade) {
                return true;
            }
        }
        return false;
    }

    /** Verifica se existe voo partindo de uma cidade origem para uma cidade
     * destino, no máximo até a data especificada.
     * @param origem cidade de origem do voo
     * @param destino cidade de destino do voo
     * @param data data máxima para a verificação
     * @return true se e somente se há algum voo que atende às especificações
     */
    private boolean existeVooAteData(Cidade origem, Cidade destino, LocalDate data) {
        for (Voo voo : voos) {
            if (voo.getOrigem() == origem && voo.getDestino() == destino && !voo.getData().isAfter(data)) {
                return true;
            }
        }
        return false;
    }

    /** Verifica se existe voo partindo de uma cidade origem para uma cidade
     * destino, a partir de uma data especificada.
     * @param origem cidade de origem do voo
     * @param destino cidade de destino do voo
     * @param data data mínima para a verificação
     * @return true se e somente se há algum voo que atende às especificações
     */
    private boolean existeVooAPartirDe(Cidade origem, Cidade destino, LocalDate data) {
        for (Voo voo : voos) {
            if (voo.getOrigem() == origem && voo.getDestino() == destino && !voo.getData().isBefore(data)) {
                return true;
            }
        }
        return false;
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um voo à lista de voos do servidor, busca registros de
     * interesse que são atendidos por esse voo e envia notificação aos
     * respectivos clientes.
     * @param voo voo já instanciado e inicializado
     */
    public void adicionarVoo(Voo voo) {
        voos.add(voo);
        notificarInteresseVoo(voo);
        notificarInteressePacote(voo);
    }

    private void notificarInteresseVoo(Voo v) {
        List<Evento> evList = EventoController.getInstance().consultar(v);
        ArrayList<Evento> evARemover = new ArrayList<>();
        if (evList != null) {
            for (Evento ev : evList) {
                String notif = "Um voo foi encontrado de " + v.getOrigem() + " para " + v.getDestino() + "!";

                try {
                    ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                }
                catch (RemoteException e) {
                    // O cliente provavelmente não existe mais. Exclui o registro.
                    evARemover.add(ev);
                }
            }

            // Remove os registros depois, para evitar ConcurrentModification
            for (Evento ev : evARemover) {
                EventoController.getInstance().remover(ev.getInteresse().getId());
            }
        }
    }

    private void notificarInteressePacote(Voo v) {
        // Lista de interesses em que esse voo pode ser o voo de ida
        List<Evento> evListIda = EventoController.getInstance().consultar(Interesse.TipoInteresse.PACOTE, v.getOrigem(), v.getDestino());

        // Lista de interesses em que esse voo pode ser o voo de volta
        List<Evento> evListVolta = EventoController.getInstance().consultar(Interesse.TipoInteresse.PACOTE, v.getDestino(), v.getOrigem());

        // Lista de eventos a remover depois de notificar
        ArrayList<Evento> evARemover = new ArrayList<>();

        // Verifica se ocorreu algum evento de pacote em que v é o voo de ida
        if (evListIda != null) for (Evento ev : evListIda) {
            Cidade origem = ev.getInteresse().getOrigem();
            Cidade destino = ev.getInteresse().getDestino();
            LocalDate dataVooIda = v.getData();

            // Se não achou hotel, esse evento não ocorreu
            if (!existeHotel(destino)) {
                continue;
            }
            // Verifica se há voo de volta
            if (existeVooAPartirDe(destino, origem, dataVooIda)) {
                // Se achou voo de volta, ocorreu um evento de pacote
                String notif = "Há novos pacotes de " + origem + " para " + destino + "!";

                try {
                    ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                }
                catch (RemoteException e) {
                    // O cliente provavelmente não existe mais. Exclui o registro.
                    evARemover.add(ev);
                }
            }
        }

        // Verifica se ocorreu algum evento de pacote em que v é o voo de volta
        if (evListVolta != null) for (Evento ev : evListVolta) {
            Cidade origem = ev.getInteresse().getOrigem();
            Cidade destino = ev.getInteresse().getDestino();
            LocalDate dataVooVolta = v.getData();

            // Verifica se há hotel
            if (!existeHotel(destino)) {
                continue;
            }
            // Verifica se há voo de ida
            if (existeVooAteData(origem, destino, dataVooVolta)) {
                // Se achou voo de ida, ocorreu um evento de pacote
                String notif = "Há novos pacotes de " + origem + " para " + destino + "!";

                try {
                    ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                }
                catch (RemoteException e) {
                    // O cliente provavelmente não existe mais. Exclui o registro.
                    evARemover.add(ev);
                }
            }
        }


        // Remove os registros depois, para evitar ConcurrentModification
        for (Evento ev : evARemover) {
            EventoController.getInstance().remover(ev.getInteresse().getId());
        }
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um hotel à lista de hotéis do servidor
     * @param hotel hotel já instanciado e inicializado
     */
    public void adicionarHotel(Hotel hotel) {
        hoteis.add(hotel);
        notificarInteresseHotel(hotel);
        notificarInteressePacote(hotel);
    }

    /** Permite adicionar novas hospedagens (datas) a um hotel já existente, a
     * partir de seu identificador.
     * @param idHotel identificador do hotel
     * @param dataIni data de início do período
     * @param dataFim data de fim do período (também é incluída no intervalo)
     */
    public void adicionarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim) {
        // Obtém o hotel
        Hotel h = hoteis.stream().filter(item -> (item.getId() == idHotel)).findFirst().orElse(null);

        if (h != null) {
            h.adicionarHospedagem(dataIni, dataFim);
        }
    }

    /** Envia notificações a clientes cujo interesse em um hotel pode ser
     * atendido por um hotel h.
     * @param h hotel
     */
    private void notificarInteresseHotel(Hotel h) {
        if (h != null) {
            // Verifica interesses e notifica clientes
            List<Evento> evList = EventoController.getInstance().consultar(h);
            ArrayList<Evento> evARemover = new ArrayList<>();
            if (evList != null) {
                for (Evento ev : evList) {
                    String notif = "Um hotel foi encontrado!\n" +
                            "Nome: " + h.getNome() + "\n" +
                            "Cidade: " + ev.getInteresse().getDestino();

                    try {
                        ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                    }
                    catch (RemoteException e) {
                        // O cliente provavelmente não existe mais. Exclui o registro.
                        evARemover.add(ev);
                    }
                }

                // Remove os registros depois, para evitar ConcurrentModification
                for (Evento ev : evARemover) {
                    EventoController.getInstance().remover(ev.getInteresse().getId());
                }
            }
        }
    }

    private void notificarInteressePacote(Hotel h) {
        // Pega todos os possíveis eventos que vão para a cidade do hotel
        ArrayList<Evento> evList = new ArrayList<>();
        for (Cidade origem : Cidade.values()) {
            evList.addAll(EventoController.getInstance().consultar(Interesse.TipoInteresse.PACOTE, origem, h.getLocal()));
        }

        // Lista de eventos a remover no final
        ArrayList<Evento> evARemover = new ArrayList<>();

        // Itera pelos eventos
        for (Evento ev : evList) {
            // Agora é necessário ver se há um hotel que atende ao requisito
            Cidade destino = ev.getInteresse().getDestino();
            Cidade origem = ev.getInteresse().getOrigem();

            // Itera pelos voos de ida
            loopVooIda:
            for (Voo vooIda : voos) {
                if (vooIda.getOrigem() != origem || vooIda.getDestino() != destino) {
                    continue;
                }
                for (Voo vooVolta : voos) {
                    if (vooVolta.getOrigem() == destino && vooVolta.getDestino() == origem) {
                        String notif = "Há novos pacotes de " + origem + " para " + destino + "!";

                        try {
                            ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                        }
                        catch (RemoteException e) {
                            // O cliente provavelmente não existe mais. Exclui o registro.
                            evARemover.add(ev);
                        }

                        // Impede que seja enviada uma notificação para cada hotel
                        break loopVooIda;
                    }
                }
            }
        }

        // Remove os registros depois, para evitar ConcurrentModification
        for (Evento ev : evARemover) {
            EventoController.getInstance().remover(ev.getInteresse().getId());
        }
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo,
            Cidade origem, Cidade destino, LocalDate dataIda,
            LocalDate dataVolta, int numPessoas) throws RemoteException {
        // TODO: Colocar null-check em todos os parâmetros
        ArrayList<InfoVoo> result = new ArrayList<>();
        for (Voo voo : voos) {
            // FIXME: precisa synchronized para ler?

            // Adiciona voos de ida
            if (origem.equals(voo.getOrigem()) &&
                    destino.equals(voo.getDestino()) &&
                    dataIda.equals(voo.getData()) &&
                    numPessoas <= voo.getPoltronasDisp()) {
                result.add(voo.getInfoVoo());
            }

            // Adiciona voos de volta
            if (tipo == TipoPassagem.IDA_E_VOLTA && dataVolta != null) {
                if (origem.equals(voo.getDestino()) &&
                        destino.equals(voo.getOrigem()) &&
                        dataVolta.equals(voo.getData()) &&
                        numPessoas <= voo.getPoltronasDisp()) {
                    result.add(voo.getInfoVoo());
                }
            }
        }
        return result;
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda,
            int idVooVolta, int numPessoas) throws RemoteException {
        if (tipo == TipoPassagem.IDA_E_VOLTA) {
            return comprarPassagensIdaEVolta(idVooIda, idVooVolta, numPessoas);
        }
        return comprarPassagensSomenteIda(idVooIda, numPessoas);
    }

    /** Tenta efetuar compra de passagem de ida.
     * Chamada pela função comprarPassagens.
     * @param idVoo identificador do voo de ida
     * @param numPessoas número de passagens a adquirir
     * @return true se e somente se a compra for bem sucedida
     */
    private boolean comprarPassagensSomenteIda(int idVoo, int numPessoas) {
        Voo voo = null;

        // Busca o voo
        for (Voo v : voos) {
            if (v.getId() == idVoo) {
                voo = v;
                break;
            }
        }

        // Retorna false se não encontrou o voo
        if (voo == null) {
            return false;
        }

        // Retorna true se conseguir comprar o número desejado,
        // e false caso contrário
        return voo.reservar(numPessoas) != null;
    }

    /** Tenta efetuar compra de passagens de ida e volta.
     * Chamada pela função comprarPassagens.
     * @param idVooIda identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida para ambos os voos
     */
    private boolean comprarPassagensIdaEVolta(int idVooIda, int idVooVolta,
            int numPessoas) {
        Voo vooIda = null;
        Voo vooVolta = null;

        // Busca os voos
        for (Voo v : voos) {
            int idVoo = v.getId();

            if (idVoo == idVooIda) {
                vooIda = v;
            }
            else if (idVoo == idVooVolta) {
                vooVolta = v;
            }

            if (vooIda != null && vooVolta != null) {
                break;
            }
        }

        // Retorna false se não encontrou algum dos voos
        if (vooIda == null || vooVolta == null) {
            return false;
        }

        Reserva reservaVooIda = vooIda.reservar(numPessoas);
        if (reservaVooIda != null) {
            if (vooVolta.reservar(numPessoas) != null) {
                // Retorna true se conseguir comprar ida e volta
                return true;
            }
            // Caso não consiga comprar a volta,
            // faz rollback na compra do voo de ida e retorna false
            vooIda.estornar(reservaVooIda);
        }
        // Retorna false se não conseguir comprar o número desejado para a ida
        return false;
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public ArrayList<InfoHotelRet> consultarHospedagens(Cidade local,
            LocalDate dataIni, LocalDate dataFim, int numQuartos,
            int numPessoas) throws RemoteException {
        // Cria um vetor de InfoHotelRet para enviar ao cliente
        ArrayList<InfoHotelRet> result = new ArrayList<>();

        for (Hotel h : hoteis) {
            // Pula hotéis em outras cidades
            if (!local.equals(h.getLocal())) {
                continue;
            }

            // Inicia a contagem de número de quartos disponíveis no número
            // máximo de quartos do hotel
            int quartosDisp = h.getInfoHotel().getNumQuartos();

            // Flag usada depois do loop para adicionar um hotel ou não à lista
            // de retorno
            boolean podeReceber = true;

            // Considera-se que o cliente sai na data de volta.
            // Portanto, não são incluídas hospedagens para o dia de volta.
            LocalDate data = dataIni.plusDays(0);
            while (data.isBefore(dataFim)) {
                Hospedagem hosp = h.getHospedagemData(data);
                if (hosp == null) {
                    // Deu ruim, esse hotel não está oferecendo hospedagem em
                    // um dos dias do período
                    podeReceber = false;
                    break;
                }

                int hospQuartosDisp = hosp.getQuartosDisp();
                if (hospQuartosDisp < numPessoas) {
                    // Deu ruim, esse hotel não pode receber o cliente em todos
                    // os dias do período
                    podeReceber = false;
                    break;
                }

                if (hospQuartosDisp < quartosDisp) {
                    // Há um dia mais lotado, atualiza o número
                    quartosDisp = hospQuartosDisp;
                }

                // FIXME: vamos só ignorar o número de pessoas?

                data = data.plusDays(1);
            }

            // Apenas envia o hotel se tiver vagas em todos os dias do período
            if (podeReceber) {
                InfoHotelRet ihr = new InfoHotelRet(h.getInfoHotel(),
                        quartosDisp, dataIni, dataFim);
                result.add(ihr);
            }
        }

        return result;
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni,
            LocalDate dataFim, int numQuartos) throws RemoteException {
        // Busca o hotel
        Hotel hotel = null;
        for (Hotel h : hoteis) {
            if (h.getId() == idHotel) {
                hotel = h;
                break;
            }
        }

        if (hotel == null) {
            // Hotel não existe
            return false;
        }

        // Faz a reserva, se possível, e retorna true se bem sucedido
        return hotel.reservar(dataIni, dataFim, numQuartos);
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    public ConjuntoPacote consultarPacotes(Cidade origem, Cidade destino,
            LocalDate dataIda, LocalDate dataVolta, int numQuartos,
            int numPessoas) throws RemoteException {
        // Obtém todos os voos
        ArrayList<InfoVoo> voos = consultarPassagens(TipoPassagem.IDA_E_VOLTA,
                origem, destino, dataIda, dataVolta, numPessoas);

        // Obtém as informações de hotéis
        ArrayList<InfoHotelRet> hosps = consultarHospedagens(
                destino, dataIda, dataVolta, numQuartos, numPessoas);

        ConjuntoPacote conjuntoPacote = new ConjuntoPacote();
        if (!voos.isEmpty() && !hosps.isEmpty()) {
            for (InfoVoo v : voos) {
                if (v.getOrigem() == origem) {
                    conjuntoPacote.adicionarVooIda(v);
                } else {
                    conjuntoPacote.adicionarVooVolta(v);
                }
            }
            for (InfoHotelRet ihr : hosps) {
                conjuntoPacote.adicionarHospedagem(ihr);
            }
        }

        return conjuntoPacote;
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public boolean comprarPacote(int idVooIda, int idVooVolta, int idHotel,
            LocalDate dataIda, LocalDate dataVolta, int numQuartos,
            int numPessoas) throws RemoteException {
        Voo vooIda = null;
        Voo vooVolta = null;

        // Busca os voos
        for (Voo v : voos) {
            int idVoo = v.getId();

            if (idVoo == idVooIda) {
                vooIda = v;
            }
            else if (idVoo == idVooVolta) {
                vooVolta = v;
            }

            if (vooIda != null && vooVolta != null) {
                break;
            }
        }

        // Retorna false se não encontrou algum dos voos
        if (vooIda == null || vooVolta == null) {
            return false;
        }

        // Busca o hotel
        Hotel hotel = null;
        for (Hotel h : hoteis) {
            if (h.getId() == idHotel) {
                hotel = h;
                break;
            }
        }

        if (hotel == null) {
            // Hotel não existe
            return false;
        }

        // Tenta reservar voo de ida
        Reserva reservaVooIda = vooIda.reservar(numPessoas);
        if (reservaVooIda != null) {
            // Tenta reservar voo de volta
            Reserva reservaVooVolta = vooVolta.reservar(numPessoas);
            if (reservaVooVolta != null) {
                // Tenta reservar hotel
                if (hotel.reservar(dataIda, dataVolta, numQuartos)) {
                    // Retorna true se conseguiu tudo
                    return true;
                }
                // Caso dê ruim, desfaz a reserva do voo de volta
                vooVolta.estornar(reservaVooVolta);
            }
            // Caso dê ruim, desfaz a reserva do voo de ida
            vooIda.estornar(reservaVooIda);
        }

        // Retorna false se não conseguir comprar
        return false;
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public int registrarInteresseVoo(Cidade origem, Cidade destino,
            LocalDate data, AgencyClient clientRef) throws RemoteException {
        InteresseVoo iv = new InteresseVoo(origem, destino, data, clientRef);

        if (interessesVoo.colocarInteresse(iv)) {
            return iv.getId();
        }
        return -1;
    }

    /*------------------------------------------------------------------------*/

    /*
    @Override
    public <?> removerInteresseVoo(<?>) throws RemoteException {
    }
     */

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public int registrarInteresseHotel(Cidade destino, LocalDate dataIni,
            LocalDate dataFim, AgencyClient clientRef) throws RemoteException {
        InteresseHotel ih = new InteresseHotel(destino, dataIni, dataFim,
                clientRef);

        if (interessesHotel.colocarInteresse(ih)) {
            return ih.getId();
        }
        return -1;
    }

    /*------------------------------------------------------------------------*/

    /*
    @Override
    public <?> removerInteresseHotel(<?>) throws RemoteException {
    }
     */

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public int registrarInteressePacote(Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, AgencyClient clientRef) throws RemoteException {
        return -1;
    }

    /*------------------------------------------------------------------------*/

    /*
    @Override
    public <?> removerInteressePacote(<?>) throws RemoteException {
    }
     */

    /*------------------------------------------------------------------------*/

    @Override
    public int registrarInteresse(Interesse interesse, AgencyClient client) throws RemoteException {
        return EventoController.getInstance().registrar(interesse, client).getInteresse().getId();
    }

    /*------------------------------------------------------------------------*/

    @Override
    public boolean removerInteresse(int id, AgencyClient client) throws RemoteException {
        /*
        Evento ev = EventoController.getInstance().consultar(id);
        if (ev.getClientRef().equals(client)) {
            return false;
        }
        */
        return EventoController.getInstance().remover(id);
    }
}
