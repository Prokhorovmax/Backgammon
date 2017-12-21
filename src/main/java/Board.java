
public class Board {

    Cell[] cells;
    Dice dices;

    public Board() {
        this.cells = new Cell[26];
        this.dices = new Dice();
        createBoard();
    }

    private void createBoard() {
        for (int i = 0; i < 26; i++) {
            cells[i] = new Cell();
        }
        cells[1].setColor(PlayerColor.BLACK);
        cells[1].setNumber(2);
        cells[6].setColor(PlayerColor.WHITE);
        cells[6].setNumber(5);
        cells[8].setColor(PlayerColor.WHITE);
        cells[8].setNumber(3);
        cells[12].setColor(PlayerColor.BLACK);
        cells[12].setNumber(5);
        cells[13].setColor(PlayerColor.WHITE);
        cells[13].setNumber(5);
        cells[17].setColor(PlayerColor.BLACK);
        cells[17].setNumber(3);
        cells[19].setColor(PlayerColor.BLACK);
        cells[19].setNumber(5);
        cells[24].setColor(PlayerColor.WHITE);
        cells[24].setNumber(2);
    }

    public void move(Move move) {
        this.cells[move.getStart()].checkerGone();
        this.cells[move.getFinish()].checkerCame(move.getPlayerColor());
    }
}