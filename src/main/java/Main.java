import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {
    public static final int NUM_OF_THREADS = 4;
    public static final int NUM_OF_ELEMENTS = 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        List<Long> nums = LongStream.range(0, NUM_OF_ELEMENTS)
                .boxed()
                .collect(Collectors.toList());

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        RecursiveSum recursiveSum = new RecursiveSum(nums);
        long forkStart = System.currentTimeMillis();
        Long forkJoinSum = forkJoinPool.invoke(recursiveSum);
        long forkEnd = System.currentTimeMillis();
        System.out.println("ForkJoin: " + forkJoinSum + ", time: " + (forkEnd - forkStart));

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        List<Callable<Long>> callableList = new ArrayList<>();
        int n = nums.size() / 10;
        for (int i = 0; i < 10; i++) {
            Callable<Long> callable = new CallableSum(nums.subList(i * n, (n * (i + 1))));
            callableList.add(callable);
        }
        long executorStart = System.currentTimeMillis();
        List<Future<Long>> futures = executorService.invokeAll(callableList);
        long executorSum = futures.stream()
                .mapToLong(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return 0;
                    }
                }).sum();
        long executorEnd = System.currentTimeMillis();
        executorService.shutdown();
        System.out.println("Executor: " + executorSum + ", time: " + (executorEnd - executorStart));
    }
}
