package mnkgame;

import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;

public class GFPlayer implements MNKPlayer {
    private Random rand;
    private MNKBoard B ;
    private int TIMEOUT ;
    private MNKGameState myWin;
    private MNKGameState yourWin;
    private int M, N, K;
   
   public class Coppia{
        protected int level;
        protected int val;
    }

    public class TreeNode{
        Coppia node;
        TreeNode First, Next;
    }

    //q.poll() toglie primo el in coda
    //q.peek() vedo elemento dopo senza toglierlo dallla coda
    //q.size() ritorna dimensione coda  
    //q.contains(elem) ritorna true se presente, false altrimenti
    //q.toArray()[1] ritorna elemento in pos 1 (converte in array) 

       public int BFS(TreeNode T){
        Queue<E> q = new LinkedList<E>(); 
        if(T != null){
            q.enqueue(q, T);
        } 
        do{
            TreeNode x = q.dequeue(q);
            q.visit(x);
            TreeNode tmp=x.First;
            do{
            q.enqueue(q,tmp);
            }while(tmp != null);
        }while(q.size()!=0);
    }
   
   
   
    public GFPlayer(){}// empty constructor

    
    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) { //inizializzazione 
        this.M = M; 
        this.N = N;
        this.K = K;
        B   = new MNKBoard(M,N,K);
        rand = new Random(System.currentTimeMillis());
        TIMEOUT = timeout_in_secs;
        myWin = first ? MNKGameState.WINP1 : MNKGameState.WINP2;
        yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;
    }
	
	/*------------------------ */
    int min(int a, int b){
        if(a<b)return a;
        else return b;
    }

    int max(int a, int b){
        if(a>b)return a;
        else return b;
    }

    //Nota Bene, da capire come gestire e dove inizializzare depth, e come capire il turno da mettere in
    //isMaximizing 
    //int depth=0;
    MNKCell MiniMax(MNKBoard B, int depth, bool isMaximizing ){
        //caso base
       //one possible move.
       if (FC.length == 1) {              
       return FC[0];
       }
       else{
       if (isMaximizing){//come capisco se tocca a me o all'avversario?
       int bestscore = -1000000;
       for(int i=0; i<M; i++){//check rows
           for(int j=0; j<N; j++){//check columns
               //is the spot available?
               if(B.MNKCellState == "FREE"){
                   //suppongo ai che massimizza e GFPlayer che minimizza
                   int score = MiniMax(B, depth+1, false);//come gestisco depth?
                   score=max(score, bestscore);
                   int bestrow = i;
                   int bestcol = j;
                   MNKCell b = FC[bestrow][bestcol];
                   B.markCell(b.i, b.j);
                   }
               }
           }
           return b;
       }
       else{
           int bestscore = 1000000;
       for(int i=0; i<M; i++){//check rows
           for(int j=0; j<N; j++){//check columns
               //is the spot available?
               if(B.MNKCellState == "FREE"){
                   //suppongo ai che massimizza e GFPlayer che minimizza
                   int score = MiniMax(B, depth+1, true);//impl
                   score=min(score, bestscore);
                   int bestrow = i;
                   int bestcol = j;
                   MNKCell b = FC[bestrow][bestcol];
                   B.markCell(b.i, b.j);
                   }
               }
           }
           return b;
       }
   }
}
/*---------------------------- */
	
    // FC array di celle libere 
    // MC array di celle gia occupate 
    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) { // 2 liste -> MNKCell
        long start = System.currentTimeMillis();

        if (MC.length > 0) {
            MNKCell c = MC[MC.length - 1]; // Recover the last move from MC
            B.markCell(c.i, c.j); // Save the last move in the local MNKBoard
        }
        //one possible move.
        if (FC.length == 1)
            return FC[0];



        //1)
       
        for (MNKCell d : FC) {
        
            if ((System.currentTimeMillis() - start) / 1000.0 > TIMEOUT * (99.0 / 100.0)) {
                MNKCell c = FC[rand.nextInt(FC.length)];
                B.markCell(c.i, c.j);
                return c;
                
            } else if (B.markCell(d.i, d.j) == myWin) {
                return d;
            } else {
                B.unmarkCell();
            } 
        }
            System.out.println("qui 1");
        //2) // non funziona non capisco perche 
       
        MNKCell c = FC[0];
        B.markCell(c.i,c.j);
        for (int k = 1; k < FC.length; k++){
            if((System.currentTimeMillis()-start)/1000.0 > TIMEOUT*(99.0/100.0)) {
				System.out.println("qui 2");
                return c;

			} else {
                MNKCell d = FC[k];
                if(B.markCell(d.i, d.j) == yourWin) {
                    B.unmarkCell();
                    B.unmarkCell();
                    B.markCell(d.i, d.j);
                    System.out.println("qui 3");
                    return d;
                } else {
                    B.unmarkCell();
                    System.out.println("qui 4");
                }
            }
        }
        System.out.println("qui 5");
        B.unmarkCell();
        MNKCell d = FC[1];
        B.markCell(d.i, d.j);
        if(B.markCell(c.i, c.j)== yourWin) {
            B.unmarkCell();
            B.unmarkCell();
            B.markCell(c.i, c.j);
            System.out.println("qui 6");
            return c;
        }

 

            //  System.out.println(FC );
            int pos = rand.nextInt(FC.length);
            System.out.println("v = "+ pos);
             c = FC[pos];

        return c;
        
    }


