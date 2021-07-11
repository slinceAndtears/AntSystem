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
 * 全局信息素更新机制 tudo
 * 修改速度-流量的公式
 * 223所在的区域，为起始   0
 * 129 所在的区域，为终点  6
 * 目前的问题 如果同层之前可以随意走，那么会走回头路，小概率会一直循环
 * 如果同层之前也不能走，那么有概率会到不了终点。
 * 解决办法 手动绘制分区图？？ 以后不太现实
 * 统计总路程
 * 释放的信息素的倒数，通过总时间的倒数。
 * 每个蚂蚁释放的信息素数量，通过的时间
 *
 * Tb全局最优蚂蚁的时间
 * Tk 本次迭代 的top w 个蚂蚁
 * 本次迭代最优的k个蚂蚁和全局最优的蚂蚁，如何计算获取
 * */
public class Aco {
    public static final int ANT_NUM = 100;//蚂蚁数量
    public static final int MAX_ITE = 10;//最大迭代次数
    public static final double T0 = 0.002d;//初始信息素含量
    public static final double B = 3d;//启发式信息计算公式中的参数β
    public static final double C = 0.2d;//全局信息素更新的参数
    public static final double q0 = 0.1d;//轮盘赌与贪心的阈值 小于该值则采用贪心
    public static Graph graph;//图数据对象 使用邻接表存储
    public static SubGraphs subGraph;//所有的子图
    public static double[][] pheromone;//信息素矩阵
    public static double phe = 0.6d;//局部信息素更新的参数
    public static int[][] flow;//流量矩阵
    public static final double VELOCITY = 0.1d;//蚂蚁的速度
    public static final int w = 10;//全局信息素更新的排序参数
    public static final double W = 0.001d;//速度-流量的参数
    public static Graph allGraph;//整个图
    public static double sumTime = 0;//通过的所有时间
    public static double p = 0.5d;//全局信息素更新的参数
    public static List<Integer> startNodeList;
    public static List<Integer> endNodeList;
    public static double pathLength=0d;//通过路径的总长度
    public static double globalBestAntTime=Integer.MAX_VALUE;
    public static List<Integer> globalBestAntPath;//全局最优蚂蚁得路径
    public static double[] antPathTime;//
    public static double[] antPhe;

    //从文件中导入图 然后初始化信息素和流量矩阵,目前需要对分区图进行处理，以免出现来回走的情况，
    public static void initialGraph() {
        String fileName = "src/program/AntSystem/friedrichshain/finalLink.txt";
        subGraph = new SubGraphs();
        allGraph = new Graph();
        ReadFile.initialSubGraph(allGraph, subGraph, fileName);
        graph = subGraph.areaGraph;
        pheromone = new double[graph.nodeNum][graph.nodeNum];
        flow = new int[allGraph.nodeNum][allGraph.nodeNum];
        for (int i = 0; i < pheromone.length; ++i) {
            Arrays.fill(pheromone[i], T0);
        }
        fileName="src/program/AntSystem/friedrichshain/startend.txt";
        List<List<Integer>> nodes=ReadFile.readIntData(fileName);
        startNodeList=nodes.get(0);
        endNodeList=nodes.get(1);
        antPathTime=new double[ANT_NUM];
        antPhe=new double[ANT_NUM];
        globalBestAntPath=new ArrayList<>();
    }

    //蚂蚁根据当前顶线选择下一节点
    public static int nextStep(int now_node) {
        List<Integer> nbr = graph.vertex.get(now_node).getAllNbr();//获取所有的邻居 然后做出选择
        //在此处，利用分层，来筛选掉部分数据，，同层之间怎么办,先不做限制试试，权值还没有导入
        if (nbr.size()==0){
            System.out.println("起点为： "+now_node+"没有路可以走");
            return -1;//如果没有路可以走
        }
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
        return -1;
    }

    public static double getVelocity(int start, int end) {
        double density = flow[start][end] / allGraph.vertex.get(start).getWeight(end);
        return VELOCITY * Math.exp(-1 * W * density);
        //return VELOCITY;
    }

