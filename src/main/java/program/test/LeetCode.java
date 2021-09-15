package program.test;

import java.util.*;

class ListNode{
    int val;
    ListNode next;
    ListNode(){}
    ListNode(int val){
        this.val=val;
    }
}
class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(){}
    TreeNode(int val){
        this(val,null,null);
    }
    TreeNode(int val,TreeNode left,TreeNode right){
        this.val=val;
        this.left=left;
        this.right=right;
    }
}
public class LeetCode {
    static ListNode createList(int[] a) {
        ListNode h = new ListNode(a[0]);
        ListNode p = h;
        for (int i = 1; i < a.length; ++i) {
            ListNode t = new ListNode(a[i]);
            p.next = t;
            p = t;
        }
        p.next = null;
        return h;
    }

    static int hIndex(int[] citations) {
        //6 5 3 1 0
        Arrays.sort(citations);
        int res = 0;
        for (int i = citations.length - 1; i >= 0; --i) {
            if (citations[i] > res) {
                ++res;
            }
        }
        return res;
    }

    static List<String> res;

    static void dfs(int left, int right, StringBuilder t) {
        if (right == 0) {
            res.add(t.toString());
            return;
        }
        if (left != right && right > 0) {
            StringBuilder tmp = new StringBuilder(t);
            dfs(left, right - 1, tmp.append(')'));
        }
        if (left > 0) {
            StringBuilder tmp = new StringBuilder(t);
            dfs(left - 1, right, tmp.append('('));
        }
    }

    static List<String> generateParenthesis(int n) {
        res = new ArrayList<>();
        dfs(n, n, new StringBuilder());
        return res;
    }

    static int searchInsert(int[] nums, int target) {
        if (target > nums[nums.length - 1]) {
            return nums.length;
        }
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    static int trap(int[] height) {
        int[] tmp = new int[height.length];
        int max = 0;
        for (int i = 0; i < height.length; ++i) {
            max = Math.max(max, height[i]);
            tmp[i] = max;
        }
        int res = 0;
        max = 0;
        for (int i = height.length - 1; i >= 0; --i) {
            max = Math.max(max, height[i]);
            res += Math.min(tmp[i], max) - height[i];
        }
        return res;
    }

    static int maxLen(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left - 1;
    }

    // 1 2
    static String longestPalindrome(String s) {
        int max = 0;
        int resLeft = 0;
        int resRight = 0;
        for (int i = 0; i < s.length(); ++i) {
            int one = maxLen(s, i, i);//奇数
            if (one > max) {
                max = one;
                resLeft = i - one / 2;
                resRight = i + one / 2;
            }
            int two = maxLen(s, i, i + 1);//偶数
            if (two > max) {
                max = two;
                resLeft = i - two / 2 + 1;
                resRight = i + two / 2;
            }
        }
        return s.substring(resLeft, resRight + 1);
    }

    static String convert(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }
        List<StringBuilder> tmp = new ArrayList<>();
        for (int i = 0; i < numRows; ++i) {
            tmp.add(new StringBuilder());
        }
        int index = 0;
        boolean tag = true;
        for (int i = 0; i < s.length(); ++i) {
            tmp.get(index).append(s.charAt(i));
            if (tag) {
                ++index;
                if (index == numRows - 1) {
                    tag = false;
                }
            } else {
                --index;
                if (index == 0) {
                    tag = true;
                }
            }
        }
        StringBuilder res = new StringBuilder();
        for (StringBuilder t : tmp) {
            res.append(t);
        }
        return res.toString();
    }

    static List<List<Integer>> resp;
    static boolean[] tag;

    static void dfs(int[] nums, int cur, List<Integer> tmp) {
        if (cur == nums.length) {
            resp.add(tmp);
            return;
        }
        for (int i = 0; i < nums.length; ++i) {
            if (!tag[i]) {
                List<Integer> t = new ArrayList<>(tmp);
                t.add(nums[i]);
                tag[i] = true;
                dfs(nums, cur + 1, t);
                tag[i] = false;
            }
        }
    }

    static List<List<Integer>> permute(int[] nums) {
        resp = new ArrayList<>();
        tag = new boolean[nums.length];
        dfs(nums, 0, new ArrayList<>());
        return resp;
    }

