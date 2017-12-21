import java.util.LinkedList;

public class Core {

    static private LinkedList<Move> lastMoves = new LinkedList<>();
    private Logic logic = new Logic(PlayerColor.BLACK);
    private Board board;
    private BoardPanel boardPanel;

    public Core() {
        this.board = new Board();
        this.boardPanel = new BoardPanel(board);
    }

    private void checkWin() {
        if (board.cells[0].getNumber() == 15) {
            boardPanel.win(PlayerColor.WHITE);
        } else if (board.cells[25].getNumber() == 15) {
            boardPanel.win(PlayerColor.BLACK);
        }
    }

    public void endTurn() {
        while (boardPanel.lastPlayerMoves.size() != 0) {
            lastMoves.add(boardPanel.lastPlayerMoves.pollFirst());
        }
        boardPanel.lastPlayerMoves.clear();
        boardPanel.repaint();
        checkWin();
        board.dices.throwDices();
        logic.makeTurn(board);
        board.dices.throwDices();
        boardPanel.repaint();
        checkWin();
    }

    public BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    static public boolean isMovePossible(Move move, Board board) {
        if (board.cells[move.getStart()].isEmpty()) return false;
        if (!board.cells[move.getStart()].isEmpty() &&
                (board.cells[move.getStart()].getColor() != move.getPlayerColor())) return false;
        if (!board.cells[move.getFinish()].isEmpty() &&
                board.cells[move.getFinish()].getColor() != move.getPlayerColor()) return false;
        if (move.isBearoff()) {
            for (int i = 1; i < board.cells.length - 2; i++) {
                if ((!board.cells[i].isEmpty() && board.cells[i].getColor() == PlayerColor.WHITE && (i > 6) &&
                        (move.getPlayerColor() == PlayerColor.WHITE)) ||
                        (!board.cells[i].isEmpty() && board.cells[i].getColor() == PlayerColor.BLACK && (i < 19) &&
                                (move.getPlayerColor() == PlayerColor.BLACK))) {
                    return false;
                }
            }
        }
        final int a = board.dices.getA();
        final int b = board.dices.getB();
        final int amount = move.getAmount();
        return ((amount == a) || (amount == b) || (amount == a + b));
    }

    static void addLastMove(Move move) {
        lastMoves.add(move);
    }

    static void clearLastMoves() {
        lastMoves.clear();
    }

    public void undo() {
        while (lastMoves.size() != 0) {
            final Move move = lastMoves.pollFirst();
            board.move(new Move(move.getPlayerColor(), move.getFinish(), move.getStart()));
        }
        lastMoves.clear();
        boardPanel.repaint();
    }
}