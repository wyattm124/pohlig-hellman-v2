package pohlighellman;

import java.util.HashMap;
import java.util.Iterator;

// PH implements an un-optimized version of the Pohlig-Hellman algorithim
// Note that it uses many of the functions from Utilz in conjunction to 
// carry out the computation
public class PH {
   public static int solveDLP(int g, int h, int p) {
     Factored factorization = new Factored(p-1);
     HashMap<Integer, Integer> fact = factorization.factors();
     int totalCongs = fact.size();
     int[] qs = new int[totalCongs];
     int[] gs = new int[totalCongs];
     int[] hs = new int[totalCongs];
     int[] ys = new int[totalCongs];
     Iterator<Integer> preq = fact.keySet().iterator();
     int i = 0;
     int k;
     while(preq.hasNext()){
       k = preq.next();
       qs[i++] = Utilz.expr(k, fact.get(k), p-1);
       // System.out.println("got a q : " + qs[i-1]);
     }
     // qs come out right
     for(i = 0; i < totalCongs; i++) {
       gs[i] = Utilz.expr(g, (p-1)/qs[i], p);
       hs[i] = Utilz.expr(h, (p-1)/qs[i], p);
       // System.out.println("got a g : " + gs[i]);
       // System.out.println("got a h : " + hs[i]); 
     }
     // we make it to here, assume the the FastMult works
     for(i = 0; i < totalCongs; i++){
       ys[i] = Utilz.shanks(gs[i], hs[i], p);
       // System.out.println("shanked : " + ys[i]);
     }
     return Utilz.solve(ys, qs);
   }
   // here are some simple tests. The third fails because of some overflow
   // error somewhere (I need to still fix this)
   public static void main(String[] args) {
     System.out.println(solveDLP(7, 166, 433));
     System.out.println(solveDLP(10, 243278, 746497));
     System.out.println(solveDLP(2, 39183497, 41022299));
     System.out.println(solveDLP(17, 192988, 1291799));
   }
}
