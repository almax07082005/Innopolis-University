#include <iostream>
#include <vector>
#include <memory>

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

    std::unique_ptr<Matrix> operator+(const Matrix& m) const {
        auto res = std::make_unique<Matrix>(rowsCount, columnsCount);

        if (rowsCount != m.rowsCount || columnsCount != m.columnsCount) {
            throw std::invalid_argument(ERROR);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                res->matrix.at(i).at(j) = matrix.at(i).at(j) + m.matrix.at(i).at(j);
            }
        }

        return res;
    }

    std::unique_ptr<Matrix> operator-(const Matrix& m) const {
        auto res = std::make_unique<Matrix>(rowsCount, columnsCount);

        if (rowsCount != m.rowsCount || columnsCount != m.columnsCount) {
            throw std::invalid_argument(ERROR);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                res->matrix.at(i).at(j) = matrix.at(i).at(j) - m.matrix.at(i).at(j);
            }
        }

        return res;
    }

    std::unique_ptr<Matrix> operator*(const Matrix& m) const {
        auto res = std::make_unique<Matrix>(rowsCount, m.columnsCount);

        if (columnsCount != m.rowsCount) {
            throw std::invalid_argument(ERROR);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < m.columnsCount; j++) {
                for (int k = 0; k < columnsCount; k++) {
                    res->matrix.at(i).at(j) += matrix.at(i).at(k) * m.matrix.at(k).at(j);
                }
            }
        }

        return res;
    }

    std::unique_ptr<Matrix> operator~() const {
        auto res = std::make_unique<Matrix>(columnsCount, rowsCount);

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                res->matrix.at(j).at(i) = matrix.at(i).at(j);
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

    std::unique_ptr<SquareMatrix> operator+(const SquareMatrix& m) const {
        std::unique_ptr<Matrix> res = std::move(Matrix::operator+(m));
        std::unique_ptr<SquareMatrix> result = std::make_unique<SquareMatrix>(*reinterpret_cast<SquareMatrix*>(res.get()));
        return result;
    }

    std::unique_ptr<SquareMatrix> operator-(const SquareMatrix& m) const {
        std::unique_ptr<Matrix> res = std::move(Matrix::operator-(m));
        std::unique_ptr<SquareMatrix> result = std::make_unique<SquareMatrix>(*reinterpret_cast<SquareMatrix*>(res.get()));
        return result;
    }

    std::unique_ptr<SquareMatrix> operator*(const SquareMatrix& m) const {
        std::unique_ptr<Matrix> res = std::move(Matrix::operator*(m));
        std::unique_ptr<SquareMatrix> result = std::make_unique<SquareMatrix>(*reinterpret_cast<SquareMatrix*>(res.get()));
        return result;
    }

    std::unique_ptr<SquareMatrix> operator~() const {
        std::unique_ptr<Matrix> res = std::move(Matrix::operator~());
        std::unique_ptr<SquareMatrix> result = std::make_unique<SquareMatrix>(*reinterpret_cast<SquareMatrix*>(res.get()));
        return result;
    }
};

int main() {
    SquareMatrix A, B, C;
    std::cin >> A >> B >> C;

    try {
        std::cout << *(A + B);
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    try {
        std::cout << *(B - A);
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    try {
        std::cout << *(C * A);
    } catch (const std::invalid_argument& exception) {
        std::cout << exception.what();
    }

    std::cout << *~A;

    return EXIT_SUCCESS;
}
