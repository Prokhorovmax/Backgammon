import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardPanel extends JPanel {
    private Board board;
    private Polygon[] polygons;
    private List<Polygon> possiblePlayerMoves;
    private int lastMoveStart = 0;
    LinkedList<Move> lastPlayerMoves;

    public BoardPanel(Board board) {
        super();
        setBackground(Color.GRAY);
        this.board = board;
        this.possiblePlayerMoves = new ArrayList<>();
        this.lastPlayerMoves = new LinkedList<>();
        this.polygons = new Polygon[26];
        this.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            mPressed(e.getX(), e.getY());
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            mReleased(e.getX(), e.getY());
                        }
                    }
                }
        );

        for (int i = 1; i < 13; i++) {
            int x = 832 - 66 * i;
            int y = 510;
            int[] xPoints = {x, x + 33, x + 66};
            int[] yPoints = {y, y - 200, y};
            polygons[i] = new Polygon(xPoints, yPoints, 3);
        }
        for (int i = 13; i < 25; i++) {
            int x = 40 + 66 * (i - 13);
            int y = 10;
            int[] xPoints = {x, x + 33, x + 66};
            int[] yPoints = {y, y + 200, y};
            polygons[i] = new Polygon(xPoints, yPoints, 3);
        }
        polygons[25] = new Polygon(new int[]{860, 860, 920, 920}, new int[]{50, 250, 250, 50}, 4);
        polygons[0] = new Polygon(new int[]{860, 860, 920, 920}, new int[]{270, 470, 470, 270}, 4);
    }

    private void mPressed(int x, int y) {
        Core.clearLastMoves();
        for (int i = 0; i < board.cells.length; i++) {
            if (polygons[i].contains(x, y)) {
                if (board.cells[i].getColor() == PlayerColor.WHITE) {
                    board.cells[i].checkerGone();
                    lastMoveStart = i;
                    paintMove(i);
                }
            }
        }
    }

    private void mReleased(int x, int y) {
        for (int i = 0; i < board.cells.length; i++) {
            if (polygons[i].contains(x, y)) {
                if ((board.cells[i].getColor() != PlayerColor.BLACK) && (lastMoveStart != 0)) {
                    board.cells[i].checkerCame(PlayerColor.WHITE);
                    lastPlayerMoves.add(new Move(PlayerColor.WHITE, lastMoveStart, i));
                    lastMoveStart = 0;
                    eraseMove();
                }
            }
        }
    }

    private void paintMove(int i) {
        final int a = board.dices.getA();
        final int b = board.dices.getB();
        if ((i - a > 0) && (board.cells[i - a].getColor() != PlayerColor.BLACK)) {
            possiblePlayerMoves.add(polygons[i - a]);
        }
        if ((i - b > 0) && (board.cells[i - b].getColor() != PlayerColor.BLACK)) {
            possiblePlayerMoves.add(polygons[i - b]);
        }
        if ((i - a - b > 0) && (board.cells[i - a - b].getColor() != PlayerColor.BLACK)) {
            possiblePlayerMoves.add(polygons[i - a - b]);
        }
        repaint();
    }

    private void eraseMove() {
        possiblePlayerMoves.clear();
        repaint();
    }

    public void win(PlayerColor color) {
        if (color == PlayerColor.BLACK) {
            JOptionPane.showMessageDialog(null, "BLACK WON!");
        } else if (color == PlayerColor.WHITE) {
            JOptionPane.showMessageDialog(null, "WHITE WON!");
        }
        if (getMouseListeners().length != 0) {
            removeMouseListener(getMouseListeners()[0]);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(40, 10, 900, 500);
        g.setColor(Color.BLACK);
        g.drawLine(436, 10, 436, 510);
        g.drawLine(832, 10, 832, 510);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        for (int i = 0; i < 26; i++) {
            if ((i > 0) && (i < 25)) {
                g.setColor(new Color(139, 0, 0));
            } else if (i == 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }
            g.fillPolygon(polygons[i]);
            g.setColor(Color.BLACK);
            g.drawPolygon(polygons[i]);
        }

        for (int i = 1; i < 13; i++) {
            if (!board.cells[i].isEmpty()) {
                g.setColor((board.cells[i].getColor() == PlayerColor.BLACK) ? Color.BLACK : Color.WHITE);
                g.fillOval(polygons[i].xpoints[0] + 3, polygons[i].ypoints[0] - 60, 60, 60);
                g.setColor(Color.BLACK);
                g.drawOval(polygons[i].xpoints[0] + 3, polygons[i].ypoints[0] - 60, 60, 60);
                g.setColor(Color.ORANGE);
                g.drawString(board.cells[i].getNumber() + "", polygons[i].xpoints[0] + 23, polygons[i].ypoints[0] - 17);
            }
        }
        for (int i = 13; i < 25; i++) {
            if (!board.cells[i].isEmpty()) {
                g.setColor((board.cells[i].getColor() == PlayerColor.BLACK) ? Color.BLACK : Color.WHITE);
                g.fillOval(polygons[i].xpoints[0] + 3, polygons[i].ypoints[0], 60, 60);
                g.setColor(Color.BLACK);
                g.drawOval(polygons[i].xpoints[0] + 3, polygons[i].ypoints[0], 60, 60);
                g.setColor(Color.ORANGE);
                g.drawString(board.cells[i].getNumber() + "", polygons[i].xpoints[0] + 23, polygons[i].ypoints[0] + 40);
            }
        }
        if (!board.cells[0].isEmpty()) {
            g.setColor(Color.GREEN);
            if (board.cells[0].getNumber() < 10) {
                g.drawString(board.cells[0].getNumber() + "", 880, 380);
            } else {
                g.drawString(board.cells[0].getNumber() + "", 873, 380);
            }

        }
        if (!board.cells[25].isEmpty()) {
            g.setColor(Color.GREEN);
            if (board.cells[25].getNumber() < 10) {
                g.drawString(board.cells[25].getNumber() + "", 880, 160);
            } else {
                g.drawString(board.cells[25].getNumber() + "", 873, 160);
            }
        }

        g.setColor(Color.BLACK);
        g.drawRect(40, 10, 900, 500);
        g.setColor(Color.DARK_GRAY);
        g.drawString(board.dices.getA() + "", 410, 270);
        g.drawString(board.dices.getB() + "", 445, 270);

        for (Polygon polygon : possiblePlayerMoves) {
            if (polygon.ypoints[0] > 400) {
                g.setColor(Color.BLACK);
                g.drawOval(polygon.xpoints[1] - 20, polygon.ypoints[1], 40, 40);
                g.setColor(Color.YELLOW);
                g.fillOval(polygon.xpoints[1] - 20, polygon.ypoints[1], 40, 40);
            } else {
                g.setColor(Color.BLACK);
                g.drawOval(polygon.xpoints[1] - 20, polygon.ypoints[1] - 40, 40, 40);
                g.setColor(Color.YELLOW);
                g.fillOval(polygon.xpoints[1] - 20, polygon.ypoints[1] - 40, 40, 40);
            }
        }
    }
}