package program.AntSystem.function;

import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraphs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 简单的实现一个ACO 寻找最短路径的ACO算法
 * 节点还是从0 开始比较方便
 * 两个分区之间的连通性，目前设置所有点都可以，
 * 两个分区之间的权值，就设置为他们相连边的数量
 * 找到合适的图数据
 * 全局信息素更新机制
 * 寻找合适的起始区域和终点区域
 *
 * 223所在的区域，为起始   0
 * 142 所在的区域，为终点  20
 * */
public class Aco {
    public static final int ANT_NUM =500;//蚂蚁数量
    public static final int MAX_ITE = 1;//最大迭代次数
    public static final double T0 = 0.002d;//初始信息素含量
    public static final double B = 3d;//启发式信息计算公式中的参数β
    public static final double C = 0.2d;//全局信息素更新的参数
    public static final double q0 = 0.5d;//轮盘赌与贪心的阈值
    public static Graph graph;//图数据对象 使用邻接表存储
    public static SubGraphs subGraph;//所有的子图
    public static double[][] pheromone;//信息素矩阵
    public static double phe = 0.6d;//局部信息素更新的参数
    public static int[][] flow;//流量矩阵
    public static final double VELOCITY = 0.5d;//蚂蚁的速度
    public static final int w = 3;//全局信息素更新的排序参数
    public static final double W=0.01d;//速度-流量的参数
    public static Graph allGraph;//整个图
    public static double sumTime = 0;//通过的所有时间
    public static double p = 0.5d;//全局信息素更新的参数

    //从文件中导入图 然后初始化信息素和流量矩阵,目前需要对分区图进行处理，以免出现来回走的情况，广度优先
    public static void initialGraph() {
        String fileName="src/program/AntSystem/friedrichshain/finalLink.txt";
        subGraph = new SubGraphs();
        allGraph = new Graph();
        ReadFile.initialSubGraph(allGraph, subGraph,fileName);
        graph = subGraph.areaGraph;
        pheromone = new double[graph.nodeNum][graph.nodeNum];
        flow = new int[allGraph.nodeNum][allGraph.nodeNum];
        for (int i = 0; i < pheromone.length; ++i) {
            Arrays.fill(pheromone[i], T0);
        }
    }

    //蚂蚁根据当前顶线选择下一节点
    public static int nextStep(int now_node) {
        List<Integer> nbr = graph.vertex.get(now_node).getAllNbr();//获取所有的邻居 然后做出选择
        //需要检查邻居的可行性吗？？？？不检查了
        double[] state = new double[nbr.size()];//邻居的转移信息
        double n_ij = 0d;
        double t_ij = 0d;
        double sum = 0;
        Random r = new Random();//随机数实现轮盘赌
        for (int i = 0; i < nbr.size(); ++i) {
            int density = flow[now_node][nbr.get(i)] == 0 ? 1 : flow[now_node][nbr.get(i)];
            n_ij = 1d / (graph.vertex.get(now_node).getWeight(nbr.get(i)) * density);
            t_ij = (1 - C) * pheromone[now_node][nbr.get(i)] + C * T0;
            state[i] = t_ij * Math.pow(n_ij, B);
            sum += state[i];
        }
        double q = r.nextDouble();
        if (q <= q0) {
            int next_node = nbr.get(0);
            double max_phe = state[0];
            for (int i = 1; i < nbr.size(); ++i) {
                if (max_phe < state[i]) {
                    max_phe = state[i];
                    next_node = nbr.get(i);
                }
            }
            return next_node;
        } else {
            double ran = r.nextDouble();
            for (int i = 0; i < state.length; ++i) {
                state[i] = state[i] / sum;
                if (ran < state[i]) {
                    return nbr.get(i);
                } else {
                    ran -= state[i];
                }
            }
        }
        return -1;//如果没有路可以走
    }

    public static double getVelocity(int start, int end) {
        double density = flow[start][end] / allGraph.vertex.get(start).getWeight(end);
        return VELOCITY * Math.exp(-1 * W * density);
        //return VELOCITY;
    }

