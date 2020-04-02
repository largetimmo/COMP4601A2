package service.crawler;

import Jama.Matrix;
import org.jgrapht.graph.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.stream.IntStream;
import java.util.zip.ZipInputStream;

public class GraphManager {

    public DirectedPseudograph<Integer, DefaultEdge> generateGraph(){
        DirectedPseudograph<Integer,DefaultEdge> graph = new DirectedPseudograph<>(DefaultEdge.class);
        IntStream.range(0,7).forEach(graph::addVertex);
        graph.addEdge(0,2);
        graph.addEdge(2,0);
        graph.addEdge(2,2);
        graph.addEdge(1,2);
        graph.addEdge(1,1);
        graph.addEdge(2,3);
        graph.addEdge(3,3);
        graph.addEdge(3,4);
        graph.addEdge(6,3);
        graph.addEdge(4,6);
        graph.addEdge(6,4);
        graph.addEdge(6,6);
        graph.addEdge(5,6);
        graph.addEdge(5,5);
        return graph;
    }

    public void getRank(DirectedPseudograph<Integer,DefaultEdge> graph){

        Double alpha = 0.15;
        Double alphaN = alpha/graph.vertexSet().size();

        Matrix base = new Matrix(graph.vertexSet().size(),graph.vertexSet().size());
        for (DefaultEdge e: graph.edgeSet()){
            Integer from = graph.getEdgeSource(e);
            Integer to = graph.getEdgeTarget(e);
            base.set(from,to,1);
        }
        double[][] baseArray = base.getArray();
        IntStream.range(0,graph.vertexSet().size()).forEach(i->{
            int count = 0;
            for(double d : baseArray[i]){
                if (d> 0){
                    count++;
                }
            }
            int finalCount = count;
            IntStream.range(0,graph.vertexSet().size()).forEach(j->{
                if(baseArray[i][j] > 0){
                    base.set(i,j, 1d / finalCount * (1-alpha) + alphaN);
                }else{
                    base.set(i,j,alphaN);
                }

            });
        });
        System.out.println("Transition Matrix:");
        for (double[] darr: base.getArray()){
            for (double d: darr){
                System.out.printf("%.3f",d);
                System.out.print(" ");
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
        System.out.println();
        Matrix x = new Matrix(1,graph.vertexSet().size());
        x.set(0,0,1);
        for (int i = 0; i < 100; i++){
            x = x.times(base);
            System.out.println("After iter"+i);
            for(double d: x.getArray()[0]){
                System.out.printf("%.3f",d);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Final result");
        for(double d: x.getArray()[0]){
            System.out.printf("%.3f",d);
            System.out.print(" ");
        }



    }

    public static void main(String[] args) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new URL("https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/pages.zip").openStream());
        ZipInputStream zipInputStream = new ZipInputStream(bis);
        System.out.println(zipInputStream.getNextEntry());
        System.out.println(zipInputStream.getNextEntry());
        System.out.println(zipInputStream.getNextEntry());
        System.out.println(zipInputStream.getNextEntry());
    }
}


