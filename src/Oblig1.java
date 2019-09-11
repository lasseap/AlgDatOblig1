// Lasse Apalnes Pedersem, s333761
// Fredrik Arne Rikheim, s328084
// Andreas Bjørkås Grymyr, s333778

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class Oblig1 {


    public static void main(String[] args) {
        int[] a = randPerm(20);
        randPerm(a);
        System.out.println(ombyttinger(a));
        randPerm(a);
        System.out.println(ombyttinger(a));
        randPerm(a);
        System.out.println(ombyttinger(a));
        randPerm(a);
        System.out.println(ombyttinger(a));
        randPerm(a);
        System.out.println(ombyttinger(a));

    }

    /*
    Oppgave 1
    Antall sammenligninger blir n-1 som blir O(n).
    Det blir flest ombyttinger når det største tallet er første elementet i arrayen.
    Det blir færrest ombyttinger når største tallet er sist i arrayen.
    Gjennomsnittlig antall ombyttninger blir...
     */
    public static int maks(int[] a) {
        if (a.length == 0) {
            throw new NoSuchElementException("arrayen er tom");
        }
        int temp; //midlertidig lagringvariabel

        for (int i = 1; i < a.length; i++) {

            if (a[i - 1] > a[i]) {
                temp = a[i];
                a[i] = a[i - 1];
                a[i - 1] = temp;
            }
        }
        return a[a.length - 1];
    }

    public static int ombyttinger(int[] a) {
        if (a.length == 0) {
            throw new NoSuchElementException("arrayen er tom");
        }
        int temp; //midlertidig lagringvariabel
        int teller = 0;

        for (int i = 1; i < a.length; i++) {

            if (a[i - 1] > a[i]) {
                temp = a[i];
                a[i] = a[i - 1];
                a[i - 1] = temp;
                teller++;
            }
        }
        return teller;
    }

    //Oppgave 2
    public static int antallUlikeSortert(int[] a) {
        int teller = 0;


        if (a.length == 0) {
            return teller;
        }
        int tall = a[0];
        teller++;
        for (int i = 1; i < a.length; i++) {
            if (a[i] > tall) {
                teller++;
                tall = a[i];
            }
            if (a[i] < tall) {
                throw new IllegalStateException("Tabellen er ikke sortert");
            }
        }
        return teller;
    }

    //Oppgave 3
    public static int antallUlikeUsortert(int[] a) {
        int teller = 0;


        if (a.length == 0) {
            return teller;
        }
        int tall; //hjelpevariabel hvilket tall
        boolean lik; // hjelpevariabel er tallet lik et tidligere tall
        teller++;// teller første tallet
        for (int i = 1; i < a.length; i++) {
            tall = a[i];
            lik = false;
            for (int j = 0; j < i; j++) {
                if (a[j] == tall) {
                    lik = true;
                }
            }
            if (!lik) {
                teller++;
            }
        }
        return teller;
    }

    //Oppgave 4

    public static void delsortering(int[] a) {
        if (a.length == 0) {
            return;
        }
        int partallindex = a.length-1;
        int oddetallindex = 0;
        int temp;


        boolean ikkePartisjonert = true;
        while (ikkePartisjonert)                                  // stopper når v > h
        {
            while ((oddetallindex <= partallindex) && ((a[oddetallindex] % 2 == 1)||(a[oddetallindex] % 2 == -1))) {
                oddetallindex++;   // partallindex er stoppverdi for oddetalsindex
            }
            while ((oddetallindex <= partallindex) && (a[partallindex] % 2 == 0)) {
                partallindex--;  // oddetallindex er stoppverdi for partallindex
            }

            if (oddetallindex < partallindex) {
                temp = a[partallindex];
                a[partallindex] = a[oddetallindex];
                a[oddetallindex] = temp;
            } else {
                ikkePartisjonert = false;
            }
        }
        partallindex++;
        oddetallindex--;
        System.out.println(oddetallindex +" " +partallindex);
        if (partallindex > 1) { //hvis jeg fant 2 eller flere oddetall
            int sortert = 0;
            int oddetall;

            while (sortert < partallindex) {
                oddetall = a[sortert];
                oddetallindex = sortert;
                for (int i = sortert + 1; i < partallindex; i++) {
                    if (oddetall > a[i]) {
                        oddetall = a[i];
                        oddetallindex = i;
                    }
                }
                if (oddetallindex != sortert) {
                    temp = a[sortert];
                    a[sortert] = oddetall;
                    a[oddetallindex] = temp;
                    sortert++;
                } else {
                    sortert++;
                }

            }
        }

        if (partallindex < a.length - 1) { //hvis jeg har 2 eller flere partall
            int sortert = partallindex;
            int partall;

            while (a.length - 1 > sortert) {
                partall = a[sortert];
                partallindex = sortert;
                for (int i = sortert + 1; i < a.length; i++) {
                    if (partall > a[i]) {
                        partall = a[i];
                        partallindex = i;
                    }
                }
                if (partallindex != sortert) {
                    temp = a[sortert];
                    a[sortert] = partall;
                    a[partallindex] = temp;
                    sortert++;
                } else {
                    sortert++;
                }
            }
        }
    }


    //Oppgave 5
    public static void rotasjon(char[] a) {

        if(a.length < 2) {
            return;
        }

        char temp; //midlertidig lagringvariabel

        for(int i = 1; i < a.length; i++) {
            temp = a[i];
            a[i] = a[0];
            a[0] = temp;
        }

    }

    public static int[] randPerm(int n)  // en effektiv versjon
    {
        Random r = new Random();         // en randomgenerator
        int[] a = new int[n];            // en tabell med plass til n tall

        Arrays.setAll(a, i -> i + 1);    // legger inn tallene 1, 2, . , n

        for (int k = n - 1; k > 0; k--)  // løkke som går n - 1 ganger
        {
            int i = r.nextInt(k+1);        // en tilfeldig tall fra 0 til k
            bytt(a,k,i);                   // bytter om
        }

        return a;                        // permutasjonen returneres
    }

    public static void bytt(int[] a, int i, int j)
    {
        int temp = a[i]; a[i] = a[j]; a[j] = temp;
    }
    public static void randPerm(int[] a)  // stokker om a
    {
        Random r = new Random();     // en randomgenerator

        for (int k = a.length - 1; k > 0; k--)
        {
            int i = r.nextInt(k + 1);  // tilfeldig tall fra [0,k]
            bytt(a,k,i);
        }
    }
}
