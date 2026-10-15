public class Matrix {
    private static final double EPS = 1e-10;

    private final int rows;
    private final int cols;
    private final ComplexNumber[][] data;

    public Matrix(ComplexNumber[][] values) {
        if (values == null || values.length == 0
                || values[0] == null || values[0].length == 0) {
            throw new IllegalArgumentException("Пустая матрица");
        }

        rows = values.length;
        cols = values[0].length;
        data = new ComplexNumber[rows][cols];

        for (int i = 0; i < rows; i++) {
            if (values[i] == null || values[i].length != cols) {
                throw new IllegalArgumentException("Неверная форма матрицы");
            }

            for (int j = 0; j < cols; j++) {
                if (values[i][j] == null) {
                    throw new IllegalArgumentException("Пустой элемент матрицы");
                }
                data[i][j] = values[i][j];
            }
        }
    }

    public Matrix add(Matrix other) {
        if (rows != other.rows || cols != other.cols) {
            throw new IllegalArgumentException("Размеры не подходят для сложения");
        }

        ComplexNumber[][] result = new ComplexNumber[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = data[i][j].add(other.data[i][j]);
            }
        }

        return new Matrix(result);
    }

    public Matrix multiply(Matrix other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException("Размеры не подходят для умножения");
        }

        ComplexNumber[][] result = new ComplexNumber[rows][other.cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                ComplexNumber sum = ComplexNumber.ZERO;

                for (int k = 0; k < cols; k++) {
                    sum = sum.add(data[i][k].multiply(other.data[k][j]));
                }

                result[i][j] = sum;
            }
        }

        return new Matrix(result);
    }

    public Matrix divide(Matrix other) {
        return multiply(other.inverse());
    }

    public Matrix transpose() {
        ComplexNumber[][] result = new ComplexNumber[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = data[i][j];
            }
        }

        return new Matrix(result);
    }

    public ComplexNumber determinant() {
        if (rows != cols) {
            throw new IllegalArgumentException("Матрица должна быть квадратной");
        }

        ComplexNumber[][] copy = copyData();
        ComplexNumber answer = new ComplexNumber(1, 0);

        for (int col = 0; col < rows; col++) {
            int pivotRow = findPivot(copy, col, rows);

            if (copy[pivotRow][col].absSquared() < EPS * EPS) {
                return ComplexNumber.ZERO;
            }

            if (pivotRow != col) {
                swapRows(copy, pivotRow, col);
                answer = answer.multiply(new ComplexNumber(-1, 0));
            }

            ComplexNumber pivot = copy[col][col];
            answer = answer.multiply(pivot);

            for (int row = col + 1; row < rows; row++) {
                ComplexNumber factor = copy[row][col].divide(pivot);

                for (int j = col + 1; j < cols; j++) {
                    copy[row][j] = copy[row][j].subtract(
                            factor.multiply(copy[col][j])
                    );
                }

                copy[row][col] = ComplexNumber.ZERO;
            }
        }

        return answer;
    }

    private Matrix inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException(
                    "Для деления вторая матрица должна быть квадратной"
            );
        }

        int n = rows;
        ComplexNumber[][] extended = new ComplexNumber[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                extended[i][j] = data[i][j];
                extended[i][n + j] = new ComplexNumber(i == j ? 1 : 0, 0);
            }
        }

        for (int col = 0; col < n; col++) {
            int pivotRow = findPivot(extended, col, n);

            if (extended[pivotRow][col].absSquared() < EPS * EPS) {
                throw new IllegalArgumentException("Вторая матрица необратима");
            }

            swapRows(extended, pivotRow, col);

            ComplexNumber pivot = extended[col][col];
            for (int j = 0; j < 2 * n; j++) {
                extended[col][j] = extended[col][j].divide(pivot);
            }

            for (int row = 0; row < n; row++) {
                if (row == col) {
                    continue;
                }

                ComplexNumber factor = extended[row][col];
                for (int j = 0; j < 2 * n; j++) {
                    extended[row][j] = extended[row][j].subtract(
                            factor.multiply(extended[col][j])
                    );
                }
            }
        }

        ComplexNumber[][] result = new ComplexNumber[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = extended[i][n + j];
            }
        }

        return new Matrix(result);
    }

    private ComplexNumber[][] copyData() {
        ComplexNumber[][] copy = new ComplexNumber[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, copy[i], 0, cols);
        }

        return copy;
    }

    private static int findPivot(ComplexNumber[][] matrix, int col, int height) {
        int best = col;

        for (int row = col + 1; row < height; row++) {
            if (matrix[row][col].absSquared()
                    > matrix[best][col].absSquared()) {
                best = row;
            }
        }

        return best;
    }

    private static void swapRows(ComplexNumber[][] matrix, int first, int second) {
        ComplexNumber[] temp = matrix[first];
        matrix[first] = matrix[second];
        matrix[second] = temp;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                text.append(data[i][j]);

                if (j < cols - 1) {
                    text.append("\t");
                }
            }

            if (i < rows - 1) {
                text.append("\n");
            }
        }

        return text.toString();
    }
}