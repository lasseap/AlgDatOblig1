////////////////// class DobbeltLenketListe //////////////////////////////


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;



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
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        //throw new NotImplementedException();
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
                        Node<T> node = new Node<>(a[i], hode, null);
                        hode.neste = node;
                        hale = node;
                        endringer++;
                        antall++;
                    }
                }
            }
        }
        System.out.println(antall);

    }

    public Liste<T> subliste(int fra, int til){
        throw new NotImplementedException();
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
        Objects.requireNonNull(verdi, "Verdi kan ikke være null");

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
        throw new NotImplementedException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public T hent(int indeks) {
        throw new NotImplementedException();
    }

    @Override
    public int indeksTil(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new NotImplementedException();
    }

    @Override
    public boolean fjern(T verdi) {
        if(verdi == null) {
            return false;
        }

        Node<T> q = hode;
        Node<T> p = null;

        while(q != null) {
            if(q.verdi.equals(verdi)) {
                break;
            }
            p = q;
            q = q.neste;
        }

        if(q == null) {
            return false;
        }
        else if(q == hode) {
            hode = hode.neste;
        }
        else {
            p.neste = q.neste;
        }

        if(q == hale) {
            hale = p;
        }

        q.verdi = null;
        q.neste = null;

        antall--;
        endringer++;

        return true;
    }

    @Override
    public T fjern(int indeks) {
        if(indeks < 0 || indeks > antall) {
            return null;
        }

        T temp;

        if(indeks == 0) {
            temp = hode.verdi;
            hode = hode.neste;
            if(antall == 1) {
                hale = null;
            }
        }
        else {
            Node<T> p = finnNode(indeks - 1);
            Node<T> q = p.neste;
            temp = q.verdi;

            if(q == hale) {
                hale = p;
            }
            p.neste = q.neste;
        }

        antall--;
        endringer++;
        return temp;
    }

    @Override
    public void nullstill() {
        throw new NotImplementedException();
    }

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
        throw new NotImplementedException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new NotImplementedException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){
            throw new NotImplementedException();
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            throw new NotImplementedException();
        }

        @Override
        public void remove(){
            if(fjernOK == false) {
                throw new IllegalStateException("ikke laget en iterator");
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
                denne.forrige.neste = denne.neste;
                denne.neste.forrige = denne.forrige;
            }
            antall--;
            endringer++;
            iteratorendringer++;

        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new NotImplementedException();
    }

} // class DobbeltLenketListe


