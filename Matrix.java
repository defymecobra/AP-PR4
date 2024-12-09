import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class Matrix {

    public static void main(String[] args) {

        int rows = 3; // Кількість рядків
        int columns = 3; // Кількість стовпців

        long startTime = System.currentTimeMillis();

        // Генеруємо двовимірний масив асинхронно
        CompletableFuture<int[][]> matrixFuture = CompletableFuture.supplyAsync(() -> {
            long taskStartTime = System.currentTimeMillis();
            int[][] matrix = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = ThreadLocalRandom.current().nextInt(1, 100);
                }
            }
            System.out.println("Matrix generated in: " + (System.currentTimeMillis() - taskStartTime) + "ms");
            return matrix;
        });

        // Обробка матриці та вивід в строгому порядку
        matrixFuture.thenApplyAsync(matrix -> {
            long taskStartTime = System.currentTimeMillis();
            System.out.println("Generated Matrix:");
            for (int[] row : matrix) {
                for (int value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
            System.out.println("Matrix printed in: " + (System.currentTimeMillis() - taskStartTime) + "ms");
            return matrix;
        }).thenApplyAsync(matrix -> {
            long taskStartTime = System.currentTimeMillis();
            String[] columnResults = new String[columns];
            for (int j = 0; j < columns; j++) {
                StringBuilder columnBuilder = new StringBuilder("Column " + (j + 1) + ": ");
                for (int i = 0; i < rows; i++) {
                    columnBuilder.append(matrix[i][j]).append(" ");
                }
                columnResults[j] = columnBuilder.toString();
            }
            System.out.println("Columns processed in: " + (System.currentTimeMillis() - taskStartTime) + "ms");
            return columnResults;
        }).thenAcceptAsync(columnsArray -> {
            long taskStartTime = System.currentTimeMillis();
            for (String column : columnsArray) {
                System.out.println(column);
            }
            System.out.println("Columns printed in: " + (System.currentTimeMillis() - taskStartTime) + "ms");
        }).thenRunAsync(() -> {
            System.out.println("All tasks completed successfully!");
            System.out.println("Total execution time: " + (System.currentTimeMillis() - startTime) + "ms");
        }).join(); // Блокування для гарантії виконання всіх задач
    }
}
