package program.work;

import java.util.*;

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

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode();
        ListNode p = head;
        int jump = 0;
        while (l1 != null || l2 != null) {
            int a = l1 == null ? 0 : l1.val;
            int b = l2 == null ? 0 : l2.val;
            int res = a + b + jump;
            jump = 0;
            if (res > 9) {
                jump = 1;
                res -= 10;
            }
            if (l1 != null ) {
                l1.val = res;
                p.next = l1;
                p = l1;
            } else {
                l2.val = res;
                p.next = l2;
                p = l2;
            }
            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
        }
        if (jump != 0 ) {
            ListNode t = new ListNode(1);
            p.next = t;
            p = t;
        }
        p.next = null;
        return head.next;
    }

    public int lengthOfLongestSubstring(String s) {
        int res = 0;
        Set<Character> tmp = new HashSet<>();
        int low = 0;
        int high = 0;
        while (high < s.length()) {
            while (high < s.length() &&!tmp.contains(s.charAt(high))) {
                tmp.add(s.charAt(high++));
            }
            res = Math.max(res, high - low);
            if (high >= s.length()) {
                return res;
            }
            while (low < high && s.charAt(low) != s.charAt(high)) {
                tmp.remove(s.charAt(low++));
            }
            tmp.remove(s.charAt(low++));
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(maxSubArray(nums));
    }
}
