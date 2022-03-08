package program.AntSystem.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraph;
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
 *   以下点没有边
 *   184 208 236 265 271 590 639 670 671 895 896 897 898 899 1075 1076 1077 1078 1101 1102
 *   1107 1108 1218 1219 1220 1994 1996 1997 1998 2003 2004 2005 2007 2008 2009
 *  2022-02-14
 *      节点下标从1开始
 *      起点区域集合为11
 *      终点区域集合为97
 *  2022-02-17
 *      目前存在的一个问题：由于有38个点完全没有边。同时目前图的导入是根据边来导入的。所以在导入图的时候，这些点都无法导入进去
 *      导致点的数量减小，需要在图的导入地方作出改进？？ 或者重新导入这些点
 *   2022-02-19
 *      删除上述没有边的点。
 *      起点区域更改为96
 *      终点区域更改为63
 *
 *   2022-02-27
 *      调整为多目标蚁群—— by scottishi@tencent.com
 * */
public class Aco {
    private static final Logger logger = LoggerFactory.getLogger(Aco.class);


    public static final int ANT_NUM = 100;//蚂蚁数量
    public static final int MAX_ITE = 50;//最大迭代次数
    public static final double T0 = 0.6d;//初始信息素含量
    public static final double B = -2d;//启发式信息计算公式中的参数β 目前分区图的路径是根据连接点设置的，所以路径越长，选择概率越大
    public static final double C = 0.2d;//全局信息素更新的参数
    public static final double q0 = 0.9d;//轮盘赌与贪心的阈值 小于该值则采用贪心
    public static Graph graph;//图数据对象 使用邻接表存储
    public static SubGraphs subGraph;//所有的子图
    public static double[][] pheromone;//信息素矩阵
    public static double phe = 0.6d;//局部信息素更新的参数
    public static int[][] flow;//流量矩阵
    public static final double VELOCITY = 20d;//蚂蚁的速度
    public static final int w = 5;//全局信息素更新的排序参数
    public static final double W = 0.73d;//速度-流量的参数
    public static Graph allGraph;//整个图
    public static double p = 0.5d;//全局信息素更新的参数
    public static List<Integer> startNodeList;
    public static List<Integer> endNodeList;
    public static List<Solution> globalBestSolution;//全局最优蚂蚁的解
    public static int Sn = 12;//每次迭代产生解的数量
    public static PriorityQueue<SolutionWithPathTime> topWAnt;
    public static List<Integer> area;
    public static Set<Integer> guerNode;
    public static final int MAX_FLOW = 40;
    public static double timeOffset = 1e2;
    public static double lengthOffset = 1e5;
    
    public static void detectGuerNode() {
        guerNode = new HashSet<>();
        for (int i = 0; i < 100; ++i) {
            Graph graph = subGraph.subGraphs.get(i);
            Set<Integer> nodes = graph.vertex.keySet();
            for (Integer n : nodes) {
                if (graph.vertex.get(n).getAllNbr().size() == 0) {
                    guerNode.add(n);
                }
            }
        }
    }

    //从文件中导入图 然后初始化信息素和流量矩阵,目前需要对分区图进行处理，以免出现来回走的情况，
    public static void initialGraph() {
    	// 此处生成finallink.txt
    	ReadFile.main(null);
        String fileName = "src/main/java/program/AntSystem/beijing/finalLink.txt";
        subGraph = new SubGraphs();
        allGraph = new Graph();
        ReadFile.initialSubGraph(allGraph, subGraph, fileName);
        graph = subGraph.areaGraph;
        pheromone = new double[allGraph.nodeNum][allGraph.nodeNum];
        flow = new int[allGraph.nodeNum][allGraph.nodeNum];
        for (int i = 0; i < pheromone.length; ++i) {
            Arrays.fill(pheromone[i], T0);
        }
        fileName = "src/main/java/program/AntSystem/beijing/startend.txt";
        List<List<Integer>> nodes = ReadFile.readIntData(fileName);
        startNodeList = nodes.get(0);
        endNodeList = nodes.get(1);
        //globalBestSolution = new ArrayList<>(new Solution(new ArrayList<>(), Integer.MAX_VALUE, new ArrayList<>(), 0));
        globalBestSolution = new ArrayList<>();
        topWAnt = new PriorityQueue<>(ANT_NUM, (o1, o2) -> o1.sumTime > o2.sumTime && o1.sumLength > o2.sumLength ? 1 : -1);
        area = new ArrayList<>();
        List<List<Integer>> t = ReadFile.readIntData("src/main/java/program/AntSystem/beijing/area.txt");
        for (List<Integer> x : t) {
            area.add(x.get(0));
        }
    }

