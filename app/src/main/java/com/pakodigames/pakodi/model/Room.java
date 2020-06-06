package com.pakodigames.pakodi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room {

    static public int CREATE_ROOM = 0;
    static public int WAITING = 1;
    static public int PROGRESS = 2;
    public static int TIC_TAC_TOE = 0;
    public static int CONNECT_FOUR = 1;
    public static int PULI_MEKA = 2;

    private String roomId;
    private int roomType;
    private List<Player> players;
    private String playerTurn;
    private int state;
    public static String currentRoomId;

    public Room(){
    }

    public Room(String roomId,Player player,int roomType) {
        this.roomId = roomId;
        if(player!=null) {
            players = Arrays.asList(player);
        }
        state = CREATE_ROOM;
        this.roomType = roomType;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public void addPlayer(Player player){
        if(players == null){
            players = new ArrayList<Player>();
        }
        if(!players.contains(player)) {
            players.add(player);
        }
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState()
    {
        return state;
    }

    public Integer getPlayerPosition(String playerId) {
        for(int i =0;i<players.size();i++){
            if(players.get(i).getPlayerId().equals(playerId)){
                return i+1;
            }
        }
        return 0;
    }

    public void nextTurn() {
        for(int i=0;i<players.size();i++){
            if(players.get(i).getPlayerId().equals(playerTurn)){
                playerTurn = players.get((i+1)%players.size()).getPlayerId();
                return;
            }
        }
    }
}
