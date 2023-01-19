import javax.swing.*;

public class LaunchGame {
    public static void main(String[] args){
        JFrame nameFrame = new JFrame();
        String name = JOptionPane.showInputDialog(nameFrame, "Please enter your name:", "Player1");
        System.out.println(name);
        nameFrame.dispose();
        Game game = new Game("MyGame",1000,500,name);
        game.start();
    }
}
