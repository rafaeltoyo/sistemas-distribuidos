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
        self.requests = RequestController()

        # Ao invés dos [], pode passar argumentos
        self.__app = QtWidgets.QApplication([])

        self.__ui = None

    def __del__(self):
        self.__app = None

    def run(self):
        window = QtWidgets.QMainWindow()

        self.__ui = Ui_MainWindow()
        self.__ui.setupUi(window)
        self.__ui.buttonConsultarVoo.clicked.connect(self.consultarVoo)
        self.__ui.buttonConsultarHosp.clicked.connect(self.consultarHotel)
        self.__ui.buttonConsultarPacote.clicked.connect(self.consultarPacote)
        self.__ui.buttonComprarVoo.clicked.connect(self.comprarVoo)
        self.__ui.buttonComprarHosp.clicked.connect(self.comprarHospedagem)
        self.__ui.buttonComprarPacote.clicked.connect(self.comprarPacote)

        # TODO: Bind nos botões com as funções de dinamização da tela
        # TODO: Ajustar Listagens

        window.show()
        try:
            self.__app.exec_()
        except Exception as e:
            print("Error!", str(e))

    def consultarVoo(self):
        type = None
        if self.__ui.radioSomenteIdaVoo.isChecked():
            type = TipoVoo.SOMENTE_IDA
        elif self.__ui.radioIdaEVoltaVoo.isChecked():
            type = TipoVoo.IDA_E_VOLTA
        else:
            # FIXME: alerta
            print("ERROR!")
            return

        origem = Cidade(self.__ui.choiceOrigemVoo.currentText())
        destino = Cidade(self.__ui.choiceDestinoVoo.currentText())
        dataIda = datetime.strptime(self.__ui.dateVooIda.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        dataVolta = datetime.strptime(self.__ui.dateVooVolta.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        numPessoas = self.__ui.spinnerNumPessoasVoo.value()

        # Realizar a consulta
        try:
            passagens = self.requests.get_passagens(type, origem, destino, dataIda, dataVolta, numPessoas)

            passagens_ida = []
            passagens_volta = []
            for p in passagens:
                if p.origem == origem:
                    passagens_ida.append(p)
                elif p.destino == origem:
                    passagens_volta.append(p)

            self.__ui.updateTableVooIda(passagens_ida)
            self.__ui.updateTableVooVolta(passagens_volta)
        except Exception as e:
            print(e)

    def consultarHotel(self):
        cidade = Cidade(self.__ui.choiceCidadeHosp.currentText())
        dataIni = datetime.strptime(self.__ui.dateChegadaHosp.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        dataFim = datetime.strptime(self.__ui.dateSaidaHosp.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        numQuartos = self.__ui.spinnerNumQuartosHosp.value()
        numPessoas = self.__ui.spinnerNumPessoasHosp.value()

        # Realizar a consulta
        try:
            hospedagens = self.requests.get_hospedagens(cidade, dataIni, dataFim, numQuartos, numPessoas)

            self.__ui.updateTableHospedagem(hospedagens)
        except Exception as e:
            print(e)

    def consultarPacote(self):
        origem = Cidade(self.__ui.choiceOrigemPacote.currentText())
        destino = Cidade(self.__ui.choiceDestinoPacote.currentText())
        dataIda = datetime.strptime(self.__ui.datePacoteIda.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        dataVolta = datetime.strptime(self.__ui.datePacoteVolta.date().toString("yyyy-MM-dd"), "%Y-%m-%d")
        numQuartos = self.__ui.spinnerNumQuartosPacote.value()
        numPessoas = self.__ui.spinnerNumPessoasPacote.value()

        # Realizar a consulta
        try:
            pacotes = self.requests.get_pacotes(origem, destino, dataIda, dataVolta, numPessoas, numQuartos)

            self.__ui.updateTableVooIdaPac(pacotes['voosIda'])
            self.__ui.updateTableVooVoltaPac(pacotes['voosVolta'])
            self.__ui.updateTableHospedagemPac(pacotes['hospedagens'])
        except Exception as e:
            print(e)

    def comprarVoo(self):
        numPessoas = self.__ui.spinnerNumPessoasVoo.value()
        tipoPassagem = TipoVoo.IDA_E_VOLTA if self.__ui.radioIdaEVoltaVoo.isChecked() else TipoVoo.SOMENTE_IDA

        linhaVooIda = self.__ui.tableVooIda.selectedItems()
        linhaVooVolta = self.__ui.tableVooVolta.selectedItems()

        if len(linhaVooIda) != 6 or (tipoPassagem == TipoVoo.IDA_E_VOLTA and len(linhaVooVolta) != 6):
            # TODO: alert
            print("Form inválido")
            return

        # Sim, isso é triste
        dictVooIda = {}
        for item in linhaVooIda:
            dictVooIda[item.column()] = item.text()
        dictVooVolta = {}
        for item in linhaVooVolta:
            dictVooVolta[item.column()] = item.text()

        vooIda = Voo(dictVooIda[0], Cidade[dictVooIda[1]], Cidade[dictVooIda[2]], datetime.strptime(dictVooIda[3], "%d/%m/%Y"), Dinheiro(dictVooIda[4]), numPessoas)
        vooVolta = Voo(dictVooVolta[0], Cidade[dictVooVolta[1]], Cidade[dictVooVolta[2]], datetime.strptime(dictVooVolta[3], "%d/%m/%Y"), Dinheiro(dictVooVolta[4]), numPessoas) if tipoPassagem == TipoVoo.IDA_E_VOLTA else None

        response = self.requests.put_passagem(vooIda, vooVolta, numPessoas)
        if response.text == "true":
            # TODO: alert
            print("Deu boa")
        else:
            # TODO: alert
            print("Compra falhou")

    def comprarHospedagem(self):
        numPessoas = self.__ui.spinnerNumPessoasHosp.value()
        numQuartos = self.__ui.spinnerNumQuartosHosp.value()

        linhaHotel = self.__ui.tableHospedagem.selectedItems()

        if len(linhaHotel) != 9:
            # TODO: alert
            print("Form inválido")
            return

        # Sim, isso é triste
        dictHotel = {}
        for item in linhaHotel:
            dictHotel[item.column()] = item.text()

        print(dictHotel)

        dataEntrada = datetime.strptime(dictHotel[4], "%d/%m/%Y")
        dataSaida = datetime.strptime(dictHotel[5], "%d/%m/%Y")
        hotel = HotelRet(dictHotel[0], dictHotel[1], Cidade[dictHotel[2]], numQuartos, numQuartos, dataEntrada, int(dictHotel[6]), Dinheiro(dictHotel[7]), Dinheiro(dictHotel[8]))
        print(hotel)

        response = self.requests.put_hospedagem(hotel, dataEntrada, dataSaida, numQuartos)
        if response.text == "true":
            # TODO: alert
            print("Deu boa")
        else:
            # TODO: alert
            print("Compra falhou")

    def comprarPacote(self):
        pass

    def teste(self):
        # FIXME: Exemplo de uso do webservice

        # Realizar a consulta
        passagens = self.requests.get_passagens(
            TipoVoo.IDA_E_VOLTA,
            Cidade.CURITIBA,
            Cidade.FLORIANOPOLIS,
            datetime.strptime("2018-10-17", "%Y-%m-%d"),
            datetime.strptime("2018-10-20", "%Y-%m-%d"),
            1
        )
        if not passagens:
            return
        print("#" + ("=" * 78) + "#\n")
        print("\n".join([str(p) for p in passagens]))
        print("\n#" + ("=" * 78) + "#")

        # Realizar a consulta
        hospedagens = self.requests.get_hospedagens(
            Cidade.CURITIBA,
            datetime.strptime("2018-10-17", "%Y-%m-%d"),
            datetime.strptime("2018-10-20", "%Y-%m-%d"),
            2,
            3
        )
        if not hospedagens:
            return
        print("#" + ("=" * 78) + "#\n")
        print("\n".join([str(p) for p in hospedagens]))
        print("\n#" + ("=" * 78) + "#")

        # Realizar a consulta
        pacotes = self.requests.get_pacotes(
            Cidade.CURITIBA,
            Cidade.FLORIANOPOLIS,
            datetime.strptime("2018-10-17", "%Y-%m-%d"),
            datetime.strptime("2018-10-20", "%Y-%m-%d"),
            2,
            3
        )
        if not pacotes:
            return
        print("#" + ("=" * 78) + "#\n")
        print("\n".join([str(p) for p in pacotes['voosIda']]))
        print("\n#" + ("-" * 78) + "#\n")
        print("\n".join([str(p) for p in pacotes['voosVolta']]))
        print("\n#" + ("-" * 78) + "#\n")
        print("\n".join([str(p) for p in pacotes['hospedagens']]))
        print("\n#" + ("=" * 78) + "#")

        # Comprar
        """
        print(self.requests.put_passagem(passagens[0], passagens[1], 1))
        
        print(self.requests.put_hospedagem(hospedagens[0], datetime.strptime("2018-10-17", "%Y-%m-%d"),
                                           datetime.strptime("2018-10-20", "%Y-%m-%d"), 2))

        print(self.requests.put_pacote(pacotes['voosIda'][0], pacotes['voosVolta'][0], pacotes['hospedagens'][1],
                                       datetime.strptime("2018-10-17", "%Y-%m-%d"),
                                       datetime.strptime("2018-10-20", "%Y-%m-%d"), 2, 2))
        """

# ==================================================================================================================== #
