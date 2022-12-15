import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
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
    private int score;
    private String LIVES_FORMAT = "Lives : %s";
    private int reamingLives;
    private int iteration = 0;


    public Game(String title, int width, int height) {
        this.heightOfWindow = height;
        this.widthOfWindow = width;
        this.titleOfwindow = title;
    }

    private void generateEnemies() {
        iteration++;
        numberOfEnemies = new Random().nextInt(MAX_NUMBER_ENEMIES_PER_MINUTE) + 1;
        System.out.println("Iteration : " + iteration);
        System.out.println("numberOfEnemies : " + numberOfEnemies);
        for (int i = 0; i < numberOfEnemies; i++) {
            //(int) ((Math.random() * (max - min)) + min)
            int x = (int) ((Math.random() * (widthOfWindow/4)));
            int y = (int) ((Math.random() * ((heightOfWindow -20) - 60)) + 60);
            enemies.add(new Coordinates(x, y));
        }
    }

    private void initialize() {
        score = 0;
        reamingLives = 10;
        displayGame = new DisplayGame(titleOfwindow, widthOfWindow, heightOfWindow);
        displayGame.getCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                eliminateEnemies(e.getX(),e.getY());
            }
        });
        generateEnemies();
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
                enemy.setX(enemy.getX() + 1);
            else {
                reamingLives--;
                enemiesToRemove.add(enemy);
            }
        }
        enemies.removeAll(enemiesToRemove);
        if (enemies.size() <= 1) {
            generateEnemies();
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
        graphics.drawRect(10,5,100,40);
        graphics.drawString(String.format(SCORE_FORMAT,score), 15,20);
        graphics.drawString(String.format(LIVES_FORMAT,reamingLives), 15,40);
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
}
