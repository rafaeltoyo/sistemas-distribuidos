# ==================================================================================================================== #
#   Thiago Bispo dá o cu
# -------------------------------------------------------------------------------------------------------------------- #
#   Author
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
        passagens = self.requests.get_passagens(
            TipoVoo.IDA_E_VOLTA,
            Cidade.CURITIBA,
            Cidade.FLORIANOPOLIS,
            datetime.strptime("2018-10-17", "%Y-%m-%d"),
            datetime.strptime("2018-10-20", "%Y-%m-%d"),
            1
        )
        print("#" + ("=" * 78) + "#\n")
        print("\n".join([str(p) for p in passagens]))
        print("\n#" + ("=" * 78) + "#")

# ==================================================================================================================== #
