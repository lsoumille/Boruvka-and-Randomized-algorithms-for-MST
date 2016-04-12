package business;

import utils.Randomizer;

import java.util.*;

/**
 * Created by lucas on 12/04/16.
 */
public class Graph {

    private int V; //nb sommet
    private int E; //nb arete
    private Map<Integer, List<Edge>> allVerticesWithEdges; //ensemble des arretes du graphe en fonction du sommet
    private List<Edge> allEdges;

    /**
     * Creation des sommets (aucune arete)
     * @param v
     */
    public Graph(int v) {
        V = v;
        this.E = 0;
        this.allVerticesWithEdges = new HashMap<>();
        this.allEdges = new ArrayList<>();
    }

    public Graph(int v, int e) {
        V = v;
        this.allVerticesWithEdges = new HashMap<>();
        this.allEdges = new ArrayList<>();
        for(int i = 0 ; i < e ; ++i){
            int[] verticesId = Randomizer.getDiffRandoms(0,v);
            int weight = Randomizer.getWeight();
            Edge newEdge = new Edge(verticesId[0], verticesId[1], weight);
            addEdge(newEdge);
        }
    }

    public int getV() {
        return V;
    }

    public int getE() {
        return E;
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        if(checkVertex(v) && checkVertex(w)){
            //si le sommet contient deja une arrete on met a jour la liste
            if(allVerticesWithEdges.get(new Integer(v)) != null)
                allVerticesWithEdges.get(new Integer(v)).add(e);
            //sinon on crée une nouvelle entrée dans la map
            else {
                List<Edge> edges = new ArrayList<>();
                edges.add(e);
                allVerticesWithEdges.put(new Integer(v), edges);
            }
            if(allVerticesWithEdges.get(new Integer(w)) != null)
                allVerticesWithEdges.get(new Integer(w)).add(e);
                //sinon on crée une nouvelle entrée dans la map
            else {
                List<Edge> edges = new ArrayList<>();
                edges.add(e);
                allVerticesWithEdges.put(new Integer(w), edges);
            }
            allEdges.add(e);
            E++;
        }
    }

    /**
     * remove the edges in the graph
     * @param edges
     */
    public void deleteListEdges(List<Edge> edges){
        for(Edge e : edges){
            allEdges.remove(e);
            for(Map.Entry<Integer,List<Edge>> entry : allVerticesWithEdges.entrySet()){
                entry.getValue().remove(e);
            }
            E--;
        }


    }

    /**
     * return the edges connected to the vertice
     * @param vertice
     * @return
     */
    public Iterable<Edge> getAdjacent(int vertice){
        return allVerticesWithEdges.get(new Integer(vertice));
    }

    public Iterable<Edge> getAllEdges() {
        return allEdges;
    }

    /**
     * check if the vertex is in the graph
     * @param vertex
     * @return
     */
    private boolean checkVertex(int vertex){
        return (vertex >= 0 && vertex < V);
    }

    /**
     * delete edges with the same endpoint (v == w)
     */
    public void deleteLoopEdges(){
        //suppression du graphe
        for(Map.Entry<Integer,List<Edge>> entry : allVerticesWithEdges.entrySet()){
            Iterator<Edge> it = entry.getValue().iterator();
            while(it.hasNext()){
                Edge e = it.next();
                if(e.either() == e.other(e.either()))
                    it.remove();
            }
        }
        //suppression de la list
        Iterator<Edge> it = allEdges.iterator();
        while(it.hasNext()){
            Edge e = it.next();
            if(e.either() == e.other(e.either())){
                it.remove();
                E--;
            }
        }
    }

    /**
     * Remove an edge if another one connect the same endpoint with a lower weight
     */
    public void deleteMultipleEdgesBetweenVertices(){
        List<Edge> toRm = new ArrayList<>();
        for (int i = 0; i < allEdges.size(); i++) {
            if(toRm.contains(i)) continue;
            for (int j = i+1; j < allEdges.size(); j++) {
                if(allEdges.get(i).sameEdge(allEdges.get(j))) {
                    System.out.println(allEdges.get(i) + " / " + allEdges.get(j));
                    if (allEdges.get(i).getWeight() < allEdges.get(j).getWeight()) {
                        toRm.add(allEdges.get(j));
                    } else if (allEdges.get(i).getWeight() > allEdges.get(j).getWeight()) {
                        toRm.add(allEdges.get(i));
                        break;
                    }
                }
            }
        }
        for(Edge e : toRm)
            System.out.println(e);
        deleteListEdges(toRm);
    }

    @Override
    public String toString() {
        String intro = "Graph{" +
                "V=" + V +
                ", E=" + E +
                "}\n";
        for(Map.Entry<Integer,List<Edge>> entry : allVerticesWithEdges.entrySet()){
            intro += entry.getKey() + " : ";
            for(Edge e : entry.getValue()){
                intro += e.toString() + " ";
            }
            intro += "\n";
        }
        return intro;
    }

    public static void main(String[] args) {
        Graph g = new Graph(5,5);
        g.addEdge(new Edge(4,4,7));
        //System.out.println(g.toString());
        g.deleteLoopEdges();
        //System.out.println(g.toString());
        g.addEdge(new Edge(4,3,7));
        g.addEdge(new Edge(3,4,6));
        g.addEdge(new Edge(4,3,5));
        System.out.println(g.toString());
        g.deleteMultipleEdgesBetweenVertices();
        System.out.println(g.toString());
    }
}
