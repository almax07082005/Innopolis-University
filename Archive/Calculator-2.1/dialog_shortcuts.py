import PyQt5.QtWidgets as w
from PyQt5 import uic
from PyQt5.QtGui import QIcon


class Shortcuts(w.QDialog):
    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):
        uic.loadUi('shortcuts.ui', self)
        self.setWindowIcon(QIcon('icon.ico'))