    public static double addTime(List<Integer> path) {
        if (path.size() < 2) {
            throw new IllegalArgumentException("路径长度小于2");
        }
        int start = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            double velocity = getVelocity(start, path.get(i));
            ++flow[start][path.get(i)];
            sumTime += allGraph.vertex.get(start).getWeight(path.get(i)) / velocity;
            pathLength+=allGraph.vertex.get(start).getWeight(path.get(i));
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

    //全局信息素更新，每跑完一代之后执行
    public static void update() {
        for (int i = 0; i < pheromone.length; ++i) {
            for (int j = 0; j < pheromone[i].length; ++j) {
                if (subGraph.areaGraph.vertex.get(i).getWeight(j) != Integer.MAX_VALUE) {
                    double t = 0d;
                    for(int k=1;k<w;++k){
                        double Tk=0d;//通过的总时间的倒数
                        double b=0d;//释放信息素
                        t=t+(w-k)*+w*b;
                    }
                    pheromone[i][j] = (1 - p) * pheromone[i][j] + t;
                }
            }
        }
    }

    //获取从起始分区到达终点分区的所有点
    public static List<Integer> getConnectNode(int start_area, int end_area) {
        List<Integer> connect = new ArrayList<>();
        List<Integer> startNode = subGraph.subGraphs.get(start_area).getAllVertex();
        List<Integer> endNode = subGraph.subGraphs.get(end_area).getAllVertex();
        for (Integer x : startNode) {
            for (Integer y : endNode) {
                if (allGraph.vertex.get(x).getWeight(y) != Integer.MAX_VALUE) {
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
        List<List<Integer>> allPath = new ArrayList<>();
        for (int i = 0; i < ANT_NUM; ++i) {
            int end_node = endNodeList.get(i);
            int end_area = 6;
            int now_area = 0;
            int next_area = 0;
            int now_node = startNodeList.get(i);
            int next_node = 0;
            List<Integer> oneAntPath=new ArrayList<>();//一只蚂蚁从起点到终点的路径
            while (now_node != end_node) {
                if (now_area != end_area) {
                    next_area = nextStep(now_area);//根据蚁群算法挑选出下一个区域
                    //List<Integer> connect = subGraph.subGraphs.get(now_area).connect.get(next_area);
                    List<Integer> connect = getConnectNode(now_area, next_area);
                    next_node = connect.get(r.nextInt(connect.size()));
                } else {
                    next_node = end_node;
                }
                //List<Integer> path = Dijkstra.dijkstra(subGraph.subGraphs.get(now_area), now_node, next_node);
                //目前使用大图来算区间内的路径，因为分区不合理，区间内的点不在区间内不互通
                List<Integer> path = Dijkstra.dijkstra(allGraph, now_node, next_node);
                localPheUpdate(now_area, next_area);
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
                //List返回的都是Integer对象，所以需要使用equals来进行比较
                if (!path.get(0).equals(startNodeList.get(i))){
                    Integer t=path.remove(0);
                }
                oneAntPath.addAll(path);
            }
            allPath.add(oneAntPath);
        }
        return allPath;
    }
    //将路径保存至txt文件
    public static void savePath(List<List<Integer>> allPath) {
        String fileName = "src/program/AntSystem/friedrichshain/finalPath.txt";
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
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void runACS() {
        for (int i=0;i<MAX_ITE;++i){
            acoDemo();
            System.out.println(pathLength/sumTime);
        }
    }

    //在两个区域内随机生成起点和终点
    public static void generateStartAndEndNode() {
        initialGraph();
        List<Integer> zero = subGraph.subGraphs.get(0).getAllVertex();
        List<Integer> six = subGraph.subGraphs.get(6).getAllVertex();
        List<Integer> start = new ArrayList<>();
        List<Integer> end = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < ANT_NUM; ++i) {
            start.add(zero.get(r.nextInt(zero.size())));
            end.add(six.get(r.nextInt(six.size())));
        }
        String fileName = "src/program/AntSystem/friedrichshain/startend.txt";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            StringBuilder s = new StringBuilder();
            StringBuilder e = new StringBuilder();
            for (int i = 0; i < start.size(); ++i) {
                s.append(start.get(i));
                s.append(' ');
                e.append(end.get(i));
                e.append(' ');
            }
            s.append('\n');
            System.out.println(s);
            //writer.write(s.toString());
            //writer.write(e.toString());
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

    public static void testLevel() {
        initialGraph();
        List<Integer> paths=Dijkstra.dijkstra(allGraph,178,189);
        addTime(paths);
        System.out.println(sumTime);
    }

    public static void testFlow(){
        initialGraph();
        for (int i=0;i<100;++i){
            flow[178][201]=i;
            System.out.println(getVelocity(178,201));
        }
    }

    public static void main(String[] args) {
        //testLevel();
        runACS();
    }
}
