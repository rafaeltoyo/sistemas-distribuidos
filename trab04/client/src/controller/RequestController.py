# ==================================================================================================================== #
#   Thiago Bispo dá o cu
# -------------------------------------------------------------------------------------------------------------------- #
#   Author
# ==================================================================================================================== #

import requests
import xml.etree.ElementTree as ET
import datetime

from src.model.Voo import Voo
from src.model.Cidade import Cidade
from src.model.Dinheiro import Dinheiro


BASE_URL = "http://localhost:8080/AgencyServer/webresources/server/"


class RequestController(object):

    def get_passagens(self):
        data = {
            'tipo_passagem': "IDA_E_VOLTA",
            'origem': "CURITIBA",
            'destino': "FLORIANOPOLIS",
            'data_ida': '2018-10-17',
            'data_volta': "2018-10-20",
            'num_pessoas': 1
        }
        # Realizar a consulta
        req = requests.get(BASE_URL + "consultar_passagens", params=data)

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
                datetime.datetime.strptime(str(voo[2].text), "%Y-%m-%d"),
                Dinheiro(voo[3].text),
                int(voo[4].text)
            )
            print(voo)

            #for info in voo:
            #    print(info.tag, info.text)

    def put_passagem(self):
        pass

    def get_hospedagens(self):
        pass

    def put_hospedagem(self):
        pass

# ==================================================================================================================== #
