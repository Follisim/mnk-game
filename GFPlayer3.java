package mnkgame;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;


public class GFPlayer3 implements MNKPlayer{

    private int N;
    private int M;
    private int K;
    private MNKCellState Me;
    private MNKCellState Enemy;

    private int TIMEOUT;
    private boolean first;
    private int Alpha;
    private int Beta;
    private int MAX_DEPTH = 10;


public GFPlayer3(){}

public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {

    TIMEOUT = timeout_in_secs;
    this.first = first;
    Me = first ? MNKCellState.P1 : MNKCellState.P2;
    Enemy = first ? MNKCellState.P2 : MNKCellState.P1;

    this.M = M;
    this.N = N;
    this.K = K;

}

public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
    long start = System.currentTimeMillis(); // gestione del tempo ancora da fare

    Board B = new Board(M, N, K); // tabella allo stato attuale 
    if (MC.length > 0) {
        Boolean t = first;
        for (int w = 0; w < MC.length; w++) {
            B.markCell(MC[w].i, MC[w].j, t); // copy oll moves in B
            t = !t;

        }
    } else {// first move
        // fast selection
    }

    // last move
    if (FC.length == 1)
        return FC[0];


  
    //  libere HashSet
    HashSet<MNKCell> H = new HashSet<MNKCell>((int) 21 );
    for (int i = 0; i < FC.length; i++){
        H.add(FC[i]);
        if (B.isWinningCell(FC[i].i, FC[i].j, Me))
                return FC[i];
    }

    // occupate HashSet
    HashSet<MNKCell> Z = new HashSet<MNKCell>((int) 21);
    for (int i = 0; i < MC.length; i++) {
        Z.add(MC[i]);
    }
    double tmp;
    for(MNKCell cell : H){

        B.markCell(cell.i, cell.j, true);
        double e = AlphaBeta(H, B,false, -10, 10, 0);
        B.freeCell(cell.i, cell.j);

        e = Math.max(tmp, e); 
        tmp = e; 
    }

    return 
}

public String playerName() {
    return "GFPlayer2";
}



/*---------------------------------------------------------------------------------------------------- */



public int evaluate(Board B, MNKCell c) {

    if (B.isWinningCell(c.i, c.j, Me)) {

        return 1;
    } else if (B.isWinningCell(c.i, c.j, Enemy)) {

        return -1;
    } else

        return 0;

}


// lo trasformiamo in coda con metodo fromArrayToQueue, e lanciamo alphabeta

public int AlphaBeta(HashSet<MNKCell> H,Board B, boolean t, int Alpha, int Beta, int depth) {
    // System.out.println(Q.size());
    int ev;
    int t_ev;
    if (H.size() == 1 || depth == MAX_DEPTH) {
        return valutazione euristica ;
    }

    if (t) { // P1 massimizza
       // se ho vinto ritorno + 100/depth

        ev = -100;
        t_ev = -100;
        MNKCell[] cells = H.toArray(new MNKCell[H.size()]);

        for (int i = 0; i < cells.length ; i++) {
            MNKCell c = cells[i];
            B.markCell(c.i, c.j, true);
            H.remove(c);

            ev = Math.max(ev, AlphaBeta(H, B, false, Alpha, Beta, depth + 1));
            t_ev= Math.max(t_ev, ev);
            Alpha = Math.max(Alpha, t_ev);
            B.freeCell(c.i, c.j);
            H.add(c);
            if (Beta <= Alpha)
                break;

        // il risultatp deve essere diviso per un fattore profondita 
        //per favorire i cammini brevi 
        // es. lose = 100/depth
        }
    } else {
        
        ev = 100;
        t_ev = 100;
        MNKCell[] cells = H.toArray(new MNKCell[H.size()]);

        for (int i = 0; i < cells.length; i++) {
            MNKCell c = cells[i];
            B.markCell(c.i, c.j, false);
            H.remove(c);

            ev = Math.min(ev, AlphaBeta(H, B, true, Alpha, Beta, depth + 1));
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

}