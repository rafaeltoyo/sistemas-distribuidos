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
        self.__value = float(value.replace('R$ ', ''))

    def __str__(self):
        return str(self.__value)

    def __float__(self):
        return self.__value

# ==================================================================================================================== #