    public static double addTime(List<Integer> path) {
        if (path.size() < 2) {
            throw new IllegalArgumentException("");
        }
        int start = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            double velocity = getVelocity(start, path.get(i));
            ++flow[start][path.get(i)];
            sumTime += allGraph.vertex.get(start).getWeight(path.get(i)) / velocity;
            start = path.get(i);
        }
        return sumTime;
    }

    //局部信息素的更新
    public static void localPheUpdate(int start, int end) {
        pheromone[start][end] = (1 - phe) * pheromone[start][end] + phe * T0;
    }

    public static void runOneAnt() {//一只蚂蚁走完全部的路程
        List<List<Integer>> allAntPath = new ArrayList<>();//记录所有蚂蚁路径的列表
        for (int i = 0; i < ANT_NUM; ++i) {
            int start_node = 0;//初始点
            int end_node = 3;//终止点
            int now_node = 0;//当前顶点
            List<Integer> path = new ArrayList<>();//当前蚂蚁经过的路径
            path.add(start_node);
            while (now_node != end_node && path.size() < 10) {
                now_node = nextStep(now_node);
                path.add(now_node);
            }
            allAntPath.add(path);
        }
        System.out.println(allAntPath.get(0));
    }

    public static void update() {//更新新信息素
        for (int i=0;i<pheromone.length;++i){
            for (int j=0;j<pheromone[i].length;++j){
                if (subGraph.areaGraph.vertex.get(i).getWeight(j)!=Integer.MAX_VALUE){
                    double t=0d;

                    pheromone[i][j]=(1-p)*pheromone[i][j]+t;
                }
            }
        }
    }

    //获取从起始分区到达终点分区的所有点
    public static List<Integer> getConnectNode(int start_area,int end_area){
        List<Integer> connect=new ArrayList<>();
        List<Integer> startNode=subGraph.subGraphs.get(start_area).getAllVertex();
        List<Integer> endNode=subGraph.subGraphs.get(end_area).getAllVertex();
        for (Integer x:startNode){
            for (Integer y:endNode){
                if (allGraph.vertex.get(x).getWeight(y)!=Integer.MAX_VALUE){
                    connect.add(x);
                    break;
                }
            }
        }
        return connect;
    }

    //输出的路径是分开的 后续需要想如何将路径整合一起，后续整合还是前期整合
    public static List<List<Integer>> acoDemo() {
        Random r = new Random();
        initialGraph();
        List<List<Integer>> allPath=new ArrayList<>();
        for (int k = 0; k < MAX_ITE; ++k) {
            for (int i = 0; i < ANT_NUM; ++i) {
                int end_node = 3;
                int end_area = 1;
                int now_area = 0;
                int next_area = 0;
                int now_node = 0;
                int next_node = 0;
                while (now_node != end_node) {
                    if (now_area != end_area) {
                        next_area = nextStep(now_area);
                        //List<Integer> connect = subGraph.subGraphs.get(now_area).connect.get(next_area);
                        List<Integer> connect=getConnectNode(now_area,next_area);
                        next_node = connect.get(r.nextInt(connect.size()));
                    } else {
                        next_node = end_node;
                    }
                    List<Integer> path = Dijkstra.dijkstra(subGraph.subGraphs.get(now_area), now_node, next_node);
                    localPheUpdate(now_area,next_area);
                    now_area = next_area;
                    now_node = next_node;
                    if (now_node != end_node) {
                        List<Integer> nbr = allGraph.vertex.get(now_node).getAllNbr();
                        List<Integer> connectNextArea = new ArrayList<>();
                        for (Integer x : nbr) {
                            if (subGraph.subGraphs.get(next_area).vertex.containsKey(x)) {
                                connectNextArea.add(x);
                            }
                        }
                        now_node = connectNextArea.get(r.nextInt(connectNextArea.size()));
                        path.add(now_node);
                    }
                    addTime(path);
                    allPath.add(path);
                }
            }
        }
        return allPath;
    }

    public static void savePath(List<List<Integer>> allPath) {
        String fileName = "src/program/AntSystem/path.txt";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            StringBuilder path;
            for (int i = 0; i < allPath.size(); ++i) {
                path = new StringBuilder();
                for (int j = 0; j < allPath.get(i).size(); ++j) {
                    path.append(allPath.get(i).get(j));
                    path.append(" ");
                }
                path.append("\n");
                writer.write(path.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void runACS(){
        List<List<Integer>> allPath=acoDemo();
        //System.out.println(allPath);
        System.out.println(sumTime);
        //savePath(allPath);
    }

    public static void test(){
        initialGraph();
        System.out.println(subGraph.subGraphs.get(17).getAllVertex());
    }

    public static void main(String[] args) {
        test();
        //runACS();
    }
}
