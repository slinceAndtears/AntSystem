package program.work;

import java.util.HashMap;
import java.util.Map;

public class LeetCodeNew {
    //如果前面的累加和小于0，就抛弃掉，累加和赋值为当前下标
    public static int maxSubArray(int[] nums) {
        int res = nums[0];
        int add = nums[0];
        for (int i = 1; i < nums.length; ++i) {
            if (add < 0) {
                add = nums[i];
            } else {
                add += nums[i];
            }
            res = Math.max(add, res);
        }
        return res;
    }

    public static int getAddSum(int i) {
        int sum = 0;
        while (i != 0) {
            sum += i% 10;
            i = i/10;
        }
        return sum;
    }

    public static int maximumSum(int[] nums) {
        int res = -1;
        int[] addSum = new int[nums.length];
        for (int i = 0; i < nums.length; ++i) {
            addSum[i] = getAddSum(nums[i]);
        }
        Map<Integer, Integer> tmp = new HashMap<>();
        for (int i = 0; i < addSum.length; ++i) {
            if (tmp.containsKey(addSum[i])) {
                res = Math.max(tmp.get(addSum[i]) + nums[i], res);
                tmp.put(addSum[i], Math.max(tmp.get(addSum[i]), nums[i]));
            } else {
                tmp.put(addSum[i], nums[i]);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(maxSubArray(nums));
    }
}
