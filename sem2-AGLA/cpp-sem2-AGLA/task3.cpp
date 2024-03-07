#include <iostream>
#include <vector>

#define ERROR "Error: the dimensional problem occurred\n"
typedef std::vector<std::vector<int>> vvint;

class Matrix {
protected:
    int rowsCount{0};
    int columnsCount{0};
    vvint matrix;
public:
    explicit Matrix(int rowsCount = 0, int columnsCount = 0): rowsCount(rowsCount), columnsCount(columnsCount) {
        for (int i = 0; i < rowsCount; i++) {
            matrix.emplace_back(columnsCount);
        }
    }

    Matrix(const Matrix& m) {
        rowsCount = m.rowsCount;
        columnsCount = m.columnsCount;
        matrix.clear();
        matrix.insert(matrix.begin(), m.matrix.begin(), m.matrix.end());
    }

    int getRowsCount() const {
        return rowsCount;
    }

    int operator()(int indexI, int indexJ) const {
        return matrix.at(indexI).at(indexJ);
    }

    Matrix& operator=(const Matrix& m) {
        rowsCount = m.rowsCount;
        columnsCount = m.columnsCount;
        matrix.clear();
        matrix.insert(matrix.begin(), m.matrix.begin(), m.matrix.end());
        return *this;
    }

    friend std::istream& operator>>(std::istream& cin, Matrix& m) {
        cin >> m.rowsCount >> m.columnsCount;

        for (int i = 0; i < m.rowsCount; i++) {
            m.matrix.emplace_back();
            for (int j = 0; j < m.columnsCount; j++) {
                int value;
                std::cin >> value;
                m.matrix.at(i).push_back(value);
            }
        }

        return cin;
    }

    friend std::ostream& operator<<(std::ostream& cout, const Matrix& m) {
        for (int i = 0; i < m.rowsCount; i++) {
            for (int j = 0; j < m.columnsCount; j++) {
                cout << m.matrix.at(i).at(j) << (j + 1 == m.columnsCount ? '\n' : ' ');
            }
        }

        return cout;
    }

    Matrix operator+(const Matrix& m) const {
        Matrix res(rowsCount, columnsCount);

        if (rowsCount != m.rowsCount || columnsCount != m.columnsCount) {
            throw std::invalid_argument(ERROR);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                res.matrix.at(i).at(j) = matrix.at(i).at(j) + m.matrix.at(i).at(j);
            }
        }

        return res;
    }

    Matrix operator-(const Matrix& m) const {
        Matrix res(rowsCount, columnsCount);

        if (rowsCount != m.rowsCount || columnsCount != m.columnsCount) {
            throw std::invalid_argument(ERROR);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                res.matrix.at(i).at(j) = matrix.at(i).at(j) - m.matrix.at(i).at(j);
            }
        }

        return res;
    }

    Matrix operator*(const Matrix& m) const {
        Matrix res(rowsCount, m.columnsCount);

        if (columnsCount != m.rowsCount) {
            throw std::invalid_argument(ERROR);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < m.columnsCount; j++) {
                for (int k = 0; k < columnsCount; k++) {
                    res.matrix.at(i).at(j) += matrix.at(i).at(k) * m.matrix.at(k).at(j);
                }
            }
        }

        return res;
    }

    Matrix operator~() const {
        Matrix res(columnsCount, rowsCount);

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                res.matrix.at(j).at(i) = matrix.at(i).at(j);
            }
        }

        return res;
    }
};

class SquareMatrix: public Matrix {
public:
    explicit SquareMatrix(int size = 0): Matrix(size, size) {}
    friend std::istream& operator>>(std::istream& cin, SquareMatrix& m) {
        cin >> m.rowsCount;
        m.columnsCount = m.rowsCount;

        for (int i = 0; i < m.rowsCount; i++) {
            m.matrix.emplace_back();
            for (int j = 0; j < m.rowsCount; j++) {
                int value;
                cin >> value;
                m.matrix.at(i).push_back(value);
            }
        }

        return cin;
    }
    friend std::ostream& operator<<(std::ostream& cout, const SquareMatrix& m) {
        for (int i = 0; i < m.rowsCount; i++) {
            for (int j = 0; j < m.rowsCount; j++) {
                cout << m.matrix.at(i).at(j) << (j + 1 == m.rowsCount ? '\n' : ' ');
            }
        }

        return cout;
    }
};

class IdentityMatrix: public SquareMatrix {
public:
    explicit IdentityMatrix(int size = 0): SquareMatrix(size) {
        for (int i = 0; i < size; i++) {
            matrix.at(i).at(i) = 1;
        }
    }
};

class EliminationMatrix: public IdentityMatrix {
public:
    explicit EliminationMatrix(int size = 0): IdentityMatrix(size) {}
    void changeCoefficient(const SquareMatrix& m, int rowIndex, int columnIndex) {
        int coefficient = 0;
        int elemToBeChanged = m(rowIndex, columnIndex);
        if (!elemToBeChanged) return;

        for (int i = 0; i < m.getRowsCount(); i++) {
            if (i == rowIndex) continue;
            int currentElem = m(i, columnIndex);

            if (!(elemToBeChanged % currentElem) && currentElem) {
                coefficient = -(elemToBeChanged / currentElem);
                break;
            }
        }

        matrix.at(rowIndex).at(columnIndex) = coefficient;
    }
};

class PermutationMatrix: public IdentityMatrix {
public:
    explicit PermutationMatrix(int size = 0): IdentityMatrix(size) {}

    void changeRows(int rowIndex1, int rowIndex2) {
        std::swap(matrix.at(rowIndex1), matrix.at(rowIndex2));
        /*for (int i = 0; i < columnsCount; i++) {
            std::swap(matrix.at(rowIndex1).at(i), matrix.at(rowIndex2).at(i));
        }*/
    }
};

int main() {
    SquareMatrix A;
    std::cin >> A;
    EliminationMatrix E(A.getRowsCount());
    PermutationMatrix P(A.getRowsCount());

    try {
        std::cout << IdentityMatrix(3);
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    try {
        E.changeCoefficient(A, 1, 0);
        std::cout << E;
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    try {
        std::cout << E * A;
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    try {
        P.changeRows(1, 0);
        std::cout << P;
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    try {
        std::cout << P * A;
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    return EXIT_SUCCESS;
}
