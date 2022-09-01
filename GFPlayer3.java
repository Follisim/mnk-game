package mnkgame;

import java.util.HashSet;
import java.util.LinkedList;


public class GFPlayer3 implements MNKPlayer{

    private int N;
    private int M;
    private Board B;
    private MNKCellState Me;
    private MNKCellState Enemy;
    private LinkedList<MNKCell> L;

    private double  TIMEOUT;
    private boolean first;

    


public GFPlayer3(){}

//M numero righe
//N numero colonne 
// c.i =riga i
// c.j = colonna j 

public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {

    TIMEOUT = timeout_in_secs;
    this.first = first;
    Me = first ? MNKCellState.P1 : MNKCellState.P2;
    Enemy = first ? MNKCellState.P2 : MNKCellState.P1;

    this.B = new Board(M, N, K, first); // creo una board per memorizzare le mosse // board ha alcuni metodi che ci aiutano nel semplificare il codice 
    L = new LinkedList<MNKCell>(); // creo una lista di mosse di riserva
    shortcutMoves(L,M,N,K-1);// per affiancare l'alphaBeta nel caso non trovasse una mossa abbastanza soddisfacente 
    // System.out.println(L);

    this.M = M;
    this.N = N;


}

public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {

    long start = System.currentTimeMillis(); 

    if (MC.length > 0) {//ci troviamo nel corso della partita
        Boolean t = first;
        //System.out.println("lengt"+MC.length);
        B.freeMapp();  //pulisco la mappa per evitare errori 
        for (int w = 0; w < MC.length; w++) {
            B.markCell(MC[w].i, MC[w].j, t,t); // copy oll moves in B   
            t = !t;
            // System.out.println(MC[w]);
        }
        
        //B.printMap();
    } else {// prima mossa della partita viene messa in un posto gia progrmmato, perche la prima mossa è anche la piu costosa computazionalmente 
          return L.poll(); 
    }

    // last move
    if (FC.length == 1)
        return FC[0];


    // HashSet celle libere 
    HashSet<MNKCell> H = new HashSet<MNKCell>((int) Math.ceil((FC.length) / 0.75) );
    for (int i = 0; i < FC.length; i++){
        H.add(FC[i]);
        if (B.isWinningCell(FC[i].i, FC[i].j, Me)){// se trovo una cella che mi fa vincere ritorno quella cella
            return FC[i];
        }
            
    }
    for (int i = 0; i < FC.length; i++) {
        if (B.isWinningCell(FC[i].i, FC[i].j, Enemy)){// se trovo una cella che fa vincere in nemico in una mossa ritorno quella cella 
            return FC[i];
        }
    }

    
    CellaValore finalCell = new CellaValore();//coppia di valori [cella , valore], per assegnare ad ogni cella un punteggio
    finalCell.cell = FC[0];
    finalCell.val = 0;

    int depth = 1; // calcolo brutale della profondita ricavato da alcuni test  
    if(M==3)
        depth =10;
    else if(M==4)
        depth = 5;
    else if(M==5)
        depth = 3; 
    else if(M==6)
        depth = 2;
    else if(M==7)
        depth = 2;
    else if(M==8)
        depth = 2;
    else if(M==70)
        depth = 0;


    

// ciclo di decisione della finalCell 
    for(int i=0; i < FC.length; i++){
        
        if(checkTime(start)){
            //System.out.println("no mor time");
            return getShortcutMove();
        }
        B.markCell(FC[i].i, FC[i].j, true);

        double e = AlphaBeta(H, B,false, -100, 100,depth);

        B.freeCell(FC[i].i, FC[i].j);

        CellaValore h = new CellaValore();
        h.cell = FC[i];
        h.val = e;
        
        if(h.val< 0){
            h.val += 0.1;
        }
        if(finalCell.val < h.val)
            finalCell = h;
       
    }
    //System.out.println(finalCell.val);
    if(finalCell.val < 0.9)
        return getShortcutMove();
    
    //System.out.println("finito");    
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




// alphaBeta
private double AlphaBeta(HashSet<MNKCell> H,Board B, boolean t, double Alpha, double Beta, int depth) {
    
    double ev;
    double t_ev;
    if (H.size() == 0 || depth == 0) {
        return evaluate(B) ;
    }

    if (t) { //  massimizza
       

        ev = -100;
        t_ev = -100;
        MNKCell[] cells = H.toArray(new MNKCell[H.size()]);// copio Hin un array

        for (int i = 0; i < cells.length ; i++) {// visita a tutti i figli del nodo
            MNKCell c = cells[i];
            B.markCell(c.i, c.j, true);
            if(B.MyWin){ // trovo una foglia interompo il ciclo 
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
        MNKCell[] cells = H.toArray(new MNKCell[H.size()]);// copio Hin un array
        
        for (int i = 0; i < cells.length; i++){//visita a tutti i figli del nodo
            MNKCell c = cells[i];
            B.markCell(c.i, c.j, false);
            if (B.EnemyWin){ //foglia 
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

public  static double evaluate(Board B) {// valutazione euristica
return B.evaluate();
}



private void shortcutMoves(LinkedList <MNKCell> L,int M, int N, int k){
    MNKCell c;
    c = new MNKCell(1,1);
    L.add(c);

    for (int i = 1; i < k-1 && c.i + i < M && c.j + i < N; i++) {// obb d,r
        MNKCell tmp = new MNKCell(c.i + i, c.j + i);
        L.add(tmp);
    }
    for(int i = 1; i < k-1 && c.i+i < M; i++){//ver, r
        MNKCell tmp = new MNKCell(c.i+i, c.j);
        L.add(tmp);
    }
    for (int i = 1; i < k-1 && c.j+i < N; i++) {// or, d
        MNKCell tmp = new MNKCell(c.i + i, c.j + i);
        L.add(tmp);
    }
}

private MNKCell getShortcutMove() {
    if(!L.isEmpty()){//se non ho piu mosse buone e non ho piu tempo, scelgo una mossa casuale
        MNKCell c = L.poll();

        while (B.getCell(c.i, c.j) != MNKCellState.FREE ){
            if(!L.isEmpty())
                c = L.poll();
            else 
                return B.randCell();
        }
        return c;
    }else{
        return B.randCell();
    }
}

private boolean checkTime (long start){//controllo base del tempo rimasto
    if((TIMEOUT-0.5) <= System.currentTimeMillis()- start )
        return true;
    else
        return false;
}



}
