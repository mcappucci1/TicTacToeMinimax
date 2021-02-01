import java.util.ArrayList;

// Class used to represent individual game states
public class State {
    // Game board
    private char[][] board;
    // Game state score
    private int score;
    // Possible turn
    private char turn;
    // Arraylist to store game states at t+1
    private ArrayList<State> next;

    // Constructor
    public State(char[][] board, char turn) {
        this.board = copyBoard(board);
        this.turn = turn;
        next = new ArrayList<State>();
    }

    // Helper method to create copies of game boards to there are no pass by reference issues
    public char[][] copyBoard(char[][] board) {
        char[][] copy = new char[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    // Getter and setter methods
    public char[][] getBoard() { return board; }
    public void setBoard(char[][] board) { this.board = board; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public char getTurn() { return turn; }
    public ArrayList<State> getNext() { return next; }
    public void setTurn(char turn) { this.turn = turn; }
    // Method that adds next game state
    public void addNext(State state) { next.add(state); }
    // Method that checks whether given state is possible t+1 next state 
    public boolean nextContains(State state) {
        for(State nextState : next) {
            char[][] nextBoard = nextState.getBoard();
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(nextBoard[i][j] != board[i][j])
                        return false;
                }
            }
        }
        return true;
    }
    // Method that finds whether one player has won
    public int win() {
        // Variables to track entries in horizontal, vertical, or either diagonal
        char v = '-', h = '-', d1 = '-', d2 = '-';
        // Loops through all three rows/cols and diagonals to find win
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                // Set h and v on 0 index of row or col
                if(j == 0) {
                    v = board[j][i];
                    h = board[i][j];
                }
                // Check if player has streak in horizontal
                if(h != '-' && h != board[i][j])
                    h = '-';
                // Check if player has streak in vertical
                if(v != '-' && v != board[j][i])
                    v = '-';
            }
            // Set diagonals on 0 index
            if(i == 0) {
                d1 = board[i][i];
                d2 = board[2-i][i];
            }
            // Check for streak in diagonal1
            if(d1 != '-' && d1 != board[i][i])
                d1 = '-';
            // Check for streak in diagonal2
            if(d2 != '-' && d2 != board[2-i][i])
                d2 = '-';
            // See if one of the players had all three positions in horizontal
            if(h != '-')
                return (h == 'X') ? 1 : -1;
            // See if one player had all positions in vertical
            if(v != '-')
                return (v == 'X') ? 1 : -1;
        }
        // check if any player had all positions in any of the diagonal directions
        if(d1 != '-')
            return (d1 == 'X') ? 1 : -1;
        if(d2 != '-')
            return (d2 == 'X') ? 1 : -1;
        // For no win return 0
        return 0;
    }

    // Method that tells if the current state is full
    public boolean isFull() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(board[i][j] == '-')
                    return false;
            }
        }
        return true;
    }
}