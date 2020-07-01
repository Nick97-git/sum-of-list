import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> nums = IntStream.range(0, 1_000_000)
                .boxed()
                .collect(Collectors.toList());

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        RecursiveSum recursiveSum = new RecursiveSum(nums);
        Integer forkJoinSum = forkJoinPool.invoke(recursiveSum);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Callable<Integer> callable = new CallableSum(nums);
        Future<Integer> future = executorService.submit(callable);
        Integer executorSum = future.get();
        executorService.shutdown();
    }
}
