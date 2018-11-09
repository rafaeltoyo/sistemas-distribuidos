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

import requests
import xml.etree.ElementTree as ET
from datetime import datetime

from src.enum.Cidade import Cidade
from src.enum.TipoVoo import TipoVoo

from src.model.Voo import Voo
from src.model.Dinheiro import Dinheiro


class RequestController(object):

    BASE_URL = "http://192.168.108.172:8080/AgencyServer/webresources/server/"

    def __init__(self, url=BASE_URL):
        """
        Construtor padrão do controlador de requisições
        :param url: URL base de consulta
        :type url: str
        """
        self.__url = url

    def get_passagens(self,
              tipoVoo: TipoVoo,
              origem: Cidade,
              destino: Cidade,
              dataIda: datetime,
              dataVolta: datetime,
              numPessoas: int):
        """
        Função de consulta das passagens por filtros
        :param tipoVoo: Tipo de Voo das passagens
        :type tipoVoo: TipoVoo
        :param origem: Cidade de origem de Voo das passagens
        :type origem: Cidade
        :param destino: Cidade de destino de Voo das passagens
        :type destino: Cidade
        :param dataIda: Data de ida de Voo das passagens
        :type dataIda: datetime
        :param dataVolta: Data de volta de Voo das passagens
        :type dataVolta: datetime
        :param numPessoas: Número de pessoas/passagens
        :type numPessoas: int
        :return: Lista dos Voos parseados
        """
        data = {
            'tipo_passagem': str(tipoVoo.name),
            'origem': str(origem.name),
            'destino': str(destino.name),
            'data_ida': dataIda.strftime("%Y-%m-%d"),
            'data_volta': dataVolta.strftime("%Y-%m-%d"),
            'num_pessoas': numPessoas if numPessoas > 0 else 0
        }
        # Realizar a consulta
        req = requests.get(self.__url + "consultar_passagens", params=data)

        # Ler o retorno XML
        result = ET.fromstring(req.content)

        # Iterar por cada item (Voo)
        voos = []

        for voo in result:

            voo_id = voo.attrib

            voo = Voo(
                voo.attrib['id'],
                Cidade[voo[0].text],
                Cidade[voo[1].text],
                datetime.strptime(str(voo[2].text), "%Y-%m-%d"),
                Dinheiro(voo[3].text),
                int(voo[4].text)
            )
            voos.append(voo)

        return voos

    def put_passagem(self):
        pass

    def get_hospedagens(self):
        data = {
            'tipo_passagem': "IDA_E_VOLTA",
            'origem': "CURITIBA",
            'destino': "FLORIANOPOLIS",
            'data_ida': '2018-10-17',
            'data_volta': "2018-10-20",
            'num_pessoas': 1
        }

    def put_hospedagem(self):
        pass

# ==================================================================================================================== #
