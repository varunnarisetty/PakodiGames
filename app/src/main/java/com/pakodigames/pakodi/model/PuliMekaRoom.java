package com.pakodigames.pakodi.model;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PuliMekaRoom extends Room {
    public static int EMPTY = 0;
    public static int LAMB = 1;
    public static int TIGER = 2;
    private List<Integer> cells;
    private int lambsOutside = 15;
    private int lambsDead = 0;
    private int selectedPosition = -1;
    private int turn = LAMB;

    private int lastMoveStart = -1;
    private int lastMoveEnd = -1;
    private int lastMoveJump = -1;
    private int lastMoveChar = EMPTY;
    private String lambPlayer;

    public PuliMekaRoom(){
        init();
    }

    public PuliMekaRoom(String roomId, Player player){
        super(roomId, player,Room.PULI_MEKA);
        init();
    }

    private void init(){
        cells = new ArrayList();
        for(int i=0;i<23;i++){
            cells.add(0);
        }
        cells.set(0,TIGER);
        cells.set(3,TIGER);
        cells.set(4,TIGER);
    }

    public List<Integer> getCells() {
        return cells;
    }

    public void setCells(List<Integer> cells) {
        this.cells = cells;
    }

    public int getLambsOutside() {
        return lambsOutside;
    }

    public void setLambsOutside(int lambsOutside) {
        this.lambsOutside = lambsOutside;
    }

    public boolean addLambAt(int position){
        if(cells.get(position) != EMPTY){
            return false;
        }
        cells.set(position,LAMB);
        lambsOutside--;
        return true;
    }

    public int getLambsDead() {
        return lambsDead;
    }

    public void setLambsDead(int lambsDead) {
        this.lambsDead = lambsDead;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getCellAt(int position){
        return cells.get(position);
    }

    public List<Integer> getAdjacentList(int position){
        List<Integer> adjacentList = Collections.emptyList();
        switch (position){
            case 0:
                adjacentList = getEmptyAdjacent(Arrays.asList(2,3,4,5));
                break;
            case 1:
                adjacentList = getEmptyAdjacent(Arrays.asList(2,7));
                break;
            case 2:
                adjacentList = getEmptyAdjacent(Arrays.asList(0,1,3,8));
                break;
            case 3:
                adjacentList = getEmptyAdjacent(Arrays.asList(0,2,4,9));
                break;
            case 4:
                adjacentList = getEmptyAdjacent(Arrays.asList(0,3,5,10));
                break;
            case 5:
                adjacentList = getEmptyAdjacent(Arrays.asList(0,4,6,11));
                break;
            case 6:
                adjacentList = getEmptyAdjacent(Arrays.asList(5,12));
                break;
            case 7:
                adjacentList = getEmptyAdjacent(Arrays.asList(1,8,13));
                break;
            case 8:
                adjacentList = getEmptyAdjacent(Arrays.asList(2,7,9,14));
                break;
            case 9:
                adjacentList = getEmptyAdjacent(Arrays.asList(3,8,10,15));
                break;
            case 10:
                adjacentList = getEmptyAdjacent(Arrays.asList(4,9,11,16));
                break;
            case 11:
                adjacentList = getEmptyAdjacent(Arrays.asList(5,10,12,17));
                break;
            case 12:
                adjacentList = getEmptyAdjacent(Arrays.asList(6,11,18));
                break;
            case 13:
                adjacentList = getEmptyAdjacent(Arrays.asList(7,14));
                break;
            case 14:
                adjacentList = getEmptyAdjacent(Arrays.asList(8,13,15,19));
                break;
            case 15:
                adjacentList = getEmptyAdjacent(Arrays.asList(9,14,16,20));
                break;
            case 16:
                adjacentList = getEmptyAdjacent(Arrays.asList(10,15,17,21));
                break;
            case 17:
                adjacentList = getEmptyAdjacent(Arrays.asList(11,16,18,22));
                break;
            case 18:
                adjacentList = getEmptyAdjacent(Arrays.asList(12,17));
                break;
            case 19:
                adjacentList = getEmptyAdjacent(Arrays.asList(14,20));
                break;
            case 20:
                adjacentList = getEmptyAdjacent(Arrays.asList(15,19,21));
                break;
            case 21:
                adjacentList = getEmptyAdjacent(Arrays.asList(16,20,22));
                break;
            case 22:
                adjacentList = getEmptyAdjacent(Arrays.asList(17,21));
                break;
        }
        return adjacentList;
    }

    private List<Integer> getEmptyAdjacent(List<Integer> possibles){
        List<Integer> validList = new ArrayList();
        for(int i=0;i<possibles.size();i++){
            if(cells.get(possibles.get(i)) == 0) {
                validList.add(possibles.get(i));
            }
        }
        return validList;
    }

    public List<Integer> getAdjacentJumpList(int position){
        List<Integer> adjacentList = new ArrayList();
        switch (position){
            case 0:
                if(checkJump(2,8))
                    adjacentList.add(8);
                if(checkJump(3,9))
                    adjacentList.add(9);
                if(checkJump(4,10))
                    adjacentList.add(10);
                if(checkJump(5,11))
                    adjacentList.add(11);
                break;
            case 1:
                if(checkJump(2,3))
                    adjacentList.add(3);
                if(checkJump(7,13))
                    adjacentList.add(13);
                break;
            case 2:
                if(checkJump(3,4))
                    adjacentList.add(4);
                if(checkJump(8,14))
                    adjacentList.add(14);
                break;
            case 3:
                if(checkJump(2,1))
                    adjacentList.add(1);
                if(checkJump(4,5))
                    adjacentList.add(5);
                if(checkJump(9,15))
                    adjacentList.add(15);
                break;
            case 4:
                if(checkJump(5,6))
                    adjacentList.add(6);
                if(checkJump(3,2))
                    adjacentList.add(2);
                if(checkJump(10,16))
                    adjacentList.add(16);
                break;
            case 5:
                if(checkJump(4,3))
                    adjacentList.add(3);
                if(checkJump(11,17))
                    adjacentList.add(17);
                break;
            case 6:
                if(checkJump(5,4))
                    adjacentList.add(4);
                if(checkJump(12,18))
                    adjacentList.add(18);
                break;
            case 7:
                if(checkJump(8,9))
                    adjacentList.add(9);
                break;
            case 8:
                if(checkJump(2,0))
                    adjacentList.add(0);
                if(checkJump(9,10))
                    adjacentList.add(10);
                if(checkJump(14,19))
                    adjacentList.add(19);
                break;
            case 9:
                if(checkJump(3,0))
                    adjacentList.add(0);
                if(checkJump(8,7))
                    adjacentList.add(7);
                if(checkJump(10,11))
                    adjacentList.add(11);
                if(checkJump(15,20))
                    adjacentList.add(20);
                break;
            case 10:
                if(checkJump(4,0))
                    adjacentList.add(0);
                if(checkJump(9,8))
                    adjacentList.add(8);
                if(checkJump(16,21))
                    adjacentList.add(21);
                if(checkJump(11,12))
                    adjacentList.add(12);
                break;
            case 11:
                if(checkJump(5,0))
                    adjacentList.add(0);
                if(checkJump(10,9))
                    adjacentList.add(9);
                if(checkJump(17,22))
                    adjacentList.add(22);
                break;
            case 12:
                if(checkJump(11,10))
                    adjacentList.add(10);
                break;
            case 13:
                if(checkJump(7,1))
                    adjacentList.add(1);
                if(checkJump(14,15))
                    adjacentList.add(15);
                break;
            case 14:
                if(checkJump(8,2))
                    adjacentList.add(2);
                if(checkJump(15,16))
                    adjacentList.add(16);
                break;
            case 15:
                if(checkJump(9,3))
                    adjacentList.add(3);
                if(checkJump(14,13))
                    adjacentList.add(13);
                if(checkJump(16,17))
                    adjacentList.add(17);
                break;
            case 16:
                if(checkJump(10,4))
                    adjacentList.add(4);
                if(checkJump(15,14))
                    adjacentList.add(14);
                if(checkJump(17,18))
                    adjacentList.add(18);
                break;
            case 17:
                if(checkJump(11,5))
                    adjacentList.add(5);
                if(checkJump(16,15))
                    adjacentList.add(15);
                break;
            case 18:
                if(checkJump(12,6))
                    adjacentList.add(6);
                if(checkJump(17,16))
                    adjacentList.add(16);
                break;
            case 19:
                if(checkJump(14,8))
                    adjacentList.add(8);
                if(checkJump(20,21))
                    adjacentList.add(21);
                break;
            case 20:
                if(checkJump(15,9))
                    adjacentList.add(9);
                if(checkJump(21,22))
                    adjacentList.add(22);
                break;
            case 21:
                if(checkJump(16,10))
                    adjacentList.add(10);
                if(checkJump(20,19))
                    adjacentList.add(19);
                break;
            case 22:
                if(checkJump(21,20))
                    adjacentList.add(20);
                if(checkJump(17,11))
                    adjacentList.add(11);
                break;
        }
        return adjacentList;
    }

    private boolean checkJump(int jumpOver, int destPos){
        if(cells.get(jumpOver) == LAMB && cells.get(destPos) == EMPTY)
            return true;
        return false;
    }


    public PointF getPoint(int p, int h){
        switch (p){
            case 0:
                return new PointF(50.0f*h,0.0f*h);
            case 1:
                return new PointF(0.0f*h,25.0f*h);
            case 2:
                return new PointF(37.5f*h,25.0f*h);
            case 3:
                return new PointF(45.83f*h,25.0f*h);
            case 4:
                return new PointF(54.17f*h,25.0f*h);
            case 5:
                return new PointF(62.5f*h,25.0f*h);
            case 6:
                return new PointF(100.0f*h,25.0f*h);
            case 7:
                return new PointF(0.0f*h,50.0f*h);
            case 8:
                return new PointF(25.0f*h,50.0f*h);
            case 9:
                return new PointF(41.67f*h,50.0f*h);
            case 10:
                return new PointF(58.33f*h,50.0f*h);
            case 11:
                return new PointF(75.0f*h,50.0f*h);
            case 12:
                return new PointF(100.0f*h,50.0f*h);
            case 13:
                return new PointF(0.0f*h,75.0f*h);
            case 14:
                return new PointF(12.5f*h,75.0f*h);
            case 15:
                return new PointF(37.5f*h,75.0f*h);
            case 16:
                return new PointF(62.5f*h,75.0f*h);
            case 17:
                return new PointF(87.5f*h,75.0f*h);
            case 18:
                return new PointF(100.0f*h,75.0f*h);
            case 19:
                return new PointF(0.0f*h,100.0f*h);
            case 20:
                return new PointF(33.33f*h,100.0f*h);
            case 21:
                return new PointF(66.66f*h,100.0f*h);
            case 22:
                return new PointF(99.99f*h,100.0f*h);
        }
        return new PointF(50f,0f);
    }

    public void setCellAt(int selectedPosition, int i) {
        cells.set(selectedPosition,i);
    }

    public int getLastMoveStart() {
        return lastMoveStart;
    }

    public void setLastMoveStart(int lastMoveStart) {
        this.lastMoveStart = lastMoveStart;
    }

    public int getLastMoveEnd() {
        return lastMoveEnd;
    }

    public void setLastMoveEnd(int lastMoveEnd) {
        this.lastMoveEnd = lastMoveEnd;
    }

    public int getLastMoveJump() {
        return lastMoveJump;
    }

    public void setLastMoveJump(int lastMoveJump) {
        this.lastMoveJump = lastMoveJump;
    }

    public int getLastMoveChar() {
        return lastMoveChar;
    }

    public void setLastMoveChar(int lastMoveChar) {
        this.lastMoveChar = lastMoveChar;
    }

    public void setLambPlayer(String player){
        lambPlayer = player;
    }

    public String getLambPlayer(){
        return lambPlayer;
    }

    public int currentTurn(){
        if(lambPlayer == null){
            lambPlayer = getPlayerTurn();
        }
        if(getPlayerTurn().equals(lambPlayer)){
            return LAMB;
        }else{
            return TIGER;
        }
    }

    public void killLamb(int killPosition) {
        if(killPosition != -1 && cells.get(killPosition) == LAMB){
            cells.set(killPosition,EMPTY);
            lambsDead++;
        }
    }

    public int getKillPosition(int start, int end) {
            switch (start){
                case 0:
                    if(checkJump(2,8))
                        return 2;
                    if(checkJump(3,9))
                        return 3;
                    if(checkJump(4,10))
                        return 4;
                    if(checkJump(5,11))
                        return 5;
                    break;
                case 1:
                    if(checkJump(2,3))
                        return 2;
                    if(checkJump(7,13))
                        return 7;
                    break;
                case 2:
                    if(checkJump(3,4))
                        return 3;
                    if(checkJump(8,14))
                        return 8;
                    break;
                case 3:
                    if(checkJump(2,1))
                        return 2;
                    if(checkJump(4,5))
                        return 4;
                    if(checkJump(9,15))
                        return 9;
                    break;
                case 4:
                    if(checkJump(5,6))
                        return 5;
                    if(checkJump(3,2))
                        return 3;
                    if(checkJump(10,16))
                        return 10;
                    break;
                case 5:
                    if(checkJump(4,3))
                        return 4;
                    if(checkJump(11,17))
                        return 11;
                    break;
                case 6:
                    if(checkJump(5,4))
                        return 5;
                    if(checkJump(12,18))
                        return 12;
                    break;
                case 7:
                    if(checkJump(8,9))
                        return 8;
                    break;
                case 8:
                    if(checkJump(2,0))
                        return 2;
                    if(checkJump(9,10))
                        return 9;
                    if(checkJump(14,19))
                        return 14;
                    break;
                case 9:
                    if(checkJump(3,0))
                        return 3;
                    if(checkJump(8,7))
                        return 8;
                    if(checkJump(10,11))
                        return 10;
                    if(checkJump(15,20))
                        return 15;
                    break;
                case 10:
                    if(checkJump(4,0))
                        return 4;
                    if(checkJump(9,8))
                        return 9;
                    if(checkJump(16,21))
                        return 16;
                    if(checkJump(11,12))
                        return 11;
                    break;
                case 11:
                    if(checkJump(5,0))
                        return 5;
                    if(checkJump(10,9))
                        return 10;
                    if(checkJump(17,22))
                        return 17;
                    break;
                case 12:
                    if(checkJump(11,10))
                        return 11;
                    break;
                case 13:
                    if(checkJump(7,1))
                        return 7;
                    if(checkJump(14,15))
                        return 14;
                    break;
                case 14:
                    if(checkJump(8,2))
                        return 8;
                    if(checkJump(15,16))
                        return 15;
                    break;
                case 15:
                    if(checkJump(9,3))
                        return 9;
                    if(checkJump(14,13))
                        return 14;
                    if(checkJump(16,17))
                        return 16;
                    break;
                case 16:
                    if(checkJump(10,4))
                        return 10;
                    if(checkJump(15,14))
                        return 15;
                    if(checkJump(17,18))
                        return 17;
                    break;
                case 17:
                    if(checkJump(11,5))
                        return 4;
                    if(checkJump(16,15))
                        return 16;
                    break;
                case 18:
                    if(checkJump(12,6))
                        return 12;
                    if(checkJump(17,16))
                        return 17;
                    break;
                case 19:
                    if(checkJump(14,8))
                        return 14;
                    if(checkJump(20,21))
                        return 20;
                    break;
                case 20:
                    if(checkJump(15,9))
                        return 15;
                    if(checkJump(21,22))
                        return 21;
                    break;
                case 21:
                    if(checkJump(16,10))
                        return 16;
                    if(checkJump(20,19))
                        return 20;
                    break;
                case 22:
                    if(checkJump(21,20))
                        return 21;
                    if(checkJump(17,11))
                        return 17;
                    break;
            }
            return -1;
    }
}