/*import java.util.ArrayDeque;
import java.util.Queue;
public class Demo {
    public static void main(String[] args) {
      Queue<String> aq = new ArrayDeque<String>(); 
      aq.add("first"); 
      aq.add("second");
      aq.add("third");
      aq.add("fourth");
      System.out.println("Added Queue in memory: " +aq);     
    }
}*/

    //implementazioene AlphaBeta con profondita limitata
    //taglio alberi ricorrenti 
    //valutazione di un nodo dell'albero (fase intermedia di gioco)
            
    

    public String playerName() {// nome player 
        return "G.F.Player";
    }

    /*
     * per capire il quadrato di gioco.
     * if(siamo i primi)
     *      min e max su tutti i k per MC[k.i], MC[k.j] ; k= numeri dispari
     * else k= n pari;
     * 
     * 
     * ------
     * valutazione di un tabella non completa
     * 
     * function eval(MNKCell MC[] oppure una matrice creata da noi oppure B ) -> int
     *      controllo se vinco o perdo.
     * 
     *      4 controlli
     * 
     *      sx-dx orizzontale
     *      sx-dx obliquo
     *      up-down obliquo
     *      alto-basso 
     *      
     *             es     5 5 4 game 
     *                  
     *     sx-dx             | / | O | O | X | X |   
     *                         0   +1 +1  
     *                         ^ mossa sbagliata 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
/*    

     if(FC.length==1)return FC[0];

    if(MC.length>1)

    {
        MNKCell c = MC[MC.length - 2];
        if (c.i + 1 < M) {
            MNKCell d = new MNKCell(c.i + 1, c.j);
            if (d.state == MNKCellState.FREE)// metti la mossa di fianco
                System.out.println("returned d1");
            return d;
        } else if (c.i - 1 >= M) {
            MNKCell d = new MNKCell(c.i - 1, c.j);
            if (d.state == MNKCellState.FREE)
                System.out.println("returned d2");
            return d;
        } // else if()
    }
*/

     /*
     * for (int k = 0; k < FC.length; k++){
     *      MNKCell d = FC[k];
     *      if (B.markCell(d.i,d.j)== yourWin ){
     *          B.unmarkCell();
     *          B.markCell(d.i, d.j);
     *          return d;
     *      } else {
     *      }
     * }
     */
}


/*
function BFS(Tree T)
2: Let Q be a new Queue
3: if T != NIL then
4: enqueue(Q,T)
5: while Q.size != 0 do
6: x = dequeue(Q)
7: visit(x)
8: if x.left != NIL then
9: enqueue(Q, x.left)
10: if x.right != NIL then
11: enqueue(Q, x.right) */

/*static class TreeNode {
       int data;
       TreeNode left, right;
 
       public TreeNode(int key) {
           data = key;
           left = right = null;
       }
   }


/*
static void printLevelOrder(TreeNode root) {
      Queue<TreeNode> queue = new LinkedList<TreeNode>();
      queue.add(root);
      while (!queue.isEmpty()) {
          TreeNode temp = queue.poll();
          System.out.print(temp.data + " ");
 
          // add left child to the queue 
          if (temp.left != null) {
              queue.add(temp.left);
          }
 
          //add right right child to the queue 
          if (temp.right != null) {
              queue.add(temp.right);
          }
      }
  } 
  
  */



/*
 * F (Nodo T)-> int
 * 
 * queue q <- new Queue()
 * q.enqueuq([T,0])
 * int livelloCorrente <- 0
 *  
 * while q.first != null do
 *      //estrae dalla coda una nuova coppia [N,1]
 *      [N,l] <- q.dequeue()
 *      if l != livellocorrente then
 *          //N è il primo nodo di un nuovo livello l 
 *          livelloCorrente <- l 
 *      else 
 *          // N è un nodo dell'attuale livello 
 *      end
 * 
 *      for x appartenente N.children do 
 *          q.enqueue([x,l+1])
 *      end
 * end 
 * 
 * 
 *  */

 /*1: function BFS(Tree T)
2: Let Q be a new Queue
3: if T != NIL then
4: enqueue(Q,T)
5: while Q.size != 0 do
6: x = dequeue(Q)
7: visit(x)
8: tmp = x.first
9: while tmp != NIL do
10: enqueue(Q,tmp)
*/
