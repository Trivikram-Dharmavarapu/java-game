import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayGame {

    private JFrame gameWindow;
    private JMenu gameMenu, gameHelp, gameAbout;
    private JMenuBar gameMenuBar;
    private Canvas canvas;

    private String titleOfwindow;
    private int widthOfWindow, heightOfWindow;
    public boolean newGame;
    public boolean highScore;
    public boolean levels;
    public boolean exitGame;
    MenuListener gameHelpHandler;
    MenuListener gameAboutHandler;


    public DisplayGame(String title, int width, int height,MenuListener gameHelpHandler,MenuListener gameAboutHandler){
        this.heightOfWindow = height;
        this.widthOfWindow = width;
        this.titleOfwindow = title;
        this.gameHelpHandler=gameHelpHandler;
        this.gameAboutHandler=gameAboutHandler;
        createDisplay();
    }

    private void createDisplay(){
        gameWindow = new JFrame(titleOfwindow);
        gameWindow.setSize(widthOfWindow, heightOfWindow);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);
        gameWindow.setResizable(false);
        gameWindow.setJMenuBar(createMenuBar());
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(widthOfWindow,heightOfWindow));
        canvas.setMaximumSize(new Dimension(widthOfWindow,heightOfWindow));
        canvas.setMinimumSize(new Dimension(widthOfWindow,heightOfWindow));
        canvas.setFocusable(false);

        gameWindow.add(canvas);
        gameWindow.pack();
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getGameWindow(){
        return gameWindow;
    }

    private JMenuBar createMenuBar(){

        JMenuItem menuItemNew = new JMenuItem("New Game");
        JMenuItem menuItemHighScores = new JMenuItem("High Scores");
        JMenuItem menuItemLevels = new JMenuItem("Levels");
        JMenuItem menuItemExit = new JMenuItem("Exit");

        gameMenu = new JMenu("Menu");
        gameMenu.add(menuItemNew);
        gameMenu.add(menuItemHighScores);
        gameMenu.add(menuItemLevels);
        gameMenu.add(menuItemExit);

        gameHelp = new JMenu("Help");
        gameAbout = new JMenu("About");

        menuItemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame = true;
            }
        });
        menuItemHighScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highScore = true;
            }
        });
        menuItemLevels.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levels = true;
            }
        });
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame = true;
            }
        });

        gameHelp.addMenuListener(gameHelpHandler);
        gameAbout.addMenuListener(gameAboutHandler);

        gameMenuBar = new JMenuBar();
        gameMenuBar.add(gameMenu);
        gameMenuBar.add(gameHelp);
        gameMenuBar.add(gameAbout);
        return gameMenuBar;
    }
}

