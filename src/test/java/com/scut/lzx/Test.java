package com.scut.lzx;


import java.util.*;

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

public class Test {
    static void quickSort(int[] a, int low, int high) {
        if (low >= high) {
            return;
        }
        int i = low;
        int j = high;
        int temp = a[low];
        while (i < j) {
            while (i < j && a[j] >= temp) {
                --j;
            }
            a[i] = a[j];
            while (i < j && a[i] <= temp) {
                ++i;
            }
            a[j] = a[i];
        }
        a[i] = temp;
        quickSort(a, low, i - 1);
        quickSort(a, i + 1, high);
    }

    // 0 1 3
    static void merge(int[] a, int low, int mid, int high) {
        int[] temp1 = new int[mid - low + 1];
        int[] temp2 = new int[high - mid];
        for (int i = low; i <= high; ++i) {
            if (i <= mid) {
                temp1[i - low] = a[i];
            } else {
                temp2[i - mid - 1] = a[i];
            }
        }
        int index = low;
        int index1 = 0;
        int index2 = 0;
        while (index1 < temp1.length || index2 < temp2.length) {
            if (index2 == temp2.length || (index1 < temp1.length && temp1[index1] < temp2[index2])) {
                a[index++] = temp1[index1++];
            } else {
                a[index++] = temp2[index2++];
            }
        }
    }

