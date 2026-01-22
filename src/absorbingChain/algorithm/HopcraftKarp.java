package absorbingChain.algorithm;

import java.util.*;

/*
 * Got inputs from the following
 * 
 * http://en.wikipedia.org/wiki/Hopcroft_Karp
 * http://zobayer.blogspot.com/2010/05/maximum-matching.html
 * 
 * 
 */

public class HopcraftKarp {

int MAX=100001;
int NIL= 0;
int INF= 1<<28;

public HashMap<Integer, ArrayList<Integer>> graph;
int n=9, m=9;
public ArrayList<Integer> dist = new ArrayList<Integer>();
public ArrayList<Integer> match = new ArrayList<Integer>();        
Queue<Integer> Q = new LinkedList<Integer>();

HopcraftKarp(){
	graph = new HashMap<Integer, ArrayList<Integer>>();
	ArrayList<Integer> al = new ArrayList<Integer>(m);
	al.add(3);al.add(2);al.add(7);al.add(2);al.add(3);al.add(8);al.add(4);al.add(1);al.add(7);
	graph.put(1, al);
	
	al.add(3);al.add(2);al.add(7);al.add(4);al.add(5);al.add(8);al.add(4);al.add(14);al.add(2);
	graph.put(2, al);
}

boolean bfs() {
    int i, u, v, len;    
    for(i=1; i<=n; i++) {
        if(match.size() < i) {
            dist.add(0);
            Q.add(i);
        }
        else dist.add(INF);
    }
    dist.add(NIL, INF);
    
    while(!Q.isEmpty()) {
        u = Q.poll();
        
        if(u!=0) {
            len = graph.get(u).size();
            for(i=0; i<len; i++) {                
                v=graph.get(u).get(i);
                if(dist.get(match.get(v))==INF) {
                    dist.add(match.get(v),(dist.get(u) + 1));
                    Q.add(match.get(v));
                }
            }
        }
    }
    return (dist.get(0)!=INF);
}

boolean dfs(int u) {
    int i, v, len;
    if(u!=0) {
        len = graph.get(u).size();
        for(i=0; i<len; i++) {
            v = graph.get(u).get(i);
            if(dist.get(match.get(v))==(dist.get(u) + 1)) {
                if(dfs(match.get(v))) {
                    match.add(v, u);
                    match.add(u, v);
                    return true;
                }
            }
        }
        dist.add(u, INF);
        return false;
    }
    return true;
}

int hopcraft_karp() {
    int matching = 0, i;
    // match[] is assumed NIL for all vertex in G
    while(bfs())
        for(i=1; i<=n; i++)
            if(match.get(i) ==0 && dfs(i))
                matching++;
    return matching;
}

public static void main(String args[])  {
    
    HopcraftKarp matching = new HopcraftKarp();
    System.out.println(" Matching "+matching.hopcraft_karp());
    
}

    
}