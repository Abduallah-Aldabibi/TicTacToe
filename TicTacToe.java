import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * The TicTacToe class is a game that allows two players to compete on a 3x3 grid using x's and o's.
 * 
 * @author Abduallah Aldabibi
 * @version 4/8/2023
 */
public class TicTacToe extends JFrame implements ActionListener {

    public static final String PLAYER_X = "X";
    public static final String PLAYER_O = "O";
    public static final String EMPTY = " ";
    public static final String TIE = "T";
    private int numWinsX = 0;
    private int numWinsO = 0;
    private int numTies = 0;

    private JButton[][] buttons;
    private JLabel messageLabel;
    private String player;
    private String winner;
    private int numFreeSquares;
    private String[][] board;
    private JLabel scoreLabel;
    
    private JFrame frame;
    private JPanel panel;
    
    private ImageIcon xIcon;
    private ImageIcon oIcon;
    private AudioClip click;
    private boolean gamestarted;
  
    

    /**
     * Constructor of objects used in TicTacToe Class
     */
    public TicTacToe() {
    setTitle("Tic Tac Toe");
    setSize(500, 500);
    // Set the window to not be resizable
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    

    // Initialize the game board
    board = new String[3][3];
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            board[i][j] = EMPTY;
        }
    }

    // Initialize the GUI components
    messageLabel = new JLabel("TicTacToe: Player X's turn");
    messageLabel.setFont(new Font("Arial", Font.BOLD, 25));
    messageLabel.setForeground(Color.BLUE);
    scoreLabel = new JLabel("Score(X): 0    Score(O):0    Ties: 0");
    JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
    buttonPanel.setBackground(Color.LIGHT_GRAY);
    buttons = new JButton[3][3];
    
    Border roundedBorder = BorderFactory.createLineBorder(Color.GRAY, 3);
    
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            buttons[i][j] = new JButton(EMPTY);
            buttons[i][j].addActionListener(this);
            buttons[i][j].setBackground(Color.WHITE);
            buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
            buttons[i][j].setBorder(roundedBorder);
            buttons[i][j].setFocusPainted(false);
            buttonPanel.add(buttons[i][j]);
        }
    }
    

    JPanel turnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    turnPanel.setBackground(Color.BLACK);
    turnPanel.add(messageLabel);
    JPanel stats = new JPanel(new FlowLayout(FlowLayout.CENTER));
    stats.add(scoreLabel);

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(buttonPanel, BorderLayout.CENTER);
    contentPane.add(turnPanel, BorderLayout.NORTH);
    contentPane.add(stats, BorderLayout.SOUTH);
    

    // Start the game with player X
    gamestarted = false;
    player = PLAYER_X;
    winner = EMPTY;
    numFreeSquares = 9;
    JMenuBar menuBar = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGameMenuItem = new JMenuItem("New Game");
    JMenuItem resetScoreMenuItem = new JMenuItem("Reset Score");
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    JMenuItem switchPlayerItem = new JMenuItem("Switch Starting Player");

        newGameMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        resetScoreMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numWinsX = 0;
                numWinsO = 0;
                numTies = 0;
                updateScoreLabel();
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        switchPlayerItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        switchStartingPlayer();
        }
        });

        
        gameMenu.add(newGameMenuItem);
        gameMenu.add(resetScoreMenuItem);
        gameMenu.add(switchPlayerItem);
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);
        
    

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
        loadImages();
}

/**
 * Loads the images used in the game and initializes them.
 */
private void loadImages() {
    try {
        Image xImage = ImageIO.read(new File("x.png"));
        xImage = xImage.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        xIcon = new ImageIcon(xImage);
    } catch (IOException e) {
        e.printStackTrace();
        // Handle the exception appropriately
    }

    try {
        Image oImage = ImageIO.read(new File("o.png"));
        oImage = oImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        oIcon = new ImageIcon(oImage);
    } catch (IOException e) {
        e.printStackTrace();
        // Handle the exception appropriately
    }
}



/**
 * Interacts with functions and deals with users action input.
 * 
 * @param e The even to processed
 */
public void actionPerformed(ActionEvent e) {
    JButton button = (JButton) e.getSource();
    int row = getRowFromButton(button);
    int col = getColFromButton(button);
    updateGameStateAndGUI(row, col);
}

/**
 * Finds the row of the button that was clicked
 * @params button The button that was pressed
 * @return The row where the button is located
 */
private int getRowFromButton(JButton button) {
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (button == buttons[i][j]) {
                return i;
            }
        }
    }
    return -1; // button not found
}

/**
 * Finds the column of the button that was clicked
 * @params button The button that was pressed
 * @return The column where the button is located
 * 
 */
private int getColFromButton(JButton button) {
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (button == buttons[i][j]) {
                return j;
            }
        }
    }
    return -1; // button not found
}

/**
 * Updates the game grid and checks for win/tie condition.
 * 
 * @param row The row where the button was pressed
 * @param col The column where the button was pressed
 * 
 */
