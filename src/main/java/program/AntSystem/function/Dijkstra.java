package program.AntSystem.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraphs;

import java.util.*;


/**
 * 权值的改变只用于选路，
 * 时间的计算公式 还是按照蚁群的算
 * */
public class Dijkstra {
    private static final Logger logger = LoggerFactory.getLogger(Dijkstra.class);
    private static final int ANT_NUM = Aco.ANT_NUM;
    private static Graph graph;
    private static final double VELOCITY = Aco.VELOCITY;
    private static int[][] flow;
    public static final double W = Aco.W;//速度-流量的参数
    public static Graph staticGraph;
    public static List<Integer> startNodeList;
    public static List<Integer> endNodeList;
    public static double pathLength = 0d;
    public static double timeOffset = Aco.timeOffset;

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
                tmp = graph.vertex.get(allVertex.get(k)).getWeight(allVertex.get(j)) == (Integer.MAX_VALUE)
                        ? Integer.MAX_VALUE :
                        (min + graph.vertex.get(allVertex.get(k)).getWeight(allVertex.get(j)));
                if (flag.get(allVertex.get(j)) == 0 && tmp < dist.get(allVertex.get(j))) {
                    dist.put(allVertex.get(j), tmp);
                    pre.put(allVertex.get(j), allVertex.get(k));
                }
            }
        }
        //根据pre数组来求出经过的路径
        path.add(end);
        int t = end;
        while (t != start && !pre.isEmpty()) {
            if (flag.get(t) == 0) {
                logger.error("start node is {}, end node is {}", start, end);
                logger.error("{}", t);
                throw new RuntimeException("graph is not link");
            }
            t = pre.get(t);
            path.add(0, t);
        }
        return path;
    }

    public static double changeVelocity() {
        return 0d;
    }

    public static void initGraph() {
    	ReadFile.main(null);
        String fileName = Aco.filePath + "finalLink.txt";
        graph = new Graph();
        staticGraph = new Graph();
        ReadFile.initialSubGraph(graph, new SubGraphs(), fileName);
        ReadFile.initialSubGraph(staticGraph, new SubGraphs(), fileName);
        flow = new int[graph.nodeNum][graph.nodeNum];
        fileName = Aco.filePath + "startend.txt";
        List<List<Integer>> nodes = ReadFile.readIntData(fileName);
        startNodeList = nodes.get(0);
        endNodeList = nodes.get(1);
        for (int i = 0; i < flow.length; ++i) {
            Arrays.fill(flow[i], 1);
        }
    }

    public static double getVelocity(int start, int end) {
        double weight = staticGraph.vertex.get(start).getWeight(end);
        if (weight < 1e-6) {
            return VELOCITY;
        }
        double density = flow[start - 1][end - 1] / weight;
        double velocity = VELOCITY * Math.exp(-1 * W * density);
        //double velocity = VELOCITY * (1 - flow[start][end] / 50d);
        return velocity;
    }

    public static double getVelocity(int start, int end, double cur_time) {
        double weight = staticGraph.vertex.get(start).getWeight(end);
        if (weight < 1e-6) {
            return VELOCITY;
        }
        double density = flow[start - 1][end - 1] / weight;

        double velocity = VELOCITY * Math.exp(-1 * W * density * (cur_time/timeOffset));

        //double velocity = VELOCITY * (1 - flow[start][end] / 50d);
        return velocity;
    }

    public static void changeWeight(int start, int end) {
        //double density = flow[start][end] / graph.vertex.get(start).getWeight(end);
        double velocity = getVelocity(start, end);
        //时间应该是权值没有变化过的来除
        double time = staticGraph.vertex.get(start).getWeight(end) / velocity;
        double weight = time * VELOCITY;
        graph.vertex.get(start).addNbr(end, weight);
    }

    public static double getTime(int start, int end, double cur_time) {
        double velocity = getVelocity(start, end, cur_time);
        return staticGraph.vertex.get(start).getWeight(end) / velocity;
    }

    public static double getSumTime() {
        double sumTime = 0d;
        for (int i = 0; i < ANT_NUM; ++i) {
        	double this_sum_time = 0.0;
            int start = startNodeList.get(i);
            int end = endNodeList.get(i);
            List<Integer> path = dijkstra(graph, start, end);//迪杰斯特拉算法挑选路径
            int s = path.get(0);
            for (int j = 1; j < path.size(); ++j) {
                // 此处调整速度的计算方程
                double cost = getTime(s, path.get(j), this_sum_time);
                this_sum_time += cost;
                sumTime += cost;
                pathLength += staticGraph.vertex.get(s).getWeight(path.get(j));
                //changeWeight(s, path.get(j)); 核心
                ++flow[s - 1][path.get(j) - 1];
                s = path.get(j);
            }
        }
        return sumTime;
    }

    public static void getStartEndNode() {
        //initGraph();
        //System.out.println("总时间"+getSumTime());
        //System.out.println("总路径"+pathLength);
        //System.out.println("平均速度"+(pathLength/getSumTime()));
        //List<Integer> path=dijkstra(staticGraph,178,189);
        Graph graph = new Graph();
        String fileName = Aco.filePath + "finalLink.txt";
        SubGraphs subGraphs = new SubGraphs();
        ReadFile.initialSubGraph(graph, subGraphs, fileName);
        int startArea = 11;
        int endArea = 97;
        List<Integer> startNodeList = new ArrayList<>(subGraphs.subGraphs.get(startArea).vertex.keySet());
        List<Integer> endNodeList = new ArrayList<>(subGraphs.subGraphs.get(endArea).vertex.keySet());
        Random random = new Random();
        StringBuilder startNode = new StringBuilder();
        StringBuilder endNode = new StringBuilder();
        for (int i = 0; i < 100; ++i) {
            int s = random.nextInt(startNodeList.size());
            int e = random.nextInt(endNodeList.size());
            startNode.append(startNodeList.get(s));
            startNode.append(' ');
            endNode.append(endNodeList.get(e));
            endNode.append(' ');
        }
        System.out.println(startNode);
        System.out.println(endNode);
    }

    public static void test1() {
        initGraph();
        double sumTime = getSumTime();

        System.out.println(sumTime + " " + pathLength + " " + pathLength/sumTime);

        //System.out.println(startNodeList.size());
    }

    public static void main(String[] args) {
        test1();
    }
}
