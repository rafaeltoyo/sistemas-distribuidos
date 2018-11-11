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


class MainController(object):

    def __init__(self):
        self.requests = RequestController()

        # Ao invés dos [], pode passar argumentos
        self.__app = QtWidgets.QApplication([])

    def __del__(self):
        self.__app = None

    def run(self):
        window = QtWidgets.QMainWindow()

        self.__ui = Ui_MainWindow()
        self.__ui.setupUi(window)
        self.__ui.buttonConsultarVoo.clicked.connect(self.consultarVoo)

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
