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
from src.enum.Cidade import Cidade
from src.model.Dinheiro import Dinheiro


class Voo(object):

    @staticmethod
    def parse(content):
        """
        Função de gerar um objeto Voo a partir do dicionário construído do XML do Response do servidor
        :param content: Dicionário dos dados para o Voo
        :type content: dict
        :return: Instância do Voo parseado
        :rtype: Voo
        """
        return Voo(
            int(content['@id']),
            Cidade[content['origem']],
            Cidade[content['destino']],
            datetime.strptime(content['data'], "%Y-%m-%d"),
            Dinheiro(content['precoPassagem']),
            int(content['poltronasDisp'])
        )

    def __init__(
            self,
            id: int,
            origem: Cidade,
            destino: Cidade,
            data: datetime,
            preco: Dinheiro,
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
        :param preco: Preço do voo
        :type preco: Dinheiro
        :param poltronas_disp: Número de poltronas disponíveis nesse voo
        :type poltronas_disp: int
        """
        self.id = id
        self.origem = origem
        self.destino = destino
        self.data = data
        self.preco = preco
        self.poltronas_disp = poltronas_disp

    def __str__(self):
        return "{} {} {} {} {} {}".format(
            self.id,
            self.origem,
            self.destino,
            self.data,
            self.preco,
            self.poltronas_disp
        )

# ==================================================================================================================== #
