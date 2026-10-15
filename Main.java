import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Первая матрица:");
        Matrix current = readMatrix(input);

        while (true) {
            System.out.println("\nМатрица:\n" + current);
            System.out.println("1) Новая  2) +  3) *  4) /  5) T  6) det  0) Выход");

            int choice = readChoice(input);

            try {
                switch (choice) {
                    case 0:
                        input.close();
                        return;
                    case 1:
                        current = readMatrix(input);
                        break;
                    case 2:
                        System.out.println("Вторая матрица:");
                        current = current.add(readMatrix(input));
                        break;
                    case 3:
                        System.out.println("Вторая матрица:");
                        current = current.multiply(readMatrix(input));
                        break;
                    case 4:
                        System.out.println("Вторая матрица:");
                        current = current.divide(readMatrix(input));
                        break;
                    case 5:
                        current = current.transpose();
                        break;
                    case 6:
                        System.out.println("det = " + current.determinant());
                        break;
                    default:
                        System.out.println("Нет такого пункта");
                }
            } catch (IllegalArgumentException error) {
                System.out.println("Ошибка: " + error.getMessage());
            }
        }
    }

    private static int readChoice(Scanner input) {
        while (true) {
            System.out.print("> ");

            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException error) {
                System.out.println("Введите номер пункта");
            }
        }
    }

    private static Matrix readMatrix(Scanner input) {
        int rows = 0;
        int cols = 0;

        while (rows <= 0 || cols <= 0) {
            System.out.print("Размер (строки столбцы): ");
            String[] parts = input.nextLine().trim().split("\\s+");

            try {
                if (parts.length != 2) {
                    throw new NumberFormatException();
                }

                rows = Integer.parseInt(parts[0]);
                cols = Integer.parseInt(parts[1]);

                if (rows <= 0 || cols <= 0) {
                    System.out.println("Размеры должны быть положительными");
                }
            } catch (NumberFormatException error) {
                System.out.println("Введите два целых числа");
            }
        }

        ComplexNumber[][] values = new ComplexNumber[rows][cols];
        System.out.println("Элементы (Re Im):");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                while (true) {
                    System.out.print("[" + (i + 1) + "," + (j + 1) + "]: ");
                    String[] parts = input.nextLine().trim().split("\\s+");

                    if (parts.length != 2) {
                        System.out.println("Нужно два числа");
                        continue;
                    }

                    try {
                        double real = Double.parseDouble(parts[0]);
                        double imaginary = Double.parseDouble(parts[1]);

                        if (!Double.isFinite(real)
                                || !Double.isFinite(imaginary)) {
                            throw new NumberFormatException();
                        }

                        values[i][j] = new ComplexNumber(real, imaginary);
                        break;
                    } catch (NumberFormatException error) {
                        System.out.println("Неверный ввод. Пример: 2.5 -1");
                    }
                }
            }
        }

        return new Matrix(values);
    }
}
