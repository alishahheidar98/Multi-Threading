import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*******\nNOTE: Columns of 1st matrix must be equal to rows of 2nd matrix!");
        System.out.println("NOTE: Number of rows and columns must be greater than 0!\n*******");
        System.out.println("Please insert 1.NUMBER OF ROWS, 2.NUMBER OF COLUMNS, 3.MEAN and 4.STD of first matrix:");
        System.out.println("Like this: 100 100 2 1");
        String input1 = scanner.nextLine().trim();
        String[] inputs = (input1.split("\\s+"));
        Matrix firstMatrix = new Matrix(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));

        System.out.println("Please insert 1.NUMBER OF ROWS, 2.NUMBER OF COLUMNS, 3.MEAN and 4.STD of second matrix:");
        System.out.println("Like this: 100 100 3 2");
        String input2 = scanner.nextLine().trim();
        inputs = (input2.split("\\s+"));
        Matrix secondMatrix = new Matrix(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));

        //System.out.println("First " + firstMatrix);
        //System.out.println("Second " + secondMatrix);
        Matrix seqResult = firstMatrix.sequentialMultiply(secondMatrix);
        //System.out.println("Sequential Result " + seqResult);
        Matrix conResult = firstMatrix.concurrentMultiply(secondMatrix);
        //System.out.println("Concurrent Result " + conResult);
    }
}