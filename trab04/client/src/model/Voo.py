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

import datetime
from src.model.Cidade import Cidade


class Voo(object):

    def __init__(
            self,
            id: int,
            origem: Cidade,
            destino: Cidade,
            data: datetime,
            poltronas_disp: int):
        """
        Construtor padrão
        :param id: ID do voo
        :type id: int
        :param origem: Cidade de origem do voo
        :type origem: Cidade
        :param destino: Cidade de destino do voo
        :type destino: Cidade
        :param data: Data desse voo
        :type data: datetime
        :param poltronas_disp: Número de poltronas disponíveis nesse voo
        :type poltronas_disp: int
        """
        self.id = id
        self.origem = origem
        self.destino = destino
        self.data = data
        self.poltronas_disp = poltronas_disp

# ==================================================================================================================== #
