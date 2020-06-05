package com.pakodigames.pakodi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.model.Player;

import java.util.List;

public class PlayerAdapter extends BaseAdapter {
    private List<Player> mPlayers;
    public PlayerAdapter(List<Player> players){
        mPlayers = players;
    }

    @Override
    public int getCount() {
        return mPlayers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlayers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.player_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name_tv);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image_iv);
        imageView.setImageResource(R.mipmap.emo_green);
        textView.setText(mPlayers.get(position).getPlayerName());

        return rowView;
    }

    public void updatePlayers(List<Player> players){
        mPlayers = players;
        notifyDataSetChanged();
    }
}
