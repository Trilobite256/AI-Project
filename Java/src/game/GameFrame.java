package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * The GameFrame class is an JFrame that displays all the different
 * game board components. It creates and displays the player panels and 
 * stats, the different game options and buttons, and the player 
 * move history. It also contains listeners for the buttons.
 * 
 * @author A00965803
 * @version 2018/03/10
 */
public class GameFrame extends JFrame {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = 2986344142823166606L;
    
    private static final int PANEL_WIDTH = 205;
    private static final int PANEL_HEIGHT = 300;
    
    // Board panel height
    private static final int BOARD_HEIGHT = 550;
    
    private ArrayList<JRadioButton> boardButtons = new ArrayList<JRadioButton>() {
        private static final long serialVersionUID = -4924548428540373984L;
    {
        add(new JRadioButton("Standard"));
        add(new JRadioButton("German Daisy"));
        add(new JRadioButton("Belgian Daisy"));
    }};
     
    private ArrayList<JRadioButton> playerButtons = new ArrayList<JRadioButton>() {
        private static final long serialVersionUID = 1593599663555850435L;

    {
        add(new JRadioButton("AI is Black"));
        add(new JRadioButton("AI is White"));
    }};
    
    private ArrayList<JRadioButton> modeButtons = new ArrayList<JRadioButton>() {
        private static final long serialVersionUID = -7232779521562355537L;

    {
        add(new JRadioButton("Human VS Human"));
        add(new JRadioButton("Human VS Computer"));
        add(new JRadioButton("Computer VS Computer"));
    }};
    
    // Declaring game object
    private Game game;
    
    // Declaring board object
    private Board boardLayout;
    
    // Declaring GameTimer object to keep track of the game time
    private GameTimer gameTimer;
    
    // JButton to start the game
    private JButton start;
    
    // JButton to stop the game
    private JButton stop;
    
    // JButton to reset the game
    private JButton reset;
    
    // JButton to pause the game
    private JButton pause;
    
    private JPanel museum;
    
    HistoryPanel blackMoveHistory;
    HistoryPanel whiteMoveHistory;

    // ArrayList to hold the spaces on the board
    // private ArrayList<Space> spaceList;
    
    // Move Limit for the game 
    private int moveLimit;
    
    // Time limit for the game
    private long timePerMove;
    
    // Turn Timer GameTimer objects					!AH
    private GameTimer blackTurnTimer;
    private GameTimer whiteTurnTimer;
    
    // Boolean to check if the player is black or white
    private boolean aiIsBlack;
    
    private JPanel gameBoard;
    
    private boolean gamePaused;
    
    private MarblePanel whiteMarblePanel;
    private MarblePanel blackMarblePanel;
    
    private JLabel nextRecommendedMove;

    /**
     * Constructor that creates the initial state of the game.
     * Populates the JFrame.
     * 
     * @param g the current game being played
     */
    public GameFrame(Game g) {
        setTitle("Abalone");
        
        //this.spaceList = new ArrayList<Space>();
        //BoardPanel board = new BoardPanel(g);

        this.game = g;
        //this.spaceList = new ArrayList<Space>();
        gameTimer = new GameTimer("Total Game Time: ");
        boardLayout = Board.copyBoard(g.getBoard());
        gamePaused = false;
        
        this.setLayout(new BorderLayout());
        this.add(createPlayerPanel(), BorderLayout.WEST);
        this.add(createGamePanel(new BoardPanel(this.game, this)),
                BorderLayout.CENTER);
        this.add(createMuseumPanel(), BorderLayout.EAST);
        gameTimer.startTimer();
    }

