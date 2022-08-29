package mnkgame;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;


public class GFPlayer3 implements MNKPlayer{

    private int N;
    private int M;
    private Board B;
    private MNKCellState Me;
    private MNKCellState Enemy;
    private LinkedList<MNKCell> L;

    private int TIMEOUT;
    private boolean first;
    private double Alpha;
    private double Beta;
    


public GFPlayer3(){}

public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {

    TIMEOUT = timeout_in_secs;
    this.first = first;
    Me = first ? MNKCellState.P1 : MNKCellState.P2;
    Enemy = first ? MNKCellState.P2 : MNKCellState.P1;

    this.B = new Board(M, N, K, first);
    L = new LinkedList<MNKCell>();
    shortcutMoves(L,M,N,K-1);
    // System.out.println(L);

    this.M = M;
    this.N = N;


}

public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {


    long start = System.currentTimeMillis(); // gestione del tempo ancora da fare

    
    if (MC.length > 0) {
        Boolean t = first;
        //System.out.println("lengt"+MC.length);
        B.freeMapp();
        for (int w = 0; w < MC.length; w++) {
            B.markCell(MC[w].i, MC[w].j, t,t); // copy oll moves in B  // t = false -> me= P2  Enemy= P1   
            t = !t;
            // System.out.println(MC[w]);
        }
        
        //B.printMap();
    } else {// mossa al centro della tabella
          return L.poll(); 
    }

    // last move
    if (FC.length == 1)
        return FC[0];

    
    


  
    // HashSet celle libere 
    HashSet<MNKCell> H = new HashSet<MNKCell>((int) 21 );
    for (int i = 0; i < FC.length; i++){
        H.add(FC[i]);
        if (B.isWinningCell(FC[i].i, FC[i].j, Me)){
            return FC[i];
        }
            
    }
    for (int i = 0; i < FC.length; i++) {
        if (B.isWinningCell(FC[i].i, FC[i].j, Enemy)){
            return FC[i];
        }
    }

    // occupate HashSet
    HashSet<MNKCell> Z = new HashSet<MNKCell>((int) 21);
        for (int i = 0; i < MC.length; i++) {
        Z.add(MC[i]);
    }
    
    CellaValore finalCell = new CellaValore();
    finalCell.cell = FC[1];
    finalCell.val = 0;
    
    int depth = 4;  //da calcolare


    for(int i=0; i < FC.length; i++){

       // System.out.println("ciclo for ");
        B.markCell(FC[i].i, FC[i].j, true);

        double e = AlphaBeta(H, B,false, -100, 100,depth);

        B.freeCell(FC[i].i, FC[i].j);

        CellaValore h = new CellaValore();
        //System.out.println(e);
        h.cell = FC[i];
        h.val = e;
        if(h.val< 0){
            h.val= -(h.val+ 0.1);
        }
        if(finalCell.val < h.val)
            finalCell = h;
       
    }
    //System.out.println(finalCell.val);
    if(finalCell.val == 0){
        MNKCell c = L.poll();
        while(B.getCell(c.i, c.j) != MNKCellState.FREE)
            c = L.poll();
        return c;
    }
    //System.out.println("valore cella finale "+finalCell.val);
    return finalCell.cell;
}

public String playerName() {
    return "GFPlayer2";
}



/*---------------------------------------------------------------------------------------------------- */

private class CellaValore {
    double val;
    MNKCell cell;
}



// lo trasformiamo in coda con metodo fromArrayToQueue, e lanciamo alphabeta

private double AlphaBeta(HashSet<MNKCell> H,Board B, boolean t, double Alpha, double Beta, int depth) {
    
    double ev;
    double t_ev;
    if (H.size() == 0 || depth == 0) {
        return evaluate(B) ;
    }

    if (t) { //  massimizza
       

        ev = -100;
        t_ev = -100;
        MNKCell[] cells = H.toArray(new MNKCell[H.size()]);

        for (int i = 0; i < cells.length ; i++) {
            MNKCell c = cells[i];
            B.markCell(c.i, c.j, true);
            if(B.MyWin){ // trovo una foglia interrompo il ciclo 
                t_ev = Math.max(t_ev, depth);
                break;
            }
           

            H.remove(c);

            ev = Math.max(ev, AlphaBeta(H, B, false, Alpha, Beta, depth - 1));
            
            t_ev= Math.max(t_ev, ev);
            Alpha = Math.max(Alpha, t_ev);
            B.freeCell(c.i, c.j);
            H.add(c);
            if (Beta <= Alpha)
                break;


        }
    } else {
        
        ev = 100;
        t_ev = 100;
        MNKCell[] cells = H.toArray(new MNKCell[H.size()]);

        for (int i = 0; i < cells.length; i++) {
            MNKCell c = cells[i];
            B.markCell(c.i, c.j, false);
            if (B.EnemyWin){ 
                t_ev = Math.min(t_ev, -(depth));
                break;
            }
            H.remove(c);

            ev = Math.min(ev, AlphaBeta(H, B, true, Alpha, Beta, depth - 1));
            t_ev = Math.min(t_ev, ev);
            Beta = Math.min(Beta, t_ev);
            B.freeCell(c.i, c.j);
            H.add(c);
            if (Beta <= Alpha)
                break;
        }
    }
    return t_ev;
}

public double evaluate(Board B) {// valutazione euristica
    if (B.MyWin) {
        return 1;
    } else if (B.EnemyWin) {
        return -1;
    } else
        return 0;

}



private void shortcutMoves(LinkedList <MNKCell> L,int M, int N, int k){
    MNKCell c;
    if(M < 4 && N < 4 )
        c = new MNKCell(2, (int) Math.ceil(N / 2));
    else
        c = new MNKCell(1,1);
    L.add(c);

    for (int i = 1; i < k && c.i + i < M && c.j + i < N; i++) {// obb d,r
        MNKCell tmp = new MNKCell(c.i + i, c.j + i);
        L.add(tmp);
    }
    for(int i=1; i<k && c.i+i < M; i++){//oriz, r
        MNKCell tmp = new MNKCell(c.i+i, c.j);
        L.add(tmp);
    }
    for (int i = 1; i < k && c.j+i < N; i++) {// vert, d
        MNKCell tmp = new MNKCell(c.i + i, c.j + i);
        L.add(tmp);
    }
}

}
