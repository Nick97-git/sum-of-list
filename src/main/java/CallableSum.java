import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

public class CallableSum implements Callable<Integer> {
    private static final int THRESHOLD = 10000;
    private final List<Integer> nums;

    public CallableSum(List<Integer> nums) {
        this.nums = nums;
    }

    @Override
    public Integer call() throws Exception {
        int size = nums.size();
        if (size < THRESHOLD) {
            return IntStream.range(0, size)
                    .map(nums::get)
                    .sum();
        }
        int middle = size / 2;
        Callable<Integer> firstHalf = new CallableSum(nums.subList(0, middle));
        Callable<Integer> secondHalf = new CallableSum(nums.subList(middle, size));
        return firstHalf.call() + secondHalf.call();
    }
}
