# -*- coding: utf-8 -*-

# ==================================================================================================================== #
__author__ = "Rafael Hideo Toyomoto, Victor Barpp Gomes"
__copyright__ = "Copyright 2018, TBDC"
__credits__ = ["Rafael Hideo Toyomoto", "Victor Barpp Gomes"]

__license__ = "TBDC"
__version__ = "1.0"
__maintainer__ = "Rafael Hideo Toyomoto"
__email__ = "toyomoto@alunos.utfpr.edu.br"
__status__ = "Production"
# ==================================================================================================================== #

from datetime import datetime

from PyQt5 import QtWidgets

from src.controller.RequestController import RequestController
from src.view.interface import Ui_MainWindow

from src.enum.Cidade import Cidade
from src.enum.TipoVoo import TipoVoo
from src.model.Dinheiro import Dinheiro
from src.model.HotelRet import HotelRet
from src.model.Voo import Voo

class MainController(object):

    def __init__(self):
        """
        Construtor padrão
        """

        # Controlador de requisições para o servidor
        self.requests = RequestController()

        # Ao invés dos [], pode passar argumentos
        # Criar a aplicação que irá executar
        self.__app = QtWidgets.QApplication([])

        # Declarar o atributo que armazenará todos componentes da tela
        self.__ui = None

    def __del__(self):
        """
        Destrutor padrão
        :return: não retorna nada
        """
        # Limpar dados armazenados
        self.__app = None
        self.__ui = None

    def run(self):
        """
        Rodar o código principal do controlador
        :return: não retorna nada
        """

        # Criar uma janela (principal)
        window = QtWidgets.QMainWindow()

        # Gerar os componentes nessa janela
        self.__ui = Ui_MainWindow()
        self.__ui.setupUi(window)
        self.__ui.buttonConsultarVoo.clicked.connect(self.consultarVoo)
        self.__ui.buttonConsultarHosp.clicked.connect(self.consultarHotel)
        self.__ui.buttonConsultarPacote.clicked.connect(self.consultarPacote)
        self.__ui.buttonComprarVoo.clicked.connect(self.comprarVoo)
        self.__ui.buttonComprarHosp.clicked.connect(self.comprarHospedagem)
        self.__ui.buttonComprarPacote.clicked.connect(self.comprarPacote)

        # Apresentar a janela
        window.show()
        try:
            # Loop de execução da aplicação
            self.__app.exec_()
        except Exception as e:
            # Algum erro aconteceu durante a execução do programa
            print("Error!", str(e))

    def consultarVoo(self):
        """
        Função que retira os dados da tela e tenta realizar a consulta por Voos com eles
        :return: não retorna nada
        """

        # Obter qual tipo de Voo é desejado pelo usuário
        type = None
        if self.__ui.radioSomenteIdaVoo.isChecked():
            type = TipoVoo.SOMENTE_IDA
        elif self.__ui.radioIdaEVoltaVoo.isChecked():
            type = TipoVoo.IDA_E_VOLTA
        else:
            # FIXME: alerta
            print("ERROR!")
            return

        # Obter a cidade de origem do Voo
        origem = Cidade(self.__ui.choiceOrigemVoo.currentText())
        # Obter a cidade de destino do Voo
        destino = Cidade(self.__ui.choiceDestinoVoo.currentText())
        # Obter a data de partida do Voo
        dataIda = datetime.strptime(self.__ui.dateVooIda.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        # Obter a data de retorno do Voo
        dataVolta = datetime.strptime(self.__ui.dateVooVolta.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        # Obter o número de pessoas do Voo
        numPessoas = self.__ui.spinnerNumPessoasVoo.value()

        try:
            # Realizar a consulta
            passagens = self.requests.get_passagens(type, origem, destino, dataIda, dataVolta, numPessoas)

            # Separar o resultado entre Voos de ida e de volta
            passagens_ida = []
            passagens_volta = []
            for p in passagens:
                if p.origem == origem:
                    passagens_ida.append(p)
                elif p.destino == origem:
                    passagens_volta.append(p)

            # Atualizar as tabelas com os dados
            self.__ui.updateTableVooIda(passagens_ida)
            self.__ui.updateTableVooVolta(passagens_volta)
        except Exception as e:
            print(e)

    def consultarHotel(self):
        """
        Função que retira os dados da tela e tenta realizar a consulta por Hospedagens com eles
        :return: não retorna nada
        """

        # Obter a cidade do local da Hospedagem
        cidade = Cidade(self.__ui.choiceCidadeHosp.currentText())
        # Obter a data de entrada na Hospedagem
        dataIni = datetime.strptime(self.__ui.dateChegadaHosp.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        # Obter a data de saída da Hospedagem
        dataFim = datetime.strptime(self.__ui.dateSaidaHosp.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        # Obter o número de quartos necessários da Hospedagem
        numQuartos = self.__ui.spinnerNumQuartosHosp.value()
        # Obter o número necessário de pessoas por quarto da Hospedagem
        numPessoas = self.__ui.spinnerNumPessoasHosp.value()

        try:
            # Realizar a consulta
            hospedagens = self.requests.get_hospedagens(cidade, dataIni, dataFim, numQuartos, numPessoas)

            # Atualizar as tabelas com os dados
            self.__ui.updateTableHospedagem(hospedagens)
        except Exception as e:
            print(e)

    def consultarPacote(self):
        """
        Função que retira os dados da tela e tenta realizar a consulta por Pacotes com eles
        :return:
        """

        # Obter a cidade de origem do Voo do Pacote
        origem = Cidade(self.__ui.choiceOrigemPacote.currentText())
        # Obter a cidade de destino do Voo e Hospedagem do Pacote
        destino = Cidade(self.__ui.choiceDestinoPacote.currentText())
        # Obter a data de partida do Voo e entrada na Hospedagem do Pacote
        dataIda = datetime.strptime(self.__ui.datePacoteIda.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        # Obter a data de retorno do Voo e saída da Hospedagem do Pacote
        dataVolta = datetime.strptime(self.__ui.datePacoteVolta.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        # Obter o número de quartos necessários da Hospedagem do Pacote
        numQuartos = self.__ui.spinnerNumQuartosPacote.value()
        # Obter o número necessário de passagens de Voo e pessoas por quarto da Hospedagem do Pacote
        numPessoas = self.__ui.spinnerNumPessoasPacote.value()

        try:
            # Realizar a consulta
            pacotes = self.requests.get_pacotes(origem, destino, dataIda, dataVolta, numPessoas, numQuartos)

            # Atualizar as tabelas com os dados
            self.__ui.updateTableVooIdaPac(pacotes['voosIda'])
            self.__ui.updateTableVooVoltaPac(pacotes['voosVolta'])
            self.__ui.updateTableHospedagemPac(pacotes['hospedagens'])
        except Exception as e:
            print(e)

    def comprarVoo(self):
        """
        Função que retira os dados da tela e tenta realizar a compra de Voo com eles
        :return: não retorna nada
        """

        # Obter o número de passagens desejadas
        numPessoas = self.__ui.spinnerNumPessoasVoo.value()
        # Obter o tipo do voo desejado
        tipoPassagem = TipoVoo.IDA_E_VOLTA if self.__ui.radioIdaEVoltaVoo.isChecked() else TipoVoo.SOMENTE_IDA
        # Obter o Voo de ida selecionado para comprar
        linhaVooIda = self.__ui.tableVooIda.selectedItems()
        # Obter o Voo de volta selecionado para comprar
        linhaVooVolta = self.__ui.tableVooVolta.selectedItems()

        # Validar as linhas selecionadas
        if len(linhaVooIda) != 6 or (tipoPassagem == TipoVoo.IDA_E_VOLTA and len(linhaVooVolta) != 6):
            # TODO: alert
            print("Form inválido")
            return

        # Converter a linha da tabela em um objeto Voo
        vooIda = Voo.transform(linhaVooIda, numPessoas)
        # Converter a linha da tabela em um objeto Voo
        vooVolta = Voo.transform(linhaVooVolta, numPessoas) if tipoPassagem == TipoVoo.IDA_E_VOLTA else None

        # Enviar a requisição
        response = self.requests.put_passagem(vooIda, vooVolta, numPessoas)
        # Verificar resposta
        if response.text == "true":
            # TODO: alert
            print("Deu boa")
        else:
            # TODO: alert
            print("Compra falhou")

    def comprarHospedagem(self):
        """
        Função que retira os dados da tela e tenta realizar a compra de Hospedagem com eles
        :return: não retorna nada
        """

        # Obter o número de passagens desejadas
        numPessoas = self.__ui.spinnerNumPessoasHosp.value()
        # Obter o número de quartos desejadas
        numQuartos = self.__ui.spinnerNumQuartosHosp.value()

        # Obter a Hospedagem selecionada para comprar
        linhaHotel = self.__ui.tableHospedagem.selectedItems()

        if len(linhaHotel) != 9:
            # TODO: alert
            print("Form inválido")
            return

        # Converter a linha da tabela em um objeto HotelRet
        hotel, dataEntrada, dataSaida = HotelRet.transform(linhaHotel, numQuartos)

        # Enviar a requisição
        response = self.requests.put_hospedagem(hotel, dataEntrada, dataSaida, numQuartos)
        # Verificar resposta
        if response.text == "true":
            # TODO: alert
            print("Deu boa")
        else:
            # TODO: alert
            print("Compra falhou")

    def comprarPacote(self):
        """
        Função que retira os dados da tela e tenta realizar a compra de Pacote com eles
        :return: não retorna nada
        """

        # Obter o número de passagens desejadas
        numPessoas = self.__ui.spinnerNumPessoasPacote.value()
        # Obter o número de quartos desejadas
        numQuartos = self.__ui.spinnerNumQuartosPacote.value()

        # Obter o Voo de ida selecionado para comprar
        linhaVooIda = self.__ui.tableVooIdaPac.selectedItems()
        # Obter o Voo de volta selecionado para comprar
        linhaVooVolta = self.__ui.tableVooVoltaPac.selectedItems()
        # Obter a Hospedagem selecionada para comprar
        linhaHotel = self.__ui.tableHospedagemPac.selectedItems()

        # Validar as linhas selecionadas
        if len(linhaVooIda) != 6 or len(linhaVooVolta) != 6 or len(linhaHotel) != 9:
            # TODO: alert
            print("Form inválido")
            return

        # Converter a linha da tabela em um objeto Voo
        vooIda = Voo.transform(linhaVooIda, numPessoas)
        # Converter a linha da tabela em um objeto Voo
        vooVolta = Voo.transform(linhaVooVolta, numPessoas)
        # Converter a linha da tabela em um objeto HotelRet
        hotel, dataEntrada, dataSaida = HotelRet.transform(linhaHotel, numQuartos)

        # Enviar a requisição
        response = self.requests.put_pacote(vooIda, vooVolta, hotel, dataEntrada, dataSaida, numQuartos, numPessoas)
        # Verificar resposta
        if response.text == "true":
            # TODO: alert
            print("Deu boa")
        else:
            # TODO: alert
            print("Compra falhou")

# ==================================================================================================================== #
