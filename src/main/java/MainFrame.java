import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;


public class MainFrame extends JFrame {

    public MainFrame() {

        setSize(1000, 600);
        setTitle("Backgammon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width / 2 - getWidth() / 2, size.height / 2 - getHeight() / 2);

        setLayout(new BorderLayout());

        JButton resetButton = new JButton("RESET");
        JButton endTurnButton = new JButton("END TURN");
        JButton undoButton = new JButton("UNDO");
        JPanel toolbar = new JPanel();

        toolbar.setLayout(new FlowLayout());
        toolbar.add(resetButton);
        toolbar.add(endTurnButton);
        toolbar.add(undoButton);
        toolbar.setBackground(Color.GRAY);

        Core core = new Core();
        add(core.getBoardPanel(), CENTER);
        add(toolbar, NORTH);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(
                e -> new Main().newGame()
        );
        endTurnButton.setFocusPainted(false);
        endTurnButton.addActionListener(
                e -> {
                    core.endTurn();
                }
        );
        undoButton.setFocusPainted(false);
        undoButton.addActionListener(
                e -> {
                    core.undo();
                }
        );

        setVisible(true);
    }
}