    static void mergeSort(int[] a, int low, int high) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergeSort(a, low, mid);
            mergeSort(a, mid + 1, high);
            merge(a, low, mid, high);
        }
    }

    static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    static void adjustHeap(int[] a, int i, int length) {
        int temp = a[i];
        for (int k = i * 2 + 1; k < length; ++k) {
            if (k + 1 < length && a[k] < a[k + 1]) {
                ++k;
            }
            if (a[k] > temp) {
                a[i] = a[k];
                i = k;
            }
        }
        a[i] = temp;
    }

    static void heapSort(int[] a) {
        for (int i = (a.length - 1) / 2; i >= 0; --i) {
            adjustHeap(a, i, a.length);
        }
        for (int i = a.length - 1; i >= 0; --i) {
            swap(a, 0, i);
            adjustHeap(a, 0, i);
        }
    }

    static boolean isValid(String s) {
        Deque<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(' || s.charAt(i) == '{' || s.charAt(i) == '[') {
                stack.push(s.charAt(i));
            } else if (s.charAt(i) == ')') {
                if (stack.size() != 0 && stack.peek() == '(') {
                    stack.pop();
                } else {
                    return false;
                }
            } else if (s.charAt(i) == ']') {
                if (stack.size() != 0 && stack.peek() == '[') {
                    stack.pop();
                } else {
                    return false;
                }
            } else {
                if (stack.size() != 0 && stack.peek() == '{') {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    static ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode h = new ListNode();
        h.next = head;
        ListNode p = h.next;
        head = head.next;
        while (head != null) {
            if (head.val != p.val) {
                p.next = head;
                p = head;
            }
            head = head.next;
        }
        p.next = null;
        return h.next;
    }

    static int searchInsert(int[] nums, int target) {
        if (nums[nums.length - 1] < target) {
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

    static int findPeakElement(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (mid == 0 || mid == nums.length - 1) {
                return mid;
            } else if (nums[mid] < nums[mid + 1]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int low = 0;
        int high = m * n - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (matrix[mid / n][mid % n] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return matrix[low / n][low % n] == target;
    }

    static int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[low] < nums[mid]) {
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

    static int findMin(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
        }
        return 0;
    }

    static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int left = 0;
        int right = matrix[0].length - 1;
        int top = 0;
        int down = matrix.length - 1;
        int sum = matrix.length * matrix[0].length;
        while (res.size() < sum) {
            for (int i = left; i <= right && res.size() < sum; ++i) {
                res.add(matrix[top][i]);
            }
            ++top;
            for (int i = top; i <= down && res.size() < sum; ++i) {
                res.add(matrix[i][right]);
            }
            --right;
            for (int i = right; i >= left && res.size() < sum; --i) {
                res.add(matrix[down][i]);
            }
            --down;
            for (int i = down; i >= top && res.size() < sum; --i) {
                res.add(matrix[i][left]);
            }
            ++left;
        }
        return res;
    }

    static int SEG_COUNT = 3;
    static List<String> res;

    static void dfs(String s, int cur, int seg, StringBuilder t) {
        if (cur == s.length() && seg <= 3) {
            return;
        }
        if (seg == SEG_COUNT) {
            if ((s.length() - cur < 4 && Integer.valueOf(s.substring(cur)) <= 255) || (cur == s.length() - 1)) {
                t.append(s.substring(cur));
                res.add(t.toString());
            }
            return;
        } else if (s.charAt(cur) == '0') {
            StringBuilder tmp = new StringBuilder(t);
            tmp.append("0.");
            dfs(s, cur + 1, seg + 1, tmp);
        } else {
            int i = 1;
            while (i <= 3 && cur + i < s.length() && Integer.valueOf(s.substring(cur, cur + i)) <= 255) {
                StringBuilder tmp = new StringBuilder(t);
                tmp.append(Integer.valueOf(s.substring(cur, cur + i)));
                tmp.append('.');
                dfs(s, cur + i, seg + 1, tmp);
                ++i;
            }
        }
    }

    static List<String> restoreIpAddress(String s) {
        res = new ArrayList<>();
        dfs(s, 0, 0, new StringBuilder());
        return res;
    }

    static void dfs(char[][] grid, int x, int y) {
        if (x < 0 || x == grid[0].length || y < 0 || y == grid.length || grid[x][y] != '1') {
            return;
        }
        grid[x][y] = '0';
        dfs(grid, x + 1, y);
        dfs(grid, x - 1, y);
        dfs(grid, x, y + 1);
        dfs(grid, x, y - 1);
    }

    static int numsIslands(char[][] grid) {
        int res = 0;
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                if (grid[i][j] == '1') {
                    ++res;
                    dfs(grid, i, j);
                }
            }
        }
        return res;
    }

    static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return res;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; ++i) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int j = i + 1;
            int k = nums.length - 1;
            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];
                if (sum > 0) {
                    --k;
                } else if (sum < 0) {
                    ++j;
                } else {
                    res.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    while (j < k && nums[j] == nums[j + 1]) {
                        ++j;
                    }
                    ++j;
                    while (j < k && nums[k] == nums[k - 1]) {
                        --k;
                    }
                    --k;
                }
            }
        }
        return res;
    }

    static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (int[] o1, int[] o2) -> o1[0] - o2[0]);
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < intervals.length; ++i) {
            int l = intervals[i][0];
            int r = intervals[i][1];
            if (res.size() == 0 || res.get(res.size() - 1)[1] <= l) {
                res.add(new int[]{l, r});
            } else {
                res.get(res.size() - 1)[1] = Math.max(r, res.get(res.size() - 1)[1]);
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    static ListNode rotateRight(ListNode head, int k) {
        int len = 0;
        ListNode p = head;
        while (p.next != null) {
            p = p.next;
            ++len;
        }
        p.next = head;
        k = k % (len + 1);
        k = len - k;
        for (int i = 0; i < k; ++i) {
            p = p.next;
        }
        head = p.next;
        p.next = null;
        return head;
    }

    static int[][] generateMatrix(int n) {
        int[][] res = new int[n][n];
        int i = 1;
        int x = 0;
        int y = 0;
        char direction = 'r';
        while (i <= n * n) {
            if (direction == 'r') {//向右
                res[x][y] = i++;
                if (y == n - 1 || res[x][y + 1] != 0) {
                    direction = 'd';
                    ++x;
                } else {
                    ++y;
                }
            } else if (direction == 'd') {//向下
                res[x][y] = i++;
                if (x == n - 1 || res[x + 1][y] != 0) {
                    direction = 'l';
                    --y;
                } else {
                    ++x;
                }
            } else if (direction == 'l') {//想左
                res[x][y] = i++;
                if (y == 0 || res[x][y - 1] != 0) {
                    direction = 't';
                    --x;
                } else {
                    --y;
                }
            } else {//向上
                res[x][y] = i++;
                if (x == 0 || res[x - 1][y] != 0) {
                    direction = 'r';
                    ++y;
                } else {
                    --x;
                }
            }
        }
        return res;
    }

    static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode h = new ListNode();
        ListNode p = h;
        int jump = 0;
        while (l1 != null || l2 != null) {
            int num1 = l1 == null ? 0 : l1.val;
            int num2 = l2 == null ? 0 : l2.val;
            int res = num1 + num2 + jump;
            if (res > 9) {
                jump = 1;
                res -= 10;
            } else {
                jump = 0;
            }
            if (l1 != null) {
                l1.val = res;
                p.next = l1;
                p = l1;
            } else {
                l2.val = res;
                p.next = l2;
                p = l2;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (jump != 0) {
            ListNode s = new ListNode(jump);
            p.next = s;
            p = s;
        }
        p.next = null;
        return h.next;
    }

    static int minDistance(String word1, String word2) {
        if (word1.length() * word2.length() == 0) {
            return word1.length() + word2.length();
        }
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i < word1.length(); ++i) {
            dp[i][0] = i;
        }
        for (int i = 0; i < word2.length(); ++i) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= word1.length(); ++i) {
            for (int j = 1; j <= word2.length(); ++j) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }

    static class UnionFind {
        int count;
        int[] parent;
        int[] rank;

        public UnionFind(char[][] grid) {
            count = 0;
            int m = grid.length;
            int n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (grid[i][j] == '1') {
                        parent[i * n + j] = i * n + j;
                        ++count;
                    }
                    rank[i * n + j] = 0;
                }
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                } else {
                    parent[rooty] = rootx;
                    rank[rootx] += 1;
                }
                --count;
            }
        }
        public int getCount(){
            return this.count;
        }
    }

    static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        Test.UnionFind unionFind = new Test.UnionFind(grid);
        for (int r = 0; r < m; ++r) {
            for (int c = 0; c < n; ++c) {
                if (grid[r][c] == '1') {
                    grid[r][c] = '0';
                    if (r - 1 >= 0 && grid[r - 1][c] == '1') {
                        unionFind.union(r * n + c, (r - 1) * n + c);
                    }
                    if (r + 1 < m && grid[r + 1][c] == '1') {
                        unionFind.union(r * n + c, (r + 1) * n + c);
                    }
                    if (c - 1 >= 0 && grid[r][c - 1] == '1') {
                        unionFind.union(r * n + c, r * n + c - 1);
                    }
                    if (c + 1 < n && grid[r][c + 1] == '1') {
                        unionFind.union(r * n + c, r * n + c + 1);
                    }
                }
            }
        }
        return unionFind.getCount();
    }

    @org.junit.Test
    public void test1() {
        System.out.println(minDistance("distance","springbok"));
    }
}
