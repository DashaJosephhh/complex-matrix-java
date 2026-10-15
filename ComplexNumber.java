import java.util.Locale;

public class ComplexNumber {
    public static final ComplexNumber ZERO = new ComplexNumber(0, 0);

    private final double real;
    private final double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(
                real + other.real,
                imaginary + other.imaginary
        );
    }

    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(
                real - other.real,
                imaginary - other.imaginary
        );
    }

    public ComplexNumber multiply(ComplexNumber other) {
        return new ComplexNumber(
                real * other.real - imaginary * other.imaginary,
                real * other.imaginary + imaginary * other.real
        );
    }

    public ComplexNumber divide(ComplexNumber other) {
        double denominator = other.real * other.real
                + other.imaginary * other.imaginary;

        if (denominator == 0) {
            throw new IllegalArgumentException("Деление на ноль");
        }

        return new ComplexNumber(
                (real * other.real + imaginary * other.imaginary) / denominator,
                (imaginary * other.real - real * other.imaginary) / denominator
        );
    }

    public double absSquared() {
        return real * real + imaginary * imaginary;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.2f%+.2fi", real, imaginary);
    }
}