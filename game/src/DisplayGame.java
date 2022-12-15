import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DisplayGame {

    private JFrame gameWindow;
    private JMenu gameMenu, gameHelp, gameAbout;
    private JMenuBar gameMenuBar;
    private Canvas canvas;

    private String titleOfwindow;
    private int widthOfWindow, heightOfWindow;

    public DisplayGame(String title, int width, int height){
        this.heightOfWindow = height;
        this.widthOfWindow = width;
        this.titleOfwindow = title;
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
        gameMenu = new JMenu("Menu");
        gameMenu.add(new JMenuItem("New Game"));
        gameMenu.add(new JMenuItem("High Scores"));
        gameMenu.add(new JMenuItem("Levels"));
        gameMenu.add(new JMenuItem("Exit"));


        gameHelp = new JMenu("Help");
        gameAbout = new JMenu("About");

        gameMenuBar = new JMenuBar();
        gameMenuBar.add(gameMenu);
        gameMenuBar.add(gameHelp);
        gameMenuBar.add(gameAbout);
        return gameMenuBar;
    }
}

