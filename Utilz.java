package pohlighellman;

import java.math.BigInteger;
import java.util.Arrays;

public class Utilz {
  private static BigInteger TWO = BigInteger.valueOf(2);
  // solve implements the chinese remainder theorem where
  // ind[i] is the number that x is equal to mod mod[i] in
  // the ith congruence 
  public static int solve(int[] ind, int[] mod) {
    if(ind.length != mod.length || ind.length == 0) return -1;
    int ans = ind[0];
    int m = mod[0];
    int cnst;
    int[] eucRes;
    for(int i = 1; i < ind.length; i++) {
      cnst = ind[i] - ans;
      while(cnst < 0) cnst += mod[i];
      eucRes = extEucAlg(mod[i], (m % mod[i]));
      if (eucRes[0] != 1) return -1;
      while(eucRes[2] < 0) eucRes[2] += mod[i];
        cnst = cnst*eucRes[2] % mod[i];
        ans = ans + cnst*m;
        m *= mod[i];
      }
      return ans;
  }
  // expr along with exprHelper computes a^b mod m
  // using the fast exponentiation algorith, which
  // is also called the method of repeated squaring
  public static int expr(int a, int b, int m) {
    BigInteger A = BigInteger.valueOf(a);
    BigInteger B = BigInteger.valueOf(b);
    BigInteger M = BigInteger.valueOf(m);
    return exprHelper(A,B,M).intValue();
  }
  private static BigInteger exprHelper (BigInteger A, BigInteger B, BigInteger M) {
    if (B.signum() < 0) {return BigInteger.valueOf(-1);}
    if (B.equals(BigInteger.ZERO)) {
      return BigInteger.ONE;
    } else {
      if (B.mod(TWO).equals(BigInteger.ONE)) {
        return A.multiply(exprHelper(A.modPow(TWO,M), B.divide(TWO), M)).mod(M);
      } else {
        return exprHelper(A.modPow(TWO,M), B.divide(TWO), M).mod(M);
      }
    }
  }
  // This extEucAlg along with extEuchelper calculate the
  // extended Euclidean algorithim of a and b.
  // It is coded to do so efficiently by using tail calls.
  public static int[] extEucAlg(int a, int b) {
    a = Math.abs(a);
    b = Math.abs(b);
    if (a < b) {
      int temp = a;
      a = b;
      b = temp;
    }
    int q = a/b;
    int r = a % b;
    return extEuchelper(b, r, 0, 1, 1, (-q));
  }
  private static int[] extEuchelper(int a, int b, int u_1, int v_1, int u_2, int v_2) {
    int q = a/b;
    int r = a % b;
    if (r == 0) {
      return new int[] {b, u_2, v_2};
    } else {
      return extEuchelper(b, r, u_2, v_2, (u_1 - q*u_2), (v_1 - q*v_2));
    }
  }
  // shanks implements Shank's baby-step-giant-step algorithim to
  // naively solve the discrete logorithim problem: g^x = h mod p.
  // Note that the Pohlig-Hellman algorithim is more efficient,
  // but it still uses Shank's as a subroutine.
  public static int shanks(int g, int h, int p) {
    int n = 1 + ((int) Math.sqrt(p));
    Indexed[] baby = new Indexed[n];
    Indexed[] giant = new Indexed[n];
    baby[0] = new Indexed(0, BigInteger.ONE);
    giant[0] = new Indexed(0, BigInteger.valueOf(h));
    int gInverse = extEucAlg(p, g)[2];
    while(gInverse < 0) gInverse += p;
    gInverse = gInverse % p;
    int gIPow = expr(gInverse, n, p);
    BigInteger G = BigInteger.valueOf(g);
    BigInteger GIP = BigInteger.valueOf(gIPow);
    BigInteger P = BigInteger.valueOf(p);
    for(int i = 1; i < n; i++) {
      baby[i] = new Indexed(i, baby[i-1].getV().multiply(G).mod(P));
      giant[i] = new Indexed(i, giant[i-1].getV().multiply(GIP).mod(P));
    }
    Arrays.sort(baby, baby[0]);
    Arrays.sort(giant, giant[0]);
    int k;
    for (int j = 0; j < n; j++) {
      k = Arrays.binarySearch(giant, baby[j]);
      if(k > 0) { return ((baby[j].getI() + (giant[k].getI())*n) % (p - 1)); }
    }
      return -1;
  }
}
