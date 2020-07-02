import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SumExecutorService {
    private static final int DIVISOR = 10;
    private final ExecutorService executorService;
    private final List<Long> nums;

    public SumExecutorService(List<Long> nums, int numOfThreads) {
        executorService = Executors.newFixedThreadPool(numOfThreads);
        this.nums = nums;
    }

    public Long calculateListSum() throws InterruptedException {
        List<Future<Long>> futures = executorService.invokeAll(getCallableList());
        return futures.stream()
                .mapToLong(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return 0;
                    }
                }).sum();
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private List<Callable<Long>> getCallableList() {
        List<Callable<Long>> callableList = new ArrayList<>();
        int n = nums.size() / DIVISOR;
        for (int i = 0; i < DIVISOR; i++) {
            Callable<Long> callable = new CallableSum(nums.subList(i * n, (n * (i + 1))));
            callableList.add(callable);
        }
        return callableList;
    }
}
