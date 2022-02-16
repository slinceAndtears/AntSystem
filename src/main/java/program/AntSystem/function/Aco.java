package program.AntSystem.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraphs;

import java.io.*;
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
 * Tb 全局最优的蚁群所需的总时间
 * Tk top w个蚁群
 * 每轮迭代 10个解 ，top 2 的和最优的蚂蚁 可以释放信息素√
 *  测试信息素的影响力
 *  2021-07-24
 *  分区之间利用迪杰斯特拉算法确定的方法。
 *  1. 首先利用迪杰斯特拉算法跑完全程，然后找出路径上面的点经过了那些区域。
 *  这个区域的序列就是蚁群算法参考的路径？
 *  2021-07-25
 *  给几个方法添加参数判断。
 *  蚁群算法跑出完整的结果.
 *  起点区域 0 1 3 终点区域 7 8
 *  2022-01-12 修改
 *  修改最后一个区域的部分路径的时间和路径没有加上的错误
 *  添加每段路径的耗时，新建一个SolutionWithPathTime 用于专门存放
 *  修改PriorityQueue中的Compare ，改成lambda表达式
 *
 *  2022-02-13
 *   新增的北京地图，有38个点没有边连接
 *
 * */
public class Aco {
    private static final Logger logger= LoggerFactory.getLogger(Aco.class);


    public static final int ANT_NUM = 100;//蚂蚁数量
    public static final int MAX_ITE = 10;//最大迭代次数
    public static final double T0 = 0.02d;//初始信息素含量
    public static final double B = -2d;//启发式信息计算公式中的参数β 目前分区图的路径是根据连接点设置的，所以路径越长，选择概率越大
    public static final double C = 0.2d;//全局信息素更新的参数
    public static final double q0 = 0.9d;//轮盘赌与贪心的阈值 小于该值则采用贪心
    public static Graph graph;//图数据对象 使用邻接表存储
    public static SubGraphs subGraph;//所有的子图
    public static double[][] pheromone;//信息素矩阵
    public static double phe = 0.6d;//局部信息素更新的参数
    public static int[][] flow;//流量矩阵
    public static final double VELOCITY = 0.1d;//蚂蚁的速度
    public static final int w = 5;//全局信息素更新的排序参数
    public static final double W = 0.05d;//速度-流量的参数
    public static Graph allGraph;//整个图
    public static double p = 0.5d;//全局信息素更新的参数
    public static List<Integer> startNodeList;
    public static List<Integer> endNodeList;
    public static Solution globalBestSolution;//全局最优蚂蚁的解
    public static int Sn = 10;//每次迭代产生解的数量
    public static PriorityQueue<SolutionWithPathTime> topWAnt;
    public static List<Integer> area;

    //从文件中导入图 然后初始化信息素和流量矩阵,目前需要对分区图进行处理，以免出现来回走的情况，
    public static void initialGraph() {
        String fileName = "src/main/java/program/AntSystem/subshain/finalLink.txt";
        subGraph = new SubGraphs();
        allGraph = new Graph();
        ReadFile.initialSubGraph(allGraph, subGraph, fileName);
        graph = subGraph.areaGraph;
        pheromone = new double[allGraph.nodeNum][allGraph.nodeNum];
        flow = new int[allGraph.nodeNum][allGraph.nodeNum];
        for (int i = 0; i < pheromone.length; ++i) {
            Arrays.fill(pheromone[i], T0);
        }
        fileName = "src/main/java/program/AntSystem/subshain/startend.txt";
        List<List<Integer>> nodes = ReadFile.readIntData(fileName);
        startNodeList = nodes.get(0);
        endNodeList = nodes.get(1);
        globalBestSolution = new Solution(new ArrayList<>(), Integer.MAX_VALUE, new ArrayList<>(), 0);
        topWAnt = new PriorityQueue<>(w - 1, (o1, o2) -> o1.sumTime < o2.sumTime ? 1 : -1);
        area = new ArrayList<>();
        List<List<Integer>> t = ReadFile.readIntData("src/main/java/program/AntSystem/subshain/area.txt");
        for (List<Integer> x : t) {
            area.add(x.get(0));
        }
    }

    public static int nextStep(int now_node){
        return nextStep(graph,now_node,null);
    }

