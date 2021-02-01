import java.util.ArrayList;

// Class that implements minimax algorithm
public class Tree {
    // Root of minimax decision tree
    private State root;
    // Current game state
    private State current;
    
    // Constructor
    public Tree() {
        // Create root as empty game board and set current to root
        char[][] board = {{'-','-','-'}, {'-','-','-'}, {'-','-','-'}};
        root = new State(board, '-');
        current = root;
        // Store all valid tic tac toe game states in tree
        fillTree(root);
        // Set the scores for the tree
        setScores(root, 'X');
    }

    // Method that create a copy of a game board
    // Used so that there are no pass by reference issues
    private char[][] copyBoard(char[][] board) {
        char[][] copy = new char[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    // Helper method that tells if a state is within the tree
    // Returns state in tree if its there, otherwise returns newly made State add
    private State contains(State root, State add) {
        // Get board and next state arraylist
        char[][] addBoard = add.getBoard();
        ArrayList<State> next = root.getNext();
        State ret = add;
        // Search through each of the next states, calling contains recursively until add if definitively found or not
        for(State nextState : next) {
            char[][] nextBoard = nextState.getBoard();
            boolean check = true;
            boolean same = true;
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(addBoard[i][j] != nextBoard[i][j])
                        same = false;
                    if(nextBoard[i][j] != addBoard[i][j] && nextBoard[i][j] != '-') {
                        check = false;
                        break;
                    }
                }
            }
            if(same)
                return nextState;
            if(check && (ret = contains(nextState, add)) != add)
                return ret;
        }   
        return add;     
    }

    // Method that fills the tree with all valid tic tac toe game states
    // Works by having state fill all empty spots with new palyer move and calling fillTree recursively
    // Terminating condition is when the game state is a tie or one player has won
    public void fillTree(State root) {
        // Return for tie or one player winning
        if(root.win() != 0 || root.isFull())
            return;
        // Make a copy of the current root's board so method does not change it, and get root's turn
        char[][] copy = copyBoard(root.getBoard());
        char turn = root.getTurn();
        // Loop that changes each empty spot in the game state to mimick player turn until all game states have been met
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                // Skip position if it is not empty
                if(copy[i][j] != '-')
                    continue;
                // If the game state needs an X to be valid, create new state with X at every empty position
                if(turn == 'X') {
                    copy[i][j] = 'X';
                    State newState = new State(copy, '-');
                    fillTree(newState);
                    root.addNext(contains(root, newState));
                }
                // If the game state needs an O to be valid, create new state with O at every empty position
                else if(turn == 'O') {
                    copy[i][j] = 'O';
                    State newState = new State(copy, '-');
                    fillTree(newState);
                    root.addNext(contains(root, newState));
                }
                // If the game state can take either X or O, then create state with each
                else {
                    copy[i][j] = 'X';
                    State newState = new State(copy, 'O');
                    fillTree(newState);
                    root.addNext(contains(root, newState));
                    copy[i][j] = 'O';
                    newState = new State(copy, 'X');
                    fillTree(newState);
                    root.addNext(contains(root, newState));
                }
                // Reset copy so only one turn is taken at a time
                copy[i][j] = '-';
            }
        }
    }

    // Method that adds scores to each of the states based on who's turn it is
    // O is trying to minimize score with each turn, X is trying to maximize score with each turn
    // End game states are given score based on tie == 0, X win == 1, O win == -1
    // Non-end game states are given score based on minimax trying to maximize or minimize
        // If its X's turn the states score will be the maximum score from its list of next states
        // If its O's turn its score will be the minimum of all the next states
    public void setScores(State root, char turn) {
        // Set score if end game state is reached
        int score = root.win();
        if(score != 0 || root.isFull()) {
            root.setScore(score);
            return;
        }
        // Get the list of root's next states
        ArrayList<State> next = root.getNext();
        // Store the minimum and maximum values
        int max = -2, min = 2;
        // If the root state cannot have both O and X turn then change turn to the proper player if necessary
        if(root.getTurn() != '-' && turn != root.getTurn()) {
            turn = root.getTurn();
        }
        // Loop through each of the next states to find the minimum and maximum
        // Also call setScores() recursively on each of the next states so they can have their scores set first
        for(State nextState : next) {
            setScores(nextState, (turn == 'X') ? 'O' : 'X');
            if(turn == 'X' && max < nextState.getScore())
                max = nextState.getScore();
            else if(turn == 'O' && min > nextState.getScore())
                min = nextState.getScore();
        }
        // Set root's score to the maximum or minimum
        if(turn == 'X')
            root.setScore(max);
        else
            root.setScore(min);
    }

    // Function used for the minimax to move
    // Picks a move based of max score among current's next states if turn == 'X', or min score if turn == 'O'
    public void move(char turn) {
        ArrayList<State> next = current.getNext();
        int max = -2, min = 2;
        State move = null;
        for(State state : next) {
            int score = state.getScore();
            if(max < score && turn == 'X' && state.getTurn() != 'X') {
                max = score;
                move = state;
            }
            else if(min > score && turn == 'O' && state.getTurn() != 'O') {
                min = score;
                move = state;
            }
        }
        current = move;
    }

    // Method prints the current board
    public void printCurrent() {
        char[][] board = current.getBoard();
        System.out.println("\n  1 2 3");
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(j == 0)
                    System.out.print(i+1 + " " + board[i][j] + " ");
                else
                    System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Method returns the current board
    public char[][] getCurrentBoard() { return copyBoard(current.getBoard()); }

    // Method sets the current state
    public void setCurrentState(char[][] board) {
        State compare = new State(board, '-');
        compare = contains(root, compare);
        current = compare;
    }

    // Method returns whether current is a end game state or not
    public boolean gameOver() { return (current.win() != 0 || current.isFull()) ? true : false; }
}
