/*
 * Board viene usata in GFPlayer per tenere traccia delle messo precedenti
 * e semplifica i calcoli euristici che vengono fatti direttamente 
 * all'interno della struttura.
 */

package mnkgame;

import java.util.LinkedList;
import java.util.Random;

public class Board {
    private int N;
    private int M;
    private int K;
    private Random rand;
    private MNKCellState Me;
    private MNKCellState Enemy;
    private MNKCellState[][] B;
    private LinkedList<MNKCell> myMove;
    
    
    public boolean MyWin;
    public boolean EnemyWin;



    public Board(int M, int N, int K ,boolean F){
        this.N = N;
        this.M = M;
        this.K = K;
        rand = new Random(System.currentTimeMillis());
        Me = F ? MNKCellState.P1 : MNKCellState.P2;
        Enemy = F ? MNKCellState.P2 : MNKCellState.P1;
        MyWin = false;
        EnemyWin = false;
        B = new MNKCellState[M][N];
        myMove = new LinkedList<MNKCell>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j] = MNKCellState.FREE;
            }
        }
    }

    //segna la mossa nella matrice B, controlla se qualcuno vince, aggiunge alla lista le nostre mosse 
    // costo : è uguale al costo di isWinningCell O(K)
    public void markCell(int i, int j, boolean p) {
        if (p){
            B[i][j] = Me;
            MyWin = isWinningCell(i, j, Me);
            MNKCell tmp = new MNKCell(i, j);
            myMove.add(tmp);
        }else{
            B[i][j] = Enemy;
            EnemyWin = isWinningCell(i, j, Enemy);
        }
            

        
    }


    // segna le mosse nella matrice B, senza controllare se qualcuno vince, costo caso pessimo O(1)
    public void markCell(int i, int j, boolean p,boolean s) {
        
        if (p){ 
            B[i][j] = Me;
            MNKCell tmp = new MNKCell(i, j);
            myMove.add(tmp);
        } else 
            B[i][j] = Enemy;
    }

    // libera una cella  costo O(K)
    public void freeCell(int i, int j){
        B[i][j] = MNKCellState.FREE;
        MNKCell tmp = new MNKCell(i, j);
        myMove.remove(tmp); // costo O(K) perche la lista è lunga K
    }

    //mette tutta la mappa a free   costo O(N*M)
    public void freeMapp(){
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j]= MNKCellState.FREE;
            }
        }
        cleanList(myMove);
    }


    // return lo stato della cella, costo O(1)
    public MNKCellState getCell(int i, int j) {
        return B[i][j];
    }

    //sceglie una cella a caso, costo O(N*M)
    public MNKCell randCell(){
        int i =0, j=0;
        do{
            i = rand.nextInt(M);
            j = rand.nextInt(N);
        }while(getCell(i, j)!= MNKCellState.FREE);
        MNKCell c = new MNKCell(i, j);
        return c;
    }

    //rimuovi tutti gli elementi nella lista, costo O(k)  K-> lunghezza delle caselle da allineare  
    private void cleanList(LinkedList<MNKCell> L){
        while(!L.isEmpty()){
            L.remove(); //O(1)
        }
    }

    // disegna la mappa (usata a capire come evolve la mappa all'interno dell'alpha beta), non viene usata 
    public void printMap(){
        for(int i=0;i<M;i++){

            for(int j=0;j<M;j++){
                System.out.print("|"+B[i][j]);
            }
            System.out.println("|");
        }
        System.out.println(" ");
        System.out.println(" ");
    }

    //controlla se MNKCellState s vince con la mossa in i,j costo O(K)
    public boolean isWinningCell(int i, int j, MNKCellState s) {
		
	int n;

	// orizzontale
	n = 1;
	for (int k = 1 ; j - k >= 0 && B[i][j - k] == s; k++)
		n++; 
	for (int k = 1; j + k < N && B[i][j + k] == s; k++)
		n++; 
	if (n >= K)
		return true;
		

    // verticale
    n=1;
    for(int k = 1; i-k>=0 && B[i-k][j]==s; k++)
        n++;
    for(int k = 1; i+k<M && B[i+k][j]==s; k++)
        n++;
    if(n>=K)
        return true;
    

    // diagonale
    n=1;
    for(int k = 1; i-k>=0 && j-k>=0 && B[i-k][j-k]==s; k++)
        n++;
    for(int k = 1; i+k<M && j+k<N && B[i+k][j+k]==s; k++)
        n++;
    if(n>=K)
        return true;


    // diagonale 2
    n=1;
    for(int k = 1;i-k>=0 && j+k<N && B[i-k][j+k]==s; k++)
        n++;
    for(int k = 1; i+k<M && j-k>=0 && B[i+k][j-k]==s; k++)
        n++;
    if(n>=K)
        return true;
  
  
    return false;
    }

    //valutazione euristica  costo: ripetiamo c.length volte O(K) quindi O(K^2)
    public double evaluate(){
        double ev=0;
        MNKCell[] c = myMove.toArray(new MNKCell[myMove.size()]); 
        for(int k=0; k<c.length;k++){
            ev += evalMuve(c[k].i, c[k].j);
        }
        return ev;
    }

    // valutazione èuristica di una mossa costo O(K)
    private double evalMuve(int i, int j) {
    double c = 0;
    double n = 0;

      // Diagonale
    n = 1;
    for (int k = 1; i - k >= 0 && j - k >= 0 && B[i - k][j - k] != Enemy; k++){
        if(B[i - k][j - k]== Me){
            n++;
        }else{
            n += 0.5;
            break;
        }
    }
        
    for (int k = 1; i + k < M && j + k < N && (B[i + k][j + k] != Enemy); k++){
        if(B[i + k][j + k]== Me){
            n++;
        }else{
            n += 0.5;
            break;
        }
    }
    c += (double) (n / (K - 1));
      

      // diagonale 2
    n = 1;
    for (int k = 1; i - k >= 0 && j + k < N && (B[i - k][j + k] != Enemy); k++){
        if(B[i - k][j + k]== Me){
            n++;
        }else{
            n += 0.5;
            break;
        }
    }
    for (int k = 1; i + k < M && j - k >= 0 && (B[i + k][j - k] != Enemy); k++){
        if (B[i + k][j - k] == Me) {
            n++;
        } else {
            n += 0.5;
            break;
        }
    }
    c += (double) (n / (K - 1));
 

      // orizzontale
    n = 1;
    for (int k = 1; j - k >= 0 && (B[i][j - k] != Enemy); k++){
        if (B[i][j - k] == Me) {
            n++;
        } else {
            n += 0.5;
            break;
        }
    } 
    for (int k = 1; j + k < N && (B[i][j + k] != Enemy); k++){
        if (B[i][j + k] == Me) {
            n++;
        } else {
            n+= 0.5;
            break;
        }
    }
    c += (double) (n / (K - 1));
      

      // verticale
    n = 1;
    for (int k = 1; i - k >= 0 && (B[i - k][j] != Enemy); k++){
        if (B[i - k][j] == Me) {
            n++;
        } else {
            n +=0.5;
            break;
        }
    } 
    for (int k = 1; i + k < M && (B[i + k][j] != Enemy); k++){
        if (B[i + k][j] == Me) {
            n++;
        } else {
            n += 0.5;
            break;
        }
    }  
    c += (double) (n / (K - 1));
      

      return (c/8);
  }

}
