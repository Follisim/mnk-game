package mnkgame;

import java.util.Random;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.*;


public class GFPlayer2 implements MNKPlayer {

    private int N;
    private int M;
    private int K;
    private MNKCellState Me;
    private MNKCellState Enemy;
    private Random rand;
    

    private int TIMEOUT;
    private boolean first;
    private int Alpha;
    private int Beta;
    
    public class Cella_Valore{
        int valEu;//valutazione euristica cella
        MNKCell cella;
    }


    public GFPlayer2() {
    }

    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
  
        
        TIMEOUT = timeout_in_secs;
        this.first = first ;
        if(first){
            Me = MNKCellState.P1;
            Enemy = MNKCellState.P2; 
        }else{
            Me = MNKCellState.P2;
            Enemy = MNKCellState.P1;
        }

        this.M = M;
        this.N = N; 
        this.K = K;
        
    }

  
    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        long start = System.currentTimeMillis(); //gestione del tempo ancora da fare 
       
        Board B = new Board(M, N, K);
        if (MC.length > 0) {
            Boolean t = first;
            for(int i=0; i<MC.length; i++){
                for(int j=0; j< MC.length; j++ ){
                    B.markCell(i, j, t); //copy oll moves in B
                    t = !t;
                }
            }
        } else {// first move 
           // fast selection 
        }
 
        if (FC.length == 1)
            return FC[0];



    //sort
    mergeSort(FC);
    // faccio una Queue 
    Queue<MNKCell> Q = new LinkedList<MNKCell>();
    Q = fromArrayToQueue(FC);


    Cella_Valore finalCel = new Cella_Valore();
    finalCel.cella = FC[0];
    finalCel.valEu = 0;
    
    while (!Q.isEmpty()){
        MNKCell d = (MNKCell) Q.poll();
        Cella_Valore h = new Cella_Valore();
        Board B1 = new Board(M, N, K);
        B1 = B;
        System.out.println("nuovo ciclo wile ");
        
        int eval = AlphaBeta(B1, Q, first, -10, 10, 5 );

        System.out.println("AlphaBeta exit");

        h.cella = d;
        h.valEu = eval;
   
        if(finalCel.valEu < h.valEu){
            finalCel = h;
            
        }
         
    }
     
    System.out.println("return decisione finale");
        return finalCel.cella;     
    }
  
    public Queue<MNKCell> fromArrayToQueue(MNKCell FC[]){
        Queue<MNKCell> Q = new LinkedList<MNKCell>();
        for(int i=0; i<FC.length; i++){ //al contrario
            Q.add(FC[i]);
        }
        return Q;
    }

    public int evaluate(Board B, Queue<MNKCell> Q) {
        System.out.println("evaluate");
        //MNKCell c = FC;
        MNKCell c = Q.poll();
        //MNKCell C[] = Q.toArray();
        //MNKCell c= C[0];
       
        
        if (B.isWinningCell(c.i, c.j, Me)){
            System.out.println("myWin");
            return 1;
        }else if (B.isWinningCell(c.i, c.j, Enemy)){
            System.out.println("yorWin");
            return -1;
        }else
            System.out.println("ret 0");
            return 0;
        
    }

    //in selectCell prendiamo array FC[], lo ordiniamo con MergeSort,
    //lo trasformiamo in coda con metodo fromArrayToQueue, e lanciamo alphabeta
    


    public int AlphaBeta (Board B ,Queue <MNKCell> Q, boolean first, int Alpha, int Beta, int depth){
        System.out.println("AlphaBeta");
        int eval;
      if(Q.size()== 1 || depth==0){
        return evaluate(B, Q);     
      } 
      
      if(first ) {  //P1 massimizza
            eval = -10;
            for (int i=0; i < Q.size();  i++ ){
                MNKCell c = Q.poll();
                B.markCell(c.i,c.j,true);
                eval = Math.max (eval, AlphaBeta(B, Q, false, Alpha, Beta, depth -1 ));
                Alpha = Math.max(Alpha, eval); 
                if (Beta <= Alpha)
                    break;
            }
        } else { 
            eval = 10;
            for (int i=0;i < Q.size(); i++ ){
                MNKCell c = Q.poll();
                B.markCell(c.i,c.j,false);
                eval = Math.min (eval, AlphaBeta(B, Q, true, Alpha, Beta, depth -1 ));
                Beta = Math.min(Beta, eval);
                if (Beta <= Alpha) 
                    break;
            }
        }
    return eval;
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

    public String playerName() {
        return "GFPlayer2";
    }
}


//sistamare alphaBeta con la valutazione base win/lose 
//gestire il tempo 
//valutazione euristica tabelle 
