package program.AntSystem.function;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHandler {
    public static void main(String[] args) {
        List<List<Double>> node=ReadFile.readFile("src/program/AntSystem/friedrichshain/testPath.txt");
        Collections.sort(node, new Comparator<List<Double>>() {
            @Override
            public int compare(List<Double> o1, List<Double> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });
        BufferedWriter writer=null;
        try {
            writer=new BufferedWriter(new FileWriter("src/program/AntSystem/friedrichshain/res.txt"));
            for (List<Double> t:node){
                StringBuilder res=new StringBuilder();
                res.append(t.get(0));
                res.append(" ");
                res.append(t.get(1));
                writer.write(res.toString());
                writer.newLine();
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
}
