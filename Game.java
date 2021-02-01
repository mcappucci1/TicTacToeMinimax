import java.util.Scanner;

// Class that runs the game
public class Game {

    // main function of the program, runs the game
    public static void main(String args[]) {
        // Instantiate the minimax game tree, game board, and scanner
        Tree tree = new Tree();
        char[][] board = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};
        Scanner scan = new Scanner(System.in);
        // Get whether the user wants to go first or not
        char first;
        do {
            System.out.println("Do you want to go first (Y/N)? ");
            first = scan.nextLine().charAt(0);
        } while(first != 'y' && first != 'Y' && first != 'n' && first != 'N');
        if(first == 'y')
            first = 'Y';
        else if(first == 'n')
            first = 'N';
        // Print the initial state of the game (empty board)
        tree.printCurrent();
        // Game loop
        while(!tree.gameOver()) {
            // Actions taken if the user wants to go first
            if(first == 'Y') {
                // Get the row, column position that the user wants to go on the board
                int row, col;
                do {
                    System.out.println("Enter row/col of next input (row col)? ");
                    try {
                        row = scan.nextInt();
                        col = scan.nextInt();
                        row--;
                        col--;
                    } catch(Exception e) {
                        scan.nextLine();
                        row = -1;
                        col = -1;
                    }
                    if(row < 0 || row > 2 || col < 0 || col > 2)
                        continue;
                    if(board[row][col] != '-')
                        row = -1;
                } while(row < 0 || row > 2 || col < 0 || col > 2);
                // Update minimax current state and print board with user's move
                board[row][col] = 'X';
                tree.setCurrentState(board);
                tree.printCurrent();
                // Check that the game is still going, if not then break game loop
                if(tree.gameOver())
                    break;
                // Perform the minimax AI's move
                tree.move('O');
                System.out.println("Minimax moved.");
                tree.printCurrent();
                // Update the board
                board = tree.getCurrentBoard();
            }
            // Actions taken if the user does not want ot go first
            else {
                // Perform the minimax AI's move
                tree.move('X');
                System.out.println("Minimax moved.");
                tree.printCurrent();
                // Update game board
                board = tree.getCurrentBoard();
                // Break the game loop if the game is over
                if(tree.gameOver())
                    break;
                // Get the position where the user wants to move
                int row, col;
                do {
                    System.out.println("Enter row/col of next input (row col)? ");
                    try {
                        row = scan.nextInt();
                        col = scan.nextInt();
                        row--;
                        col--;
                    } catch(Exception e) {
                        scan.nextLine();
                        row = -1;
                        col = -1;
                    }
                    if(row < 0 || row > 2 || col < 0 || col > 2)
                        continue;
                    if(board[row][col] != '-')
                        row = -1;
                } while(row < 0 || row > 2 || col < 0 || col > 2);
                // Update the minimax's current state with the player's move
                board[row][col] = 'O';
                tree.setCurrentState(board);
                tree.printCurrent();
            }
        }
        // Get the win value for the final state
        State end = new State(board, '-');
        int win = end.win();
        // Print end game message
        if(win == 0)
            System.out.println("Tie.");
        else if((win < 0 && first == 'N') || (win > 0 && first == 'Y'))
            System.out.println("You wins.");
        else
            System.out.println("You lose");
        scan.close();
    }
}
