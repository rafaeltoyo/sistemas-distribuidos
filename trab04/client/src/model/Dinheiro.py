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


class Dinheiro(object):

    def __init__(self, value):
        """
        Construtor de um objeto que representa dinheiro
        :param value:
        """
        self.__value = float(value.replace('R$ ', '') if isinstance(value, str) else value)

    def __str__(self):
        return "R$ %.2f" % self.__value

    def __float__(self):
        return self.__value

# ==================================================================================================================== #