    public static int nextStep(int now_node) {
        return nextStep(graph, now_node, null);
    }

    //蚂蚁根据当前顶点选择下一节点
    public static int nextStep(Graph graph, int now_node, Map<Integer, Integer> level) {
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
                try {
                    if (level.get(x) < level.get(now_node) && flow[now_node][x] < MAX_FLOW) {
                        nbr.add(x);
                    }
                } catch (Exception e) {
                    logger.error("graph node list is {}", graph.getAllVertex());
                    logger.error("now node is: {}, x is {}", now_node, x);
                    logger.error("level is {}", level);
                    throw new RuntimeException();
                }
            }
        }
        //logger.info("start node is {},nbr size is {}",now_node, nbr.size());
        //掉在此处，利用分层，来筛选掉部分数据，，同层之间怎么办,先不做限制试试，权值还没有导入
        if (nbr.size() == 0) {
            logger.error("起点为 {} 没有路可以走, level is {}, graph is {}", now_node, level, graph);

            throw new RuntimeException(String.format("起点%s没有路可以走", now_node));
            //return -1;//如果没有路可以走
        } else if (nbr.size() == 1) {
            return nbr.get(0);
        }
        double[] state = new double[nbr.size()];//邻居的转移信息
        double n_ij = 0d;
        double t_ij = 0d;
        double sum = 0;
        Random r = new Random();//随机数实现轮盘赌
        for (int i = 0; i < nbr.size(); ++i) {
            int density = flow[now_node][nbr.get(i)] == 0 ? 1 : flow[now_node][nbr.get(i)];
            double weight = graph.vertex.get(now_node).getWeight(nbr.get(i));
            if (weight < 1e-6) {
                continue;
            }
            n_ij = 1d / (weight * density);
            //t_ij = (1 - C) * pheromone[now_node][nbr.get(i)] + C * T0;
            // 暂时修改 不知道为什么如上写 信息素直接使用即可
            t_ij = pheromone[now_node][nbr.get(i)];
            state[i] = t_ij * Math.pow(n_ij, 
            		
            		B);
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
        double weight = allGraph.vertex.get(start).getWeight(end);
        if (weight < 1e-6) {
            return VELOCITY;
        }
        double density = flow[start - 1][end - 1] / weight;
        double v = VELOCITY * Math.exp(-1 * W * density);
        //logger.info("start node is {}, end node is {}, density is {} ,velocity is {}", start, end, density, v);
        return v;
        //return VELOCITY * (1 - flow[start - 1][end - 1] / 50d);
    }

    //车辆-流速公式-T模式
    public static double getVelocity(int start, int end, double cur_time) {
        double weight = allGraph.vertex.get(start).getWeight(end);
        if (weight < 1e-6) {
            return VELOCITY;
        }
        double density = flow[start - 1][end - 1] / weight;
        double v = VELOCITY * Math.exp(-1 * W * density * (cur_time / 1e5));
        //logger.info("start node is {}, end node is {}, density is {} ,velocity is {}", start, end, density, v);
        return v;
        //return VELOCITY * (1 - flow[start - 1][end - 1] / 50d);
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
            // 注 此处可用两种速度计算方式
        	//double this_sum_time = 0.0;
            double velocity = getVelocity(start, path.get(i),sumTime);
            ++flow[start][path.get(i)];
            sumLength += allGraph.vertex.get(start).getWeight(path.get(i));
            double cost = allGraph.vertex.get(start).getWeight(path.get(i)) / velocity;
            sumTime += cost;
            //this_sum_time
            start = path.get(i);
        }
        return new double[]{sumTime, sumLength};
    }

    public static double[] getPathLengthAndTimeByPath(List<Integer> path, SolutionWithPathTime solution, int antNum, double cur_time) {
        double sumLength = 0d;
        double sumTime = 0d;
        int start = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            double velocity = getVelocity(start, path.get(i));
            ++flow[start - 1][path.get(i) - 1];
            sumLength += allGraph.vertex.get(start).getWeight(path.get(i));
            double time = allGraph.vertex.get(start).getWeight(path.get(i)) / velocity;
            sumTime += time;
            solution.pathTime.get(antNum).add(time);
            start = path.get(i);
        }
        return new double[]{sumTime, sumLength};
    }

    //局部信息素的更新
    public static void localPheUpdate(int start, int end) {
        pheromone[start][end] = (1 - phe) * pheromone[start][end] + phe * T0;
    }

    //利用蚁群算法，选出一条完整的路径
    public static List<Integer> antSystemPath(Graph graph, int start, int end) {
        if (graph == null || !graph.vertex.containsKey(start) || !graph.vertex.containsKey(end)) {
            logger.error("graph is null or graph does not contains start node {} or end node {},graph is {}", start, end, graph);
            throw new IllegalArgumentException(String.format("graph is null or graph does" +
                    " not contains start node %s or end node %s", start, end));
        }
        List<Integer> path = new ArrayList<>();
        path.add(start);
        Map<Integer, Integer> level = BFS.bfdWithEnd(graph, end);
        while (start != end) {
            start = nextStep(graph, start, level);
            path.add(start);
        }
        pathPheUpdate(path);
        return path;
    }

    //对一条路径的信息素进行局部更新
    public static void pathPheUpdate(List<Integer> path) {
        if (path == null || path.size() == 0) {
            throw new RuntimeException("");
        }
        int start = path.get(0);
        int end = 0;
        for (int i = 1; i < path.size(); ++i) {
            end = path.get(i);
            localPheUpdate(start, end);
            start = end;
        }
    }

    /**
     * 一只蚂蚁走完全部的路程 分区之间用迪杰斯特拉算法确定。
     */
    public static SolutionWithPathTime runOneAnt() {
        Random r = new Random();
        double sumTime = 0d;
        double sumLength = 0d;
        //Solution solution = new Solution();
        SolutionWithPathTime solution = new SolutionWithPathTime();
        List<List<Integer>> allPath = new ArrayList<>();
        List<List<Integer>> areaPath = new ArrayList<>();
        solution.pathTime = new ArrayList<>();
        for (int i = 0; i < ANT_NUM; ++i) {
            int startNode = startNodeList.get(i);
            int endNode = endNodeList.get(i);
            //List<Integer> oneAreaPath = Dijkstra.dijkstra(graph, area.get(startNode), area.get(endNode));
            List<Integer> oneAreaPath = new ArrayList<>();
            try {
                oneAreaPath = getAreaPathByDijkstra(Dijkstra.dijkstra(allGraph, startNode, endNode));//分区路径
            } catch (Exception e) {
                logger.error("第 {} 只蚂蚁", i);
                logger.error("start Node is {}", startNode);
                logger.error("end Node is {}", endNode);
                throw new RuntimeException();
            }

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
                double[] timeAntLength = getPathLengthAndTimeByPath(path, solution, i, sumTime);
                sumTime += timeAntLength[0];
                sumLength += timeAntLength[1];
                startArea = nextArea;
                if (!path.get(0).equals(startNodeList.get(i))) {
                    Integer x = path.remove(0);
                }
                oneAntAllPath.addAll(path);
            }
            if (oneAntAllPath.size() == 0) {
                continue;
            }
            int lastNode = oneAntAllPath.get(oneAntAllPath.size() - 1);
            if (endNode != lastNode) {
                List<Integer> lastPath = antSystemPath(subGraph.subGraphs.get(startArea), lastNode, endNode);
                double[] timeAntLength = getPathLengthAndTimeByPath(lastPath, solution, i, sumTime);
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

    public static boolean judgePareto(Solution[] curBest, Solution curAnt) {
        int cur_len = curBest.length;
        for (int i = 0; i < cur_len; ++i) {
            if (curBest[i] == null) {
                continue;
            }
            if (curBest[i].sumLength < curAnt.sumLength && curBest[i].sumTime < curAnt.sumTime) {
                return false;
            }
        }
        return true;
    }


    /**
     * 全局信息素更新，每跑完一代之后执行 一次将优先队列中的solution出队，然后全局更新
     * 需要重复更新吗，首先计算出需要更新的数量 所以我的解 只用更新区域图就可以了
     * 其他路径的信息素，直接蒸发 p了？？
     **/
    public static void update() {

        boolean[][] tag = new boolean[pheromone.length][pheromone.length];//用于标志是否有路径走过
        Solution[] localBest = new Solution[topWAnt.size()];
        //将局部最优蚂蚁出队，获取
        int real_size = 0;
        for (int i = 0; i < localBest.length; ++i) {
            Solution tmp_sol = topWAnt.poll();
            if (!judgePareto(localBest, tmp_sol)) {
                // 已经不是帕累托解
                break;
            }

            localBest[real_size] = tmp_sol;
            real_size++;
        }
        double t = 0d;
        for (int i = 1; i <= real_size; ++i) {
            //t[i] =  (1 / localBest[i - 1].sumTime);
            t = (100000 / localBest[i - 1].sumTime); //+ w * (1 / globalBestSolution.sumTime);
        }
        //全局最优蚂蚁进行信息素更新进行全局信息素更新
        for (Solution g_ant : globalBestSolution) {
            for (List<Integer> ap : g_ant.areaPath) {
                int start = ap.get(0);
                for (int i = 1; i < ap.size(); ++i) {
                    pheromone[start][ap.get(i)] = (1 - p) * pheromone[start][ap.get(i)] + t
                    				+(1 / (getGlobalBestAvgTime(globalBestSolution)/timeOffset*
                                             getGlobalBestAvgLength(globalBestSolution)/lengthOffset) );

                    tag[start][ap.get(i)] = true;
                    start = ap.get(i);
                }
            }
        }

        //局部最优蚂蚁走过的变进行信息素更新
        // 更新注 此处修改不需要局部最优更新  2022/02/27
        /*for (int i = 0; i < localBest.length; ++i) {
            if (localBest[i] == null) {
                continue;
            }
            List<List<Integer>> path = localBest[i].areaPath;//
            for (List<Integer> ap : path) {
                int start = ap.get(0);
                for (int j = 1; j < ap.size(); ++j) {
                    pheromone[start][ap.get(j)] = (1 - p) * pheromone[start][ap.get(j)] + t;
                    tag[start][ap.get(j)] = true;
                    start = ap.get(j);
                }
            }
        }*/

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
    public static List<Integer> getAreaPathByDijkstra(List<Integer> path) {
        List<Integer> areaPath = new ArrayList<>();
        for (Integer x : path) {
            int a = area.get(x - 1);
            if (areaPath.size() == 0 || a != areaPath.get(areaPath.size() - 1)) {
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
                    if (connect.size() == 0) {
                        logger.error("connected node sum is 0, now area is");
                        throw new IllegalArgumentException("");
                    }
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
                double[] timeAntLength = getPathLengthAndTimeByPath(path);
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

    public static boolean judgeGlobal(List<Solution> globalBest, Solution cur_sol) {
    	
    	/*for(int i=0; i<globalBest.size(); ++i) {
    		if(cur_sol.sumLength >= globalBest.get(i).sumLength
    				&& cur_sol.sumTime >= globalBest.get(i).sumTime) {
    			// 被支配直接返回 因为集合中不会有其他被cur_sol支配的
    			return false;
    		}
    		if(cur_sol.sumLength < globalBest.get(i).sumLength
    				&& cur_sol.sumTime < globalBest.get(i).sumTime) {
    			globalBest.remove(i);
    		}
    	}*/
        Iterator<Solution> it = globalBest.iterator();
        while (it.hasNext()) {
            Solution tmp = it.next();
            if (cur_sol.sumLength >= tmp.sumLength
                    && cur_sol.sumTime >= tmp.sumTime) {
                return false;
            }
            if (cur_sol.sumLength < tmp.sumLength
                    && cur_sol.sumTime < tmp.sumTime) {
                it.remove();
            }
        }
        globalBest.add(cur_sol);
        return true;
    }

    public static double getGlobalBestAvgLength(List<Solution> globalBest) {
        double sum_len = 0d;
        for (Solution ant : globalBest) {
            sum_len += ant.sumLength;
        }
        return sum_len / globalBest.size();
    }

    public static double getGlobalBestAvgTime(List<Solution> globalBest) {
        double sum_time = 0d;
        for (Solution ant : globalBest) {
            sum_time += ant.sumTime;
        }
        return sum_time / globalBest.size();
    }

    //目前路径对象的各种复制都使用的是浅拷贝，感觉解建立之后不会改变
    public static void runACS() {
        //总迭代次数
        initialGraph();
        for (int i = 0; i < MAX_ITE; ++i) {
            //没轮迭代产生Sn个解，然后挑选出top个解，释放信息素
            for (int j = 0; j < Sn; ++j) {
                //Solution t = runOneAnt();
                try {
                    SolutionWithPathTime t = runOneAnt();
                    logger.info("第{}轮迭代第{}个解结果为{}", i, j, t);
                    judgeGlobal(globalBestSolution, t);
                    topWAnt.offer(t);
                } catch (Exception e) {
                    logger.error("第{}轮迭代第{}个解异常，异常为{}", i, j, e);
                }
                //随时更新全局最优解
                
                /*if (t.sumTime < globalBestSolution.sumTime) {
                    globalBestSolution.add(t);
                }*/
                /*if (topWAnt.size() == w) {
                    topWAnt.poll();
                }*/
                //对流量进行清空
                for (int[] x : flow) {
                    Arrays.fill(x, 0);
                }
            }
            //求出top w个解，然后对这些解经过的变和全局最优蚂蚁经过的边释放信息素
            update();
            outPraeto();
            //logger.info("{}",globalBestSolution.sumTime);
        }
        //保存路径
        // savePath(globalBestSolution.path);
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
    public static void showPhe() {
        for (double[] x : pheromone) {
            System.out.println(Arrays.toString(x));
        }
    }

    public static void testFlow() {
        initialGraph();
        Solution solution = runOneAnt();
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
            logger.info("acs" + Arrays.toString(getPathLengthAndTimeByPath(path)));
        }
    }

    static double distance(List<Double> d1, List<Double> d2) {
        double sum = 0d;
        if (d1.size() != d2.size()) {
            logger.error("d1 :{}, d2: {}  size is not equal", d1, d2);
            throw new RuntimeException("d1  d2  size is not equal");
        }
        for (int i = 0; i < d1.size(); ++i) {
            sum += (d1.get(i) - d2.get(i)) * (d1.get(i) - d2.get(i));
        }
        return Math.sqrt(sum);
    }


    static void addLinks(Graph graph) {
        int nodeNum = graph.nodeNum;
        int start = graph.getAllVertex().get(0);
        List<Integer> nodeLists = graph.getAllVertex();
        Map<Integer, Integer> integerIntegerMap = BFS.bfdWithEnd(graph, start);
        final int linkNum = 2;
        Random r = new Random();
        int bfsNum = integerIntegerMap.size();
        //System.out.println(bfsNum);
        while (nodeNum != bfsNum) {
            Set<Integer> tmp = integerIntegerMap.keySet();
            List<Integer> nodes1 = new ArrayList<>(tmp);
            for (int i = 0; i < nodeLists.size(); ++i) {
                if (!tmp.contains(nodeLists.get(i))) {
                    int node = nodeLists.get(i);
                    List<Integer> nodes = new ArrayList<>(BFS.bfsWithStart(graph, node).keySet());
                    for (int j = 0; j < linkNum; ++j) {
                        int r1 = nodes1.get(r.nextInt(nodes1.size()));
                        int r2 = nodes.get(r.nextInt(nodes.size()));
                        System.out.println(r1 + " " + r2);
                        //添加到子图里面
                        graph.vertex.get(r1).addNbr(r2, 1);
                        graph.vertex.get(r2).addNbr(r1, 1);
                    }
                }
                break;
            }
            start = graph.getAllVertex().get(r.nextInt(nodeNum));
            bfsNum = BFS.bfdWithEnd(graph, start).size();
            integerIntegerMap = BFS.bfdWithEnd(graph, start);
        }
    }

    static void detectNodeLink() {
        int nodeSum = 3340;
/*        String fileName = "src/main/java/program/AntSystem/beijing/coordinate.txt";
        List<List<Double>> cor = ReadFile.readFile(fileName);
        double[][] distance = new double[nodeSum + 1][nodeSum + 1];
        for (int i = 1; i < nodeSum; ++i) {
            for (int j = i + 1; j <= nodeSum; ++j) {
                double dis = distance(cor.get(i - 1), cor.get(j - 1)) * 1000;
                distance[i][j] = dis;
                distance[j][i] = dis;
            }
        }*/
        int sum = 0;
        for (int i = 1; i <= nodeSum; ++i) {
            if (!allGraph.vertex.keySet().contains(i)) {
                ++sum;
                System.out.println(i);
            }
        }
/*        for (int i = 1; i <= nodeSum; ++i) {
            //logger.info("node {} link sum is {}", i, allGraph.vertex.get(i).getAllNbr().size());
            if (allGraph.vertex.get(i).getAllNbr().size() < 4) {
                int linkSum = 4 - allGraph.vertex.get(i).getAllNbr().size();
                PriorityQueue<AddNode> queue = new PriorityQueue<>(linkSum, (o1, o2) -> {
                    if (o1.distance > o2.distance) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                for (int j = 1; j <= nodeSum; ++j) {
                    if (j != i) {
                        queue.offer(new AddNode(distance[i][j], j));
                        if (queue.size() > linkSum) {
                            queue.poll();
                        }
                    }
                }
                for (int j = 0; j < queue.size(); ++j) {
                    int end = queue.poll().nodeId;
                    System.out.println(i + " " + end);
                }
            }
        }
        logger.info("guer Node sum is {}", sum);*/
    }

    static void detectSubGraphNodeLink() {
        int blockSum = 33;
        for (int i = 0; i < blockSum; ++i) {
            SubGraph subGraph = Aco.subGraph.subGraphs.get(i);
            List<Integer> nodeList = subGraph.getAllVertex();
            Map<Integer, Integer> integerIntegerMap = BFS.bfsWithStart(subGraph, nodeList.get(0));
            logger.info("---------------------");
            logger.info("node num is {}", subGraph.nodeNum);
            logger.info("bfs num is {}", integerIntegerMap.size());
        }
    }

    static void detectStartEndNode() {
        int success = 0;
        int failure = 0;
        for (int i = 0; i < startNodeList.size(); ++i) {
            try {
                logger.info("start node is {},end node is {}, path length is {}", startNodeList.get(i), endNodeList.get(i),
                        Dijkstra.dijkstra(allGraph, startNodeList.get(i), endNodeList.get(i)));
                ++success;
            } catch (Exception e) {
                ++failure;
            }

        }
        System.out.println("success OD sum is" + success);
        System.out.println("failure OD sum is" + failure);
    }

    static void showGraph(Graph graph) {
        List<Integer> nodes = graph.getAllVertex();
        for (int i = 0; i < nodes.size(); ++i) {
            int node = nodes.get(i);
            List<Integer> nbrs = graph.vertex.get(node).getAllNbr();
            for (int j = 0; j < nbrs.size(); ++j) {
                int nbr = nbrs.get(j);
                System.out.println(node + " " + nbr);
                graph.vertex.get(nbr).removeNbr(node);
            }
        }
    }

    static void outPraeto() {
        for (Solution s : globalBestSolution) {
            logger.info("{}", s);
        }
    }

    public static void main(String[] args) throws IOException {
        //testFlow();
    	
        initialGraph();
        
        //detectSubGraphNodeLink();
        //showGraph(subGraph.subGraphs.get(0));
		/*
		 * for (int i=0;i<subGraph.subGraphs.size();++i){ Graph
		 * graph=subGraph.subGraphs.get(i); addLinks(graph); }
		 */
        runACS();
        //outPraeto();
        //System.out.println(startNodeList);
        //detectNodeLink();
        //List<Integer> allVertex=allGraph.getAllVertex();
        //testPath();
    }
}