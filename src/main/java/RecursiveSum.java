import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class RecursiveSum extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10000;
    private final List<Long> nums;

    public RecursiveSum(List<Long> nums) {
        this.nums = nums;
    }

    @Override
    protected Long compute() {
        int size = nums.size();
        if (size < THRESHOLD) {
            return LongStream.range(0L, size)
                    .map(i -> nums.get((int) i))
                    .sum();
        }
        int middle = size / 2;
        RecursiveSum firstHalf = new RecursiveSum(nums.subList(0, middle));
        RecursiveSum secondHalf = new RecursiveSum(nums.subList(middle, size));
        return ForkJoinTask.invokeAll(List.of(firstHalf, secondHalf))
                .stream()
                .mapToLong(ForkJoinTask::join)
                .sum();
    }
}
