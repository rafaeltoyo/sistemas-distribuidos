# -*- coding: utf-8 -*-

# ==================================================================================================================== #
__author__ = "Rafael Hideo Toyomoto, Victor Barpp Gomes"
__copyright__ = "Copyright 2018, TBDC"
__credits__ = ["Rafael Hideo Toyomoto", "Victor Barpp Gomes"]

__license__ = "TBDC"
__version__ = "1.0"
__maintainer__ = "Victor Barpp Gomes"
__email__ = ""
__status__ = "Production"
# ==================================================================================================================== #

# Form implementation generated from reading ui file 'interface.ui'
#
# Created by: PyQt5 UI code generator 5.11.3
#
# WARNING! All changes made in this file will be lost!

from datetime import timedelta

from PyQt5 import QtCore, QtGui, QtWidgets

from src.enum.Cidade import Cidade
from src.model.HotelRet import HotelRet
from src.model.Voo import Voo


class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(800, 600)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.verticalLayout = QtWidgets.QVBoxLayout(self.centralwidget)
        self.verticalLayout.setObjectName("verticalLayout")
        self.tabWidget = QtWidgets.QTabWidget(self.centralwidget)
        self.tabWidget.setObjectName("tabWidget")
        self.tabVoo = QtWidgets.QWidget()
        self.tabVoo.setObjectName("tabVoo")
        self.vboxTabVoo = QtWidgets.QVBoxLayout(self.tabVoo)
        self.vboxTabVoo.setObjectName("vboxTabVoo")
        self.gridFormVoo = QtWidgets.QGridLayout()
        self.gridFormVoo.setObjectName("gridFormVoo")
        self.labelVoos = QtWidgets.QLabel(self.tabVoo)
        font = QtGui.QFont()
        font.setPointSize(20)
        font.setBold(True)
        font.setWeight(75)
        self.labelVoos.setFont(font)
        self.labelVoos.setObjectName("labelVoos")
        self.gridFormVoo.addWidget(self.labelVoos, 0, 0, 1, 1)
        self.labelOrigemVoo = QtWidgets.QLabel(self.tabVoo)
        self.labelOrigemVoo.setObjectName("labelOrigemVoo")
        self.gridFormVoo.addWidget(self.labelOrigemVoo, 1, 0, 1, 1)
        self.choiceOrigemVoo = QtWidgets.QComboBox(self.tabVoo)
        self.choiceOrigemVoo.setObjectName("choiceOrigemVoo")
        self.gridFormVoo.addWidget(self.choiceOrigemVoo, 1, 1, 1, 1)
        self.labelDestinoVoo = QtWidgets.QLabel(self.tabVoo)
        self.labelDestinoVoo.setObjectName("labelDestinoVoo")
        self.gridFormVoo.addWidget(self.labelDestinoVoo, 1, 2, 1, 1)
        self.choiceDestinoVoo = QtWidgets.QComboBox(self.tabVoo)
        self.choiceDestinoVoo.setObjectName("choiceDestinoVoo")
        self.gridFormVoo.addWidget(self.choiceDestinoVoo, 1, 3, 1, 1)
        self.labelDataIdaVoo = QtWidgets.QLabel(self.tabVoo)
        self.labelDataIdaVoo.setObjectName("labelDataIdaVoo")
        self.gridFormVoo.addWidget(self.labelDataIdaVoo, 2, 0, 1, 1)
        self.dateVooIda = QtWidgets.QDateEdit(self.tabVoo)
        self.dateVooIda.setObjectName("dateVooIda")
        self.gridFormVoo.addWidget(self.dateVooIda, 2, 1, 1, 1)
        self.labelDataVoltaVoo = QtWidgets.QLabel(self.tabVoo)
        self.labelDataVoltaVoo.setObjectName("labelDataVoltaVoo")
        self.gridFormVoo.addWidget(self.labelDataVoltaVoo, 2, 2, 1, 1)
        self.labelNumPessoasVoo = QtWidgets.QLabel(self.tabVoo)
        self.labelNumPessoasVoo.setObjectName("labelNumPessoasVoo")
        self.gridFormVoo.addWidget(self.labelNumPessoasVoo, 3, 0, 1, 1)
        self.spinnerNumPessoasVoo = QtWidgets.QSpinBox(self.tabVoo)
        self.spinnerNumPessoasVoo.setObjectName("spinnerNumPessoasVoo")
        self.gridFormVoo.addWidget(self.spinnerNumPessoasVoo, 3, 1, 1, 1)
        self.buttonConsultarVoo = QtWidgets.QPushButton(self.tabVoo)
        self.buttonConsultarVoo.setObjectName("buttonConsultarVoo")
        self.gridFormVoo.addWidget(self.buttonConsultarVoo, 3, 4, 1, 1)
        self.dateVooVolta = QtWidgets.QDateEdit(self.tabVoo)
        self.dateVooVolta.setObjectName("dateVooVolta")
        self.gridFormVoo.addWidget(self.dateVooVolta, 2, 3, 1, 1)
        self.gboxTipoVoo = QtWidgets.QGroupBox(self.tabVoo)
        self.gboxTipoVoo.setTitle("")
        self.gboxTipoVoo.setFlat(True)
        self.gboxTipoVoo.setObjectName("gboxTipoVoo")
        self.verticalLayout_4 = QtWidgets.QVBoxLayout(self.gboxTipoVoo)
        self.verticalLayout_4.setContentsMargins(0, 0, 0, 0)
        self.verticalLayout_4.setSpacing(0)
        self.verticalLayout_4.setObjectName("verticalLayout_4")
        self.radioSomenteIdaVoo = QtWidgets.QRadioButton(self.gboxTipoVoo)
        self.radioSomenteIdaVoo.setObjectName("radioSomenteIdaVoo")
        self.verticalLayout_4.addWidget(self.radioSomenteIdaVoo)
        self.radioIdaEVoltaVoo = QtWidgets.QRadioButton(self.gboxTipoVoo)
        self.radioIdaEVoltaVoo.setObjectName("radioIdaEVoltaVoo")
        self.verticalLayout_4.addWidget(self.radioIdaEVoltaVoo)
        self.gridFormVoo.addWidget(self.gboxTipoVoo, 1, 4, 2, 1)
        self.vboxTabVoo.addLayout(self.gridFormVoo)
        self.vboxVooIda = QtWidgets.QVBoxLayout()
        self.vboxVooIda.setObjectName("vboxVooIda")
        self.labelVooIda = QtWidgets.QLabel(self.tabVoo)
        font = QtGui.QFont()
        font.setPointSize(16)
        self.labelVooIda.setFont(font)
        self.labelVooIda.setObjectName("labelVooIda")
        self.vboxVooIda.addWidget(self.labelVooIda)
        self.tableVooIda = QtWidgets.QTableWidget(self.tabVoo)
        self.tableVooIda.setObjectName("tableVooIda")
        self.tableVooIda.setColumnCount(0)
        self.tableVooIda.setRowCount(0)
        self.vboxVooIda.addWidget(self.tableVooIda)
        self.vboxTabVoo.addLayout(self.vboxVooIda)
        self.vboxVooVolta = QtWidgets.QVBoxLayout()
        self.vboxVooVolta.setObjectName("vboxVooVolta")
        self.labelVooVolta = QtWidgets.QLabel(self.tabVoo)
        font = QtGui.QFont()
        font.setPointSize(16)
        self.labelVooVolta.setFont(font)
        self.labelVooVolta.setObjectName("labelVooVolta")
        self.vboxVooVolta.addWidget(self.labelVooVolta)
        self.tableVooVolta = QtWidgets.QTableWidget(self.tabVoo)
        self.tableVooVolta.setObjectName("tableVooVolta")
        self.tableVooVolta.setColumnCount(0)
        self.tableVooVolta.setRowCount(0)
        self.vboxVooVolta.addWidget(self.tableVooVolta)
        self.vboxTabVoo.addLayout(self.vboxVooVolta)
        self.buttonComprarVoo = QtWidgets.QPushButton(self.tabVoo)
        self.buttonComprarVoo.setObjectName("buttonComprarVoo")
        self.vboxTabVoo.addWidget(self.buttonComprarVoo)
        self.tabWidget.addTab(self.tabVoo, "")
        self.tabHospedagem = QtWidgets.QWidget()
        self.tabHospedagem.setObjectName("tabHospedagem")
        self.verticalLayout_5 = QtWidgets.QVBoxLayout(self.tabHospedagem)
        self.verticalLayout_5.setObjectName("verticalLayout_5")
        self.gridFormHosp = QtWidgets.QGridLayout()
        self.gridFormHosp.setObjectName("gridFormHosp")
        self.labelCidadeHosp = QtWidgets.QLabel(self.tabHospedagem)
        self.labelCidadeHosp.setObjectName("labelCidadeHosp")
        self.gridFormHosp.addWidget(self.labelCidadeHosp, 1, 0, 1, 1)
        self.choiceCidadeHosp = QtWidgets.QComboBox(self.tabHospedagem)
        self.choiceCidadeHosp.setObjectName("choiceCidadeHosp")
        self.gridFormHosp.addWidget(self.choiceCidadeHosp, 1, 1, 1, 1)
        self.labelDataIniHosp = QtWidgets.QLabel(self.tabHospedagem)
        self.labelDataIniHosp.setObjectName("labelDataIniHosp")
        self.gridFormHosp.addWidget(self.labelDataIniHosp, 2, 0, 1, 1)
        self.dateChegadaHosp = QtWidgets.QDateEdit(self.tabHospedagem)
        self.dateChegadaHosp.setObjectName("dateChegadaHosp")
        self.gridFormHosp.addWidget(self.dateChegadaHosp, 2, 1, 1, 1)
        self.labelDataFimHosp = QtWidgets.QLabel(self.tabHospedagem)
        self.labelDataFimHosp.setObjectName("labelDataFimHosp")
        self.gridFormHosp.addWidget(self.labelDataFimHosp, 2, 2, 1, 1)
        self.dateSaidaHosp = QtWidgets.QDateEdit(self.tabHospedagem)
        self.dateSaidaHosp.setObjectName("dateSaidaHosp")
        self.gridFormHosp.addWidget(self.dateSaidaHosp, 2, 3, 1, 1)
        self.labelNumQuartosHosp = QtWidgets.QLabel(self.tabHospedagem)
        self.labelNumQuartosHosp.setObjectName("labelNumQuartosHosp")
        self.gridFormHosp.addWidget(self.labelNumQuartosHosp, 3, 0, 1, 1)
        self.spinnerNumQuartosHosp = QtWidgets.QSpinBox(self.tabHospedagem)
        self.spinnerNumQuartosHosp.setObjectName("spinnerNumQuartosHosp")
        self.gridFormHosp.addWidget(self.spinnerNumQuartosHosp, 3, 1, 1, 1)
        self.labelNumPessoasHosp = QtWidgets.QLabel(self.tabHospedagem)
        self.labelNumPessoasHosp.setObjectName("labelNumPessoasHosp")
        self.gridFormHosp.addWidget(self.labelNumPessoasHosp, 3, 2, 1, 1)
        self.spinnerNumPessoasHosp = QtWidgets.QSpinBox(self.tabHospedagem)
        self.spinnerNumPessoasHosp.setObjectName("spinnerNumPessoasHosp")
        self.gridFormHosp.addWidget(self.spinnerNumPessoasHosp, 3, 3, 1, 1)
        self.buttonConsultarHosp = QtWidgets.QPushButton(self.tabHospedagem)
        self.buttonConsultarHosp.setObjectName("buttonConsultarHosp")
        self.gridFormHosp.addWidget(self.buttonConsultarHosp, 3, 4, 1, 1)
        self.labelHosp = QtWidgets.QLabel(self.tabHospedagem)
        font = QtGui.QFont()
        font.setPointSize(20)
        font.setBold(True)
        font.setWeight(75)
        self.labelHosp.setFont(font)
        self.labelHosp.setObjectName("labelHosp")
        self.gridFormHosp.addWidget(self.labelHosp, 0, 0, 1, 2)
        self.verticalLayout_5.addLayout(self.gridFormHosp)
        self.tableHospedagem = QtWidgets.QTableWidget(self.tabHospedagem)
        self.tableHospedagem.setObjectName("tableHospedagem")
        self.tableHospedagem.setColumnCount(0)
        self.tableHospedagem.setRowCount(0)
        self.verticalLayout_5.addWidget(self.tableHospedagem)
        self.buttonComprarHosp = QtWidgets.QPushButton(self.tabHospedagem)
        self.buttonComprarHosp.setObjectName("buttonComprarHosp")
        self.verticalLayout_5.addWidget(self.buttonComprarHosp)
        self.tabWidget.addTab(self.tabHospedagem, "")
        self.tabPacote = QtWidgets.QWidget()
        self.tabPacote.setObjectName("tabPacote")
        self.verticalLayout_10 = QtWidgets.QVBoxLayout(self.tabPacote)
        self.verticalLayout_10.setObjectName("verticalLayout_10")
        self.gridFormPacote = QtWidgets.QGridLayout()
        self.gridFormPacote.setObjectName("gridFormPacote")
        self.labelPacote = QtWidgets.QLabel(self.tabPacote)
        font = QtGui.QFont()
        font.setPointSize(20)
        font.setBold(True)
        font.setWeight(75)
        self.labelPacote.setFont(font)
        self.labelPacote.setObjectName("labelPacote")
        self.gridFormPacote.addWidget(self.labelPacote, 0, 0, 1, 1)
        self.labelOrigemPacote = QtWidgets.QLabel(self.tabPacote)
        self.labelOrigemPacote.setObjectName("labelOrigemPacote")
        self.gridFormPacote.addWidget(self.labelOrigemPacote, 1, 0, 1, 1)
        self.choiceOrigemPacote = QtWidgets.QComboBox(self.tabPacote)
        self.choiceOrigemPacote.setObjectName("choiceOrigemPacote")
        self.gridFormPacote.addWidget(self.choiceOrigemPacote, 1, 1, 1, 1)
        self.labelDestinoPacote = QtWidgets.QLabel(self.tabPacote)
        self.labelDestinoPacote.setObjectName("labelDestinoPacote")
        self.gridFormPacote.addWidget(self.labelDestinoPacote, 1, 2, 1, 1)
        self.choiceDestinoPacote = QtWidgets.QComboBox(self.tabPacote)
        self.choiceDestinoPacote.setObjectName("choiceDestinoPacote")
        self.gridFormPacote.addWidget(self.choiceDestinoPacote, 1, 3, 1, 1)
        self.labelDataIdaPacote = QtWidgets.QLabel(self.tabPacote)
        self.labelDataIdaPacote.setObjectName("labelDataIdaPacote")
        self.gridFormPacote.addWidget(self.labelDataIdaPacote, 2, 0, 1, 1)
        self.datePacoteIda = QtWidgets.QDateEdit(self.tabPacote)
        self.datePacoteIda.setObjectName("datePacoteIda")
        self.gridFormPacote.addWidget(self.datePacoteIda, 2, 1, 1, 1)
        self.labelDataVoltaPacote = QtWidgets.QLabel(self.tabPacote)
        self.labelDataVoltaPacote.setObjectName("labelDataVoltaPacote")
        self.gridFormPacote.addWidget(self.labelDataVoltaPacote, 2, 2, 1, 1)
        self.labelNumQuartosPacote = QtWidgets.QLabel(self.tabPacote)
        self.labelNumQuartosPacote.setObjectName("labelNumQuartosPacote")
        self.gridFormPacote.addWidget(self.labelNumQuartosPacote, 3, 0, 1, 1)
        self.spinnerNumQuartosPacote = QtWidgets.QSpinBox(self.tabPacote)
        self.spinnerNumQuartosPacote.setObjectName("spinnerNumQuartosPacote")
        self.gridFormPacote.addWidget(self.spinnerNumQuartosPacote, 3, 1, 1, 1)
        self.buttonConsultarPacote = QtWidgets.QPushButton(self.tabPacote)
        self.buttonConsultarPacote.setObjectName("buttonConsultarPacote")
        self.gridFormPacote.addWidget(self.buttonConsultarPacote, 3, 4, 1, 1)
        self.datePacoteVolta = QtWidgets.QDateEdit(self.tabPacote)
        self.datePacoteVolta.setObjectName("datePacoteVolta")
        self.gridFormPacote.addWidget(self.datePacoteVolta, 2, 3, 1, 1)
        self.labelNumPessoasPacote = QtWidgets.QLabel(self.tabPacote)
        self.labelNumPessoasPacote.setObjectName("labelNumPessoasPacote")
        self.gridFormPacote.addWidget(self.labelNumPessoasPacote, 3, 2, 1, 1)
        self.spinnerNumPessoasPacote = QtWidgets.QSpinBox(self.tabPacote)
        self.spinnerNumPessoasPacote.setObjectName("spinnerNumPessoasPacote")
        self.gridFormPacote.addWidget(self.spinnerNumPessoasPacote, 3, 3, 1, 1)
        self.verticalLayout_10.addLayout(self.gridFormPacote)
        self.vboxVooIdaPac = QtWidgets.QVBoxLayout()
        self.vboxVooIdaPac.setObjectName("vboxVooIdaPac")
        self.labelVooIdaPac = QtWidgets.QLabel(self.tabPacote)
        self.labelVooIdaPac.setObjectName("labelVooIdaPac")
        self.vboxVooIdaPac.addWidget(self.labelVooIdaPac)
        self.tableVooIdaPac = QtWidgets.QTableWidget(self.tabPacote)
        self.tableVooIdaPac.setObjectName("tableVooIdaPac")
        self.vboxVooIdaPac.addWidget(self.tableVooIdaPac)
        self.verticalLayout_10.addLayout(self.vboxVooIdaPac)
        self.vboxVooVoltaPac = QtWidgets.QVBoxLayout()
        self.vboxVooVoltaPac.setObjectName("vboxVooVoltaPac")
        self.labelVooVoltaPac = QtWidgets.QLabel(self.tabPacote)
        self.labelVooVoltaPac.setObjectName("labelVooVoltaPac")
        self.vboxVooVoltaPac.addWidget(self.labelVooVoltaPac)
        self.tableVooVoltaPac = QtWidgets.QTableWidget(self.tabPacote)
        self.tableVooVoltaPac.setObjectName("tableVooVoltaPac")
        self.vboxVooVoltaPac.addWidget(self.tableVooVoltaPac)
        self.verticalLayout_10.addLayout(self.vboxVooVoltaPac)
        self.vboxHospedagemPac = QtWidgets.QVBoxLayout()
        self.vboxHospedagemPac.setObjectName("vboxHospedagemPac")
        self.labelHospedagemPac = QtWidgets.QLabel(self.tabPacote)
        self.labelHospedagemPac.setObjectName("labelHospedagemPac")
        self.vboxHospedagemPac.addWidget(self.labelHospedagemPac)
        self.tableHospedagemPac = QtWidgets.QTableWidget(self.tabPacote)
        self.tableHospedagemPac.setObjectName("tableHospedagemPac")
        self.vboxHospedagemPac.addWidget(self.tableHospedagemPac)
        self.verticalLayout_10.addLayout(self.vboxHospedagemPac)
        self.buttonComprarPacote = QtWidgets.QPushButton(self.tabPacote)
        self.buttonComprarPacote.setObjectName("buttonComprarPacote")
        self.verticalLayout_10.addWidget(self.buttonComprarPacote)
        self.tabWidget.addTab(self.tabPacote, "")
        self.verticalLayout.addWidget(self.tabWidget)
        MainWindow.setCentralWidget(self.centralwidget)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)

        # Ajustar os textos dos elementos
        self.__retranslateUi(MainWindow)

        # Ir para aba 0 (primeira)
        self.tabWidget.setCurrentIndex(0)

        # Coloca a data atual nos seletores de data
        self.dateChegadaHosp.setDate(QtCore.QDate.currentDate())
        self.datePacoteIda.setDate(QtCore.QDate.currentDate())
        self.datePacoteVolta.setDate(QtCore.QDate.currentDate())
        self.dateSaidaHosp.setDate(QtCore.QDate.currentDate())
        self.dateVooIda.setDate(QtCore.QDate.currentDate())
        self.dateVooVolta.setDate(QtCore.QDate.currentDate())

        # Habilita calendário
        self.dateChegadaHosp.setCalendarPopup(True)
        self.datePacoteIda.setCalendarPopup(True)
        self.datePacoteVolta.setCalendarPopup(True)
        self.dateSaidaHosp.setCalendarPopup(True)
        self.dateVooIda.setCalendarPopup(True)
        self.dateVooVolta.setCalendarPopup(True)

        # Seleciona "Somente ida" por padrão nos botões radio
        self.radioSomenteIdaVoo.setChecked(True)

        # Modo de seleção das tabelas
        self.tableVooIda.setSelectionMode(QtWidgets.QAbstractItemView.SingleSelection)
        self.tableVooIda.setSelectionBehavior(QtWidgets.QAbstractItemView.SelectRows)
        self.tableVooVolta.setSelectionMode(QtWidgets.QAbstractItemView.SingleSelection)
        self.tableVooVolta.setSelectionBehavior(QtWidgets.QAbstractItemView.SelectRows)
        self.tableHospedagem.setSelectionMode(QtWidgets.QAbstractItemView.SingleSelection)
        self.tableHospedagem.setSelectionBehavior(QtWidgets.QAbstractItemView.SelectRows)
        self.tableVooIdaPac.setSelectionMode(QtWidgets.QAbstractItemView.SingleSelection)
        self.tableVooIdaPac.setSelectionBehavior(QtWidgets.QAbstractItemView.SelectRows)
        self.tableVooVoltaPac.setSelectionMode(QtWidgets.QAbstractItemView.SingleSelection)
        self.tableVooVoltaPac.setSelectionBehavior(QtWidgets.QAbstractItemView.SelectRows)
        self.tableHospedagemPac.setSelectionMode(QtWidgets.QAbstractItemView.SingleSelection)
        self.tableHospedagemPac.setSelectionBehavior(QtWidgets.QAbstractItemView.SelectRows)

        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def __retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate

        # ============================================================================================================ #
        #   Text
        MainWindow.setWindowTitle(_translate("MainWindow", "MainWindow"))
        self.labelVoos.setText(_translate("MainWindow", "Voos"))
        self.labelOrigemVoo.setText(_translate("MainWindow", "Cidade de origem:"))
        self.labelDestinoVoo.setText(_translate("MainWindow", "Cidade de destino:"))
        self.labelDataIdaVoo.setText(_translate("MainWindow", "Data de ida:"))
        self.labelDataVoltaVoo.setText(_translate("MainWindow", "Data de volta:"))
        self.labelNumPessoasVoo.setText(_translate("MainWindow", "Número de pessoas:"))
        self.buttonConsultarVoo.setText(_translate("MainWindow", "Consultar"))
        self.radioSomenteIdaVoo.setText(_translate("MainWindow", "Somente ida"))
        self.radioIdaEVoltaVoo.setText(_translate("MainWindow", "Ida e volta"))
        self.labelVooIda.setText(_translate("MainWindow", "Voos de ida"))
        self.labelVooVolta.setText(_translate("MainWindow", "Voos de volta"))
        self.buttonComprarVoo.setText(_translate("MainWindow", "Comprar"))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tabVoo), _translate("MainWindow", "Voo"))
        self.labelCidadeHosp.setText(_translate("MainWindow", "Cidade:"))
        self.labelDataIniHosp.setText(_translate("MainWindow", "Data de chegada:"))
        self.labelDataFimHosp.setText(_translate("MainWindow", "Data de saída:"))
        self.labelNumQuartosHosp.setText(_translate("MainWindow", "Número de quartos:"))
        self.labelNumPessoasHosp.setText(_translate("MainWindow", "Número de pessoas:"))
        self.buttonConsultarHosp.setText(_translate("MainWindow", "Consultar"))
        self.labelHosp.setText(_translate("MainWindow", "Hospedagens"))
        self.buttonComprarHosp.setText(_translate("MainWindow", "Comprar"))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tabHospedagem), _translate("MainWindow", "Hospedagem"))
        self.labelPacote.setText(_translate("MainWindow", "Pacotes"))
        self.labelOrigemPacote.setText(_translate("MainWindow", "Cidade de origem:"))
        self.labelDestinoPacote.setText(_translate("MainWindow", "Cidade de destino:"))
        self.labelDataIdaPacote.setText(_translate("MainWindow", "Data de ida:"))
        self.labelDataVoltaPacote.setText(_translate("MainWindow", "Data de volta:"))
        self.labelNumQuartosPacote.setText(_translate("MainWindow", "Número de quartos:"))
        self.buttonConsultarPacote.setText(_translate("MainWindow", "Consultar"))
        self.labelNumPessoasPacote.setText(_translate("MainWindow", "Número de pessoas:"))
        self.labelVooIdaPac.setText(_translate("MainWindow", "Voo de ida"))
        self.labelVooVoltaPac.setText(_translate("MainWindow", "Voo de volta"))
        self.labelHospedagemPac.setText(_translate("MainWindow", "Hospedagem"))
        self.buttonComprarPacote.setText(_translate("MainWindow", "Comprar"))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tabPacote), _translate("MainWindow", "Pacote"))

        # ============================================================================================================ #
        #   Combobox
        self.choiceOrigemVoo.addItems([item.value for item in Cidade])
        self.choiceDestinoVoo.addItems([item.value for item in Cidade])
        self.choiceCidadeHosp.addItems([item.value for item in Cidade])
        self.choiceDestinoPacote.addItems([item.value for item in Cidade])
        self.choiceOrigemPacote.addItems([item.value for item in Cidade])

        # ============================================================================================================ #
        #   Table
        for iter_table in [self.tableVooIda, self.tableVooVolta, self.tableVooIdaPac, self.tableVooVoltaPac]:
            iter_table.setColumnCount(6)
            iter_table.setHorizontalHeaderLabels(["ID", "Origem", "Destino", "Data", "Preço", "Polt. disp."])
        for iter_table in [self.tableHospedagem, self.tableHospedagemPac]:
            iter_table.setColumnCount(9)
            iter_table.setHorizontalHeaderLabels(
                ["ID", "Nome", "Local", "Quartos", "Data Entrada", "Data Saída", "Diárias", "Preço por dia",
                 "Preço total"])

    def __updateTableVoo(self, table: QtWidgets.QTableWidget, data):
        table.clearContents()
        table.setRowCount(len(data))
        row_i = 0

        for item in data:
            if not isinstance(item, Voo):
                continue
            table.setItem(row_i, 0, QtWidgets.QTableWidgetItem(str(item.id)))
            table.setItem(row_i, 1, QtWidgets.QTableWidgetItem(str(item.origem.name)))
            table.setItem(row_i, 2, QtWidgets.QTableWidgetItem(str(item.destino.name)))
            table.setItem(row_i, 3, QtWidgets.QTableWidgetItem(item.data.strftime("%d/%m/%Y")))
            table.setItem(row_i, 4, QtWidgets.QTableWidgetItem(str(item.preco)))
            table.setItem(row_i, 5, QtWidgets.QTableWidgetItem(str(item.poltronas_disp)))
            row_i += 1

    def __updateTableHospedagem(self, table: QtWidgets.QTableWidget, data):
        table.clearContents()
        table.setRowCount(len(data))
        row_i = 0

        for item in data:
            if not isinstance(item, HotelRet):
                continue
            table.setItem(row_i, 0, QtWidgets.QTableWidgetItem(str(item.id)))
            table.setItem(row_i, 1, QtWidgets.QTableWidgetItem(item.nome))
            table.setItem(row_i, 2, QtWidgets.QTableWidgetItem(item.local.name))
            table.setItem(row_i, 3, QtWidgets.QTableWidgetItem(str(item.quartos_disp) + "/" + str(item.num_quartos)))
            table.setItem(row_i, 4, QtWidgets.QTableWidgetItem(item.data_entrada.strftime("%d/%m/%Y")))
            table.setItem(row_i, 5, QtWidgets.QTableWidgetItem((item.data_entrada + timedelta(days=item.num_diarias)).strftime("%d/%m/%Y")))
            table.setItem(row_i, 6, QtWidgets.QTableWidgetItem(str(item.num_diarias)))
            table.setItem(row_i, 7, QtWidgets.QTableWidgetItem(str(item.preco_diaria)))
            table.setItem(row_i, 8, QtWidgets.QTableWidgetItem(str(item.preco_total)))
            row_i += 1

    def updateTableVooIda(self, data):
        self.__updateTableVoo(self.tableVooIda, data)

    def updateTableVooVolta(self, data):
        self.__updateTableVoo(self.tableVooVolta, data)

    def updateTableVooIdaPac(self, data):
        self.__updateTableVoo(self.tableVooIdaPac, data)

    def updateTableVooVoltaPac(self, data):
        self.__updateTableVoo(self.tableVooVoltaPac, data)

    def updateTableHospedagem(self, data):
        self.__updateTableHospedagem(self.tableHospedagem, data)

    def updateTableHospedagemPac(self, data):
        self.__updateTableHospedagem(self.tableHospedagemPac, data)