    /**
     * Creates a JPanel containing each of the different marble teams and
     * their respective stats. 
     * 
     * @return the players JPanel with all the swing elements
     */
    private JPanel createPlayerPanel() {     
        JPanel players = new JPanel();
        players.setLayout(new BoxLayout(players, BoxLayout.PAGE_AXIS));
       
        whiteMarblePanel = new MarblePanel(this, game, "Team White", 
                false, Color.WHITE, Color.BLACK);
     // Get MarblePanel's turnTimer object, add it to players, and reset the timer to show 0.0				AH
        whiteTurnTimer = whiteMarblePanel.getTurnTimer();
        players.add(whiteMarblePanel);
        whiteTurnTimer.resetStopTimer();
        
        blackMarblePanel = new MarblePanel(this, game, "Team Black",
                true, Color.BLACK, Color.WHITE);
     // same as above, but for black																			AH
        blackTurnTimer = blackMarblePanel.getTurnTimer();
        players.add(blackMarblePanel);
        blackTurnTimer.resetStopTimer();
        
        return players;
    }

    /**
     * Creates a JPanel containing the game-board itself and options pertaining
     * to game-board. 
     * 
     * Note in the future could use ArrayList.
     * 
     * @param board the board layout 
     * @return the game JPanel with all the swing elements
     */
    private JPanel createGamePanel(BoardPanel board) {
        final int fontSizeGameStats = 15;
        
        gameBoard = new JPanel();
        gameBoard.setPreferredSize(new Dimension(0, BOARD_HEIGHT));
        gameBoard.setLayout(new BorderLayout());
        gameBoard.add(board, BorderLayout.CENTER);
        
        JPanel gameLabels = new JPanel();
        gameLabels.setLayout(new BoxLayout(gameLabels, BoxLayout.PAGE_AXIS));
        gameLabels.setBorder(new EmptyBorder(10, 10, 10, 10));
        gameLabels.setBackground(Color.WHITE);
        
        gameLabels.add(gameTimer);
        
        nextRecommendedMove = createLabel(nextRecommendedMove, "Next Recommended Move: " 
                + game.getRecommended().toString(), 
                fontSizeGameStats, Color.BLACK);
        gameLabels.add(nextRecommendedMove);
        
        JPanel options = new JPanel();
        options.setBackground(Color.WHITE);
        options.setPreferredSize(new Dimension(10, 80));
        options.setLayout(new BorderLayout());
        options.add(gameLabels, BorderLayout.NORTH);

        ArrayList<JButton> buttons = new ArrayList<>();
        start = createButton(start, "Start Game");
        buttons.add(start);
        stop = createButton(stop, "Stop Game");
        buttons.add(stop);
        pause = createButton(pause, "Pause Game");
        buttons.add(pause);
        reset = createButton(reset, "Reset Game");
        buttons.add(reset);
        
        JPanel optionButtons = new JPanel();
        options.add(optionButtons, BorderLayout.SOUTH);

        optionButtons = new JPanel();
        optionButtons.setBackground(Color.WHITE);
        options.add(optionButtons, BorderLayout.SOUTH);

        for (JButton btn : buttons) {
            optionButtons.add(btn);
        }

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.PAGE_AXIS));
        gamePanel.add(gameBoard);
        gamePanel.add(options);
        
        return gamePanel;
    }

    /**
     * Create a JPanel that holds each JPanel for the players' 
     * move and time history.
     * 
     * @return a JPanel containing player history.
     */
    private JPanel createMuseumPanel() {

        museum = new JPanel();
        blackMoveHistory = new HistoryPanel(game, new JLabel("Black Move History"), 
                game.activeIsBlack(), Color.WHITE, Color.BLACK);
        whiteMoveHistory = new HistoryPanel(game, new JLabel("White Move History"), 
                game.activeIsBlack(), Color.BLACK, Color.WHITE);
        museum.setLayout(new BoxLayout(museum, BoxLayout.PAGE_AXIS));
        museum.add(whiteMoveHistory);
        museum.add(blackMoveHistory);
        
        return museum;
    }      

    /**
     * Creates a JLabel that takes an label and sets it text to the 
     * inputed String parameter text. Sets the font and size.
     * 
     * @param label to display on the GUI
     * @param text to be added to the label
     * @param fontSize size of the font
     * @param fontColor color of the font
     * @return A JLabel with text
     */
    public JLabel createLabel(JLabel label, String text, 
            int fontSize, Color fontColor) {
        label = new JLabel(text);
        label.setFont(new Font("SANS_SERIF", Font.PLAIN, fontSize));
        label.setForeground(fontColor);
        
        return label;
    }
    
    public JButton createButton(JButton button, String text) {
        
        button = new JButton(text);
        LineBorder line = new LineBorder(Color.GRAY);
        EmptyBorder margin = new EmptyBorder(5, 15, 5, 15);
        CompoundBorder compound = new CompoundBorder(line, margin);
        button.setBorder(compound);
        button.addActionListener(new ButtonListener());
        
        return button;
    }
    
    /**
     * Prompts the user to enter conditions for the game and reloads
     * the game based on the results.
     */
    private void initGame() {
        
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.PAGE_AXIS));
        startPanel.setBackground(Color.white);
        
        JLabel boardState = createLabel(new JLabel(), "Choose your initial board state: ", 15, Color.black);
        boardState.setBorder(new EmptyBorder(10, 0, 3, 0));
        startPanel.add(boardState);
        initRadioButtons(boardButtons, new ButtonGroup(), startPanel);

        JLabel colorChose = createLabel(new JLabel(), "Choose AI's color: ", 15, Color.black);
        colorChose.setBorder(new EmptyBorder(10, 0, 3, 0));
        startPanel.add(colorChose);
        initRadioButtons(playerButtons, new ButtonGroup(), startPanel);
        
        JLabel choseMode = createLabel(new JLabel(), "Choose mode: ", 15, Color.black);
        choseMode.setBorder(new EmptyBorder(10, 0, 3, 0));
        startPanel.add(choseMode);
        initRadioButtons(modeButtons, new ButtonGroup(), startPanel);
        
        JLabel timeLimit = createLabel(new JLabel(), "Set Time Limit per Move: ", 15, Color.black);
        timeLimit.setBorder(new EmptyBorder(10, 0, 3, 0));
        JFormattedTextField moveTime = new JFormattedTextField();
        moveTime.setText("5");
        startPanel.add(timeLimit);
        startPanel.add(moveTime);
        
        JLabel moveLimitLabel = createLabel(new JLabel(), "Set Move Limit: ", 15, Color.black);
        moveLimitLabel.setBorder(new EmptyBorder(10, 0, 3, 0));
        JFormattedTextField moveLimitNum = new JFormattedTextField();
        moveLimitNum.setText("0");
        startPanel.add(moveLimitLabel);
        startPanel.add(moveLimitNum);

        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("Panel.background", Color.white);
        
        int result = JOptionPane.showConfirmDialog(null, startPanel,
                "Game Settings", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            boardLayout = new Board();
            
            if (boardButtons.get(0).isSelected()) {                
                boardLayout = Board.copyBoard(Game.standardLayout);

            } else if (boardButtons.get(1).isSelected()) {
                boardLayout = Board.copyBoard(Game.germanDaisy);

            } else if (boardButtons.get(2).isSelected()) {
                boardLayout = Board.copyBoard(Game.belgianDaisy);
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Missing fields to start new game");
                return;
            }

            if (playerButtons.get(0).isSelected()) {
                aiIsBlack = true;
            } else if (playerButtons.get(1).isSelected()) {
                aiIsBlack = false;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Missing fields to start new game");
                return;
            }
            
            this.moveLimit = Integer.parseInt(moveLimitNum.getText());
            this.timePerMove = Long.parseLong(moveTime.getText());
        } else {
            return;
        }
        
        Game g = new Game(boardLayout, aiIsBlack, this.moveLimit, 
                this.timePerMove, gameTimer);
        
        if (g != null) {
            this.game = g;
            gameBoard.removeAll();
            gameBoard.add(new BoardPanel(game, this));
            gameTimer.resetTimer();
            blackMoveHistory.setGame(game);
            blackMoveHistory.removeHistory();
            whiteMoveHistory.setGame(game);
            whiteMoveHistory.removeHistory();
            blackMarblePanel.setGame(game);
            blackMarblePanel.removeStats();
            whiteMarblePanel.setGame(game);
            whiteMarblePanel.removeStats();
            repaint();
            
            // Once new game is started, reset and stop both timers, then start black timer.
            whiteTurnTimer.resetStopTimer();
            blackTurnTimer.resetStopTimer();
            blackTurnTimer.startTimer();
        } 
    }
    
    /**
     * Adds buttons in the arraylist to the inputted button input
     * and adds it to the panel. Made to help save lines of code on
     * the gameInit method.
     * 
     * @param btns ArrayList of JRadioButtons
     * @param group Button group for the JRadioButtons
     * @param panel The panel to be added to
     */
    private void initRadioButtons(ArrayList<JRadioButton> btns, 
            ButtonGroup group, JPanel panel) {
        
        for (int i = 0; i < btns.size(); ++i) {
            btns.get(i).setBackground(Color.white);
            btns.get(i).setActionCommand(btns.get(i).getText());
        }
        
        for (int i = 0; i < btns.size(); ++i) {
            group.add(btns.get(i));
            panel.add(btns.get(i));
        }
        
    }
    
    public void updateGameFrame(boolean activePlayerIsBlack) {
        if (activePlayerIsBlack) {
            blackMoveHistory.updateMoveHistory(activePlayerIsBlack);
            blackMarblePanel.updateScoreLabel(activePlayerIsBlack);
            blackMarblePanel.updateTurnCount(activePlayerIsBlack);
        } else {
            whiteMoveHistory.updateMoveHistory(activePlayerIsBlack);
            whiteMarblePanel.updateScoreLabel(activePlayerIsBlack);
            whiteMarblePanel.updateTurnCount(activePlayerIsBlack);
        }
        
        nextRecommendedMove.setText("Next Recommended Move: " 
                + game.getRecommended().toString());
    }
    
    /**
     * Gets the game timer.
     * 
     * @return the timer
     */
    public GameTimer getTimer() {
        return gameTimer;
    }

    /**
     * Sets the game timer.
     * 
     * @param timer the timer to set
     */
    public void setTimer(GameTimer timer) {
        this.gameTimer = timer;
    }

    private class ButtonListener implements ActionListener {    

        @Override
        public void actionPerformed(ActionEvent event) {
            
            /**
             *  Start game button, stops timer and creates a new game as per the
             *  game settings input. 
             */
            if (event.getSource() == start) {
                gameTimer.stopTimer();
                initGame();
                gameTimer.startTimer();
            } 
            
            /**
             * Stop game button, stops game in current state.
             */
            if (event.getSource() == stop) {
                gameTimer.stopTimer();
                blackTurnTimer.stopTimer();
                whiteTurnTimer.stopTimer();
            }
            
            /**
             * Reset game button, resets the game based on the last game's 
             * settings. 
             */
            if (event.getSource() == reset) {
                game = new Game(boardLayout, aiIsBlack, moveLimit, 
                        timePerMove, gameTimer);
                gameBoard.removeAll();
                gameBoard.add(new BoardPanel(game, GameFrame.this));
                repaint();
                blackMoveHistory.setGame(game);
                blackMoveHistory.removeHistory();
                whiteMoveHistory.setGame(game);
                whiteMoveHistory.removeHistory();
                blackMarblePanel.setGame(game);
                blackMarblePanel.removeStats();
                whiteMarblePanel.setGame(game);
                whiteMarblePanel.removeStats();
                gameTimer.resetTimer();
                blackTurnTimer.resetTimer();
                whiteTurnTimer.resetTimer();
            }
            
            /**
             * Pause game button, pauses the game in it's current state.
             */
            if (event.getSource() == pause) {
               
                if (gamePaused) {
                    gamePaused = false;
                    game.setGameInSession(true);
                    pause.setText("Pause game");
                    gameTimer.startTimer();
                } else {
                    gamePaused = true;
                    game.setGameInSession(false);
                    pause.setText("Unpause game");
                    gameTimer.stopTimer();
                }
            }
        }
    }
    
    public MarblePanel getWhiteMarblePanel(){
    	return whiteMarblePanel;
    }
    
    public MarblePanel getBlackMarblePanel(){
    	return blackMarblePanel;
    }
}
