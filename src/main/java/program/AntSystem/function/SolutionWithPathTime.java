package program.AntSystem.function;

import java.util.ArrayList;
import java.util.List;

public class SolutionWithPathTime extends Solution{
    public List<List<Double>> pathTime;
    public SolutionWithPathTime(){}
    public SolutionWithPathTime(List<List<Integer>> path,double sumTime,List<List<Integer>> area,
                                double length,List<List<Double>> pathTime){
        super(path,sumTime,area,length);
        this.pathTime=new ArrayList<>();
        for (List<Double> t:pathTime){
            pathTime.add(new ArrayList<>(t));
        }
    }

    public List<List<Double>> getPathTime() {
        return pathTime;
    }

    public void setPathTime(List<List<Double>> pathTime) {
        for (List<Double> t:pathTime){
            this.pathTime.add(new ArrayList<>(t));
        }
    }
}