    //蚂蚁根据当前顶点选择下一节点
    public static int nextStep(Graph graph,int now_node,Map<Integer,Integer> level) {
        if (graph == null || !graph.vertex.containsKey(now_node)) {
            logger.error(String.format("graph is null or graph do not contains now_node %s", now_node));
            throw new IllegalArgumentException(String.format("图为空或者图不包含起始节点%s", now_node));
        }
        List<Integer> nbr;
        if (level == null) {//不用进行分层处理
            nbr = graph.vertex.get(now_node).getAllNbr();//获取所有的邻居 然后做出选择
        } else {
            nbr = new ArrayList<>();
            for (Integer x : graph.vertex.get(now_node).getAllNbr()) {
                if (level.get(x) < level.get(now_node)) {
                    nbr.add(x);
                }
            }
        }
        //掉在此处，利用分层，来筛选掉部分数据，，同层之间怎么办,先不做限制试试，权值还没有导入
        if (nbr.size() == 0) {
            logger.error(String.format("起点%s没有路可以走",now_node));
            throw  new RuntimeException(String.format("起点%s没有路可以走",now_node));
            //return -1;//如果没有路可以走
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

    //车辆-流速公式
    public static double getVelocity(int start, int end) {
        double density = flow[start][end] / allGraph.vertex.get(start).getWeight(end);
        //return VELOCITY * Math.exp(-1 * W * density);
        return VELOCITY * (1 - flow[start][end] / 50d);
    }

    //返回经过这条路径的总长度和总时间，第一项为总时间，第二项而总长度
    public static double[] getPathLengthAndTimeByPath(List<Integer> path) {
        if (path == null || path.size() == 0) {
            throw new IllegalArgumentException("get path length failure, parameter path problem");
        }
        double sumLength = 0d;
        double sumTime = 0d;
        int start = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            double velocity = getVelocity(start, path.get(i));
            ++flow[start][path.get(i)];
            sumLength+= allGraph.vertex.get(start).getWeight(path.get(i));
            sumTime += allGraph.vertex.get(start).getWeight(path.get(i)) / velocity;
            start = path.get(i);
        }
        return new double[]{sumTime,sumLength};
    }

    public static double[] getPathLengthAndTimeByPath(List<Integer> path,SolutionWithPathTime solution,int antNum){
        double sumLength = 0d;
        double sumTime = 0d;
        int start = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            double velocity = getVelocity(start, path.get(i));
            ++flow[start][path.get(i)];
            sumLength+= allGraph.vertex.get(start).getWeight(path.get(i));
            double time= allGraph.vertex.get(start).getWeight(path.get(i)) / velocity;
            sumTime +=time;
            solution.pathTime.get(antNum).add(time);
            start = path.get(i);
        }
        return new double[]{sumTime,sumLength};
    }

    //局部信息素的更新
    public static void localPheUpdate(int start, int end) {
        pheromone[start][end] = (1 - phe) * pheromone[start][end] + phe * T0;
    }

    //利用蚁群算法，选出一条完整的路径
    public static List<Integer> antSystemPath(Graph graph,int start,int end){
        if (graph==null||!graph.vertex.containsKey(start)||!graph.vertex.containsKey(end)){
            logger.error(String.format("graph is null or graph does not contains start node %s or end node %s",start,end));
            throw new IllegalArgumentException(String.format("graph is null or graph does" +
                    " not contains start node %s or end node %s",start,end));
        }
        List<Integer> path=new ArrayList<>();
        path.add(start);
        Map<Integer,Integer> level=BFS.bfdWithEnd(graph,end);
        while (start!=end){
            start=nextStep(graph,start,level);
            path.add(start);
        }
        return path;
    }

