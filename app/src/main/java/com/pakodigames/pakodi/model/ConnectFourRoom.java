package com.pakodigames.pakodi.model;


import java.util.ArrayList;
import java.util.List;

public class ConnectFourRoom extends Room {
    private int numCols = 7;
    private int numRows = 6;
    private boolean hasWinner;
    private List<Integer> cells;
    private int lastUpdateCol = -2;
    public int turn;

    public ConnectFourRoom(){

    }

    public ConnectFourRoom(String roomId,Player player){
        super(roomId,player,Room.CONNECT_FOUR);
        init();
    }

    public ConnectFourRoom(int cols, int rows) {
        init();
    }

    public void init(){
        numCols = 7;
        numRows = 6;
        cells = new ArrayList();
        for(int i=0;i<numRows*numCols;i++){
            cells.add(0);
        }
        reset();
    }

    public void reset() {
        hasWinner = false;
        turn = getPlayerPosition(Player.currentPlayer.getPlayerId());
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numRows; row++) {
                cells(col,row,0);
            }
        }
    }

    public void cells(int col, int row, int i) {
        cells.set(col*numRows+row,i);
    }

    public int cells(int col,int row){
        return cells.get(col*numRows+row);
    }

    public int getLastUpdateCol() {
        return lastUpdateCol;
    }

    public void setLastUpdateCol(int lastUpdateCol) {
        this.lastUpdateCol = lastUpdateCol;
    }

    public boolean isHasWinner() {
        return hasWinner;
    }

    public void setHasWinner(boolean hasWinner) {
        this.hasWinner = hasWinner;
    }

    public List<Integer> getCells() {
        return cells;
    }

    public void setCells(List<Integer> cells) {
        this.cells = cells;
    }

    public int lastAvailableRow(int col) {
        if(col == -2){
            return -1;
        }
        for (int row = numRows - 1; row >= 0; row--) {
            if (cells(col,row) == 0) {
                return row;
            }
        }
        return -1;
    }

    public void occupyCell(int col, int row) {
        cells(col,row,turn);
//        cells[col][row].setPlayer(turn);
    }



    public void toggleTurn() {
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
        }
    }

    public boolean checkForWin(int c, int r) {
        for (int col = 0; col < numCols; col++) {
            if (isContiguous(turn, 0, 1, col, 0, 0) || isContiguous(turn, 1, 1, col, 0, 0) || isContiguous(turn, -1, 1, col, 0, 0)) {
                //hasWinner = true;
                return true;
            }
        }
        for (int row = 0; row < numRows; row++) {
            if (isContiguous(turn, 1, 0, 0, row, 0) || isContiguous(turn, 1, 1, 0, row, 0) || isContiguous(turn, -1, 1, numCols - 1, row, 0)) {
                //hasWinner = true;
                return true;
            }
        }
        return false;
    }

    private boolean isContiguous(int player, int dirX, int dirY, int col, int row, int count) {
        if (count >= 4) {
            return true;
        }
        if (col < 0 || col >= numCols || row < 0 || row >= numRows) {
            return false;
        }
        int cell = cells(col,row);
        if (cell == player) {
            return isContiguous(player, dirX, dirY, col + dirX, row + dirY, count + 1);
        } else {
            return isContiguous(player, dirX, dirY, col + dirX, row + dirY, 0);
        }
    }
}