    static int jump(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 0; i < nums.length; ++i) {
            for (int j = i + 1; j < Math.min(nums.length, i + nums[i] + 1); ++j) {
                dp[j] = Math.min(dp[j], dp[i] + 1);
            }
        }
        System.out.println(Arrays.toString(dp));
        return dp[nums.length - 1];
    }

    static int jump1(int[] nums) {
        int length = nums.length;
        int end = 0;
        int maxPosition = 0;
        int step = 0;
        for (int i = 0; i < length - 1; ++i) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == end) {
                end = maxPosition;
                ++step;
            }
        }
        return step;
    }

    static int reverse(int x) {
        if (x == 0 || x == Integer.MIN_VALUE) {
            return 0;
        }
        int tag = x > 0 ? 1 : -1;
        x = Math.abs(x);
        int boundary = Integer.MAX_VALUE / 10;
        int res = 0;
        while (x != 0) {
            int t = x % 10;
            if (res > boundary) {
                return 0;
            } else if (res == boundary) {
                if (t >= 7) {
                    return 0;
                } else {
                    res = res * 10 + t;
                }
            } else {
                res = res * 10 + t;
            }
            x = x / 10;
        }
        return res * tag;
    }

    static int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < dp.length; ++i) {
            for (int j = 0; j < dp[i].length; ++j) {
                if (i == 0 && j == 0) {
                    dp[i][j] = 0;
                } else if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    static int rob(int[] nums) {
        int yes = nums[0];
        int no = 0;
        int t;
        for (int i = 1; i < nums.length; ++i) {
            t = nums[i] + no;
            no = Math.max(no, yes);
            yes = t;

        }
        return Math.max(yes, no);
    }

    static int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        //左右两边一定有一个是有序的数组
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[low] <= nums[mid]) {
                if (target >= nums[low] && target <= nums[mid]) {
                    high = mid;
                } else {
                    low = mid + 1;
                }
            } else {
                if (target >= nums[mid] && target <= nums[high]) {
                    low = mid;
                } else {
                    high = mid - 1;
                }
            }
        }
        return nums[low] == target ? low : -1;
    }

    static int[] searchRange(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] > target) {
                high = mid - 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                low = mid;
                break;
            }
        }
        if (nums[low] != target) {
            return new int[]{-1, -1};
        }
        int left = low;
        int right = low;
        while (left >= 0 && nums[left] == target) {
            --left;
        }
        while (right < nums.length && nums[right] == target) {
            ++right;
        }
        return new int[]{left + 1, right - 1};
    }

    static int firstMissing(int[] nums) {
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] <= 0) {
                nums[i] = nums.length + 1;
            }
        }
        for (int i = 0; i < nums.length; ++i) {
            int t = nums[i];
            if (Math.abs(t) <= nums.length) {
                t = Math.abs(t) - 1;
                nums[t] = -Math.abs(nums[t]);
            }
        }
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] >= 0) {
                return i + 1;
            }
        }
        return nums.length;
    }

    static StringBuilder mul(String num1, int num2) {
        if (num2 == 0) {
            return new StringBuilder("0");
        }
        StringBuilder res = new StringBuilder();
        int i = num1.length() - 1;
        int jump = 0;
        while (i >= 0) {
            int r = num2 * (num1.charAt(i) - '0') + jump;
            jump = r / 10;
            r = r % 10;
            res.insert(0, r);
            --i;
        }
        if (jump != 0) {
            res.insert(0, jump);
        }
        return res;
    }

    static StringBuilder add(String num1, String num2) {
        StringBuilder res = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int jump = 0;
        while (i >= 0 || j >= 0) {
            int numA = i >= 0 ? num1.charAt(i) - '0' : 0;
            int numB = j >= 0 ? num2.charAt(j) - '0' : 0;
            int r = numA + numB + jump;
            if (r > 9) {
                r -= 10;
                jump = 1;
            } else {
                jump = 0;
            }
            res.insert(0, r);
            --i;
            --j;
        }
        if (jump != 0) {
            res.insert(0, jump);
        }
        return res;
    }

    static String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        StringBuilder res = new StringBuilder('0');
        int a = 1;
        for (int i = num2.length() - 1; i >= 0; --i) {
            StringBuilder t = mul(num1, num2.charAt(i) - '0');
            if (!t.equals("0")) {
                for (int j = num2.length() - 1; j > i; --j) {
                    t.append('0');
                }
            }
            res = add(res.toString(), t.toString());
        }
        return res.toString();
    }

    static List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<List<Integer>> res = new ArrayList<>();
        while (!queue.isEmpty()) {
            List<Integer> t = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; ++i) {
                root = queue.poll();
                t.add(root.val);
                if (root.left != null) {
                    queue.offer(root.left);
                }
                if (root.right != null) {
                    queue.offer(root.right);
                }
            }
            res.add(t);
        }
        return res;
    }

    static String countArray(int n) {
        if (n < 1) {
            return "";
        }
        StringBuilder res = new StringBuilder("1");
        for (int i = 2; i <= n; ++i) {
            StringBuilder t = new StringBuilder();
            int j = 0;
            while (j < res.length()) {
                int num = res.charAt(j) - '0';
                int index = j;
                while (j < res.length() - 1 && res.charAt(j) == res.charAt(j + 1)) {
                    ++j;
                }
                ++j;
                t.append(j - index);
                t.append(num);
            }
            res = new StringBuilder(t);
        }
        return res.toString();
    }

    static void numReverse(int[] nums, int low, int high) {
        while (low < high) {
            int t = nums[low];
            nums[low] = nums[high];
            nums[high] = t;
            ++low;
            --high;
        }
    }

    static void rotate(int[] nums, int k) {
        numReverse(nums, nums.length - k, nums.length - 1);
        numReverse(nums, 0, nums.length - k - 1);
        numReverse(nums, 0, nums.length - 1);
    }

    static int removeSDuplicates(int[] nums) {
        if (nums.length < 2) {
            return nums.length;
        }
        int index = 0;
        int tag = 1;
        for (int i = 1; i < nums.length; ++i) {
            if (nums[i] == nums[index]) {
                if (tag == 1) {
                    nums[++index] = nums[i];
                    ++tag;
                }
            } else {
                tag = 1;
                nums[++index] = nums[i];
            }

        }
        return index + 1;
    }

    // 0 1  第 0行，第1 列 0 3
    static void setZeros(int[][] matrix) {
        int[] x = new int[matrix[0].length];
        int[] y = new int[matrix.length];
        Arrays.fill(x, 1);
        Arrays.fill(y, 1);
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == 0) {
                    x[j] = 0;
                    y[i] = 0;
                }
            }
        }
        //某一列置为0
        for (int i = 0; i < x.length; ++i) {
            if (x[i] == 0) {
                for (int j = 0; j < matrix.length; ++j) {
                    matrix[j][i] = 0;
                }
            }
        }
        //某一行置为0
        for (int i = 0; i < y.length; ++i) {
            if (y[i] == 0) {
                Arrays.fill(matrix[i], 0);
            }
        }
    }

    static ListNode deleteDuplicates(ListNode head) {
        ListNode h = new ListNode();
        ListNode p = h;
        while (head != null) {
            if (head.next == null || head.val != head.next.val) {
                p.next = head;
                p = head;
                head = head.next;
            } else {
                int val = head.val;
                while (head != null && head.val == val) {
                    head = head.next;
                }
            }
        }
        p.next = null;
        return h.next;
    }

    static boolean isVowels(char s) {
        if (s == 'a' || s == 'A') {
            return true;
        }
        if (s == 'e' || s == 'E') {
            return true;
        }
        if (s == 'i' || s == 'I') {
            return true;
        }
        if (s == 'o' || s == 'O') {
            return true;
        }
        if (s == 'u' || s == 'U') {
            return true;
        }
        return false;
    }

    static String reverseVowels(String s) {
        StringBuilder res = new StringBuilder(s);
        int left = 0;
        int right = res.length() - 1;
        while (left < right) {
            while (left < right && !isVowels(res.charAt(right))) {
                --right;
            }
            while (left < right && !isVowels(res.charAt(left))) {
                ++left;
            }
            char t = res.charAt(right);
            res.setCharAt(right, res.charAt(left));
            res.setCharAt(left, t);
            --right;
            ++left;
        }
        return res.toString();
    }

    static int findPaths(int m, int n, int maxMove, int startRow, int startColumn) {
        final int MOD = 1000000007;
        int[][][] dp = new int[maxMove + 1][m][n];
        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};//方便遍历四个方向
        int sum = 0;
        dp[0][startRow][startColumn] = 1;
        for (int i = 0; i < maxMove; ++i) {//从第0步开始，最后就可以直接返回sum了
            for (int j = 0; j < m; ++j) {
                for (int k = 0; k < n; ++k) {
                    int now=dp[i][j][k];
                    if (now>0) {
                        for (int[] move : moves) {
                            int x = j + move[0];
                            int y = k + move[1];
                            if (x == m || x < 0 || y == n || y < 0) {
                                sum = (sum + now) % MOD;
                            }else {
                                dp[i+1][x][y]=(dp[i+1][x][y]+now)%MOD;
                            }
                        }
                    }
                }
            }
        }
        return sum;
    }

    //所有节点最短路径里面长度最长的长度
    static int networkDelayTime(int[][] times,int n,int k) {
        int[][] graph = new int[n + 1][n + 1];
        for (int[] g:graph) {
            Arrays.fill(g, Integer.MAX_VALUE);
        }
        for (int[] edge : times) {
            graph[edge[0]][edge[1]] = edge[2];
        }
        int[] pre = new int[n + 1];
        int[] dist = new int[n + 1];
        boolean[] tag = new boolean[n + 1];
        for (int i = 1; i <= n; ++i) {
            pre[i] = -1;
            dist[i] = graph[k][i];
        }
        dist[k] = 0;
        tag[k] = true;
        for (int i = 1; i < n; ++i) {
            int min = Integer.MAX_VALUE;
            int index = i;
            for (int j = 1; j <= n; ++j) {
                if (!tag[j] && dist[j] < min) {
                    index = j;
                    min = dist[j];
                }
            }
            tag[index] = true;
            for (int j = 1; j <= n; ++j) {
                int tmp = graph[index][j] == Integer.MAX_VALUE ? Integer.MAX_VALUE : graph[index][j] + min;
                if (!tag[j] && tmp < dist[j]) {
                    dist[j] = tmp;
                    pre[j] = k;
                }
            }
        }
        int ant = Arrays.stream(dist).max().getAsInt();
        return ant == Integer.MAX_VALUE ? -1 : ant;
    }

    static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length==1){
            return lists[0];
        }
        ListNode head = new ListNode();
        ListNode p = head;
        int sum = 0;
        while (sum != lists.length) {
            int index = 0;
            int num = Integer.MAX_VALUE;
            for (int i = 0; i < lists.length; ++i) {
                if (lists[i] != null && lists[i].val < num) {
                    index = i;
                    num = lists[i].val;
                }
            }
            if (num==Integer.MAX_VALUE){
                return head;
            }
            p.next = lists[index];
            p = lists[index];
            lists[index] = lists[index].next;
            if (lists[index] == null) {
                ++sum;
            }
        }
        p.next = null;
        return head.next;
    }

    static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> stack = new LinkedList<>();
        while (!stack.isEmpty() && root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            res.add(root.val);
            root = root.right;
        }
        return res;
    }

    static boolean isValidB(TreeNode node ,long lower,long upper){
        if (node==null){
            return true;
        }
        if (node.val<lower||node.val>upper){
            return false;
        }
        return isValidB(node.left,lower,node.val)&&isValidB(node,node.val,upper);
    }

    static boolean isValidBST(TreeNode root) {
        return isValidB(root,Long.MIN_VALUE,Long.MAX_VALUE);
    }

    static int[] intersection(int[] nums1,int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int index1 = 0;
        int index2 = 0;
        List<Integer> res = new ArrayList<>();
        while (index1 < nums1.length && index2 < nums2.length) {
            if (nums1[index1] == nums2[index2]) {
                int t = nums1[index1];
                res.add(nums1[index1]);
                while (index1 < nums1.length && nums1[index1] == t) {
                    ++index1;
                }
                while (index2 < nums2.length && nums2[index2] == t) {
                    ++index2;
                }
            } else {
                if (nums1[index1] < nums2[index2]) {
                    ++index1;
                } else {
                    ++index2;
                }
            }
        }
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    static int ipv4ToInt(String ipv4) {
        String[] adds = ipv4.split("\\.");
        int res = 0;
        for (int i = 0; i < adds.length; ++i) {
            int t = Integer.parseInt(adds[i]) << (8 * i);
            res = res | t;
        }
        return res;
    }

    static String intToIpv4(int add) {
        StringBuilder res = new StringBuilder();
        final int T = 255;
        for (int i = 0; i < 4; ++i) {
            int r = add & T;
            res.append(r);
            res.append('.');
            add = add >> 8;
        }
        res.deleteCharAt(res.length() - 1);
        return res.toString();
    }

    static int quickSort(int[] nums,int low,int high) {
        if (low >= high) {
            return low;
        }
        int i = low;
        int j = high;
        int temp = nums[low];
        while (i < j) {
            while (i < j && nums[j] >= temp) {
                --j;
            }
            nums[i] = nums[j];
            while (i < j && nums[i] <= temp) {
                ++i;
            }
            nums[j] = nums[i];
        }
        nums[i]=temp;
        return i;
    }

    static int findKthLargest(int[] nums,int k) {
        if (nums.length < k) {
            return -1;
        }
        k = nums.length - k;
        int low = 0;
        int high = nums.length - 1;
        int mid = -1;
        while (mid != k) {
            mid = quickSort(nums, low, high);
            if (mid > k) {
                high = mid - 1;
            } else if (mid < k) {
                low = mid + 1;
            }
        }
        return nums[mid];
    }

    static int result=0;

    static void dfs(TreeNode root,int val) {
        if (root == null) {
            return;
        } else if (root.left == null && root.right == null) {
            result += val * 10 + root.val;
        } else {
            dfs(root.left, val * 10 + root.val);
            dfs(root.right, val * 10 + root.val);
        }
    }

    static int sumNumbers(TreeNode root){
        dfs(root,root.val);
        return result;
    }

    static ListNode detectCycle(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null) {
            slow = slow.next;
            if (fast.next != null) {
                fast = fast.next.next;
            } else {
                return null;
            }
            if (fast == slow) {
                ListNode p = head;
                while (p != slow) {
                    p = p.next;
                    slow = slow.next;
                }
                return p;
            }
        }
        return null;
    }

    static void kTh(TreeNode root,int[] t) {
        if (root != null) {
            kTh(root.left, t);
            if (t[0] == 0) {
                return;
            }
            --t[0];
            if (t[0] == 1) {
                t[1] = root.val;
                return;
            }
            kTh(root.right, t);
        }
    }

    static int kthSmallest(TreeNode root, int k) {
        int[] nums = {k, 0};
        kTh(root, nums);
        return nums[1];
    }

    static int maxPathSumResult=Integer.MIN_VALUE;

    static int maxPath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = Math.max(0, maxPath(root.left));
        int right = Math.max(0, maxPath(root.right));
        int newPath = left + right + root.val;
        maxPathSumResult = Math.max(maxPathSumResult, newPath);
        return root.val + Math.max(left, right);
    }

    static int maxPathSum(TreeNode root){
        maxPath(root);
        return maxPathSumResult;
    }

    static String removeKDigits(String num, int k) {
        Deque<Character> stack = new LinkedList<>();
        int len = num.length();
        for (int i = 0; i < len; ++i) {
            char digit = num.charAt(i);
            while (!stack.isEmpty() && k > 0 && stack.peek() > digit) {
                stack.pop();
                --k;
            }
            stack.push(digit);
        }
        for (int i = 0; i < k; ++i) {
            stack.pop();
        }
        System.out.println(stack);
        StringBuilder res = new StringBuilder();
        boolean leadingZero = true;
        while (!stack.isEmpty()) {
            char digit = stack.pollLast();
            System.out.println(digit);
            if (leadingZero && digit == '0') {
                continue;
            }
            leadingZero = false;
            res.append(digit);
        }
        return res.length() == 0 ? "0" : res.toString();
    }

    static void test() {
        String s="10200";
        System.out.println(removeKDigits(s,1));
    }

    public static void main(String[] args) {
        test();
    }
}