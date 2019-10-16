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

    antall++;                             // én verdi mer i treet
    endringer++;
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
    endringer++;
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
  
  private static <T> Node<T> nesteInorden(Node<T> p) {
    if(p.høyre != null) {
      while(p.venstre != null) {
        p = p.venstre;
      }
      return p;
    }
    else {
      Node<T> q = p.forelder;
      while(q != null && p == q.høyre) {
        if(q.forelder == null && q.høyre == p) {
          return null;
        }
        else {
          p = q;
          q = q.forelder;
        }
      }
      return q;
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
    while (p != null){
      ut.add(p.verdi.toString());
      p = nesteInorden(p);
    }
    return ut.toString();

  }

  public String omvendtString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String høyreGren()
  {
    StringJoiner høyregren = new StringJoiner(", ", "[", "]");
    Node<T> bladnode = rot;
    boolean leter = false;
    if(bladnode != null) {
      leter = true;
    }

    while (leter) {
      høyregren.add(bladnode.toString());
      if(bladnode.høyre == null && bladnode.venstre == null) {
        leter = false;
      }
      else if(bladnode.høyre != null) {
        bladnode = bladnode.høyre;
      }
      else {
        bladnode = bladnode.venstre;
      }
    }
    return høyregren.toString();
  }

  private StringJoiner finnLensteGren(Node<T> node) {
    StringJoiner lengsteGren = new StringJoiner(", ", "[", "]");
    StringJoiner venstreGren;
    StringJoiner høyreGren;
    if(node.venstre != null && node.høyre != null ) { //sjekker om node har to barn
      venstreGren = finnLensteGren(node.venstre);
      høyreGren = finnLensteGren(node.høyre);
      if(venstreGren.length() < høyreGren.length()) {
        lengsteGren.merge(høyreGren);
        }
      else {
        lengsteGren.merge(venstreGren);
      }
    }
    else if (node.venstre != null) { // sjekker om noden har kun venstre barn
      lengsteGren.merge(finnLensteGren(node.venstre));
    }
    else if (node.høyre != null) { // sjekker om noden har kun høyre barn
      lengsteGren.merge(finnLensteGren(node.høyre));
    }
    else {
      lengsteGren.add(node.toString());
    }
    return lengsteGren;
  }
  
  public String lengstGren()
  {
    StringJoiner lengstegren = new StringJoiner(", ", "[", "]");

    if(rot == null) {// returnerer [] hvis rot er null
      return lengstegren.toString();
    }
    if(antall == 1) { // returner rot hvis det bare er en node i treet
      lengstegren.add(rot.toString());
      return lengstegren.toString();
    }
    if(antall == 2){ // sjekker om rot bare har en node
      lengstegren.add(rot.toString());
      if(rot.venstre != null) { //legger til venstre
        lengstegren.add(rot.venstre.toString());
      }
      else { // legger til høyre
        lengstegren.add(rot.høyre.toString());
      }
      return lengstegren.toString();
    }
    lengstegren.add(rot.toString());// legger til rot og finner lengstegren til høyre og venstre
    StringJoiner venstreGren = finnLensteGren(rot.venstre);
    StringJoiner høyreGren = finnLensteGren(rot.høyre);

    if (venstreGren.length() < høyreGren.length()) { // sjekker hvilken gren som er lengst
      lengstegren.merge(høyreGren);
    }
    else {
      lengstegren.merge(venstreGren);
    }
    return lengstegren.toString();
  }
/*
  private String[] finnGrener(Node<T> node,String[] grener ) {
    if(node.venstre != null && node.høyre != null) {
      String[] venstre = finnGrener(node.venstre);
      String[] høyre = finnGrener(node.høyre);
      int antallGrener =venstre.length + høyre.length;
      grener = new String[antallGrener];
      System.arraycopy(venstre,0,grener,0,venstre.length);
      System.arraycopy(høyre,0,grener,venstre.length,høyre.length);
    }
    else if (node.venstre != null) {
      return finnGrener(node.venstre);
    }
    else if (node.høyre != null) {
      return finnGrener(node.høyre);
    }
    else {
      String[] grenSlutt = new String[grener.length+1];
      System.arraycopy(grener,0,grenSlutt,0,grener.length);
      grenSlutt[grenSlutt.length-1] = "["
      return grener;
    }
  }*/
  
  public String[] grener() {

    if(rot == null) {
      return new String[0];
    }
    String sisteGren = høyreGren();
    if(antall < 3) {
      String[] grener = {sisteGren};
      return grener;
    }
    StringJoiner gren = new StringJoiner(", ", "[", "]");
    gren.add(rot.toString());
    ArrayList<Node<T>> sjekkedeNoder = new ArrayList<>();
    ArrayList<String> funnetGrener = new ArrayList<>();
    String[] grener;
    Node<T> node = rot.venstre;
    Node<T> nesteinorden = null;
    while (nesteinorden == null) { //finner første gren
      gren.add(node.toString());
      if(node.venstre != null) {
        node = node.venstre;
      }
      else if (node.høyre != null) {
        node = node.høyre;
      }
      else {
        nesteinorden = nesteInorden(node);
        sjekkedeNoder.add(node);
        node = rot;
        funnetGrener.add(gren.toString());
        gren = new StringJoiner(", ", "[", "]");
      }
    }
    while (funnetGrener.contains(sisteGren)) { // finner resten av grenene til de er like den høyre grenen
      gren.add(node.toString());
      if (node == nesteinorden) { //Sjekker om man er på neste node som skal sjekkes
        if (node.venstre == null && node.høyre == null) {
          nesteinorden = nesteInorden(node);
          sjekkedeNoder.add(node);
          node = rot;
          funnetGrener.add(gren.toString());
          gren = new StringJoiner(", ", "[", "]");
        } else {
          if (node.høyre == null) { // ikke en bladnode, starter gren på nytt
            nesteinorden = nesteInorden(nesteinorden);
            sjekkedeNoder.add(node);
            node = nesteinorden;
            gren = new StringJoiner(", ", "[", "]");
          } else {
            sjekkedeNoder.add(node);
            node = node.høyre;
            nesteinorden = nesteInorden(nesteinorden);
          }
        }
      }
      else { //ikke i neste inorden node
        if(sjekkedeNoder.contains(node)){
          node = node.høyre;
        }
        else {//implementer vanlig sjekk av node, venstre hvis den har venstre, høyre hvis den har den 

        }
      }
    }
    grener = new String[funnetGrener.size()];
    for (int i = 0;i< funnetGrener.size();i++) {
      grener[i] = funnetGrener.get(i);
    }
    return grener;
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
    if(rot == null) {
      return "[]";
    }

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
    public boolean hasNext() {
      return p != null;  // Denne skal ikke endres!
    }
    
    @Override
    public T next() {
      if(endringer != iteratorendringer) {
        throw new ConcurrentModificationException("Endringer og iteratorendringer er ulike");
      }
      if(!hasNext()) {
        throw new NoSuchElementException("Ingen flere bladnoder igjen");
      }

      removeOK = true;
      T verdi = p.verdi;
      q = p;
      p = finnNesteBladnode(nestePreorden(p));

      return verdi;
    }
    
    @Override
    public void remove() {
      if(!removeOK) {
        throw new IllegalStateException("Ikke laget en iterator");
      }
      if(endringer != iteratorendringer) {
        throw new ConcurrentModificationException("Endringer og iteratorendringer er ulike");
      }

      removeOK = false;
      if(p.forelder.venstre == p) {
        p.forelder.venstre = null;
      }
      else if(p.forelder.høyre == p) {
        p.forelder.høyre = null;
      }

      endringer++;
      iteratorendringer++;
    }


  } // BladnodeIterator

} // ObligSBinTre
