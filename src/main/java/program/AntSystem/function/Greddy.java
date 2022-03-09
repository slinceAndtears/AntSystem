package program.AntSystem.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraph;
import program.AntSystem.graph.SubGraphs;

import java.io.*;
import java.util.*;

public class Greddy{
    private static final Logger logger = LoggerFactory.getLogger(Greddy.class);
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
    
    public static void initGraph() {
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
    public static double testVelocity(int start, int end, double cur_time) {
    	//return 100;
        double weight = staticGraph.vertex.get(start).getWeight(end);
        if (weight < 1e-6) {
            return VELOCITY;
        }
        double density = flow[start - 1][end - 1];

        double velocity = VELOCITY * Math.exp(-1 * W * density * (cur_time/timeOffset));

        //double velocity = VELOCITY * (1 - flow[start][end] / 50d);
        return velocity;
    }
    public static double getTime(int start, int end, double cur_time) {
        double velocity = getVelocity(start, end, cur_time);
        double cur_len = staticGraph.vertex.get(start).getWeight(end);
        pathLength += cur_len;
        return cur_len / velocity;
    }
    public static int nextStepByVel(Graph graph, int now_node, Map<Integer, Integer> level, double cur_time) {
        if (graph == null || !graph.vertex.containsKey(now_node)) {
            logger.error(String.format("graph is null or graph do not contains now_node %s", now_node));
            throw new IllegalArgumentException(String.format("图为空或者图不包含起始节点%s", now_node));
        }
        // 获取邻居
        List<Integer> nbr;
        nbr = new ArrayList<>();
        for (Integer x : graph.vertex.get(now_node).getAllNbr()) {
            try {
                if (level.get(x) < level.get(now_node)) {
                    nbr.add(x);
                }
            } catch (Exception e) {
                logger.error("graph node list is {}", graph.getAllVertex());
                logger.error("now node is: {}, x is {}", now_node, x);
                logger.error("level is {}", level);
                throw new RuntimeException();
            }
       }
        
        //logger.info("start node is {},nbr size is {}",now_node, nbr.size());
        if (nbr.size() == 0) {
            logger.error("起点为 {} 没有路可以走, level is {}, graph is {}", now_node, level, graph);
            throw new RuntimeException(String.format("起点%s没有路可以走", now_node));
            //return -1;//如果没有路可以走
        } else if (nbr.size() == 1) {
            return nbr.get(0);
        }
        
        // 速度方式选择最优
        double final_vel = -1;
        int final_id = -1;
        for(int i=0; i<nbr.size();++i) {
        	double tmp_vel = testVelocity(now_node,nbr.get(i),cur_time);
        	if(tmp_vel>final_vel) {
        		final_vel = tmp_vel;
        		final_id = nbr.get(i);
        			}
        }
        return final_id;
    }
    
    public static int nextStepByLen(Graph graph, int now_node, Map<Integer, Integer> level) {
        if (graph == null || !graph.vertex.containsKey(now_node)) {
            logger.error(String.format("graph is null or graph do not contains now_node %s", now_node));
            throw new IllegalArgumentException(String.format("图为空或者图不包含起始节点%s", now_node));
        }
        // 获取邻居
        List<Integer> nbr;
        nbr = new ArrayList<>();
        for (Integer x : graph.vertex.get(now_node).getAllNbr()) {
            try {
                if (level.get(x) < level.get(now_node)) {
                    nbr.add(x);
                }
            } catch (Exception e) {
                logger.error("graph node list is {}", graph.getAllVertex());
                logger.error("now node is: {}, x is {}", now_node, x);
                logger.error("level is {}", level);
                throw new RuntimeException();
            }
       }
        
        //logger.info("start node is {},nbr size is {}",now_node, nbr.size());
        if (nbr.size() == 0) {
            logger.error("起点为 {} 没有路可以走, level is {}, graph is {}", now_node, level, graph);
            throw new RuntimeException(String.format("起点%s没有路可以走", now_node));
            //return -1;//如果没有路可以走
        } else if (nbr.size() == 1) {
            return nbr.get(0);
        }
        
        // 速度方式选择最优
        double final_len = Double.MAX_VALUE;
        int final_id = -1;
        for(int i=0; i<nbr.size();++i) {
        	int cur_node = nbr.get(i);
        	double tmp_len = graph.vertex.get(now_node).getWeight(cur_node);
        	if(tmp_len<final_len) {final_id = cur_node; final_len = tmp_len;}
        }
        return final_id;
    }
    
    // velocity 优先
    public static double VGreddy() {
        double sumTime = 0d;
        for (int i = 0; i < ANT_NUM; ++i) {
            int start = startNodeList.get(i);
            int end = endNodeList.get(i);
            Map<Integer, Integer> level = BFS.bfdWithEnd(graph, end);
            List<Integer> path = new ArrayList<>();
            double this_sum_time = 0.0;
            while (start != end) {
            	int pre = start;
                start = nextStepByVel(graph, start, level,sumTime);
                path.add(start);
                ++flow[pre - 1][start - 1];
                double cost = getTime(pre,start,this_sum_time);
                this_sum_time += cost;
                sumTime += cost;
            }
            //logger.info("VGreddy_od {} with path {}",i,path);
        }
        return sumTime; 	
    }
    
    // length 优先
    public static double LGreddy() {
        double sumTime = 0d;
        for (int i = 0; i < ANT_NUM; ++i) {
            int start = startNodeList.get(i);
            int end = endNodeList.get(i);
            Map<Integer, Integer> level = BFS.bfdWithEnd(graph, end);
            List<Integer> path = new ArrayList<>();
            double this_sum_time = 0.0;
            while (start != end) {
            	int pre = start;
                start = nextStepByLen(graph, start, level);
                path.add(start);
                ++flow[pre - 1][start - 1];
                //++flow[pre - 1][start - 1];
                double cost = getTime(pre,start,this_sum_time);
                this_sum_time += cost;
                sumTime += cost;
            }
            //logger.info("LGreddy_od {} with path {}",i,path);
        }
    	return sumTime;
    }
    
    public static void main(String[] args) {
    	initGraph();
    	double v_sumtime = VGreddy();
    	System.out.println("VGreddy: sumTime->" + "  sumLength->" + "  velocity->");
    	System.out.println(v_sumtime +" "+ pathLength +" " + pathLength/v_sumtime);
    	// length 在 getTime中更新
    	flow = new int[graph.nodeNum][graph.nodeNum];
        for (int i = 0; i < flow.length; ++i) {
            Arrays.fill(flow[i], 1);
        }
    	pathLength = 0.0;
    	double l_sumtime = LGreddy();
    	System.out.println("LGreddy: sumTime->" + "  sumLength->" + "  velocity->");
    	System.out.println(l_sumtime + " " + pathLength + " " + pathLength/l_sumtime);
    }
}