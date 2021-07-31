package program.AntSystem.function;

import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraphs;

import java.util.*;


/**
 * 权值的改变只用于选路，
 * 时间的计算公式 还是按照蚁群的算
 * */
public class Dijkstra {
    private static final int ANT_NUM = 100;
    private static Graph graph;
    //private static double INCREASE = 0.2d;
    //private static final int START = 223;
    //private static final int END = 129;
    private static final double VELOCITY = 0.1d;
    private static int[][] flow;
    public static final double W = 0.05d;//速度-流量的参数
    public static Graph staticGraph;
    public static List<Integer> startNodeList;
    public static List<Integer> endNodeList;
    public static double pathLength=0d;
    //id 为起始节点
    public static List<Integer> dijkstra(Graph graph, int start, int end) {
        Map<Integer, Integer> pre = new HashMap<>();
        Map<Integer, Double> dist = new HashMap<>();
        Map<Integer, Integer> flag = new HashMap<>();
        List<Integer> path = new ArrayList<>();
        List<Integer> allVertex = graph.getAllVertex();
        for (int i = 0; i < graph.nodeNum; ++i) {
            pre.put(allVertex.get(i), start);
            dist.put(allVertex.get(i), graph.vertex.get(start).getWeight(allVertex.get(i)));
            flag.put(allVertex.get(i), 0);
        }
        flag.put(start, 1);
        dist.put(start, 0d);
        int k = 0;
        double min = 0d;
        double tmp = 0d;
        for (int i = 1; i < graph.nodeNum; ++i) {
            min = Integer.MAX_VALUE;
            for (int j = 0; j < graph.nodeNum; ++j) {
                if (flag.get(allVertex.get(j)) == 0 && dist.get(allVertex.get(j)) < min) {
                    min = dist.get(allVertex.get(j));
                    k = j;
                }
            }
            flag.put(allVertex.get(k), 1);
            for (int j = 0; j < graph.nodeNum; ++j) {
                tmp = graph.vertex.get(allVertex.get(k)).getWeight(allVertex.get(j)) == (Integer.MAX_VALUE) ? Integer.MAX_VALUE : (min + graph.vertex.get(allVertex.get(k)).getWeight(allVertex.get(j)));
                if (flag.get(allVertex.get(j)) == 0 && tmp < dist.get(allVertex.get(j))) {
                    dist.put(allVertex.get(j), tmp);
                    pre.put(allVertex.get(j), allVertex.get(k));
                }
            }
        }
        //根据pre数组来求出经过的路径
        path.add(end);
        int t = end;
        while (t != start) {
            t = pre.get(t);
            path.add(0, t);
        }
        return path;
    }

    public static double changeVelocity() {
        return 0d;
    }

    public static void initGraph() {
        String fileName = "src/main/java/program/AntSystem/subshain/finalLink.txt";
        graph = new Graph();
        staticGraph = new Graph();
        ReadFile.initialSubGraph(graph, new SubGraphs(), fileName);
        ReadFile.initialSubGraph(staticGraph, new SubGraphs(), fileName);
        flow = new int[graph.nodeNum][graph.nodeNum];
        fileName = "src/main/java/program/AntSystem/subshain/startend.txt";
        List<List<Integer>> nodes = ReadFile.readIntData(fileName);
        startNodeList = nodes.get(0);
        endNodeList = nodes.get(1);
    }

    public static void changeWeight(int start, int end) {
        double density = flow[start][end] / graph.vertex.get(start).getWeight(end);
        double velocity = VELOCITY * Math.exp(-1 * W * density);
        //时间应该是权值没有变化过的来除
        double time = staticGraph.vertex.get(start).getWeight(end) / velocity;
        double weight = time * VELOCITY;
        graph.vertex.get(start).addNbr(end, weight);
    }

    public static double getTime(int start, int end) {
        double density = flow[start][end] / staticGraph.vertex.get(start).getWeight(end);
        double velocity = VELOCITY * Math.exp(-1 * W * density);
        return staticGraph.vertex.get(start).getWeight(end) / velocity;
    }

    public static double getSumTime() {
        double sumTime = 0d;
        for (int i = 0; i < ANT_NUM; ++i) {
            int start = startNodeList.get(i);
            int end = endNodeList.get(i);
            List<Integer> path = dijkstra(graph, start, end);//迪杰斯特拉算法挑选路径
            int s = path.get(0);
            for (int j = 1; j < path.size(); ++j) {
                sumTime += getTime(s, path.get(j));
                pathLength += staticGraph.vertex.get(s).getWeight(path.get(j));
                changeWeight(s, path.get(j));
                ++flow[s][path.get(j)];
                s = path.get(j);
            }
        }
        return sumTime;
    }

    public static void test() {
        initGraph();
        System.out.println("总时间"+getSumTime());
        System.out.println("总路径"+pathLength);
        System.out.println("平均速度"+(pathLength/getSumTime()));
        //List<Integer> path=dijkstra(staticGraph,178,189);
    }

    public static void main(String[] args) {
        test();
    }
}
