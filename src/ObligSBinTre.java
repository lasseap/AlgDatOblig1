// Lasse Apalnes Pedersem, s333761
// Fredrik Arne Rikheim, s328084
// Andreas Bjoerkas Grymyr, s333778

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T> {
  private static final class Node<T>   // en indre nodeklasse
  {
    private T verdi;                   // nodens verdi
    private Node<T> venstre, høyre;    // venstre og hoeyre barn
    private Node<T> forelder;          // forelder

    // konstruktoer
    private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
      this.forelder = forelder;
    }

    private Node(T verdi, Node<T> forelder)  // konstruktoer
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

  public ObligSBinTre(Comparator<? super T> c)    // konstruktoer
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

    // p er naa null, dvs. ute av treet, q er den siste vi passerte

    if (q == null) {
      p = new Node<>(verdi,null);
      rot = p;                                // p blir rotnode
    }
    else if (cmp < 0) {
      p = new Node<>(verdi,q);
      q.venstre = p;                          // venstre barn til q
    }
    else {
      p = new Node<>(verdi,q);
      q.høyre = p;                        // hoeyre barn til q
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

    Node<T> p = rot, q = null;   // q skal vaere forelder til p

    while (p != null)            // leter etter verdi
    {
      int cmp = comp.compare(verdi,p.verdi);      // sammenligner
      if (cmp < 0) { q = p; p = p.venstre; }      // gaar til venstre
      else if (cmp > 0) { q = p; p = p.høyre; }   // gaar til hoeyre
      else break;    // den soekte verdien ligger i p
    }
    if (p == null) {
      return false;   // finner ikke verdi
    }

    if(p.venstre == null && p.høyre == null) {
        if (p == rot) rot = null;                     // venstre i figuren
        else if (p == q.venstre) q.venstre = null;    // midten i figuren
        else q.høyre = null;                          // hoeyre i figuren
    }
    else if (p.venstre == null || p.høyre == null) {  // Tilfelle 1) og 2)
      Node<T> b;
      if(p.venstre != null) {
         b = p.venstre;
      }
      else {
         b = p.høyre;
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
      while (r.venstre != null) {
        s = r;    // s er forelder til r
        r = r.venstre;
      }

      p.verdi = r.verdi;   // kopierer verdien i r til p

      if (s != p) {
        s.venstre = r.høyre;
        if(r.høyre != null) {
          r.høyre.forelder = s;
        }
      }
      else {
        s.høyre = r.høyre;
        if(r.venstre != null) {
          r.venstre.forelder = s;
        }
      }
    }

    antall--;   // det er naa én node mindre i treet
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
  
  public int antall(T verdi) {
    int antall = 0;
    Node<T> p = rot;
    while(p.venstre != null) {
      p = p.venstre;
    }

    while(p != null) {
      if(p.verdi.equals(verdi)) {
        antall++;
      }
      p = nesteInorden(p);
    }

    return antall;
  }
  
  @Override
  public boolean tom()
  {
    return antall == 0;
  }
  
  @Override
  public void nullstill() {
    if(tom()) {
      return;
    }

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
      p = p.høyre;
      while(p.venstre != null) {
        p = p.venstre;
      }

      return p;
    }
    else {
      Node<T> q = p.forelder;
      while(q != null && p == q.høyre) {
        if(q.forelder == null) {
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
  public String toString() {
    StringJoiner ut = new StringJoiner(", ", "[", "]");

    if(tom()) {
      return ut.toString();
    }
    Node<T> p = rot;
    while(p.venstre != null) {
      p = p.venstre;
    }

    while (p != null){
      ut.add(p.toString());
      p = nesteInorden(p);
    }
    return ut.toString();

  }

  public String omvendtString() {
    StringJoiner ut = new StringJoiner(", ", "[", "]");

    if(tom()) {
      return ut.toString();
    }

    Stack<Node<T>> stack = new Stack<>();
    Node<T> p = rot;
    for( ; p.høyre != null; p = p.høyre) {
      stack.push(p);
    }

    while(true) {
      ut.add(p.toString());

      if(p.venstre != null) {
        for(p = p.venstre; p.høyre != null; p = p.høyre) {
          stack.push(p);
        }
      }
      else if(!stack.isEmpty()) {
        p = stack.pop();
      }
      else {
        break;
      }
    }

    return ut.toString();
  }
  
  public String hoeyreGren() {
    StringJoiner hoeyreGren = new StringJoiner(", ", "[", "]");
    Node<T> bladnode = rot;
    boolean leter = false;
    if(bladnode != null) {
      leter = true;
    }

    while (leter) {
      hoeyreGren.add(bladnode.toString());
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
    return hoeyreGren.toString();
  }

  private StringJoiner finnLensteGren(Node<T> node) {
    StringJoiner lengsteGren = new StringJoiner(", ", "[", "]");
    StringJoiner venstreGren;
    StringJoiner hoeyreGren;
    if(node.venstre != null && node.høyre != null ) { //sjekker om node har to barn
      lengsteGren.add(node.toString());
      venstreGren = finnLensteGren(node.venstre);
      hoeyreGren = finnLensteGren(node.høyre);
      if(venstreGren.length() < hoeyreGren.length()) {
        lengsteGren.merge(hoeyreGren);
        }
      else {
        lengsteGren.merge(venstreGren);
      }
    }
    else if (node.venstre != null) { // sjekker om noden har kun venstre barn
      lengsteGren.add(node.toString());
      lengsteGren.merge(finnLensteGren(node.venstre));
    }
    else if (node.høyre != null) { // sjekker om noden har kun hoeyre barn
      lengsteGren.add(node.toString());
      lengsteGren.merge(finnLensteGren(node.høyre));
    }
    else {
      lengsteGren.add(node.toString());
    }
    return lengsteGren;
  }
  
  public String lengstGren() {
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
      else { // legger til hoeyre
        lengstegren.add(rot.høyre.toString());
      }
      return lengstegren.toString();
    }
    lengstegren.add(rot.toString());// legger til rot og finner lengstegren til hoeyre og venstre
    StringJoiner venstreGren = new StringJoiner(", ", "[", "]");
    StringJoiner hoeyreGren = new StringJoiner(", ", "[", "]");
    String[] venstreGrenArray = new String[0];
    String[] hoeyreGrenArray = new String[0];
    if(rot.venstre != null) {
      venstreGren = finnLensteGren(rot.venstre);
      venstreGrenArray = venstreGren.toString().split(",");
    }
    if(rot.høyre != null) {
      hoeyreGren = finnLensteGren(rot.høyre);
      hoeyreGrenArray = hoeyreGren.toString().split(",");
    }

    if (venstreGrenArray.length < hoeyreGrenArray.length) { // sjekker hvilken gren som er lengst
      lengstegren.merge(hoeyreGren);
    }
    else {
      lengstegren.merge(venstreGren);
    }
    return lengstegren.toString();
  }
  
  public String[] grener() {

    if(rot == null) {
      return new String[0];
    }
    String sisteGren = hoeyreGren();
    if(antall < 3) {
      return new String[]{sisteGren};
    }
    StringJoiner gren = new StringJoiner(", ", "[", "]");
    gren.add(rot.toString());
    ArrayList<Node<T>> sjekkedeNoder = new ArrayList<>();
    ArrayList<String> funnetGrener = new ArrayList<>();
    String[] grener;
    Node<T> node = rot.venstre;
    Node<T> nesteinorden = null;
    while (nesteinorden == null) { //finner foerste gren
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
    while (!funnetGrener.contains(sisteGren)) { // finner resten av grenene til de er like den hoeyre grenen
      gren.add(node.toString());
      if (node == nesteinorden) { //Sjekker om man er paa neste node som skal sjekkes
        if (node.venstre == null && node.høyre == null) {
          nesteinorden = nesteInorden(node);
          sjekkedeNoder.add(node);
          node = rot;
          funnetGrener.add(gren.toString());
          gren = new StringJoiner(", ", "[", "]");
        } else {
          if (node.høyre == null) { // ikke en bladnode, starter gren paa nytt
            nesteinorden = nesteInorden(nesteinorden);
            sjekkedeNoder.add(node);
            node = rot;
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
        else {//kan ikke vaere bladnode, allerede sjekket
          if(node.venstre != null) {
            node = node.venstre;
          }
          else {
            node = node.høyre;
          }
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

      Node<T> current = p;
      Node<T> forelder = p.forelder;

      while(forelder != null && ((forelder.venstre == current && forelder.høyre == null)
              || (forelder.høyre == current))) {
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
      bladnoder.add(p.toString());
    }
    else {
      while(lete) {
        p = finnNesteBladnode(nestePreorden(p));
        if(p == null) {
          lete = false;
        }
        else {
          bladnoder.add(p.toString());
        }
      }
    }

    return bladnoder.toString();
  }
  
  public String postString() {
    if(rot == null) {
      return "[]";
    }

    Stack<Node<T>> stack = new Stack<>();
    String verdi;
    Node<T> temp;
    Node<T> forrige = null;
    StringJoiner postOrden = new StringJoiner(", ", "[", "]");

    stack.push(rot);

    while(!stack.isEmpty()) {
      Node<T> current = stack.peek();

      if(forrige == null || forrige.venstre == current || forrige.høyre == current ) {
        if(current.venstre != null) {
          stack.push(current.venstre);
        }
        else if(current.høyre!= null) {
          stack.push(current.høyre);
        }
        else {
          temp = stack.pop();
          verdi = temp.toString();
          postOrden.add(verdi);
        }
      }
      else if(current.venstre == forrige) {
        if(current.høyre != null) {
          stack.push(current.høyre);
        }
        else {
          temp = stack.pop();
          verdi = temp.toString();
          postOrden.add(verdi);
        }
      }
      else if(current.høyre == forrige) {
        temp = stack.pop();
        verdi = temp.toString();
        postOrden.add(verdi);
      }

    forrige = current;
    }

    return postOrden.toString();
  }
  
  @Override
  public Iterator<T> iterator() {
    return new BladnodeIterator();
  }
  
  private class BladnodeIterator implements Iterator<T> {
    private Node<T> p = rot, q = null;
    private boolean removeOK;
    private int iteratorendringer;
    
    private BladnodeIterator() {  // konstruktoer
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
      if(q.forelder == null) {
        q = null;
      }
      else if(q.forelder.venstre != null && q.forelder.høyre != null) {
        if (q.forelder.venstre == q) {
          q.forelder.venstre = null;
        }
        else if (q.forelder.høyre == q) {
          q.forelder.høyre = null;
        }
      }
      else if(q.forelder.venstre != null) {
        q.forelder.venstre = null;
      }
      else if(q.forelder.høyre != null) {
        q.forelder.høyre = null;
      }
      else {
        q = null;
      }

      antall--;
      endringer++;
      iteratorendringer++;
    }


  } // BladnodeIterator

} // ObligSBinTre
