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

import requests
import xmltodict
import json

from datetime import datetime

from src.enum.Cidade import Cidade
from src.enum.TipoVoo import TipoVoo

from src.model.Voo import Voo
from src.model.HotelRet import HotelRet
from src.model.Dinheiro import Dinheiro


class RequestController(object):
    BASE_URL = "http://localhost:8080/AgencyServer/webresources/server/"

    def __init__(self, url=BASE_URL):
        """
        Construtor padrão do controlador de requisições
        :param url: URL base de consulta
        :type url: str
        """
        self.__url = url

    @staticmethod
    def response_to_json(response: requests.Response):
        print("Response [Code %d]:" % response.status_code)
        print(response.raw.info())
        if response.status_code != 200:
            print("Error!")
            return False
        return json.loads(json.dumps(xmltodict.parse(response.content)))

    # ================================================================================================================ #

    def get_passagens(
            self,
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

        # Montar os parâmetros para a consulta
        data = {
            'tipo_passagem': str(tipoVoo.name),
            'origem': str(origem.name),
            'destino': str(destino.name),
            'data_ida': dataIda.strftime("%Y-%m-%d"),
            'data_volta': dataVolta.strftime("%Y-%m-%d"),
            'num_pessoas': numPessoas if numPessoas > 0 else 0
        }

        # Realizar a consulta e salvar o Response do servidor
        response = requests.get(self.__url + "consultar_passagens", params=data)
        result = RequestController.response_to_json(response)
        print("Response:\n\t", result)

        # Trabalhar os dados retornados
        voos = []
        for item in result['infoVooes']['infoVoo']:
            voo = Voo.parse(item)
            voos.append(voo)

        return voos

    def put_passagem(
            self,
            vooIda: Voo,
            vooVolta: Voo,
            numPessoas: int) -> requests.Response:
        """
        Função de compra de passagens
        :param tipoVoo: Tipo de Voo das passagens
        :type tipoVoo: TipoVoo
        :param origem: Cidade de origem de Voo das passagens
        :type origem: Cidade
        :param destino: Cidade de destino de Voo das passagens
        :type destino: Cidade
        :return: Response da requisição
        """

        # Montar os parâmetros para a compra
        data = {
            'tipo_passagem': str(TipoVoo.SOMENTE_IDA.name if vooVolta is None else TipoVoo.IDA_E_VOLTA.name),
            'id_voo_ida': vooIda.id if vooIda is not None else "",
            'id_voo_volta': vooVolta.id if vooVolta is not None else "",
            'num_pessoas': numPessoas
        }

        # Realizar a compra no servidor
        return requests.get(self.__url + "comprar_passagens", params=data)

    # ================================================================================================================ #

    def get_hospedagens(
            self,
            local: Cidade,
            dataInicio: datetime,
            dataFim: datetime,
            numQuartos: int,
            numPessoas: int):
        """
        Função de consulta dos hoteis por filtros
        :param local: Local do Hotel
        :type local: Cidade
        :param dataInicio: Data de entrada no Hotel
        :type dataInicio: datetime
        :param dataFim: Data de saída no Hotel
        :type dataFim: datetime
        :param numQuartos: Número de quartos desejados do Hotel
        :type numQuartos: int
        :param numPessoas: Número de pessoas por quarto do Hotel
        :type numPessoas: int
        :return:
        """

        # Montar os parâmetros para a consulta
        data = {
            'local': str(local.name),
            'data_ini': dataInicio.strftime("%Y-%m-%d"),
            'data_fim': dataFim.strftime("%Y-%m-%d"),
            'num_quartos': numQuartos if numQuartos > 0 else 0,
            'num_pessoas': numPessoas if numPessoas > 0 else 0
        }
        # Realizar a consulta e salvar o Response do servidor
        response = requests.get(self.__url + "consultar_hospedagens", params=data)
        result = RequestController.response_to_json(response)
        print("Response:\n\t", result)

        # Trabalhar os dados retornados
        hospedagens = []
        for item in result['infoHotelRets']['infoHotelRet']:
            hotel = HotelRet(
                int(item['@id']),
                item['nome'],
                Cidade[item['local']],
                int(item['numQuartos']),
                int(item['quartosDisponiveis']),
                datetime.strptime(item['dataEntrada'], "%Y-%m-%d"),
                int(item['numDiarias']),
                Dinheiro(item['precoDiaria']),
                Dinheiro(item['precoTotal'])
            )
            hospedagens.append(hotel)

        return hospedagens

    def put_hospedagem(
            self,
            hotel: HotelRet,
            dataInicio: datetime,
            dataFim: datetime,
            numQuartos: int) -> requests.Response:
        """
        Função de compra de passagens
        :param hotel: Hotel desejado
        :type hotel: HotelRet
        :param dataInicio: Data de entrada desejada
        :type dataInicio: datetime
        :param dataFim: Data de saida desejada
        :type dataFim: datetime
        :param numQuartos: Número de quartos desejados
        :type numQuartos: int
        :return: Response da requisição
        """

        # Montar os parâmetros para a compra
        data = {
            'id_hotel': hotel.id if hotel is not None else "",
            'data_ini': dataInicio.strftime("%Y-%m-%d"),
            'data_fim': dataFim.strftime("%Y-%m-%d"),
            'num_quartos': numQuartos
        }

        # Realizar a compra no servidor
        return requests.get(self.__url + "comprar_hospedagens", params=data)

    # ================================================================================================================ #

    def get_pacotes(
            self,
            origem: Cidade,
            destino: Cidade,
            dataIda: datetime,
            dataVolta: datetime,
            numPessoas: int,
            numQuartos: int):
        """
        Função de consulta dos pacotes por filtros
        :param origem: Cidade de origem de Voo das passagens
        :type origem: Cidade
        :param destino: Cidade de destino de Voo das passagens e local do Hotel
        :type destino: Cidade
        :param dataIda: Data de ida de Voo das passagens e início da reserva do Hotel
        :type dataIda: datetime
        :param dataVolta: Data de volta de Voo das passagens e fim da reserva do Hotel
        :type dataVolta: datetime
        :param numPessoas: Número de pessoas no Hotel e passagens
        :type numPessoas: int
        :param numQuartos: Número de quartos no Hotel
        :type numQuartos: int
        :return: Lista dos Voos parseados
        """

        # Montar os parâmetros para a consulta
        data = {
            'origem': str(origem.name),
            'destino': str(destino.name),
            'data_ida': dataIda.strftime("%Y-%m-%d"),
            'data_volta': dataVolta.strftime("%Y-%m-%d"),
            'num_pessoas': numPessoas if numPessoas > 0 else 0,
            'num_quartos': numQuartos if numQuartos > 0 else 0
        }
        # Realizar a consulta e salvar o Response do servidor
        response = requests.get(self.__url + "consultar_pacotes", params=data)
        result = RequestController.response_to_json(response)
        print("Response:\n\t", result)

        # Trabalhar os dados retornados
        data = {'voosIda': [], 'voosVolta': [], 'hospedagens': []}
        result = result['conjuntoPacote'] # ajustar o result

        # Parsear os Voos de Ida
        if 'voosIda' in result:
            r = result['voosIda'] if isinstance(result['voosIda'], list) else [result['voosIda']]
            for item in r:
                data['voosIda'].append(Voo.parse(item))
        # Parsear os Voos de Volta
        if 'voosVolta' in result:
            r = result['voosVolta'] if isinstance(result['voosVolta'], list) else [result['voosVolta']]
            for item in r:
                data['voosVolta'].append(Voo.parse(item))
        # Parsear os Hotéis
        if 'hospedagens' in result:
            r = result['hospedagens'] if isinstance(result['hospedagens'], list) else [result['hospedagens']]
            print(r)
            for item in r:
                data['hospedagens'].append(HotelRet.parse(item))

        return data

    def put_pacote(
            self,
            vooIda: Voo,
            vooVolta: Voo,
            hotel: HotelRet,
            dataIda: datetime,
            dataVolta: datetime,
            numQuartos: int,
            numPessoas: int) -> requests.Response:
        """
        Função de compra de pacote
        :param vooIda: Voo de ida desejado para o Pacote
        :type vooIda: Voo
        :param vooVolta: Voo de volta desejado para o Pacote
        :type vooVolta: Voo
        :param hotel: Hotel desejado para o Pacote
        :type hotel: HotelRet
        :param dataIda: Data de ida do Voo e entrada no Hotel
        :type dataIda: datetime
        :param dataVolta: Data de volta do Voo e saída do Hotel
        :type dataVolta: datetime
        :param numQuartos: Número de quartos para o Hotel do Pacote
        :type numQuartos: int
        :param numPessoas: Número de pessoas do Pacote
        :type numPessoas: int
        :return: Response da requisição
        """

        # Montar os parâmetros para a compra
        data = {
            'id_voo_ida': vooIda.id if vooIda is not None else "",
            'id_voo_volta': vooVolta.id if vooVolta is not None else "",
            'id_hotel': hotel.id,
            'data_ida': dataIda.strftime("%Y-%m-%d"),
            'data_volta': dataVolta.strftime("%Y-%m-%d"),
            'num_quartos': numQuartos if numQuartos > 0 else 0,
            'num_pessoas': numPessoas if numPessoas > 0 else 0
        }

        # Realizar a compra no servidor
        return requests.get(self.__url + "comprar_pacote", params=data)

# ==================================================================================================================== #
