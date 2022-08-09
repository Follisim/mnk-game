package mnkgame;

import java.util.Random;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.LinkedList;

public class GFPlayer2 implements MNKPlayer {
    
    private Random rand;
    private MNKBoard B;
    private MNKGameState myWin;
    private MNKGameState yourWin;
    private int TIMEOUT;


    public GFPlayer2() {
    }

    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
        // New random seed for each game
        rand = new Random(System.currentTimeMillis());
        B = new MNKBoard(M, N, K);
        myWin = first ? MNKGameState.WINP1 : MNKGameState.WINP2;
        yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;
        TIMEOUT = timeout_in_secs;
    }


    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        long start = System.currentTimeMillis();
        if (MC.length > 0) {
            MNKCell c = MC[MC.length - 1]; // Recover the last move from MC
            B.markCell(c.i, c.j); // Save the last move in the local MNKBoard
        }
       
        if (FC.length == 1)
            return FC[0];

     //q.poll() toglie primo el in coda
    //q.peek() vedo elemento dopo senza toglierlo dallla coda
    //q.size() ritorna dimensione coda  
    //q.contains(elem) ritorna true se presente, false altrimenti
    //q.toArray()[1] ritorna elemento in pos 1 (converte in array)
    // Java program to implement sorting a
    // queue data structure



     //sort
     mergeSort(FC);
     return FC[0];

        //riempimento coda con posizioni di celle libere
        
        //Queue Q = new LinkedList<MNKCell>(); 
        //    for(int j=0; j<FC.length; j++){
        //        Q.add(FC[j]);        
        //    }

      //  MNKCell c = Q.poll();
        


      //  B.markCell(c.i, c.j); 
  
        
    }
    

    // ordiniamo FC
    public static void mergeSort(MNKCell A[]) {
        mergeSortRec(A, 0, A.length - 1);
    }

    private static void mergeSortRec(MNKCell A[], int i, int f) {
        if (i >= f)
            return;
        int m = (i + f) / 2;
        mergeSortRec(A, i, m);
        mergeSortRec(A, m + 1, f);
        merge(A, i, m, f);
    }

    private static void merge(MNKCell A[], int i1, int f1, int f2) { // i1 = valore piu basso, f1= valore di mezzo, f2 = valore piu alto 
        MNKCell[] X = new MNKCell[f2 - i1 + 1]; 
        int i = 0, i2 = f1 + 1, k = i1;
        while (i1 <= f1 && i2 <= f2) { // f1 -> low   f2 -> high
            if (A[i1].i < A[i2].i){// i-> righe 
                X[i++] = A[i1++];
            }else if (A[i1].i > A[i2].i){
                X[i++] = A[i2++];
            }else {
                if (A[i1].j < A[i2].j){
                    X[i++] = A[i1++];
                }else{
                    X[i++]=A[i2++];
                }

            }

        }
        if (i1 <= f1)
            for (int j = i1; j <= f1; j++, i++)
                X[i] = A[j];
        else
            for (int j = i2; j <= f2; j++, i++)
                X[i] = A[j];
        for (int t = 0; k <= f2; k++, t++)
            A[k] = X[t];

    }


    /*-----------------------------------------------------------------
    // HeapSort

    public static void heapSort(MNKCell S[]) {

        heapify(S, S.length - 1, 1);
        for (int c = (S.length - 1); c > 0; c--) {
            int k = findMax(S);
            deleteMax(S, c);
            S[c] = k;

        }
    }

    private static void heapify(MNKCell S[], int n, int i) {
        if (i > n)
            return;
        heapify(S, n, 2 * i); // crea heap radicato in S[2*i]
        heapify(S, n, 2 * i + 1); // crea heap radicato in S[2*i+1]
        fixHeap(S, n, i);
    }

    private static void fixHeap(MNKCell S[], int c, int i) {
        int max = 2 * i; // figlio sinistro
        if (2 * i > c)
            return;
        if (2 * i + 1 <= c && S[2 * i] < S[2 * i + 1])
            max = 2 * i + 1; // figlio destro
        if (S[i] < S[max]) {
            int temp = S[max];
            S[max] = S[i];
            S[i] = temp;
            fixHeap(S, c, max);
        }
    }

    private static MNKCell findMax(MNKCell S[]) {
        return S[1];
    }

    private static MNKCell deleteMax(MNKCell S[], int c) {
        S[1] = S[c];
        fixHeap(S, c, 1);
    }
*/

    public String playerName() {
        return "R4nd0m++";
    }
}
    


