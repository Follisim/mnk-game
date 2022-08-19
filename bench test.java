package mnkgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class prove implements MNKPlayer{
    private int N;
    private int M;
    private int K;
    private MNKCellState Me;
    private MNKCellState Enemy;
    

    private int TIMEOUT;
    private boolean first;
    
public prove(){}

public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {

    TIMEOUT = timeout_in_secs;
    this.first = first;
    if (first) {
        Me = MNKCellState.P1;
        Enemy = MNKCellState.P2;
    } else {
        Me = MNKCellState.P2;
        Enemy = MNKCellState.P1;
    }

    this.M = M;
    this.N = N;
    this.K = K;

}

public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
    Board B = new Board(M, N, K);
    if (MC.length > 0) {
        Boolean t = first;
        for (int w = 0; w < MC.length; w++) {
            B.markCell(MC[w].i, MC[w].j, t); // copy oll moves in B
            t = !t;

        }
    } else {// first move
        // fast selection
    }

    if (FC.length == 1)
        return FC[0];

    
    // faccio una Queue
    HashSet<MNKCell> H = new HashSet<MNKCell>();
    for (int i = 0; i < FC.length; i++){
        H.add(FC[i]);
    }

    
     
    for(MNKCell c :H){
    
    //     H.remove(c);
   // c = new MNKCell(c.i, c.j, Me);
    //B.markCell(c.i, c.j, true);
    //stampa(H,c);  

   

    System.out.println("H   " + H.size());    
    System.out.println(c.i + "   " + c.j);
    }

    return FC[0];
}


public void stampa(HashSet<MNKCell> H,MNKCell c){
    stampa(H, H.iterator().next());
        System.out.println(c.i+"   " + c.j);
        H.remove(c);
    }





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



    public Queue<MNKCell> fromArrayToQueue(MNKCell FC[]) {
        Queue<MNKCell> Q = new LinkedList<MNKCell>();
        for (int i = 0; i < FC.length; i++) { // al contrario
            Q.add(FC[i]);
        }
        return Q;
    }


    public String playerName() {
        return "GFPlayer2";
    }

}