    /**
     * 一只蚂蚁走完全部的路程 分区之间用迪杰斯特拉算法确定。
     * */
    public static SolutionWithPathTime runOneAnt() {
        Random r = new Random();
        double sumTime = 0d;
        double sumLength = 0d;
        //Solution solution = new Solution();
        SolutionWithPathTime solution = new SolutionWithPathTime();
        List<List<Integer>> allPath = new ArrayList<>();
        List<List<Integer>> areaPath = new ArrayList<>();
        solution.pathTime=new ArrayList<>();
        for (int i = 0; i < ANT_NUM; ++i) {
            int startNode = startNodeList.get(i);
            int endNode = endNodeList.get(i);
            //List<Integer> oneAreaPath = Dijkstra.dijkstra(graph, area.get(startNode), area.get(endNode));
            List<Integer> oneAreaPath = getAreaPathByDijkstra(Dijkstra.dijkstra(allGraph, startNode, endNode));//分区路径
            List<Integer> oneAntAllPath = new ArrayList<>();//一只蚂蚁所有的路径
            solution.pathTime.add(new ArrayList<>());
            int startArea = oneAreaPath.get(0);
            for (int j = 1; j < oneAreaPath.size(); ++j) {
                int nextArea = oneAreaPath.get(j);//需要走到的区域
                List<Integer> connection = getConnectNode(startArea, nextArea);//与目标分区相连的所有顶点
                int next_node = connection.get(r.nextInt(connection.size()));//随机挑选一个作为出口
                //利用蚁群算法，算出路径
                List<Integer> path = antSystemPath(subGraph.subGraphs.get(startArea), startNode, next_node);
                //随机跳到下一个区域的起点。然后赋值
                List<Integer> conn = new ArrayList<>();
                for (Integer x : subGraph.subGraphs.get(nextArea).getAllVertex()) {
                    if (allGraph.vertex.get(next_node).getWeight(x) != Integer.MAX_VALUE) {
                        conn.add(x);
                    }
                }
                startNode = conn.get(r.nextInt(conn.size()));
                path.add(startNode);
                //double[] timeAntLength = getPathLengthAndTimeByPath(path);
                double[] timeAntLength = getPathLengthAndTimeByPath(path, solution, i);
                sumTime += timeAntLength[0];
                sumLength += timeAntLength[1];
                startArea = nextArea;
                if (!path.get(0).equals(startNodeList.get(i))) {
                    Integer x = path.remove(0);
                }
                oneAntAllPath.addAll(path);
            }
            int lastNode = oneAntAllPath.get(oneAntAllPath.size() - 1);
            if (endNode != lastNode) {
                List<Integer> lastPath = antSystemPath(subGraph.subGraphs.get(startArea), lastNode, endNode);
                double[] timeAntLength = getPathLengthAndTimeByPath(lastPath, solution, i);
                sumTime += timeAntLength[0];
                sumLength += timeAntLength[1];
                Integer x = lastPath.remove(0);
                oneAntAllPath.addAll(lastPath);
            }
            areaPath.add(oneAreaPath);
            allPath.add(oneAntAllPath);
        }
        solution.sumTime = sumTime;
        solution.path = allPath;
        solution.areaPath = areaPath;
        solution.sumLength = sumLength;
        return solution;
    }

    /**
     * 全局信息素更新，每跑完一代之后执行 一次将优先队列中的solution出队，然后全局更新
     * 需要重复更新吗，首先计算出需要更新的数量 所以我的解 只用更新区域图就可以了
     * 其他路径的信息素，直接蒸发 p了？？
     **/
    public static void update() {
        double t = 0d;
        boolean[][] tag = new boolean[pheromone.length][pheromone.length];//用于标志是否有路径走过
        Solution[] localBest = new Solution[topWAnt.size()];
        //将局部最优蚂蚁出队，获取
        for (int i = localBest.length - 1; i >= 0; --i) {
            localBest[i] = topWAnt.poll();
        }
        for (int i = 1; i <= w - 1; ++i) {
            t = (w - i) * (1 / localBest[i-1].sumTime) + w * (1/globalBestSolution.sumTime);
        }
        //全局最优蚂蚁进行信息素更新进行全局信息素更新
        for (List<Integer> ap : globalBestSolution.areaPath) {
            int start = ap.get(0);
            for (int i = 1; i < ap.size(); ++i) {
                pheromone[start][ap.get(i)] = (1 - p) * pheromone[start][ap.get(i)] + t;
                tag[start][ap.get(i)] = true;
                start = ap.get(i);
            }
        }
        //局部最优蚂蚁走过的变进行信息素更新
        for (int i = 0; i < localBest.length; ++i) {
            List<List<Integer>> path = localBest[i].areaPath;//
            for (List<Integer> ap : path) {
                int start = ap.get(0);
                for (int j = 1; j < ap.size(); ++j) {
                    pheromone[start][ap.get(j)] = (1 - p) * pheromone[start][ap.get(j)] + t;
                    tag[start][ap.get(j)] = true;
                    start = ap.get(j);
                }
            }
        }
        //剩下边信息素蒸发
        for (int i = 0; i < pheromone.length; ++i) {
            for (int j = 0; j < pheromone[i].length; ++j) {
                if (tag[i][j] == false) {
                    pheromone[i][j] = (1 - p) * pheromone[i][j];
                }
            }
        }
    }

