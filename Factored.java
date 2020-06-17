package pohlighellman;

import java.util.Stack;
import java.util.HashMap;
import java.util.Iterator;

// Factored takes in a number and computes its prime factorization using a static pool of primes
// if there are prime factors of the input number not yet in the pool of primes, Factored will
// naively find larger primes and to factor the input number.
public class Factored {
  static Stack<Integer> primes = new Stack<Integer>();
  static int frontier = 4;
  static int inc = 10;
  int originalNumber;
  HashMap<Integer, Integer> fact = new HashMap<Integer, Integer>();
  public Factored(int num) {
    if(num < 0) num = num *(-1);
    if(num == 0) num = 1;
    originalNumber = num;
    if(primes.empty()) {
      primes.push(2);
      primes.push(3);
    }
    Stack<Integer> nextPrimes = primes;
    while(num != 1) {
      num = primeBarrage(num, nextPrimes.iterator(), fact);
      nextPrimes = primeCrawl(frontier, inc, primes);
      frontier += inc;
    }
  }
  // primeBarrage tries to factor the given number using the known primes
  private int primeBarrage(int num, Iterator<Integer> primeIt, HashMap<Integer, Integer> m) {
    int cur;
    int cnt = 0;
    while(primeIt.hasNext() && num != 1) {
      cur = primeIt.next();
      while(num % cur == 0) {
        cnt++;
        num /= cur;
      }
      if(cnt > 0) m.put(cur, cnt);
      cnt = 0;
    }
    return num;
  }
  // leftOver computes what number is left when all known primes have been divided out
  private int leftOver (int num, Iterator<Integer> primeIt) {
    int cur;
    while(primeIt.hasNext() && num != 1) {
      cur = primeIt.next();
      while(num % cur == 0) num /= cur;
    }
    return num;
  }
  // If we know that all the primes less than or equal to n, then if n+1 can't be expressed as
  // a product of known primes then n+1 is a prime. primeCrawl takes advantage of this to find
  // primes one at a time.
  private Stack<Integer> primeCrawl(int start, int end, Stack<Integer> startStack) {
    int temp;
    Stack<Integer> newPrimes = new Stack<Integer>();
    for (int i = start; i <= start + inc; i++) {
      temp = leftOver(i, startStack.iterator());
      if(temp != 1) {
        startStack.push(temp);
        newPrimes.push(temp);
      }
    }
    return newPrimes;
  }
  // factors gives back a Hash map where the keys are the prim factors of the
  // number used to create the class and the values are the highest power
  // of the key that diveides the number used to create the class
  public HashMap<Integer, Integer> factors() {
    HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
    primeBarrage(originalNumber, primes.iterator(), m);
    return m;
  }  
  public String toString() {
    Iterator<Integer> toPrint = fact.keySet().iterator();
    int k;
    int v;
    String s = "Prime Factorization is : 1 : ";
    while(toPrint.hasNext()) {
      k = toPrint.next();
      v = fact.get(k);
      s += k + "^" + v + " : ";
    }
    return s;
  }
  // Here are some simple tests
  public static void main(String[] args) {
    Factored f0 = new Factored(0);
    Factored f1 = new Factored(1);
    Factored f2 = new Factored(2);
    Factored f3 = new Factored(3);
    Factored f4 = new Factored(4);
    Factored f5 = new Factored(5);
    Factored f6 = new Factored(21);
    Factored f7 = new Factored(100);
    Factored f8 = new Factored(547);
    Factored f9 = new Factored(548);
    Factored f10 = new Factored(-5);
    Factored f11 = new Factored(-548);
    System.out.println(f0.toString());
    System.out.println(f1.toString());
    System.out.println(f2.toString());
    System.out.println(f3.toString());
    System.out.println(f4.toString());
    System.out.println(f5.toString());
    System.out.println(f6.toString());
    System.out.println(f7.toString());
    System.out.println(f8.toString());
    System.out.println(f9.toString());
    System.out.println(f10.toString());
    System.out.println(f11.toString());
  }
}
