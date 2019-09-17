import java.util.Arrays;

class Test
{
    public static int indekssortering(int a[], int t)
    {

        // if array is Null
        if (a == null) {
            return -1;
        }


        // traverse in the array
        for(int i=0; i<a.length; i++){

            // if the i-th element is t
            // then return the index
            if (a[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    // Driver Code
    public static void main(String[] args)
    {
        int[] my_array = { 5, 4, 6, 1, 3, 2, 7, 8, 9 };

        // find the index of 5
        System.out.println("Index position of 5 is: "
                + indekssortering(my_array, 5));

        // find the index of 7
        System.out.println("Index position of 7 is: "
                + indekssortering(my_array, 7));
    }
}