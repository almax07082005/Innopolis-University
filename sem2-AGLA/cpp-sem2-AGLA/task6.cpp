#include <iostream>
#include <vector>
#include <iomanip>
#include <cmath>

#define DIMENSIONAL_ERROR "Error: the dimensional problem occurred\n"
#define SINGULAR_ERROR "Error: matrix A is singular\n"
#define EPSILON 1e-9
typedef std::vector<std::vector<double>> vvdouble;

class Matrix {
protected:
    int rowsCount{0};
    int columnsCount{0};
    vvdouble matrix;
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
            throw std::invalid_argument(DIMENSIONAL_ERROR);
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
            throw std::invalid_argument(DIMENSIONAL_ERROR);
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
            throw std::invalid_argument(DIMENSIONAL_ERROR);
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

class ColumnVector : public Matrix {
public:
    explicit ColumnVector(int rowsCount = 0): Matrix(rowsCount, 1) {}
    friend std::istream& operator>>(std::istream& cin, ColumnVector& c) {
        cin >> c.rowsCount;
        c.columnsCount = 1;

        for (int i = 0; i < c.rowsCount; i++) {
            c.matrix.emplace_back();
            for (int j = 0; j < c.columnsCount; j++) {
                double value;
                std::cin >> value;
                c.matrix.at(i).push_back(value);
            }
        }

        return cin;
    }
    friend std::ostream& operator<<(std::ostream& cout, const ColumnVector& c) {
        for (int i = 0; i < c.rowsCount; i++) {
            cout << c.matrix.at(i).at(1) << '\n';
        }

        return cout;
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

        ColumnVector c;
        cin >> c;
        for (int i = 0; i < m.rowsCount; i++) {
            m.matrix.at(i).push_back(c(i, 0));
        }

        return cin;
    }
    friend std::ostream& operator<<(std::ostream& cout, const SquareMatrix& m) {
        for (const auto& item : m.matrix) {
            for (int j = 0; j < m.columnsCount; j++) {
                cout << std::fixed << std::setprecision(2) << item.at(j) << (j + 1 == m.columnsCount ? '\n' : ' ');
            }
        }

        for (int i = 0; i < m.rowsCount; i++) {
            cout << std::fixed << std::setprecision(2) << m.matrix.at(i).at(m.columnsCount) << '\n';
        }

        return cout;
    }
    std::string gaussianElimination() {
        int step = 1;
        int sign = 1;
        std::stringstream output;
        output << "Gaussian process:\n";

        for (int i = 0; i < columnsCount; i++) {
            makePermutation(i, step, sign, output);
            for (int j = i + 1; j < rowsCount; j++) {
                makeElimination(j, i, step, output);
            }
        }

        double determinant = findDeterminant(sign);
        if (std::abs(determinant) < EPSILON || !std::isfinite(determinant)) {
            throw std::invalid_argument(SINGULAR_ERROR);
        }

        for (int i = columnsCount - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                makeElimination(j, i, step, output);
            }
        }

        diagonalNormalization(output);
        return output.str();
    }
    void makePermutation(int indexFrom, int& step, int& sign, std::stringstream& output) {
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
            output << "step #" << step++ << ": permutation\n";
            output << *this;
        }
    }
    void makeElimination(int rowIndex, int pivotPosition, int& step, std::stringstream& output) {
        if (std::abs(matrix.at(rowIndex).at(pivotPosition)) < EPSILON) {
            return;
        }

        double divider = matrix.at(rowIndex).at(pivotPosition) / matrix.at(pivotPosition).at(pivotPosition);
        for (int i = 0; i < columnsCount + 1; i++) {
            matrix.at(rowIndex).at(i) -= matrix.at(pivotPosition).at(i) * divider;
        }

        output << "step #" << step++ << ": elimination\n";
        output << *this;
    }
    double findDeterminant(int sign) {
        double determinant = 1;
        for (int i = 0; i < rowsCount; i++) {
            determinant *= matrix.at(i).at(i);
        }
        return determinant * sign;
    }
    void diagonalNormalization(std::stringstream& output) {
        for (int i = 0; i < rowsCount; i++) {
            double divisor = matrix.at(i).at(i);

            for (int j = 0; j < columnsCount + 1; j++) {
                matrix.at(i).at(j) /= divisor;

                if (std::abs(matrix.at(i).at(j)) < EPSILON) {
                    matrix.at(i).at(j) = 0;
                }
            }
        }

        output << "Diagonal normalization:\n";
        output << *this;
    }
    [[nodiscard]] std::string showResult() const {
        std::stringstream output;

        output << "Result:\n";
        for (int i = 0; i < rowsCount; i++) {
            output << std::fixed << std::setprecision(2) << matrix.at(i).at(columnsCount) << '\n';
        }

        return output.str();
    }
};

int main() {
    SquareMatrix m;
    std::cin >> m;

    std::string gaussianOutput, resultOutput;
    try {
        gaussianOutput = m.gaussianElimination();
        resultOutput = m.showResult();
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
        return EXIT_FAILURE;
    }

    std::cout << gaussianOutput << resultOutput;
    return EXIT_SUCCESS;
}
