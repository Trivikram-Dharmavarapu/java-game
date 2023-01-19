import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Game implements Runnable {

    private Thread thread;
    private boolean running = false;
    public String titleOfwindow;
    public int widthOfWindow, heightOfWindow;
    private DisplayGame displayGame;

    private BufferStrategy bufferStrategy;
    private Graphics graphics;
    private Set<Coordinates> enemies = new HashSet<Coordinates>();
    private int MAX_NUMBER_ENEMIES_PER_MINUTE = 5;
    private int numberOfEnemies;
    private String SCORE_FORMAT = "Score : %s";
    private int score =0;
    private String LIVES_FORMAT = "Lives : %s";
    private String NAME_FORMAT = "Player : %s";
    private int reamingLives = 3;
    private int iteration = 0;
    private boolean newGameFlag = true;
    private String[] gameOverOptions = { "New Game", "Exit" };
    private String[] newGameOptions = { "Yes", "No" };
    private int maxHighScores = 5;
    private String playerName;
    private int currentLevel = 1;
    private double levelFactor = 0.5;
    private String[] levels = { "1", "2", "3" };

    public MenuListener gameHelpHandler =new MenuListener() {
        @Override
        public void menuSelected(MenuEvent e) {
            JOptionPane.showMessageDialog(null,Constants.helpText,"HELP",JOptionPane.INFORMATION_MESSAGE);
            newGame();
        }

        @Override
        public void menuDeselected(MenuEvent e) {

        }

        @Override
        public void menuCanceled(MenuEvent e) {

        }
    };

    public MenuListener gameAboutHandler =new MenuListener() {
        @Override
        public void menuSelected(MenuEvent e) {
            JOptionPane.showMessageDialog(null,Constants.aboutText,"About",JOptionPane.INFORMATION_MESSAGE);
            newGame();
        }

        @Override
        public void menuDeselected(MenuEvent e) {

        }

        @Override
        public void menuCanceled(MenuEvent e) {

        }
    };

    public Game(String title, int width, int height, String playerName) {
        this.heightOfWindow = height;
        this.widthOfWindow = width;
        this.titleOfwindow = title;
        this.playerName = playerName;
    }

    private void generateEnemies() {
        iteration++;
        numberOfEnemies = (new Random().nextInt(MAX_NUMBER_ENEMIES_PER_MINUTE) + 1)*currentLevel;
        System.out.println("Iteration : " + iteration);
        System.out.println("numberOfEnemies : " + numberOfEnemies);
        for (int i = 0; i < numberOfEnemies; i++) {
            //(int) ((Math.random() * (max - min)) + min)
            int x = (int) ((Math.random() * (widthOfWindow/4)));
            int y = (int) ((Math.random() * ((heightOfWindow -20) - 60)) + 60);
            enemies.add(new Coordinates(x, y));
        }
    }

    private void newGame(){
        score = 0;
        reamingLives = 3;
        iteration = 0;
        enemies.clear();
        newGameFlag= true;
    }
    private void initialize() {
        if(newGameFlag == true){
        displayGame = new DisplayGame(titleOfwindow, widthOfWindow, heightOfWindow,gameHelpHandler,gameAboutHandler) ;
        displayGame.getCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                eliminateEnemies(e.getX(),e.getY());
            }
        });
            generateEnemies();
        }
    }

    void eliminateEnemies(int x, int y){
        System.out.println("Elimination process started for (x,y) = "+ x + " " + y);
        Set<Coordinates> enemiesToRemove = new HashSet<Coordinates>();
        for (Coordinates enemy : enemies) {
            System.out.println("enemy = "+ enemy.getX());
            if (x >= enemy.getX() && x<= enemy.getX() + 20 && y >= enemy.getY() && y<= enemy.getY() + 20){
                score++;
                enemiesToRemove.add(enemy);
            }
        }
        enemies.removeAll(enemiesToRemove);
    }

    private void update() {
        Set<Coordinates> enemiesToRemove = new HashSet<Coordinates>();
            for (Coordinates enemy : enemies) {
                if (enemy.getX() <= widthOfWindow)
                    enemy.setX(enemy.getX() + currentLevel);
                else {
                    reamingLives--;
                    enemiesToRemove.add(enemy);
                }
            }
        enemies.removeAll(enemiesToRemove);
        if (enemies.size() <= currentLevel) {
            generateEnemies();
        }
        if(reamingLives <=0){
            var selection = JOptionPane.showOptionDialog(null, null, "Game Over!",JOptionPane.WARNING_MESSAGE,
                    0, null, gameOverOptions, this.gameOverOptions[0]);
            if(selection == 0){
                this.restart(false);
            }
            else{
                this.close();
            }
            JOptionPane.getRootFrame().dispose();
        }
        if(displayGame.newGame == true)
            this.restart(true);
        if(displayGame.highScore == true) {
            this.displayHighScore();
        }
        if(displayGame.exitGame == true) {
            this.exitGameHandler();
        }
        if(displayGame.levels == true) {
            displayGame.levels=false;
            this.gameLevelsHandler();
        }
    }

    private void render() {
        bufferStrategy = displayGame.getCanvas().getBufferStrategy();
        if (bufferStrategy == null) {
            displayGame.getCanvas().createBufferStrategy(3);
            return;
        }
        graphics = bufferStrategy.getDrawGraphics();

        graphics.clearRect(0, 0, widthOfWindow, heightOfWindow);
        //
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, widthOfWindow, heightOfWindow);
        //
        graphics.setColor(Color.WHITE);
        graphics.drawRect(10,5,150,60);
        graphics.drawString(String.format(NAME_FORMAT,playerName), 15,20);
        graphics.drawString(String.format(SCORE_FORMAT,score), 15,40);
        graphics.drawString(String.format(LIVES_FORMAT,reamingLives), 15,60);
        //
        for (Coordinates enemy : enemies) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(enemy.getX(), enemy.getY(), 30, 30);
            graphics.setColor(Color.RED);
            graphics.fillRect(enemy.getX()+5, enemy.getY()+5, 20, 20);
        }
        //
        graphics.setColor(Color.GREEN);
        graphics.fillRect(widthOfWindow - 40,0,40, heightOfWindow);
        //
        bufferStrategy.show();
        graphics.dispose();
    }

    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        initialize();
        int fps = 60;
        double timePerUpdate = 1000000000 / fps;
        double diff = 0;
        long current;
        long prev = System.nanoTime();
        int numberofSeconds = 0;
        while (running) {
            current = System.nanoTime();
            diff += (current - prev) / timePerUpdate;
            prev = current;
            if (diff > 0) {
                update();
                render();
                diff--;
                numberofSeconds++;
            }
            //System.out.println(numberofSeconds/fps);
        }
    }

    public void restart(boolean confirmation){
        if(confirmation == false) {
            saveScore();
            newGame();
            thread.run();
        }
        else{
            var selection = JOptionPane.showOptionDialog(null, "Are you sure ?", "Confirmation",JOptionPane.WARNING_MESSAGE,
                    0, null, newGameOptions, this.newGameOptions[1]);
            if(selection == 0){
                saveScore();
                newGame();
                thread.run();
            }
            else{
                displayGame.newGame = false;
                newGameFlag = false;
                thread.run();
            }
            JOptionPane.getRootFrame().dispose();
        }
    }
    private void saveScore() {
        Path path = Paths.get("game/resources/scores.txt");
        String data = "";
        String scoresString = "";
        String namesString = "";
        String[] names = {};
        String[] scores = {};
        try {
            data = new String(Files.readAllBytes(path));
            System.out.println(data);
            if(data.length() > 0) {
                names = data.split("#")[0].split(",");
                scores = data.split("#")[1].split(",");
            }
            int length = scores.length;
            boolean  scoreAdded = false;
            if(length == 0){
                scoresString = scoresString + score;
                namesString = playerName;
            }
            for(int i=0;i<length;i++) {
                if(score >= Integer.parseInt(scores[i]) && scoreAdded == false){
                    if(i <length-1) {
                        scoresString = scoresString + score + ',' + scores[i] + ',';
                        namesString = namesString +playerName + ',' + names[i] + ',';
                    }
                    else{
                        scoresString = scoresString + score + ',' + scores[i];
                        namesString = namesString +playerName + ',' + names[i] ;
                    }
                    scoreAdded = true;
                }
                else {
                    if (i < length - 1) {
                        scoresString = scoresString + scores[i] + ',';
                        namesString = namesString + names[i] + ',';
                    } else {
                        scoresString = scoresString + scores[i];
                        namesString = namesString + names[i];
                    }
                }
            }
            names = namesString.split(",");
            scores = scoresString.split(",");
            scoresString = "";
            namesString = "";
            length = (scores.length <= maxHighScores)? scores.length: maxHighScores;
            for(int i=0;i<length;i++) {
                if(i <length-1) {
                    scoresString = scoresString+scores[i] + ',';
                    namesString = namesString + names[i] + ',';
                }
                else{
                    scoresString = scoresString+scores[i];
                    namesString = namesString+names[i];
                }
            }
            System.out.println(length);
            System.out.println(scoresString);
            System.out.println(namesString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.writeString(path, namesString+"#"+scoresString, StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void displayHighScore() {
        var selection = JOptionPane.showOptionDialog(null, "Your progress will be lost, still do you want to continue ?", "Confirmation",JOptionPane.WARNING_MESSAGE,
                0, null, newGameOptions, newGameOptions[1]);
        if(selection == 0){
            String data = "";
            String[] names = {};
            String[] scores = {};
            Path path = Paths.get("game/resources/scores.txt");
            try {
                data = new String(Files.readAllBytes(path));
                if(data.length() > 0) {
                    names = data.split("#")[0].split(",");
                    scores = data.split("#")[1].split(",");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg;
            if(scores.length > 0){
                msg = "<html>";
                for(int i=0;i<scores.length;i++) {
                    msg = msg+ (i+1)+"."+names[i] + " : " + scores[i] + "<br>";
                    }
                }
            else {
                msg = "No Scores recorded yet ..!";
            }
            JOptionPane.showMessageDialog(null,msg,"High Scores",JOptionPane.INFORMATION_MESSAGE);
            newGame();
            thread.run();
        }
        else{
            displayGame.newGame = false;
            newGameFlag = false;
            thread.run();
        }
      this.displayGame.highScore = false;
    }
    private void close(){
        System.exit(0);
    }
    private void exitGameHandler()
    {
        var selection = JOptionPane.showOptionDialog(null, "Your progress will be lost, still do you want to continue ?", "Confirmation",JOptionPane.WARNING_MESSAGE,
                0, null, newGameOptions, newGameOptions[1]);
        if(selection == 0){
            close();
        }
        else{
            displayGame.exitGame = false;
            newGameFlag = false;
            thread.run();
        }
    }

    private void gameLevelsHandler(){
        JFrame levelsFrame = new JFrame();
        int newLevel = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Select Level:","Levels",JOptionPane.QUESTION_MESSAGE,null, levels,levels[currentLevel-1]));
        System.out.println("[Info] Level Selected :"+newLevel);
        levelsFrame.dispose();
        if(currentLevel != newLevel){
            var selection = JOptionPane.showOptionDialog(null, "New Game will be started as you selected different level?", "Confirmation",JOptionPane.WARNING_MESSAGE,
                    0, null, newGameOptions, newGameOptions[0]);
            if(selection == 0){
                currentLevel = newLevel;
                this.restart(false);
                return;
            }
        }
        newGameFlag = false;
        thread.run();
    }

}
