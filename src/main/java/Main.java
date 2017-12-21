import javax.swing.*;

public class Main {

    private static JFrame mainFrame;

    public void newGame() {
        mainFrame.dispose();
        mainFrame = new MainFrame();
    }

    public static void main(String[] args) {
        mainFrame = new MainFrame();
    }
}