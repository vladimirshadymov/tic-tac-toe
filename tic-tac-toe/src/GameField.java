import java.util.Random;

public class GameField {
    public int[][] game_field;
    public int block;  //crossed cells to win
    public int size;
    public boolean in_progress;
    public static Random rand = new Random();

    public GameField(int block, int size) {
        this.game_field = game_field;
        this.in_progress = true;
        this.size = size;
        this.block = block;
        this.game_field = new int[size][size];
    }

    public void reset(){
        this.game_field = new int[size][size];
        in_progress = true;
    }

    boolean checkDiagonal(int symb_id, int offsetX, int offsetY) {
        boolean toright, toleft;
        toright = true;
        toleft = true;
        for (int i=0; i<block; i++) {
            toright &= (game_field[i+offsetX][i+offsetY] == symb_id);
            toleft &= (game_field[block-i-1+offsetX][i+offsetY] == symb_id);
        }

        if (toright || toleft) return true;

        return false;
    }


    boolean checkLanes(int symb_id, int offsetX, int offsetY) {
        boolean cols, rows;
        for (int col=offsetX; col<block+offsetX; col++) {
            cols = true;
            rows = true;
            for (int row=offsetY; row<block+offsetY; row++) {
                cols &= (game_field[col][row] == symb_id);
                rows &= (game_field[row][col] == symb_id);
            }

            if (cols || rows) return true;
        }

        return false;
    }

    boolean isMapFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (game_field[i][j] == 0) return false;
            }
        }
        return true;
    }

    boolean isCellValid(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) return false;
        if (game_field[y][x] == 0) return true;
        return false;
    }

    void aiTurn() {
        if (in_progress) {
            int x, y;
            do {
                x =rand.nextInt(size);
                y =rand.nextInt(size);
            } while (!isCellValid(x, y));
            game_field[y][x] = 1;
        }
    }

    void humanTurn(int x, int y) {
        if (isCellValid(x,y) && in_progress) {
            game_field[x][y]=-1;
        }
    }

    boolean checkWin(int symb_id) {
        for (int col=0; col<size-block+1; col++) {
            for (int row=0; row<size-block+1; row++) {
                if (checkDiagonal(symb_id, col, row) || checkLanes(symb_id, col, row)) {
                    in_progress = false;
                    return true;
                }
            }
        }
        return false;
    }
}