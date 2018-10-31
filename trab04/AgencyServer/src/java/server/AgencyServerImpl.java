package server;

import java.math.BigDecimal;
import server.controller.EventoController;
import server.model.evento.Evento;
import server.model.hotel.Hospedagem;
import server.model.hotel.Hotel;
import server.model.saldo.Reserva;
import server.model.voo.Voo;
import shared.model.cidade.Cidade;
import shared.model.evento.Interesse;
import shared.model.hotel.InfoHotelRet;
import shared.model.mensagem.Mensagem;
import shared.model.pacote.ConjuntoPacote;
import shared.model.voo.InfoVoo;
import shared.model.voo.TipoPassagem;
import shared.remote.AgencyClient;
import shared.remote.AgencyServer;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import shared.model.saldo.Dinheiro;

/** Representa o servidor da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyServerImpl implements AgencyServer {
    /** Lista de voos disponíveis */
    private ArrayList<Voo> voos = new ArrayList<>();

    /** Lista de hotéis cadastrados */
    private ArrayList<Hotel> hoteis = new ArrayList<>();
    
    /** Parser de string para números decimais */
    private static DecimalFormat df = new DecimalFormat();

    /*------------------------------------------------------------------------*/

    /** Construtor único.
     */
    public AgencyServerImpl() {
        df.setParseBigDecimal(true);
        popularServidor();
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

    /** Busca possíveis interesses no voo v e envia notificações aos clientes
     * cujos interesses são atendidos.
     * @param v voo
     */
    private void notificarInteresseVoo(Voo v) {
        List<Evento> evList = EventoController.getInstance().consultar(v);
        ArrayList<Evento> evARemover = new ArrayList<>();
        if (evList != null) {
            for (Evento ev : evList) {
                if (v.getPrecoPassagem().compareTo(ev.getInteresse().getValorMaximoVoo()) <= 0) {
                    String notif = "Um voo foi encontrado de " + v.getOrigem() + " para " + v.getDestino() + "!";

                    try {
                        ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                    } catch (RemoteException e) {
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
    }

    /** Busca possíveis interesses de pacote com o voo v e envia notificações
     * aos clientes cujos interesses são atendidos.
     * ATENÇÃO: HIC SUNT DRACONES
     * @param v voo
     */
    private void notificarInteressePacote(Voo v) {
        // Lista de interesses em que esse voo pode ser o voo de ida
        List<Evento> evListIda = EventoController.getInstance().consultar(Interesse.TipoInteresse.PACOTE, v.getOrigem(), v.getDestino());

        // Lista de interesses em que esse voo pode ser o voo de volta
        List<Evento> evListVolta = EventoController.getInstance().consultar(Interesse.TipoInteresse.PACOTE, v.getDestino(), v.getOrigem());

        // Lista de eventos a remover depois de notificar
        ArrayList<Evento> evARemover = new ArrayList<>();

        // Verifica se ocorreu algum evento de pacote em que v é o voo de ida
        if (evListIda != null) for (Evento ev : evListIda) {
            // Já pula o evento se o voo adicionado é mais caro do que o cliente deseja
            if (v.getPrecoPassagem().compareTo(ev.getInteresse().getValorMaximoVoo()) > 0) {
                continue;
            }

            Cidade origem = ev.getInteresse().getOrigem();
            Cidade destino = ev.getInteresse().getDestino();
            LocalDate dataVooIda = v.getData();

            // Procura hotéis
            find_hotel:
            for (Hotel hotel : hoteis) {
                // Pula hotéis que não estão na cidade destino ou hotéis mais caros do que o cliente quer
                if (hotel.getLocal() != destino || hotel.getPrecoDiaria().compareTo(ev.getInteresse().getValorMaximoHotel()) > 0) {
                     continue;
                }

                // Procura voos de volta
                for (Voo voo : voos) {
                    // Pula voos que não atendem a condição espacial e temporal de um voo de volta, e também os mais caros do que o cliente quer
                    if (voo.getOrigem() == destino && voo.getDestino() == origem && !voo.getData().isBefore(dataVooIda) && voo.getPrecoPassagem().compareTo(ev.getInteresse().getValorMaximoVoo()) <= 0) {
                        // Se achou voo de volta, ocorreu um evento de pacote
                        String notif = "Há novos pacotes de " + origem + " para " + destino + "!";

                        try {
                            ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                        }
                        catch (RemoteException e) {
                            // O cliente provavelmente não existe mais. Exclui o registro.
                            evARemover.add(ev);
                        }

                        break find_hotel;
                    }
                }
            }
        }

        // Verifica se ocorreu algum evento de pacote em que v é o voo de volta
        if (evListVolta != null) for (Evento ev : evListVolta) {
            // Já pula o evento se o voo adicionado é mais caro do que o cliente deseja
            if (v.getPrecoPassagem().compareTo(ev.getInteresse().getValorMaximoVoo()) > 0) {
                continue;
            }

            Cidade origem = ev.getInteresse().getOrigem();
            Cidade destino = ev.getInteresse().getDestino();
            LocalDate dataVooVolta = v.getData();

            // Procura hotéis
            find_hotel:
            for (Hotel hotel : hoteis) {
                // Pula hotéis que não estão na cidade destino ou hotéis mais caros do que o cliente quer
                if (hotel.getLocal() != destino || hotel.getPrecoDiaria().compareTo(ev.getInteresse().getValorMaximoHotel()) > 0) {
                    continue;
                }

                // Procura voos de ida
                for (Voo voo : voos) {
                    // Pula voos que não atendem a condição espacial ou temporal de um voo de ida ou também os mais caros do que o cliente quer
                    if (voo.getOrigem() == origem && voo.getDestino() == destino && !voo.getData().isAfter(dataVooVolta) && voo.getPrecoPassagem().compareTo(ev.getInteresse().getValorMaximoVoo()) <= 0) {
                        // Se achou voo de volta, ocorreu um evento de pacote
                        String notif = "Há novos pacotes de " + origem + " para " + destino + "!";

                        try {
                            ev.getClientRef().notifyEvent(new Mensagem(ev.getInteresse().getId(), notif));
                        }
                        catch (RemoteException e) {
                            // O cliente provavelmente não existe mais. Exclui o registro.
                            evARemover.add(ev);
                        }

                        break find_hotel;
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
                    if (h.getPrecoDiaria().compareTo(ev.getInteresse().getValorMaximoHotel()) <= 0) {
                        String notif = "Um hotel foi encontrado em " + ev.getInteresse().getDestino() + "!";

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
        }
    }

    /** Envia notificações a clientes cujo interesse de pacote pode ser
     * atendido por um hotel h. Busca voos de ida e volta que atendam aos
     * requisitos.
     * ATENÇÃO: HIC SUNT DRACONES
     * @param h hotel
     */
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

            // Procura voos de ida
            loopVooIda:
            for (Voo vooIda : voos) {
                // Pula voos que não atendem aos locais do pacote ou voos mais caros do que o cliente deseja
                if (vooIda.getOrigem() != origem || vooIda.getDestino() != destino || vooIda.getPrecoPassagem().compareTo(ev.getInteresse().getValorMaximoVoo()) > 0) {
                    continue;
                }

                // Procura voos de volta
                for (Voo vooVolta : voos) {
                    // Pula voos que não atendem aos locais do pacote, que são antes do voo de ida ou também os mais caros do que o cliente deseja
                    if (vooVolta.getOrigem() == destino && vooVolta.getDestino() == origem && !vooVolta.getData().isBefore(vooIda.getData()) && vooVolta.getPrecoPassagem().compareTo(ev.getInteresse().getValorMaximoVoo()) <= 0) {
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
            LocalDate dataVolta, int numPessoas) {
        // TODO: Colocar null-check em todos os parâmetros
        ArrayList<InfoVoo> result = new ArrayList<>();
        for (Voo voo : voos) {
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
            int idVooVolta, int numPessoas) {
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
            int numPessoas) {
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
                        quartosDisp, dataIni, dataFim, numQuartos);
                result.add(ihr);
            }
        }

        return result;
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni,
            LocalDate dataFim, int numQuartos) {
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
            int numPessoas) {
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
    public int registrarInteresse(Interesse interesse, AgencyClient client) {
        return EventoController.getInstance().registrar(interesse, client).getInteresse().getId();
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    @Override
    public boolean removerInteresse(int id, AgencyClient client) {
        /*
        Evento ev = EventoController.getInstance().consultar(id);
        if (ev.getClientRef().equals(client)) {
            return false;
        }
        */
        return EventoController.getInstance().remover(id);
    }
    
    /*------------------------------------------------------------------------*/
    
    private void popularServidor() {
        try {
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.parse("2018-10-16"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.parse("2018-10-16"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.parse("2018-10-17"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.parse("2018-10-17"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.parse("2018-10-18"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.parse("2018-10-18"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.parse("2018-10-19"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.parse("2018-10-19"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, LocalDate.parse("2018-10-20"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, LocalDate.parse("2018-10-20"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.parse("2018-10-16"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.parse("2018-10-16"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.parse("2018-10-17"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.parse("2018-10-17"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.parse("2018-10-18"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.parse("2018-10-18"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.parse("2018-10-19"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.parse("2018-10-19"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.CURITIBA, Cidade.SAO_PAULO, LocalDate.parse("2018-10-20"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarVoo(new Voo(Cidade.SAO_PAULO, Cidade.CURITIBA, LocalDate.parse("2018-10-20"), 100, Dinheiro.reais((BigDecimal) df.parse("100,00"))));

            adicionarHotel(new Hotel("Hotel Peru", Cidade.FLORIANOPOLIS, 20, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(0, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Pato", Cidade.FLORIANOPOLIS, 30, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(1, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Galinha", Cidade.FLORIANOPOLIS, 15, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(2, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Weeb", Cidade.CURITIBA, 20, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(3, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Noob", Cidade.CURITIBA, 30, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(4, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Bispo", Cidade.CURITIBA, 15, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(5, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Pereira", Cidade.SAO_PAULO, 20, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(6, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Madoka", Cidade.SAO_PAULO, 30, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(7, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
            adicionarHotel(new Hotel("Hotel Homura", Cidade.SAO_PAULO, 15, Dinheiro.reais((BigDecimal) df.parse("100,00"))));
            adicionarHospedagem(8, LocalDate.parse("2018-10-16"), LocalDate.parse("2018-10-20"));
        }
        catch (ParseException e) {
            // Do nothing
        }
    }
}
