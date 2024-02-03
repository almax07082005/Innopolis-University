import sys
import PyQt5.QtWidgets as w
from PyQt5.QtGui import QIcon, QDesktopServices, QKeySequence
from PyQt5 import uic
from PyQt5.QtCore import QUrl
import math
import operator
from dialog_shortcuts import Shortcuts
from os import system
from pyperclip import copy, paste

sys.set_int_max_str_digits(2000000000)


def pow(a, b):
	if not a and not b:
		raise ValueError
	return a ** b


def root(a, b):
	if a < 0 or b <= 1:
		raise ValueError
	return pow(a, 1 / b)


class MainWindow(w.QMainWindow):
	def __init__(self):
		super().__init__()
		self.initUI()

	def initUI(self):
		self.OPERATIONS = '+-%log_base n_root^fact()1/div' + '\u00F7' + '\u00D7'
		self.DICT_OPERATIONS = {
			'+': operator.add,
			'-': operator.sub,
			'\u00D7': operator.mul,
			'\u00F7': operator.truediv,
			'%': operator.mod,
			'^': pow,
			'log_base': math.log,
			'n_root': root
		}

		uic.loadUi('light.ui', self) if open('theme.txt').readline() == 'light' else uic.loadUi('dark.ui', self)
		self.setWindowIcon(QIcon('icon.ico'))
		
		self.errors.hide()
		self.history.setText('\nThere\'s no history yet')

		self.button_0.clicked.connect(self.take_digit)
		self.button_1.clicked.connect(self.take_digit)
		self.button_2.clicked.connect(self.take_digit)
		self.button_3.clicked.connect(self.take_digit)
		self.button_4.clicked.connect(self.take_digit)
		self.button_5.clicked.connect(self.take_digit)
		self.button_6.clicked.connect(self.take_digit)
		self.button_7.clicked.connect(self.take_digit)
		self.button_8.clicked.connect(self.take_digit)
		self.button_9.clicked.connect(self.take_digit)
		self.button_sign.clicked.connect(self.sign)
		self.button_dot.clicked.connect(self.dot)
		self.button_equal.clicked.connect(self.equal)
		self.button_add.clicked.connect(self.operations)
		self.button_dif.clicked.connect(self.operations)
		self.button_mult.clicked.connect(self.operations)
		self.button_div.clicked.connect(self.operations)
		self.button_mod.clicked.connect(self.operations)
		self.button_pop.clicked.connect(self.pop)
		self.button_erase.clicked.connect(self.erase)
		self.button_factorial.clicked.connect(self.factorial)
		self.button_reverse.clicked.connect(self.reverse)
		self.button_root.clicked.connect(self.root)
		self.button_pow.clicked.connect(self.pow)
		self.button_log.clicked.connect(self.log)
		self.button_pi.clicked.connect(lambda: self.screen.setText(str(math.pi)))
		self.button_e.clicked.connect(lambda: self.screen.setText(str(math.e)))
		self.button_divisors.clicked.connect(self.divisors)
		self.shortcuts.triggered.connect(self.show_shortcuts)
		self.clean_secondary_screen.triggered.connect(lambda: self.second_screen.setText(''))
		self.clean_history_screen.triggered.connect(lambda: self.history.setText('\nThere\'s no history yet'))
		self.question.triggered.connect(lambda: QDesktopServices.openUrl(QUrl('https://t.me/almax_good')))
		self.documentation.triggered.connect(lambda: system('start README.md'))
		self.copy.triggered.connect(lambda: copy(self.screen.toPlainText()))
		self.cut.triggered.connect(self.f_cut)
		self.paste.triggered.connect(self.f_paste)
		self.light_theme.triggered.connect(self.dark_to_light)
		self.dark_theme.triggered.connect(self.light_to_dark)

	def light_to_dark(self):
		theme = open('theme.txt', 'w')
		theme.write('dark')
		self.errors.setText('Restart the program')
		self.errors.show()
		theme.close()

	def dark_to_light(self):
		theme = open('theme.txt', 'w')
		theme.write('light')
		self.errors.setText('Restart the program')
		self.errors.show()
		theme.close()

	def f_paste(self):
		try:
			float(paste())
			self.screen.setText(paste())
		except:
			self.errors.setText('Invalid input')
			self.errors.show()

	def f_cut(self):
		copy(self.screen.toPlainText())
		self.screen.setText('0')

	def divisors(self):
		if not all([i not in self.second_screen.toPlainText() for i in self.OPERATIONS]):
			return
		self.optim()
		if self.second_screen.toPlainText().find('.') != -1 or self.screen.toPlainText()[0] == '-' or self.screen.toPlainText() == '0':
			self.errors.setText('Invalid input')
			self.errors.show()
			return
		try:
			div = ''
			number = int(self.screen.toPlainText())
			for i in range(1, number + 1):
				if not(number % i):
					div += str(i) + ', '
			self.second_screen.setText(f'div({number}) = ')
			self.screen.setText('{' + div[:-2] + '}')
			self.set_history()
		except:
			self.errors.setText('Invalid input')
			self.errors.show()

	def show_shortcuts(self):
		dialog = Shortcuts()
		dialog.exec()

	def set_history(self):
		if self.history.toPlainText() == '\nThere\'s no history yet':
			self.history.setText('')
		self.history.setText(self.history.toPlainText() + ('\n* ' if self.history.toPlainText() else '* ') + self.second_screen.toPlainText() + self.screen.toPlainText())

	def optim(self):
		if self.screen.toPlainText().find('.') == -1:
			return
		if all([self.screen.toPlainText()[self.screen.toPlainText().find('.') + 1:].find(i) == -1 for i in '123456789']):
			self.screen.setText(self.screen.toPlainText()[:self.screen.toPlainText().find('.')])

	def take_digit(self):
		self.screen.setText((self.screen.toPlainText()[1:] if self.screen.toPlainText() == '0' else self.screen.toPlainText()) + self.sender().text())

	def operations(self):
		if not all([i not in self.second_screen.toPlainText() for i in self.OPERATIONS]):
			return
		self.optim()
		self.second_screen.setText(self.screen.toPlainText() + ' ' + self.sender().text() + ' ')
		self.screen.setText('0')

	def log(self):
		if not all([i not in self.second_screen.toPlainText() for i in self.OPERATIONS]):
			return
		self.optim()
		if float(self.screen.toPlainText()) <= 0:
			self.errors.setText('Invalid input')
			self.errors.show()
			return
		self.second_screen.setText(self.screen.toPlainText() + ' log_base ')
		self.screen.setText('0')

	def pow(self):
		if not all([i not in self.second_screen.toPlainText() for i in self.OPERATIONS]):
			return
		self.optim()
		self.second_screen.setText(self.screen.toPlainText() + ' ^ ')
		self.screen.setText('0')

	def root(self):
		if not all([i not in self.second_screen.toPlainText() for i in self.OPERATIONS]):
			return
		self.optim()
		if float(self.screen.toPlainText()) <= 0:
			self.errors.setText('Invalid input')
			self.errors.show()
			return
		self.second_screen.setText(self.screen.toPlainText() + ' n_root ')
		self.screen.setText('0')

	def factorial(self):
		if not all([i not in self.second_screen.toPlainText() for i in self.OPERATIONS]):
			return
		self.optim()
		self.second_screen.setText(f'fact({self.screen.toPlainText()}) = ')
		try:
			self.screen.setText(str(math.factorial(int(self.screen.toPlainText()))))
			self.set_history()
		except (ValueError, TypeError):
			self.errors.setText('Invalid input')
			self.errors.show()
		except OverflowError:
			self.errors.setText('Overflow')
			self.errors.show()

	def reverse(self):
		if not all([i not in self.second_screen.toPlainText() for i in self.OPERATIONS]):
			return
		self.optim()
		self.second_screen.setText(f'1/({self.screen.toPlainText()}) = ')
		try:
			self.screen.setText(str(1 / float(self.screen.toPlainText())))
			self.optim()
			self.set_history()
		except (ValueError, ZeroDivisionError):
			self.errors.setText('Invalid input')
			self.errors.show()
		except OverflowError:
			self.errors.setText('Overflow')
			self.errors.show()

	def equal(self):
		if not self.second_screen.toPlainText() or self.second_screen.toPlainText().find('=') != -1:
			return
		self.optim()
		oper = self.second_screen.toPlainText()[self.second_screen.toPlainText().find(' ') + 1:self.second_screen.toPlainText().rfind(' ')]
		self.second_screen.setText(self.second_screen.toPlainText() + self.screen.toPlainText() + ' = ')
		try:
			if self.screen.toPlainText().find('.') == -1 and self.second_screen.toPlainText().find('.') == -1:
				self.screen.setText(str(self.DICT_OPERATIONS[oper](int(self.second_screen.toPlainText()[:self.second_screen.toPlainText().find(oper) - 1]), int(self.screen.toPlainText()))))
			else:
				self.screen.setText(str(self.DICT_OPERATIONS[oper](float(self.second_screen.toPlainText()[:self.second_screen.toPlainText().find(oper) - 1]), float(self.screen.toPlainText()))))
			self.optim()
			self.set_history()
		except(ValueError, ZeroDivisionError, TypeError):
			self.errors.setText('Invalid input')
			self.errors.show()
		except OverflowError:
			self.errors.setText('Overflow')
			self.errors.show()

	def pop(self):
		if self.screen.toPlainText() == '0':
			return
		self.screen.setText('0' if (self.screen.toPlainText()[0] == '-' and len(self.screen.toPlainText()) == 2) or not self.screen.toPlainText()[:-1] else self.screen.toPlainText()[:-1])

	def erase(self):
		self.screen.setText('0')
		self.second_screen.setText('')
		self.errors.setText('')
		self.errors.hide()

	def sign(self):
		if self.screen.toPlainText() == '0':
			return
		self.screen.setText(self.screen.toPlainText()[1:] if self.screen.toPlainText()[0] == '-' else '-' + self.screen.toPlainText())

	def dot(self):
		if self.screen.toPlainText().find('.') == -1:
			self.screen.setText(self.screen.toPlainText() + '.')


def except_hook(cls, exception, traceback):
    sys.__excepthook__(cls, exception, traceback)


if __name__ == '__main__':
	app = w.QApplication(sys.argv)
	window = MainWindow()
	window.show()
	sys.excepthook = except_hook
	sys.exit(app.exec())
