import java.text.DecimalFormat;
import java.util.*;

public class Matrix {
    private int rowNo;
    private int colNo;
    private double[][] matrix;

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0000");

    public Matrix(int rowNo, int colNo, float mean, float std) {
        this.initial(rowNo, colNo, mean, std);
    }

    public Matrix(int rowNo, int colNo) {
        this.rowNo = rowNo;
        this.colNo = colNo;
        this.matrix = new double[rowNo][colNo];
    }

    public static double[][] generateRandomNormalMatrix(int rows, int columns, float mean, float std) {
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = Matrix.normalDistributionGenerator(mean, std);
            }
        }
        return matrix;
    }

    private static double normalDistributionGenerator(float mean, float std) {
        Random random = new Random();
        double x = random.nextGaussian();
        x += mean;
        x *= std;
        return x;
    }

    public void initial(int rowNo, int colNo, float mean, float std) {
        this.rowNo = rowNo;
        this.colNo = colNo;
        this.matrix = generateRandomNormalMatrix(this.rowNo, this.colNo, mean, std);
    }

    public Matrix sequentialMultiply(Matrix matrix) {
        if (this.colNo != matrix.rowNo) {
            System.out.println("Multiplication Not Possible!");
            return null;
        }

        Date tik = new Date();

        Matrix result = new Matrix(this.rowNo, matrix.colNo);

        for (int i = 0; i < this.rowNo; i++) {
            for (int j = 0; j < matrix.colNo; j++) {
                result.matrix[i][j] = 0;
                for (int k = 0; k < this.colNo; k++)
                    result.matrix[i][j] += this.matrix[i][k] * matrix.matrix[k][j];
            }
        }

        Date tak = new Date();

        System.out.println("\nTime taken in milli second: " + (tak.getTime() - tik.getTime()));
        return result;
    }

    public Matrix concurrentMultiply(Matrix matrix) {

        if (this.colNo != matrix.rowNo) {
            System.out.println("Multiplication Not Possible!");
            return null;
        }

        Matrix result = new Matrix(this.rowNo, matrix.colNo);

        Date tik = new Date();
        ParallelThreadsCreator.multiply(this.matrix, matrix.matrix, result.matrix);
        Date tak = new Date();
        System.out.println("\nTime taken in milli second: " + (tak.getTime() - tik.getTime()));
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Matrix:\n");
        for (int i = 0; i < this.rowNo; i++) {
            for (int j = 0; j < this.colNo; j++) {
                stringBuilder.append(decimalFormat.format(this.matrix[i][j]));
                stringBuilder.append("\t");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}

class RowMultiplyWorker implements Runnable {

    private final double[][] result;
    private final double[][] matrix1;
    private final double[][] matrix2;
    private final int row;

    public RowMultiplyWorker(double[][] result, double[][] matrix1, double[][] matrix2, int row) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = row;
    }

    @Override
    public void run() {

        for (int i = 0; i < matrix2[0].length; i++) {
            result[row][i] = 0;
            for (int j = 0; j < matrix1[row].length; j++) {
                result[row][i] += matrix1[row][j] * matrix2[j][i];
            }
        }
    }
}

class ParallelThreadsCreator {
    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        int cores = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>();
        int rows1 = matrix1.length;
        for (int i = 0; i < rows1; i++) {
            RowMultiplyWorker task = new RowMultiplyWorker(result, matrix1, matrix2, i);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            if (threads.size() % cores == 0) {
                waitForThreads(threads);
            }
        }
    }

    private static void waitForThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }
}