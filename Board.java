package mnkgame;

public class Board {
    private int N;
    private int M;
    private int K;

    public int i;
    public int j;

    public MNKCellState[][] B;

    public Board(int M, int N, int K ){
        this.N = N;
        this.M = M;
        this.K = K;
        B = new MNKCellState[M][N];
                for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j] = MNKCellState.FREE;
            }
        }
    }


    public Board(int i, int j, int M, int N, int K, MNKCellState s) {
        this.i = i;
        this.j = j;
        B[i][j] = s;

    }

    public void markCell(int i, int j, boolean p) {
        if (p)
            B[i][j] = MNKCellState.P1;
        else
            B[i][j] = MNKCellState.P2;
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
