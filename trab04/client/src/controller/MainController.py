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

from src.controller.RequestController import RequestController
from src.enum.Cidade import Cidade
from src.enum.TipoVoo import TipoVoo


class MainController(object):

    def __init__(self):
        self.requests = RequestController()

    def run(self):
        # provisório
        self.teste()

    def teste(self):

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
        print(self.requests.put_passagem(passagens[0], passagens[1], 2))
        
        print(self.requests.put_hospedagem(hospedagens[0], datetime.strptime("2018-10-17", "%Y-%m-%d"),
                                           datetime.strptime("2018-10-20", "%Y-%m-%d"), 2))
        
        print(self.requests.put_pacote(pacotes['voosIda'][0], pacotes['voosVolta'][0], pacotes['hospedagens'][1],
                                       datetime.strptime("2018-10-17", "%Y-%m-%d"),
                                       datetime.strptime("2018-10-20", "%Y-%m-%d"), 2, 2))
        """

# ==================================================================================================================== #
