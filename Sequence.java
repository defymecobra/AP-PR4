import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Sequence {

    public static void main(String[] args) {
        int numberOfElements = 20; 
        long startTime = System.currentTimeMillis();

        // Генерація початкової послідовності асинхронно
        CompletableFuture<List<Double>> sequenceFuture = CompletableFuture.supplyAsync(() -> {
            long taskStartTime = System.currentTimeMillis();
            List<Double> sequence = DoubleStream.generate(() -> ThreadLocalRandom.current().nextDouble(1, 100))
                    .limit(numberOfElements)
                    .boxed()
                    .collect(Collectors.toList());
            System.out.println("Sequence generated: " + sequence);
            System.out.println("Generation completed in: " + (System.currentTimeMillis() - taskStartTime) + "ms");
            return sequence;
        });

        // Обчислення суми асинхронно
        CompletableFuture<Double> sumFuture = sequenceFuture.thenApplyAsync(sequence -> {
            long taskStartTime = System.currentTimeMillis();
            double sum = sequence.stream().mapToDouble(Double::doubleValue).sum();
            System.out.println("Sum calculated: " + sum);
            System.out.println("Sum calculation completed in: " + (System.currentTimeMillis() - taskStartTime) + "ms");
            return sum;
        });

        // Виведення результату асинхронно
        CompletableFuture<Void> printResultFuture = sumFuture.thenAcceptAsync(sum -> {
            long taskStartTime = System.currentTimeMillis();
            System.out.println("Final result (sum): " + sum);
            System.out.println("Result printed in: " + (System.currentTimeMillis() - taskStartTime) + "ms");
        });

        // Виконання завершального коду з використанням thenRunAsync
        CompletableFuture<Void> finalFuture = printResultFuture.thenRunAsync(() -> {
            System.out.println("All tasks completed successfully!");
            System.out.println("Total execution time: " + (System.currentTimeMillis() - startTime) + "ms");
        });

        // Блокування основного потоку до завершення всіх задач
        finalFuture.join();
    }
}
