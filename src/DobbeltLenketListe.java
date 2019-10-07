////////////////// class DobbeltLenketListe //////////////////////////////

// Lasse Apalnes Pedersen, s333761
// Fredrik Arne Rikheim, s328084
// Andreas Bjoerkas Grymyr, s333778

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;



public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den foerste i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    // hjelpemetode
    private Node<T> finnNode(int indeks) {
        if(antall == 0) {
            throw new IndexOutOfBoundsException("Listen er tom");
        }
        if(indeks < 0 || indeks >= antall) {
            throw new IndexOutOfBoundsException("Feil i indeks");
        }


        Node<T> temp;

        if(indeks < antall/2) {
            temp = hode;

            for(int i = 0; i < indeks; i++) {
                temp = temp.neste;
            }
        }
        else {
            temp = hale;

            for(int i = antall - 1; i > indeks; i--) {
                temp = temp.forrige;
            }
        }

        return temp;
    }

    //hjelpemetode
    private static void fratilKontroll(int antall, int fra, int til)
    {
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + antall + ")");

        if (fra > til)                                // fra er stoerre enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

    public DobbeltLenketListe() {
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }

    public DobbeltLenketListe(T[] a) {
        if(a == null) {
            throw new NullPointerException("Tabellen a er null");
        }

        if(a.length == 0) {
            endringer = 0;
            antall = 0;
        }
        else {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != null) {
                    if(hode == null) {
                        Node<T> node = new Node<>(a[i]);
                        hode = hale = node;
                        endringer++;
                        antall++;
                    }
                    else {
                        Node<T> node = new Node<>(a[i], hale, null);
                        hale.neste = node;
                        hale = node;
                        endringer++;
                        antall++;
                    }
                }
            }
        }
    }

    public Liste<T> subliste(int fra, int til){
        fratilKontroll(antall, fra, til);

        DobbeltLenketListe<T> subliste = new DobbeltLenketListe<>();

        if(fra == til) {
            return subliste;
        }

        Node<T> peker = finnNode(fra);

        for(int i = 0; i < til - fra; i++) {
            subliste.leggInn(i, peker.verdi);
            peker = peker.neste;
        }

        return subliste;
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public boolean leggInn (T verdi) {
        Objects.requireNonNull(verdi, "Verdi kan ikke vaere null");

        if(antall == 0) {
            Node<T> node = new Node<>(verdi);
            hode = hale = node;
            endringer++;
            antall++;
        }
        else {
            Node<T> node = new Node<>(verdi, hale, null);
            hale.neste = node;
            hale = node;

            antall++;
            endringer++;
        }

        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Verdi kan ikke vaere null");

        if(indeks < 0 || indeks > antall) {
            throw new IndexOutOfBoundsException("Indeksen kan ikke vaere negativ eller stoerre enn antall");
        }

        if(antall == 0) {
            leggInn(verdi);
        }
        else if(indeks == antall) {
            Node<T> node = new Node<>(verdi,hale,null);
            hale.neste = node;
            hale = node;
            endringer++;
            antall++;
        }
        else if(indeks == 0) {
            Node<T> node = new Node<>(verdi,null,hode);
            hode.forrige = node;
            hode = node;
            endringer++;
            antall++;
        }
        else {
            Node<T> r = finnNode(indeks);
            Node<T> p = r.forrige;
            Node<T> q = new Node<>(verdi,p,r);

            p.neste = q;
            r.forrige = q;

            endringer++;
            antall++;
        }

    }

    @Override
    public boolean inneholder(T verdi) {
        if(indeksTil(verdi) == -1) {
            return false;
        }

        return true;
    }

    @Override
    public T hent(int indeks) {
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if(verdi == null) {
            return -1;
        }

        Node temp = hode;

        for(int i = 0; i < antall; i++) {
            if(temp.verdi.equals(verdi)) {
                return i;
            }
            temp = temp.neste;
        }

        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        if(nyverdi == null) {
            throw new NullPointerException("Ny verdi kan ikke vaere null");
        }

        T temp = finnNode(indeks).verdi; //sjekker allerede indeksen i finnNode() og returnrerer null om indeksen ikke stemmer
        if(temp != null) {
            finnNode(indeks).verdi = nyverdi;
            endringer++;
        }

        return temp;
    }

    @Override
    public boolean fjern(T verdi) {
        if(verdi == null) {
            return false;
        }

        if(antall == 1) {
            hode = hale = null;
        }
        else {

            Node<T> q = hode;
            Node<T> p = null;

            while (q != null) {
                if (q.verdi.equals(verdi)) {
                    break;
                }
                p = q;
                q = q.neste;
            }

            if (q == null) {
                return false;
            } else if (q == hode) {
                hode = hode.neste;
                hode.forrige = null;
            } else if (q == hale) {
                hale = p;
                hale.neste = null;
            } else {
                p.neste = q.neste;
                q.neste.forrige = p;
            }
        }

        antall--;
        endringer++;

        return true;
    }

    @Override
    public T fjern(int indeks) {
        if(antall == 0) {
            throw new IndexOutOfBoundsException("Tom liste");
        }
        if(indeks < 0 || indeks >= antall) {
            throw new IndexOutOfBoundsException("Feil i indeks");
        }

        T temp;

        if(indeks == 0) {
            temp = hode.verdi;
            hode = hode.neste;
            if(antall == 1) {
                hale = null;
                hode = null;
            }
        }
        else {
            Node<T> p = finnNode(indeks - 1);
            Node<T> q = p.neste;

            temp = q.verdi;

            if(q == hale) {
                hale = p;
                p.neste = null;
            }
            else {
                Node<T> r = q.neste;
                p.neste = r;
                r.forrige = p;
            }
        }

        antall--;
        endringer++;
        return temp;
    }

    @Override
    public void nullstill() {
        Node<T> neste = hode;
        Node<T> fjernes;
        while(neste != null) {
            fjernes = neste;
            neste = neste.neste;
            fjernes.verdi = null;
            fjernes.neste = fjernes.forrige = null;
            antall--;
            endringer++;
        }

        hode = hale = null;
    }

    /*
    Vi testet begge nullstill-metodene og saa at 1. maate er raskest. Vi testet baade med din testfil og manuell test

    public void nullstill() {
        while(antall > 0) {
            fjern(0);
        }
    }
     */

    @Override
    public String toString() {
        StringJoiner ut = new StringJoiner(", ", "[", "]");
        Node<T> node = hode;

        if(antall == 0) {
            return "[]";
        }

        ut.add(node.verdi.toString());

        while (node != hale) {
            node = node.neste;
            ut.add(node.verdi.toString());
        }

        return ut.toString();
    }

    public String omvendtString() {
        StringJoiner ut = new StringJoiner(", ", "[", "]");
        Node<T> node = hale;

        if(antall == 0) {
            return "[]";
        }

        ut.add(node.verdi.toString());

        while (hode != node) {
            node = node.forrige;
            ut.add(node.verdi.toString());
        }

        return ut.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);
        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;     // p starter paa den foerste i listen
            fjernOK = false;  // blir sann naar next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){
            denne = finnNode(indeks);     // finner noden med gitt indeks
            fjernOK = false;  // blir sann naar next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            if(iteratorendringer != endringer) {
                throw new ConcurrentModificationException("Iteratorendringer er ulik endringer");
            }

            if(!hasNext()) {
                throw new NoSuchElementException("Neste element finnes ikke");
            }

            fjernOK = true;
            T verdi = denne.verdi;
            denne = denne.neste;

            return verdi;
        }

        @Override
        public void remove(){
            if(fjernOK == false) {
                throw new IllegalStateException("Ikke laget en iterator");
            }
            if(endringer != iteratorendringer) {
                throw new ConcurrentModificationException("Endringer og iteratorendringer er ulike");
            }

            fjernOK = false;
            if(antall == 1) {
                hale = hode = null;
            }
            else if(denne == null) {
                hale = hale.forrige;
                hale.neste = null;
            }
            else if (denne.forrige == hode) {
                hode = hode.neste;
                hode.forrige = null;
            }
            else {
                denne.forrige.forrige.neste = denne;
                denne.forrige = denne.forrige.forrige;
            }
            antall--;
            endringer++;
            iteratorendringer++;

        }

    }

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {

        if(liste.antall() == 0) {
            return;
        }

        int sortertIndeks = liste.antall() - 1;
        T verdi = liste.hent(sortertIndeks);

        while(sortertIndeks > 0) {
            int i = 0;
            while(i < sortertIndeks) {
                if(c.compare(liste.hent(i),verdi) > 0) {
                    liste.leggInn(sortertIndeks + 1,liste.hent(i));
                    liste.fjern(i);
                    verdi = liste.hent(sortertIndeks);
                }
                else {
                    i++;
                }
            }
            sortertIndeks--;
            verdi = liste.hent(sortertIndeks);
        }

    }


} // class DobbeltLenketListe


