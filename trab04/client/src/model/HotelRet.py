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


class HotelRet(object):

    def __init__(self,
             id: int,
             nome: str,
             local: Cidade,
             num_quartos: int,
             quartos_disp: int,
             data_entrada: datetime,
             num_diarias: int,
             preco_diaria: Dinheiro,
             preco_total: Dinheiro):
        """

        :param id: ID do hotel
        :type id: int
        :param nome: Nome do hotel
        :type nome: str
        :param local: Local do hotel
        :type local: Cidade
        :param num_quartos: Número de quartos totais do hotel
        :type num_quartos: int
        :param quartos_disp: Número de quartos disponíveis do hotel
        :type quartos_disp: int
        :param data_entrada: Data de entrada no hotel
        :type data_entrada: datetime
        :param num_diarias: Número de diárias no hotel
        :type num_diarias: int
        :param preco_diaria: Preço da diária do hotel
        :type preco_diaria: Dinheiro
        :param preco_total: Preço total das diárias no hotel
        :type preco_total: Dinheiro
        """
        self.id = id
        self.nome = nome
        self.local = local
        self.num_quartos = num_quartos
        self.quartos_disp = quartos_disp
        self.data_entrada = data_entrada
        self.num_diarias = num_diarias
        self.preco_diaria = preco_diaria
        self.preco_total = preco_total


# ==================================================================================================================== #


def create_from_XML(xml) -> HotelRet:

    id = 1
    nome = "teste"
    local = Cidade.CURITIBA
    num_quartos = 10
    quartos_disp = 10
    data_entrada = datetime.now()
    num_diarias = 10
    preco_diaria = Dinheiro()
    preco_total = Dinheiro()

    return HotelRet(id, nome, local, num_quartos, quartos_disp, data_entrada, num_diarias, preco_diaria, preco_total)

# ==================================================================================================================== #
