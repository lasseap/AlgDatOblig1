package Oblig1;// Lasse Apalnes Pedersem, s333761
// Fredrik Arne Rikheim, s328084
// Andreas Bjorkas Grymyr, s333778

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class Oblig1 {

    /*
    Oppgave 1
    Antall sammenligninger blir n-1 som blir O(n).
    Det blir flest ombyttinger nar det storste tallet er forste elementet i arrayen.
    Det blir faerrest ombyttinger nar storste tallet er sist i arrayen.
    Gjennomsnittlig antall ombyttninger blir litt mindre enn n.
    Dette betyr at denne maks metoden er litt bedre enn de vi har sett pa tidligere, men den er ogsa O(n) som de andre.
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
        teller++;// teller forste tallet
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
        while (ikkePartisjonert)                                  // stopper nar venstre(oddetalsindex) > hoyre(partallsindex)
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
        quickSort(a,0,oddetallindex);
        quickSort(a,partallindex,a.length-1);
    }

    private static void quickSort(int[] a, int fra, int til) { //Hjelpefunksjon for delsortering i oppgave 4 og 8
        if (fra < til) {
            int partitionIndex = parter(a,fra,til);

            quickSort(a, fra, partitionIndex-1);
            quickSort(a,partitionIndex+1, til);
        }

    }

    private static int parter(int[] a, int fra, int til) { //Hjelpefunksjon for quicksort i oppgave 4
        int pivot = a[til]; //Velger bakerste
        int i = (fra-1);

        for (int j = fra; j < til; j++) {
            if (a[j] <= pivot) {
                i++;

                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
// Flytter pivot etter de som er mindre
        int temp = a[i+1];
        a[i+1] = a[til];
        a[til] = temp;

        return i+1;
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

    //Oppgave 6
    public static int gcd(int a, int b) { // Metode for a finne storste fellesnevner
                                          // (hjelpemetode til oppgave 6)
        if (b == 0) {
            return a;
        }
        else {
            return gcd(b, a % b);
        }

    }

    public static void rotasjon(char[] a, int k) {

        if(a.length < 2 || (k % a.length) == 0) {
            return;
        }

        char temp; //midlertidig lagringvariabel

        if(k > a.length) {
            k = k - a.length;
        }

        if(k > (a.length / 2)) {
            k = k - a.length;
        }

        if(k > 0) {
            int i, j, l;
            int gcd = gcd(k, a.length);

            for (i = 0; i < gcd; i++) {
                temp = a[a.length - 1 - i];
                j = a.length - 1 - i;
                while(true) {
                    l = j - k;
                    if(l < 0) {
                        l = l + a.length;
                    }
                    if(l == a.length - 1 - i) {
                        break;
                    }
                    a[j] = a[l];
                    j = l;
                }
                a[j] = temp;
            }

        }
        else if(k == 0) {

        }
        else {
            k = -(k);

            int i, j, l;
            int g_c_d = gcd(k, a.length);
            for (i = 0; i < g_c_d; i++) {
                temp = a[i];
                j = i;
                while(true) {
                    l = j + k;
                    if(l >= a.length) {
                        l = l - a.length;
                    }
                    if(l == i) {
                        break;
                    }
                    a[j] = a[l];
                    j = l;
                }
                a[j] = temp;
            }
        }

    }

    //Oppgave 7
    //a)
    public static String flett(String s, String t) {
        String[] as = s.split("(?!^)");
        String[] at = t.split("(?!^)");

        String[] temp;
        String result = "";
        if(as.length > at.length) {
            temp = new String[at.length + 1];
            String rest = "";
            System.arraycopy(as, 0, temp, 0, at.length);
            for(int i = at.length; i < as.length; i++) {
                rest += as[i];
            }
            temp[temp.length - 1] = rest;

            for(int i = 0; i < temp.length - 1; i++) {
                result += temp[i] + at[i];
            }
            result += temp[temp.length - 1];
        }
        else {
            temp = new String[as.length + 1];
            String rest = "";
            System.arraycopy(at, 0, temp, 0, as.length);
            for(int i = as.length; i < at.length; i++) {
                rest += at[i];
            }
            temp[temp.length - 1] = rest;

            for(int i = 0; i < temp.length - 1; i++) {
                result += as[i] + temp[i];
            }
            result += temp[temp.length - 1];
        }

        return result;
    }

    //b)
    public static String flett(String... s) {
        String result = "";

        String[] temp;
        int[] tempLen = new int[s.length];
        int maxTemp = 2; // Forutsetter at vi har minst ett element i s som har to
                         // eller flere karakterer
        for( int j = 0; j < maxTemp; j++) {
            for (int i = 0; i < s.length; i++) {
                temp = s[i].split("(?!^)");
                tempLen[i] = temp.length;
                if (i > 0) {
                    if (temp.length > maxTemp) {
                        maxTemp = temp.length;
                    }
                }
                int teller = i;
                i = j;

                if (i >= temp.length) {
                    result += "";
                }
                else {
                    result += temp[i];
                }
                i = teller;
            }
        }

        return result;
    }
    // Oppgave 8
    public static int[] indekssortering(int[] a) {
        if(a.length == 0){
            return a;
        }
        if(a.length == 1){
            int [] indeksarray = {0};
            return indeksarray;
        }
        int [] x = a.clone(); // Kloner a
        quickSort(x, 0, x.length-1); // Bruker quicksort til a sortere den klonede arrayet
        int [] indeksarray = new int[a.length];
        int indeks = 0;

        for (int i = 0; i < x.length; i++) {
            for(int j = 0; j<a.length; j++){
                if(x[i] == a[j]){
                    indeks = j;
                }
            }
            indeksarray[i] = indeks;
        }
        return indeksarray;
    }
    // Oppgave 9

    public static int [] tredjeMin (int [] a){
        if(a.length < 3){
            throw new NoSuchElementException("Arrayen er for kort.");
        }

        int [] x_indeks;

        int [] x = new int[3];
        System.arraycopy(a, 0, x, 0, 3);
        x_indeks = Oblig1.indekssortering(x);

        int minste_indeks = x_indeks[0];
        int nesteminste_indeks = x_indeks[1];
        int tredjeminste_indeks = x_indeks[2];

        int minste = a[minste_indeks];
        int nesteminste = a[nesteminste_indeks];
        int tredjeminste = a[tredjeminste_indeks];

        if(a.length == 3){
            return x_indeks;
        }

        for(int i = 3; i<a.length; i++){
            if(a[i] < minste){
                tredjeminste = nesteminste;
                tredjeminste_indeks = nesteminste_indeks;
                nesteminste = minste;
                nesteminste_indeks = minste_indeks;
                minste = a[i];
                minste_indeks = i;
            }
            else if(a[i] < nesteminste){
                tredjeminste = nesteminste;
                tredjeminste_indeks = nesteminste_indeks;
                nesteminste = a[i];
                nesteminste_indeks = i;
            }
            else if(a[i] < tredjeminste){
                tredjeminste = a[i];
                tredjeminste_indeks = i;
            }
        }
        x_indeks[0] = minste_indeks;
        x_indeks[1] = nesteminste_indeks;
        x_indeks[2] = tredjeminste_indeks;
        return x_indeks;
    }
    public static int[] randPerm(int n)  // en effektiv versjon
    {
        Random r = new Random();         // en randomgenerator
        int[] a = new int[n];            // en tabell med plass til n tall

        Arrays.setAll(a, i -> i + 1);    // legger inn tallene 1, 2, . , n

        for (int k = n - 1; k > 0; k--)  // lokke som gar n - 1 ganger
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

    //Oppgave 10
    public static boolean inneholdt(String a, String b) {
        if(a.equals("")) {
            return true;
        }
        if(b.equals("")) {
            return false;
        }

        char[] a_array = a.toCharArray();
        char[] b_array = b.toCharArray();

        int[] alfabet = new int[29];

        for(int i = 0; i < alfabet.length; i++) {
            alfabet[i] = 0;
        }

        int indeks;

        for (char c : a_array) {
            indeks = bokstavNr(c);
            alfabet[indeks]++;
        }

        for (char c : b_array) {
            indeks = bokstavNr(c);
            if (alfabet[indeks] > 0) { // sjekker om bokstaven er i a-arrayet
                alfabet[indeks]--;
            }
        }

        int sum = 0;

        for (int value : alfabet) {
            sum += value;
        }

        // Summen bli 0 hvis alle bokstaver i a-arrayet var inneholdt i b-arrayet
        return sum == 0; // returnerer true om summen er lik 0, eller returneres false

    }

    private static int bokstavNr(char bokstav) {
        switch (bokstav) {
            case 'A':
                return 0;
            case 'B':
                return 1;
            case 'C':
                return 2;
            case 'D':
                return 3;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 6;
            case 'H':
                return 7;
            case 'I':
                return 8;
            case 'J':
                return 9;
            case 'K':
                return 10;
            case 'L':
                return 11;
            case 'M':
                return 12;
            case 'N':
                return 13;
            case 'O':
                return 14;
            case 'P':
                return 15;
            case 'Q':
                return 16;
            case 'R':
                return 17;
            case 'S':
                return 18;
            case 'T':
                return 19;
            case 'U':
                return 20;
            case 'V':
                return 21;
            case 'W':
                return 22;
            case 'X':
                return 23;
            case 'Y':
                return 24;
            case 'Z':
                return 25;
            case 'Æ':
                return 26;
            case 'Ø':
                return 27;
            case 'Å':
                return 28;
            default:
                return -1;
        }
    }
}
