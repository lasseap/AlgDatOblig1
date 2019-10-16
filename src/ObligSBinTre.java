////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{
  private static final class Node<T>   // en indre nodeklasse
  {
    private T verdi;                   // nodens verdi
    private Node<T> venstre, høyre;    // venstre og høyre barn
    private Node<T> forelder;          // forelder

    // konstruktør
    private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
      this.forelder = forelder;
    }

    private Node(T verdi, Node<T> forelder)  // konstruktør
    {
      this(verdi, null, null, forelder);
    }

    @Override
    public String toString(){ return "" + verdi;}

  } // class Node

  private Node<T> rot;                            // peker til rotnoden
  private int antall;                             // antall noder
  private int endringer;                          // antall endringer

  private final Comparator<? super T> comp;       // komparator

  public ObligSBinTre(Comparator<? super T> c)    // konstruktør
  {
    rot = null;
    antall = 0;
    comp = c;
  }
  
  @Override
  public boolean leggInn(T verdi) {
    Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

    Node<T> p = rot, q = null;               // p starter i roten
    int cmp = 0;                             // hjelpevariabel

    while (p != null)       // fortsetter til p er ute av treet
    {
      q = p;                                 // q er forelder til p
      cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
      p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
    }

    // p er nå null, dvs. ute av treet, q er den siste vi passerte

    if (q == null) {
      p = new Node<T>(verdi,null);
      rot = p;                                // p blir rotnode
    }
    else if (cmp < 0) {
      p = new Node<T>(verdi,q);
      q.venstre = p;                          // venstre barn til q
    }
    else {
      p = new Node<T>(verdi,q);
      q.høyre = p;                        // høyre barn til q
    }

    antall++;                                // én verdi mer i treet
    return true;

  }
  
  @Override
  public boolean inneholder(T verdi)
  {
    if (verdi == null) return false;

    Node<T> p = rot;

    while (p != null)
    {
      int cmp = comp.compare(verdi, p.verdi);
      if (cmp < 0) p = p.venstre;
      else if (cmp > 0) p = p.høyre;
      else return true;
    }

    return false;
  }
  
  @Override
  public boolean fjern(T verdi)
  {
    if (verdi == null) return false;  // treet har ingen nullverdier

    Node<T> p = rot, q = null;   // q skal være forelder til p

    while (p != null)            // leter etter verdi
    {
      int cmp = comp.compare(verdi,p.verdi);      // sammenligner
      if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
      else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
      else break;    // den søkte verdien ligger i p
    }
    if (p == null) return false;   // finner ikke verdi

      if(p.venstre == null && p.høyre == null) {
          if (p == rot) rot = null;                     // venstre i figuren
          else if (p == q.venstre) q.venstre = null;    // midten i figuren
          else q.høyre = null;                          // høyre i figuren
      }



      else if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
    {
      Node<T> b;
      if(p.venstre != null) {
         b = p.venstre;
         System.out.println(b.verdi);
      }
      else {
         b = p.høyre;
          System.out.println(b.verdi);
      }

      if (p == rot) {
        rot = b;
        b.forelder = null;
      }
      else if (p == q.venstre)  {
        q.venstre = b;
        b.forelder = q;
      }
      else {
        q.høyre = b;
        b.forelder = q;
      }
    }
    else  // Tilfelle 3)
    {
      Node<T> s = p, r = p.høyre;   // finner neste i inorden
      while (r.venstre != null)
      {
        s = r;    // s er forelder til r
        r = r.venstre;
      }

      p.verdi = r.verdi;   // kopierer verdien i r til p

      if (s != p) {
        s.venstre = r.høyre;
        s.venstre.forelder = s;
      }
      else {
        s.høyre = r.høyre;
        s.høyre.forelder = s;
      }
    }

    antall--;   // det er nå én node mindre i treet
    return true;
  }
  
  public int fjernAlle(T verdi)
  {
      int antall = 0;
      boolean fjerner = true;
      while (fjerner) {
          if(fjern(verdi)) {
              antall++;
          }
          else {
              fjerner = false;
          }
      }
      return antall;
  }
  
  @Override
  public int antall()
  {
    return antall;
  }
  
  public int antall(T verdi)
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  @Override
  public boolean tom()
  {
    return antall == 0;
  }
  
  @Override
  public void nullstill()
  {
    Node<T> node = nesteInorden(rot);
    while (node != null) {
      fjern(node.verdi);
      node = nesteInorden(rot);
    }
    rot = null;
    antall = 0;

  }
  
  private static <T> Node<T> nesteInorden(Node<T> p)
  {
    if(p.høyre != null) {
      while (p.venstre != null) p = p.venstre;
      {
        return p;
      }
    }
    else{
      while(p.forelder != null && p.forelder.høyre == p){
        p = p.forelder;
      }
      return p.forelder;
    }
  }
  
  @Override
  public String toString()
  {
    StringJoiner ut = new StringJoiner(", ", "[", "]");
    if(tom()) {
      return "[]";
    }
    Node<T> p = rot;
    ut.add(p.verdi.toString());
    while (p.venstre != null){
      p = nesteInorden(p);
      ut.add(p.verdi.toString());
    }
    return toString();
  }
  
  public String omvendtString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String høyreGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String lengstGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String[] grener()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }

  //Hjelpemetode for oppgave 8a)
  private Node<T> nestePreorden(Node<T> p) {
      if(p.venstre != null) {
          return p.venstre;
      }
      else if(p.høyre != null) {
          return p.høyre;
      }

      Node current = p;
      Node forelder = p.forelder;

      while(forelder != null && ((forelder.venstre == current && forelder.høyre == null) || (forelder.høyre == current))) {
          current = current.forelder;
          forelder = forelder.forelder;
      }

      if(forelder == null) {
          return null;
      }

      return forelder.høyre;
  }

  private Node<T> finnNesteBladnode(Node<T> p) {
    if(p == null) {
      return null;
    }
    else if(p.venstre == null && p.høyre == null) {
      return p;
    }
    else {
      return finnNesteBladnode(nestePreorden(p));
    }
  }

  public String bladnodeverdier() {
    Node<T> p;
    p = rot;
    if(p == null) {
      return "[]";
    }
    StringJoiner bladnoder = new StringJoiner(", ", "[", "]");

    boolean lete = true;
    if(p.venstre == null && p.høyre == null) {
      bladnoder.add(p.verdi.toString());
    }
    else {
      while(lete) {
        p = finnNesteBladnode(nestePreorden(p));
        if(p == null) {
          lete = false;
        }
        else {
          bladnoder.add(p.verdi.toString());
        }
      }
    }

    return bladnoder.toString();
  }
  
  public String postString() {
    Stack<Node> stack = new Stack<>();
    String verdi;
    Node temp;
    Node forrige = null;
    StringJoiner postOrden = new StringJoiner(", ", "[", "]");

    stack.push(rot);

    while(!stack.isEmpty()) {
      Node current = stack.peek();

      if(forrige == null || forrige.venstre == current || forrige.høyre == current ) {
        if(current.venstre != null) {
          stack.push(current.venstre);
        }
        else if(current.høyre!= null) {
          stack.push(current.høyre);
        }
        else {
          temp = stack.pop();
          verdi = temp.verdi.toString();
          postOrden.add(verdi);
        }
      }
      else if(current.venstre == forrige) {
        if(current.høyre != null) {
          stack.push(current.høyre);
        }
        else {
          temp = stack.pop();
          verdi = temp.verdi.toString();
          postOrden.add(verdi);
        }
      }
      else if(current.høyre == forrige) {
        temp = stack.pop();
        verdi = temp.verdi.toString();
        postOrden.add(verdi);
      }

    forrige = current;
    }

    return postOrden.toString();
  }
  
  @Override
  public Iterator<T> iterator()
  {
    return new BladnodeIterator();
  }
  
  private class BladnodeIterator implements Iterator<T> {
    private Node<T> p = rot, q = null;
    private boolean removeOK = false;
    private int iteratorendringer = endringer;
    
    private BladnodeIterator() {  // konstruktør
      p = finnNesteBladnode(p);
      iteratorendringer = endringer;
      removeOK = false;
    }
    
    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }
    
    @Override
    public T next() {
      if(!hasNext()) {
        throw new NoSuchElementException("Ingen flere bladnoder igjen");
      }

      removeOK = true;
      T verdi = p.verdi;
      p = finnNesteBladnode(nestePreorden(p));

      return verdi;
    }
    
    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

  } // BladnodeIterator

} // ObligSBinTre
