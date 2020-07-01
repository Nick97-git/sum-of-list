import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class RecursiveSum extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 10000;
    private final List<Integer> nums;

    public RecursiveSum(List<Integer> nums) {
        this.nums = nums;
    }

    @Override
    protected Integer compute() {
        int size = nums.size();
        if (size < THRESHOLD) {
            return IntStream.range(0, size)
                    .map(nums::get)
                    .sum();
        }
        int middle = size / 2;
        RecursiveSum firstHalf = new RecursiveSum(nums.subList(0, middle));
        RecursiveSum secondHalf = new RecursiveSum(nums.subList(middle, size));
        return ForkJoinTask.invokeAll(List.of(firstHalf, secondHalf))
                .stream()
                .mapToInt(ForkJoinTask::join)
                .sum();
    }
}
