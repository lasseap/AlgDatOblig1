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

            if(a[i-1] > a[i]) {
                temp = a[i];
                a[i] = a[i-1];
                a[i-1] = temp;
            }
        }
        return a[a.length-1];
    }

    public static int ombyttinger(int[] a) {
        if (a.length == 0) {
            throw new NoSuchElementException("arrayen er tom");
        }
        int temp; //midlertidig lagringvariabel
        int teller = 0;

        for (int i = 1; i < a.length;i++) {

            if(a[i-1] > a[i]) {
                temp = a[i];
                a[i] = a[i-1];
                a[i-1] = temp;
                teller++;
            }
        }
        return teller;
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
