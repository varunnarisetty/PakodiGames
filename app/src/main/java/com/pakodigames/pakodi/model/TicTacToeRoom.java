package com.pakodigames.pakodi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToeRoom extends Room {

    private List<Integer> gameState;
    private int[][] winCombinations = {{0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {6,4,2}};

    public TicTacToeRoom(){

    }

    public TicTacToeRoom(String roomId,Player player) {
        super(roomId,player,Room.TIC_TAC_TOE);
        gameState = Arrays.asList(0,0,0,0,0,0,0,0,0);
    }

    public List<Integer> getGameState() {
        return gameState;
    }

    public void setGameState(ArrayList<Integer> gameState) {
        this.gameState = gameState;
    }

    public String isWon(){
        for(int i=0;i<winCombinations.length;i++){
            if(areAllEqual(winCombinations[i][0],winCombinations[i][1],winCombinations[i][2])){
                return getPlayers().get(gameState.get(winCombinations[i][0])-1).getPlayerId();
            }
        }
        return null;
    }

    private boolean areAllEqual(int a, int b, int c){
        if(gameState.get(a) == 0){
            return false;
        }
        if(gameState.get(a) == gameState.get(b) && gameState.get(a) == gameState.get(c)){
            return true;
        }else{
            return false;
        }
    }

}
