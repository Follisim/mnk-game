package mnkgame;

public class Board {
    private int N;
    private int M;
    private int K;
    private MNKCellState Me;
    private MNKCellState Enemy;

    public int i;
    public int j;
    public boolean MyWin;
    public boolean EnemyWin;

    public MNKCellState[][] B;

    public Board(int M, int N, int K ,boolean F){
        this.N = N;
        this.M = M;
        this.K = K;
        Me = F ? MNKCellState.P1 : MNKCellState.P2;
        Enemy = F ? MNKCellState.P2 : MNKCellState.P1;
        MyWin = false;
        EnemyWin = false;
        B = new MNKCellState[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j] = MNKCellState.FREE;
            }
        }
    }


    public void markCell(int i, int j, boolean p) {
        if (p){
            B[i][j] = MNKCellState.P1;
            MyWin = isWinningCell(i, j, Me);
        }else{
            B[i][j] = MNKCellState.P2;
            EnemyWin = isWinningCell(i, j, Enemy);
        }
            

        
    }

    public void freeCell(int i, int j){
        B[i][j] = MNKCellState.FREE;
    }



    public MNKCellState getCell(int i, int j) {// return lo stato della cella
        return B[i][j];
    }



  public boolean isWinningCell(int i, int j, MNKCellState s) {
		
	int n;
	if (s == MNKCellState.FREE)
		return false;

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


    // Anti-diagonale
    n=1;
    for(int k = 1;i-k>=0 && j+k<N && B[i-k][j+k]==s; k++)
        n++;
    for(int k = 1; i+k<N && j-k>=0 && B[i+k][j-k]==s; k++)
        n++;
    if(n>=K)
        return true;
  
  
    return false;
  }


}