private void updateGameStateAndGUI(int row, int col) {
    if (row != -1 && col != -1 && board[row][col] == EMPTY && winner == EMPTY) {
        board[row][col] = player;
        numFreeSquares--;
        updateButtonIcon(row, col);
        checkForWinnerAndUpdateGame(row,col);
    }
}
/**
 * Updates the button where the player has clicked.
 * Also plays a sound when button is clicked.
 * 
 * @param row The row where the button was pressed
 * @param col The column where the button was pressed
 */
private void updateButtonIcon(int row, int col) {
    if (player == PLAYER_X) {
        gamestarted = true;
        buttons[row][col].setIcon(xIcon);
        buttons[row][col].setDisabledIcon(xIcon);
        buttons[row][col].setEnabled(false);        
        URL urlclick = TicTacToe.class.getResource("select.wav");
           click = Applet.newAudioClip(urlclick);
           click.play();
    } else {
        gamestarted = true;
        buttons[row][col].setIcon(oIcon);
        buttons[row][col].setDisabledIcon(oIcon);
        buttons[row][col].setEnabled(false);
        URL urlclick = TicTacToe.class.getResource("select.wav");
           click = Applet.newAudioClip(urlclick);
           click.play();
    }
}
/**
 * Check if the game should continue or a win/tie should be declared.
 * 
 * @param row The row where the button was pressed
 * @param col The column where the button was pressed
 * 
 */
private void checkForWinnerAndUpdateGame(int row,int col) {
    if (haveWinner(row, col)) {
        winner = player;
        updateScoreAndDisplayWinner();
        URL urlclick = TicTacToe.class.getResource("win.wav");
           click = Applet.newAudioClip(urlclick);
           click.play();
    } else if (numFreeSquares == 0) {
        winner = TIE;
        updateScoreAndDisplayTie();
        URL urlclick = TicTacToe.class.getResource("tie.wav");
           click = Applet.newAudioClip(urlclick);
           click.play();
    } else {
        switchPlayerAndDisplayTurn();
    }
}
/**
 * Increments the score when a winner is declared.
 * Displays a message of who won.
 */
private void updateScoreAndDisplayWinner() {
    if (player == PLAYER_X) {
        numWinsX++;
    } else {
        numWinsO++;
    }
    updateScoreLabel();
    messageLabel.setText(player + " wins!");
}

/**
 * Increments the score when a tie is declared.
 * Displays a message of a tie.
 */
private void updateScoreAndDisplayTie() {
    numTies++;
    updateScoreLabel();
    messageLabel.setText("Tie game");
}
/**
 * Switches back and forth between players as they alternate turns.
 */
private void switchPlayerAndDisplayTurn() {
    player = (player == PLAYER_X ? PLAYER_O : PLAYER_X);
    messageLabel.setText("Player " + player + "'s turn");
    if (player == PLAYER_X) {
        messageLabel.setForeground(Color.BLUE);
    } else {
        messageLabel.setForeground(Color.RED);
    }
}
  
/**
 * Clears board, restarts the game.
 */
private void restartGame() {
    // reset game state and GUI
    ImageIcon emptyIcon = new ImageIcon("empty.png");
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            board[i][j] = EMPTY;
             buttons[i][j].setIcon(null); // clear any existing icon
             buttons[i][j].setEnabled(true);
        }
    }
    gamestarted = false;
    player = PLAYER_X;
    winner = EMPTY;
    numFreeSquares = 9;
    messageLabel.setText("TicTacToe: Player X's turn");
    messageLabel.setForeground(Color.BLUE);   
}

/**
 * Checks the board for a winner.
 * 
 * @param row The row where the button was pressed
 * @param col The column where the button was pressed
 * 
 * @return boolean, True if there is a winner, false if else.
 * 
 */
private boolean haveWinner(int row, int col) {
    // Check the row
    if (board[row][0].equals(board[row][1]) && board[row][1].equals(board[row][2])) {
        return true;
    }
    // Check the column
    if (board[0][col].equals(board[1][col]) && board[1][col].equals(board[2][col])) {
        return true;
    }
    // Check the diagonal
    if (row == col && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
        return true;
    }
    if (row + col == 2 && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
        return true;
    }
    return false;
}
/**
 * Switches starting player when menu option is pressed.
 */
private void switchStartingPlayer() {
    if(!gamestarted){
    if (player == PLAYER_X) {
        player = PLAYER_O;
    } else {
        player = PLAYER_X;
    }
    messageLabel.setText("TicTacToe: Player " + player + "'s turn");
    }
    if (player == PLAYER_X){
        messageLabel.setForeground(Color.BLUE);
    } else{
        messageLabel.setForeground(Color.RED);
    }
}
/**
 * Updates the displayed score.
 */
private void updateScoreLabel() {
    scoreLabel.setText("Score(X):" + numWinsX + " Score(O):" + numWinsO + " Ties: " + numTies );
}
}