    //根据最短路径获取分区之间的路径
    public static List<Integer> getAreaPathByDijkstra(List<Integer> path){
        List<Integer> areaPath=new ArrayList<>();
        for (Integer x:path){
            int a=area.get(x);
            if (areaPath.size()==0||a!=areaPath.get(areaPath.size()-1)){
                areaPath.add(a);
            }
        }
        return areaPath;
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

    //产生一个解并且返回
    public static Solution acoDemo() {
        Random r = new Random();
        double sumTime = 0d;
        double sumLength = 0d;
        Solution solution = new Solution();
        List<List<Integer>> allPath = new ArrayList<>();
        List<List<Integer>> areaPath = new ArrayList<>();
        for (int i = 0; i < ANT_NUM; ++i) {
            int end_node = endNodeList.get(i);
            int end_area = 6;
            int now_area = 0;
            int next_area = 0;
            int now_node = startNodeList.get(i);
            int next_node = 0;
            List<Integer> oneAntPath = new ArrayList<>();//一只蚂蚁从起点到终点的路径
            List<Integer> oneAreaPath = new ArrayList<>();//分区图经过的路径
            oneAreaPath.add(now_area);
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
                //到了最后可能会重复添加2次终点分区
                if (end_area != oneAreaPath.get(oneAreaPath.size() - 1)) {
                    oneAreaPath.add(next_area);//添加分区路径
                }
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
                double[] timeAntLength=getPathLengthAndTimeByPath(path);
                sumTime += timeAntLength[0];
                sumLength += timeAntLength[1];
                //List返回的都是Integer对象，所以需要使用equals来进行比较
                if (!path.get(0).equals(startNodeList.get(i))) {
                    Integer t = path.remove(0);
                }
                oneAntPath.addAll(path);
            }
            allPath.add(oneAntPath);
            areaPath.add(oneAreaPath);
        }
        solution.sumTime = sumTime;
        solution.path = allPath;
        solution.areaPath = areaPath;
        solution.sumLength = sumLength;
        return solution;
    }

    //将路径保存至txt文件
    public static void savePath(List<List<Integer>> allPath) {
        String fileName = "src/main/java/program/AntSystem/subshain/finalPath.txt";
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

    //目前路径对象的各种复制都使用的是浅拷贝，感觉解建立之后不会改变
    public static void runACS() {
        //总迭代次数
        initialGraph();
        for (int i = 0; i < MAX_ITE; ++i) {
            //没轮迭代产生Sn个解，然后挑选出top个解，释放信息素
            for (int j = 0; j < Sn; ++j) {
                //Solution t = runOneAnt();
                SolutionWithPathTime t = runOneAnt();
                logger.info(String.format("第%s轮迭代第%s个解结果为%s",i,j,t.toString()));
                //随时更新全局最优解
                if (t.sumTime < globalBestSolution.sumTime) {
                    globalBestSolution = t;
                }
                topWAnt.offer(t);
                if (topWAnt.size() == w) {
                    topWAnt.poll();
                }
                //对流量进行清空
                for (int[] x:flow){
                    Arrays.fill(x,0);
                }
            }
            //求出top w个解，然后对这些解经过的变和全局最优蚂蚁经过的边释放信息素
            update();
            System.out.println(globalBestSolution);
        }
        //保存路径
        savePath(globalBestSolution.path);
    }

    //在起点终点区域内随机生成起点和终点
    public static void generateStartAndEndNode() {
        initialGraph();
        List<Integer> startList = new ArrayList<>();
        startList.addAll(subGraph.subGraphs.get(0).getAllVertex());
        startList.addAll(subGraph.subGraphs.get(1).getAllVertex());
        startList.addAll(subGraph.subGraphs.get(3).getAllVertex());
        List<Integer> endList = new ArrayList<>();
        endList.addAll(subGraph.subGraphs.get(7).getAllVertex());
        endList.addAll(subGraph.subGraphs.get(8).getAllVertex());
        List<Integer> start = new ArrayList<>();
        List<Integer> end = new ArrayList<>();
        Random r = new Random();
        System.out.println(startList);
        System.out.println(endList);
        for (int i = 0; i < ANT_NUM; ++i) {
            start.add(startList.get(r.nextInt(startList.size())));
            end.add(endList.get(r.nextInt(endList.size())));
        }
        String fileName = "src/main/java/program/AntSystem/subshain/startend.txt";
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
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //输出信息素的方法
    public static void showPhe(){
        for (double[] x:pheromone){
            System.out.println(Arrays.toString(x));
        }
    }

    public static void testFlow() {
        initialGraph();
        Solution solution=runOneAnt();
        logger.info(solution.toString());
        savePath(solution.path);
    }

    public static void testPath() throws IOException {
        initialGraph();
        String fileName = "src/main/java/program/AntSystem/subshain/finalPath.txt";
        List<List<Integer>> allPath = ReadFile.readIntData(fileName);
        for (List<Integer> path : allPath) {
            int start = path.get(0);
            int end = path.get(path.size() - 1);
            List<Integer> pathByDij = Dijkstra.dijkstra(allGraph, start, end);
            logger.info("acs"+Arrays.toString(getPathLengthAndTimeByPath(path)));
        }
    }

    public static void main(String[] args) throws IOException {
        //testFlow();
        //runACS();
        testPath();
    }
}
