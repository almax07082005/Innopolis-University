#include <iostream>
#include <vector>
#include <iomanip>

#define ERROR "Error: the dimensional problem occurred\n"
#define EPSILON 1e-9
typedef std::vector<std::vector<double>> vvint;

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

    double operator()(int indexI, int indexJ) const {
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
                double value;
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
                double value;
                cin >> value;
                m.matrix.at(i).push_back(value);
            }
        }

        return cin;
    }
    friend std::ostream& operator<<(std::ostream& cout, const SquareMatrix& m) {
        for (int i = 0; i < m.rowsCount; i++) {
            for (int j = 0; j < m.rowsCount; j++) {
                cout << std::fixed << std::setprecision(2) << m.matrix.at(i).at(j) << (j + 1 == m.rowsCount ? '\n' : ' ');
            }
        }

        return cout;
    }
    int gaussianElimination() {
        int step = 1;
        int sign = 1;

        for (int i = 0; i < columnsCount; i++) {
            makePermutation(i, step, sign);
            for (int j = i + 1; j < rowsCount; j++) {
                makeElimination(j, i, step);
            }
        }

        return sign;
    }
    void makePermutation(int indexFrom, int& step, int& sign) {
        double maxElem = matrix.at(indexFrom).at(indexFrom);
        int maxIndex = indexFrom;

        for (int i = indexFrom + 1; i < rowsCount; i++) {
            if (std::abs(matrix.at(i).at(indexFrom)) > std::abs(maxElem)) {
                maxElem = matrix.at(i).at(indexFrom);
                maxIndex = i;
            }
        }

        if (maxIndex != indexFrom) {
            std::swap(matrix.at(indexFrom), matrix.at(maxIndex));
            sign *= -1;
            std::cout << "step #" << step++ << ": permutation\n";
            std::cout << *this;
        }
    }
    void makeElimination(int rowIndex, int pivotPosition, int& step) {
        if (std::abs(matrix.at(rowIndex).at(pivotPosition)) < EPSILON) {
            return;
        }

        double divider = matrix.at(rowIndex).at(pivotPosition) / matrix.at(pivotPosition).at(pivotPosition);
        for (int i = 0; i < columnsCount; i++) {
            matrix.at(rowIndex).at(i) -= matrix.at(pivotPosition).at(i) * divider;
        }

        std::cout << "step #" << step++ << ": elimination\n";
        std::cout << *this;
    }
    void findDeterminant() {
        int sign = gaussianElimination();
        double result = 1;
        for (int i = 0; i < rowsCount; i++) {
            result *= matrix.at(i).at(i);
        }
        std::cout << std::fixed << std::setprecision(2) << "result:\n" << result * sign;
    }
};

int main() {
    SquareMatrix m;
    std::cin >> m;
    m.findDeterminant();
    return EXIT_SUCCESS;
}
