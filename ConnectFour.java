import java.util.Scanner;

/**
 * CS312 Assignment 10.
 *
 * On my honor, Jerry, this programming assignment is my own work and I have
 * not shared my solution with any other student in the class.
 *
 *  email address: jeehimuh@gmail.com
 *  UTEID: jee2352
 *  Section 5 digit ID: 
 *  Grader name: Nathan/Omeed
 *  Number of slip days used on this assignment: 2
 *
 * Program that allows two people to play Connect Four.
 */

public class ConnectFour {

    public static final char P1_PIECE = 'r';
    public static final char P2_PIECE = 'b';
    public static final int NUM_ROWS = 6;
	public static final int NUM_COLS = 7;     
    public static void main(String[] args) {
        intro();
        Scanner key = new Scanner(System.in);
        int player1 = 1;
        int player2 = 2;
        String playerOne = playerName(player1, key);
        String playerTwo = playerName(player2, key);
        char[][] connectFourBoard = generateBaord();
        System.out.print("Current");
        printBoard(connectFourBoard);
        playerTurnRepeater(playerOne, playerTwo, connectFourBoard, key);
        key.close();
    }

    //repeats the players turns until somebody wins or the board is filled
    public static void playerTurnRepeater(String playerOne, String playerTwo, char[][] connectFourBoard, Scanner key) {
    	boolean boardIsFull = topRowCounter(connectFourBoard);
    	boolean win = checkWin(connectFourBoard);
    	int count = 0;
    	while(count < NUM_ROWS * NUM_COLS && !win) {
    		win = playerTurn(playerOne, P1_PIECE, key, connectFourBoard, boardIsFull);
    		count++;
        	if (count < NUM_ROWS * NUM_COLS && !win) {
        		win = playerTurn(playerTwo, P2_PIECE, key, connectFourBoard, boardIsFull);
        		count++;
        	}
    	}
	}

    //counts how much of the top row which indicates the board is filled is filled and
    //updates boolean boardIsFull
    public static boolean topRowCounter(char[][] connectFourBoard) {
    	int topRowCount = 0;
    	for (int colIndex = 0; colIndex < NUM_COLS; colIndex++) {
    		char currTopRowChar = connectFourBoard[0][colIndex];
    		if (currTopRowChar != '.') {
    			topRowCount++;
    		}
    	}
    	boolean boardIsFull = topRowCount == NUM_COLS;
    	return boardIsFull;
	}

	//Goes through the players turn: tells them their piece & lets them drop it off in board
	public static boolean playerTurn(String name, char playerPiece, Scanner key, char[][] connectFourBoard, boolean boardIsFull) {
        gameOrientation(name, playerPiece);
        int playerInt = validPlayerInt(name, key, NUM_COLS);
        playerInt = validColumn(connectFourBoard, playerInt, name, key, NUM_COLS);
        dropPlayerPiece(connectFourBoard, playerInt, playerPiece);
        boardIsFull = topRowCounter(connectFourBoard);
        boolean win = checkWin(connectFourBoard);
        if (boardIsFull || win) {
        	if (win) {
        		System.out.println(name + " wins!!\n");
        	} else {
        		System.out.println("The game is a draw.\n");
        	}
        	System.out.print("Final");
            printBoard(connectFourBoard);
        } else {
        	System.out.print("Current");
        	printBoard(connectFourBoard);
        }
        return win;
	}

