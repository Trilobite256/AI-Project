/**
 * 
 */
package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author Mike
 *
 */
public class aiPlayer {

    private static final int DIRECTION_MIN = 1;
    private static final int DIRECTION_MAX = 6;

    /**
     * This method is responsible for generating a list of possible moves given the current board state
     * @return
     */
    public static ArrayList<Move> genPossibleMoves(Game g, boolean aiIsBlack){
        ArrayList<Move> moves = new ArrayList<Move>();
        //Board original = Board.copyBoard(g.getBoard());
        Board currentBoard = Board.copyBoard(g.getBoard());
        Board ownMarbles = new Board();
       /* Marble m1 = null;
        Marble m2 = null;*/

        for(Marble m: currentBoard){
            if(m.isBlack() == aiIsBlack){
                ownMarbles.add(m);
            }
        }
        
        for(Marble m : ownMarbles){
            for(int i = DIRECTION_MIN; i <= DIRECTION_MAX; ++i){
                Move move = generateMove(g, m, i);
                if (move != null) {
                    //System.out.println("before: " + move.toString());
                    moves.add(move);
                    //System.out.println("after: " + move.toString());
                }
            }
        }
        
        // Generates moves for a single marble for each marbles in the ArrayList
       /* for (int i = 0; i < currentBoard.size(); ++i) {
            
            m1 = currentBoard.get(i);
            if (m1.isBlack() == aiIsBlack) {
                int j = DIRECTION_MIN;
                while (j <= DIRECTION_MAX) {


                    Move move = generateMove(g, m1, j);

                    if (move != null) {
                        //System.out.println("before: " + move.toString());
                        moves.add(move);
                        //System.out.println("after: " + move.toString());
                    }

                    currentBoard = Board.copyBoard(original);
                    j++;
                }
            }
        }*/

        
        for(Marble m : ownMarbles){
            for(Marble o : ownMarbles){
                if(!m.equals(o)){
                    for(int i = DIRECTION_MIN; i <= DIRECTION_MAX; ++i){
                        Move move = generateMove(g, m, o, i);
                        if (move != null) {
                            //System.out.println("before: " + move.toString());
                            moves.add(move);
                            //System.out.println("after: " + move.toString());
                        }
                    }
                }
            }
        }
        // Generates moves for 2 or more marbles
        /*for (int i = 0; i < currentBoard.size() - 1; ++i) {
            for (int j = i + 1; j < currentBoard.size(); ++j) {

                m1 = currentBoard.get(i);
                m2 = currentBoard.get(j);
                if (m1.isBlack() == aiIsBlack && m2.isBlack() == aiIsBlack) {
                    int k = DIRECTION_MIN;
                    while (k <= DIRECTION_MAX) {

                        Move move = generateMove(g, m1, m2, k);

                        if (move != null) {
                            moves.add(move);
                        }

                        currentBoard = Board.copyBoard(original);
                        k++;
                    }
                }
            }
        }*/

       
        
        return moves;
    }

    /**
     * This method is responsible for generating a single legal move of one marble based on a given board state
     * @return
     */
    public static Move generateMove(Game g, Marble m, int direction){
        Game dummy = new Game(g);
        Marble nm = Game.searchBoard(dummy.getBoard(), m.getAlpha(), m.getNumeric());
        Move displayMove = new Move(m, direction);
        Move move = new Move(nm, direction);
        

        /*System.out.print("before");
        System.out.println(move);*/
        if(Game.moveIsLegal(g, move)){
            // resets the board state so that the marbles in the move display origin coordinates
            
            /*System.out.print("after");
            System.out.println(move);*/
            return displayMove;
        }      

        // return null if move was illegal   
        return null;
    }

    /**
     * This method is responsible for generating a single legal move of multiple marble based on a given board state
     * @return
     */
    public static Move generateMove(Game g, Marble m1, Marble m2, int direction){
        
        Game dummy = new Game(g);
        
        Move displayMove = new Move(m1, m2, direction);
        Marble nm1 = Game.searchBoard(dummy.getBoard(), m1.getAlpha(), m1.getNumeric());
        Marble nm2 = Game.searchBoard(dummy.getBoard(), m2.getAlpha(), m2.getNumeric());
        Move move = new Move(nm1, nm2, direction);

        if(Game.moveIsLegal(g, move)){
            // resets the board state so that the marbles in the move display origin coordinates
            //g.setBoard(gameState);
            return displayMove;
        }      

        // return null if move was illegal
        return null;
    }

    /**
     * Takes a suggested move and executes it on the specified board.
     * @param g the current state of the game
     * 
     */
    public static Board genResultState(Game g, Move mv){
        Game dummy = new Game(g);
        Board original = Board.copyBoard(g.getBoard());

        Marble m1 = mv.getMovedList().get(0);
        m1 = Game.searchBoard(dummy.getBoard(), m1.getAlpha(), m1.getNumeric());
        Marble m2 = null;
        
        System.out.println("old: " + original.toString());

        if (mv.getMovedList().size() > 1) {
            m2 = mv.getMovedList().get(1);
            if(m2 != null){
                m2 = Game.searchBoard(dummy.getBoard(), m2.getAlpha(), m2.getNumeric());
            }
        }

        if(m2 == null){// single marble move 
            //System.out.println("single");
            dummy.move(m1, mv.getDirection(), m1.isBlack());  
            
        } else {// multiple marble move
            /*System.out.println("double");
            System.out.println(m2.toString());*/
            //System.out.println("The program gets here sometimes");
            dummy.move(m1, m2, mv.getDirection(), m1.isBlack());   
        }
        
        System.out.println("new?: " + dummy.getBoard().toString());
        
        /*System.out.println(success);
        if (success) {
            System.out.println("new: " + dummy.getBoard().toString());
            //System.out.println("butts");
        }*/
        return dummy.getBoard();
    }

    /**
     * Returns resulting states from an arraylist of moves (which must already be checked for legality) as a HashMap; 
     * evaluation function values will be added later as the double values in the map
     * @param g
     * @param moves
     * @return
     */
    public static HashSet<Board> genAllResults(Game g, ArrayList<Move> moves){
        HashSet<Board> states = new HashSet<Board>();
        Board singleState = null;

        for(Move mv : moves){
            singleState = Board.copyBoard(genResultState(g, mv));
            //Collections.sort(singleState, new MarbleComparator());
            //System.out.println(singleState.toString());
            if(!states.contains(singleState)){
                states.add(singleState);

            }
        }

        return states;
    }
}
