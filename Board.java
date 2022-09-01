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

    
    
    public boolean MyWin;
    public boolean EnemyWin;

    private MNKCellState[][] B;
    private LinkedList<MNKCell> myMove;

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



    public void markCell(int i, int j, boolean p,boolean s) {
        
        if (p){ 
            B[i][j] = Me;
            MNKCell tmp = new MNKCell(i, j);
            myMove.add(tmp);
        } else 
            B[i][j] = Enemy;
    }


    public void freeCell(int i, int j){
        B[i][j] = MNKCellState.FREE;
        MNKCell tmp = new MNKCell(i, j);
        myMove.remove(tmp);
    }

    public void freeMapp(){
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j]= MNKCellState.FREE;
            }
        }
        cleanList(myMove);
    }



    public MNKCellState getCell(int i, int j) {// return lo stato della cella
        return B[i][j];
    }

    public MNKCell randCell(){
        int i =0, j=0;
        do{
            i = rand.nextInt(M);
            j = rand.nextInt(N);
        }while(getCell(i, j)!= MNKCellState.FREE);
        MNKCell c = new MNKCell(i, j);
        return c;

    }

    private void cleanList(LinkedList<MNKCell> L){
        while(!L.isEmpty()){
            L.remove();
        }

    }


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

  public double evaluate(){
    double ev=0;
    MNKCell[] c = myMove.toArray(new MNKCell[myMove.size()]); 
    for(int k=0; k<c.length;k++){
        ev += evalMuve(c[k].i, c[k].j);
    }
    return ev;
  }


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