	//returns a possible win boolean in one of the directions on the board 
	public static boolean checkWin(char[][] connectFourBoard) {
		// directions are east, south, south west, south east
		final int DIRECTIONS = 4;
		final int[] ROWDIRECTIONS = {0, 1, 1, 1};
		final int[] COLDIRECTIONS = {1, 0, -1, 1}; 
		for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
    		for(int colIndex = 0; colIndex < NUM_COLS; colIndex++) {
    			for (int i = 0; i < DIRECTIONS; i++) {
    				if(checkWinDirection(connectFourBoard, rowIndex, colIndex,
    						COLDIRECTIONS[i], ROWDIRECTIONS[i])) {
    					return true;
    				}
    			}
	    	}
		}
		return false;
	}
	
	//checks if player won from south, south west, south east, or east based off of parameters
	//passed into method
	public static boolean checkWinDirection(char[][] connectFourBoard, int rowIndex, int colIndex,
		int colAdd, int rowAdd) {
		final int SPOTS_LEFT_NEEDED = 3;
		char firstBoardSpotChar = connectFourBoard[rowIndex][colIndex];
		char nextSpotChar = connectFourBoard[rowIndex][colIndex];
		final int WIN_NUM_IN_ROW = 4;
		int numInARow = 0;
		int colRange = colIndex + colAdd * SPOTS_LEFT_NEEDED;
		int rowRange = rowIndex + rowAdd * SPOTS_LEFT_NEEDED;
		boolean winIsInRange = (0 <= colRange) && (colRange < NUM_COLS) && (0 <= rowRange) && (rowRange < NUM_ROWS);
		if (winIsInRange && firstBoardSpotChar != '.') {
			for(int i = 0; i < WIN_NUM_IN_ROW; i++) {
				if (firstBoardSpotChar == nextSpotChar) {
					numInARow++;
					colIndex += colAdd;
					rowIndex += rowAdd;
					if (numInARow == WIN_NUM_IN_ROW) {
						return true;
					}
					nextSpotChar = connectFourBoard[rowIndex][colIndex];
				}
			}
		}
		return false;
	}
	
	//drops the player's piece in the spot they chose on the board
	public static void dropPlayerPiece(char[][] connectFourBoard, int playerInt, char playerPiece) {
    	int validRowIndex = connectFourBoard.length -1;
    	int validColIndex = playerInt - 1;
    	char currBoardSpot = connectFourBoard[validRowIndex][validColIndex];
    	while (currBoardSpot != '.') {
    		validRowIndex--;
    		currBoardSpot = connectFourBoard[validRowIndex][validColIndex];
    	}
    	connectFourBoard[validRowIndex][validColIndex] = playerPiece;
	}

	//makes sure the column is not full and the player's choice is valid
    public static int validColumn(char[][] connectFourBoard, int playerInt, String playerName, Scanner key, int NUM_COLS) {
    	char topOfBoardChar = connectFourBoard[0][playerInt - 1];
    	while(topOfBoardChar != '.') {
    		System.out.println(playerInt + " is not a legal column. That column is full");
    		playerInt = validPlayerInt(playerName, key, NUM_COLS);
    		topOfBoardChar = connectFourBoard[0][playerInt - 1];
    	}
    	return playerInt;
	}

    //asks player what column they want to drop it off in, and chekcs if it's in the range of
    //the array columns
	public static int validPlayerInt(String playerName, Scanner key, int NUM_COLS) {
    	String prompt = playerName + ", enter the column to drop your checker: ";
    	System.out.print(prompt);
    	int playerInt = getInt(key, prompt);
    	System.out.println();
    	while (playerInt < 1 || playerInt > NUM_COLS) {
    		System.out.println(playerInt + " is not a valid column.");
    		System.out.print(prompt);
    		playerInt = getInt(key, prompt);
    		System.out.println();
    	}
    	return playerInt;
	}

	//tells player their name, that it's their turn and what checker they're playing with
    public static void gameOrientation(String name, char playerPiece) {
    	System.out.println(name + " it is your turn.");
    	System.out.println("Your pieces are the " + playerPiece + "'s.");
	}

    //prints the current connect four board that's passed into it with the number of columns
	public static void printBoard(char[][] connectFourBoard) {
    	System.out.println(" Board");
    	for(int columnIndex = 0; columnIndex < NUM_COLS; columnIndex++) {
    		int columnNum = columnIndex + 1;
			System.out.print(columnNum + " ");
		}
    	System.out.println(" column numbers");
    	for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
    		for(int columnIndex = 0; columnIndex < NUM_COLS; columnIndex++) {
    			System.out.print(connectFourBoard[rowIndex][columnIndex] + " ");
    		}
    		System.out.println();
    	}
    	System.out.println();
	}

	//makes initial board with .'s 
    public static char[][] generateBaord() {
    	char[][] connectFourBoard = new char[NUM_ROWS][NUM_COLS];
    	for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
    		for(int columnIndex = 0; columnIndex < NUM_COLS; columnIndex++) {
    			connectFourBoard[rowIndex][columnIndex] = '.'; 
    		}
    	}
    	return connectFourBoard;
	}

    //Tells the user for player 1 & 2 to enter their names and returns it
	public static String playerName(int playerNum, Scanner key) {
    	System.out.print("Player " + playerNum + " enter your name: ");
    	String player = key.nextLine();
    	System.out.println();
    	return player;
	}

    // Show the introduction.
    public static void intro() {
        System.out.println("This program allows two people to play the");
        System.out.println("game of Connect four. Each player takes turns");
        System.out.println("dropping a checker in one of the open columns");
        System.out.println("on the board. The columns are numbered 1 to 7.");
        System.out.println("The first player to get four checkers in a row");
        System.out.println("horizontally, vertically, or diagonally wins");
        System.out.println("the game. If no player gets fours in a row and");
        System.out.println("and all spots are taken the game is a draw.");
        System.out.println("Player one's checkers will appear as r's and");
        System.out.println("player two's checkers will appear as b's.");
        System.out.println("Open spaces on the board will appear as .'s.\n");
    }

    // Prompt the user for an int. The String prompt will
    // be printed out. key must be connected to System.in.
    public static int getInt(Scanner key, String prompt) {
    	while(!key.hasNextInt()) {
            String notAnInt = key.nextLine();
            System.out.println();
            System.out.println(notAnInt + " is not an integer.");
            System.out.print(prompt);
        }
        int result = key.nextInt();
        key.nextLine();
        return result;
    }
}