import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Logic {

    private PlayerColor playerColor;

    public Logic(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public void makeTurn(Board board) {
        System.out.println();
        System.out.println("Dice: " + board.dices.getA() + ", " + board.dices.getB());
        final List<Move> bestMoves = searchForBestMove(board, initializePossibleMoves(board));
        for (Move move : bestMoves) {
            if (board.cells[move.getStart()].getNumber() > 0) {
                board.move(move);
                Core.addLastMove(move);
                System.out.println("Start: " + move.getStart());
                System.out.println("Finish: " + move.getFinish());
            }
        }
    }

    private List<Move> searchForBestMove(Board board, LinkedList<Move> possibleMoves) {
        List<Move> result = new ArrayList<>();
        LinkedList<Integer> dices = new LinkedList<>();
        final int a = board.dices.getA();
        final int b = board.dices.getB();
        dices.add(a);
        dices.add(b);
        if (a == b) {
            dices.add(a);
            dices.add(b);
        }
        while (dices.size() != 0) {
            if (possibleMoves.size() != 0) {
                Move move = possibleMoves.pollFirst();
                if ((move.getAmount() == a + b) && dices.contains(a) && dices.contains(b) &&
                        !board.cells[move.getStart()].isEmpty()) {
                    result.add(move);
                    dices.remove((Integer) a);
                    dices.remove((Integer) b);
                    continue;
                }
                if ((move.getAmount() == a) && dices.contains(a) && !board.cells[move.getStart()].isEmpty()) {
                    result.add(move);
                    dices.remove((Integer) a);
                    continue;
                }
                if ((move.getAmount() == b) && dices.contains(b) && !board.cells[move.getStart()].isEmpty()) {
                    result.add(move);
                    dices.remove((Integer) b);
                }
            } else {
                break;
            }
        }
        return result;
    }

    private LinkedList<Move> initializePossibleMoves(Board board) {
        LinkedList<Move> possibleMoves = new LinkedList<>();
        final int a = board.dices.getA();
        final int b = board.dices.getB();
        for (int i = 1; i <= board.cells.length - 2; i++) {
            if (!board.cells[i].isEmpty() && (board.cells[i].getColor() == playerColor)) {
                Move move1 = new Move(i, a, playerColor);
                Move move2 = new Move(i, b, playerColor);
                Move move3 = new Move(i, a + b, playerColor);
                if (Core.isMovePossible(move1, board)) {
                    move1.setWeight(estimateMove(move1, board));
                    possibleMoves.add(move1);
                }
                if (Core.isMovePossible(move2, board)) {
                    move2.setWeight(estimateMove(move2, board));
                    possibleMoves.add(move2);
                }
                if (Core.isMovePossible(move3, board)) {
                    move3.setWeight(estimateMove(move3, board));
                    possibleMoves.add(move3);
                }
            }
        }
        possibleMoves.sort((Move o1, Move o2) -> Integer.compare(o2.getWeight(), o1.getWeight()));
        return possibleMoves;
    }

    private int estimateMove(Move move, Board board) {
        int result = 0;
        if (move.isBearoff()) {
            result += 150; // Bears the checker off
        }
        if ((playerColor == PlayerColor.WHITE && (move.getFinish() < 7)) ||
                (playerColor == PlayerColor.BLACK && (move.getFinish() > 18))) {
            result += 15; // Brings the checker to the own inner quadrant
        }
        if ((playerColor == PlayerColor.WHITE && (move.getFinish() < 19)) ||
                (playerColor == PlayerColor.BLACK && (move.getFinish() > 6))) {
            result += 20; // Takes the checker out of the opponent inner quadrant
        }
        if (board.cells[move.getFinish()].isEmpty()) {
            result += 30; // Occupies the empty cell
        }
        if (board.cells[move.getStart()].getNumber() == 1) {
            result -= 30; // Leaves cell empty
        }
        if (board.cells[move.getFinish()].getNumber() > 3) {
            result -= 5; // Moves checker to busy cell (Better to occupy more empty cells)
        }

        /*
         Searching for moves that separate checkers that were closer than 6 points
         or, opposite, bring closer checkers that were more than 6 points far from each other
         */
        if (playerColor == PlayerColor.WHITE) {
            final int beginning = (move.getStart() + 6 >= 24) ? 24 : move.getStart() + 6;
            final int ending = (move.getFinish() - 6 <= 1) ? 1 : move.getFinish() - 6;
            for (int i = beginning; i > ending - 1; i--) {
                if (i == move.getStart() || i == move.getFinish()) continue;
                if (!board.cells[i].isEmpty() && (board.cells[i].getColor() == playerColor) &&
                        Math.abs(i - move.getFinish()) > 6 && Math.abs(i - move.getStart()) < 6) {
                    result -= 1;
                }
                if (!board.cells[i].isEmpty() && (board.cells[i].getColor() == playerColor) &&
                        Math.abs(i - move.getStart()) > 6 && Math.abs(i - move.getFinish()) < 6) {
                    result += 2;
                }
            }
        } else {
            final int beginning = (move.getStart() - 6 <= 1) ? 1 : move.getStart() - 6;
            final int ending = (move.getFinish() + 6 >= 24) ? 24 : move.getFinish() + 6;
            for (int i = beginning; i < ending + 1; i++) {
                if (i == move.getStart() || i == move.getFinish()) continue;
                if (!board.cells[i].isEmpty() && (board.cells[i].getColor() == playerColor) &&
                        Math.abs(i - move.getFinish()) > 6 && Math.abs(i - move.getStart()) < 6) {
                    result -= 1;
                }
                if (!board.cells[i].isEmpty() && (board.cells[i].getColor() == playerColor) &&
                        Math.abs(i - move.getStart()) > 6 && Math.abs(i - move.getFinish()) < 6) {
                    result += 2;
                }
            }
        }

        /*
         Searching for moves that leave or close up a blot
         */
        if ((move.getStart() > 1) && (move.getStart() < 24) && board.cells[move.getStart()].getNumber() == 1 &&
                !board.cells[move.getStart() - 1].isEmpty() && !board.cells[move.getStart() + 1].isEmpty() &&
                board.cells[move.getStart() - 1].getColor() == playerColor &&
                board.cells[move.getStart() + 1].getColor() == playerColor) {
            result -= 5;
        }
        if ((move.getFinish() > 1) && (move.getFinish() < 24) && board.cells[move.getFinish()].isEmpty() &&
                !board.cells[move.getFinish() - 1].isEmpty() && !board.cells[move.getFinish() + 1].isEmpty() &&
                board.cells[move.getFinish() - 1].getColor() == playerColor &&
                board.cells[move.getFinish() + 1].getColor() == playerColor) {
            result += 6;
        }
        return result;
    }
}