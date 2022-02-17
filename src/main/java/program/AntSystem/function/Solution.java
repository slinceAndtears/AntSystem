package program.AntSystem.function;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<List<Integer>> path;
    public List<List<Integer>> areaPath;//区域图走的路径
    public double sumTime;
    public double sumLength;

    public Solution() {
    }

    public Solution(List<List<Integer>> p, double t, List<List<Integer>> a, double l) {
        this.sumTime = t;
        this.sumLength = l;
        path = new ArrayList<>();
        for (List<Integer> tmp : p) {
            path.add(new ArrayList<>(tmp));
        }
        areaPath = new ArrayList<>();
        for (List<Integer> list : a) {
            areaPath.add(new ArrayList<>(list));
        }
    }

    @Override
    public String toString() {
        return "总时间为：" + sumTime + "  总路径为：" + sumLength + "  平均速度为:  " + sumLength / sumTime;
    }
}
