package com.example.cowboygame.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cowboygame.MainActivity;
import com.example.cowboygame.Models.Game;
import com.example.cowboygame.Models.Player;
import com.example.cowboygame.R;

import java.util.ArrayList;

public class AdapterGames extends RecyclerView.Adapter<AdapterGames.AdapterGamesViewHolder> {
    private ArrayList<Game> games;
    private Context context;

    //AdapterPlayers's constructor
    public AdapterGames(ArrayList<Game> games) {
        this.games = games;
    }
    @NonNull
    @Override
    public AdapterGames.AdapterGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The view is inflated
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_item, parent, false);

        //The view holder is created
        AdapterGames.AdapterGamesViewHolder avh = new AdapterGames.AdapterGamesViewHolder((itemView));
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGames.AdapterGamesViewHolder holder, int position) {
        Game gameItem = games.get(position);

        String email= gameItem.getEmail();
        long hour= gameItem.getHour();
        int score = gameItem.getScore();
        long timeLeft= gameItem.getTime();

        //The player data are put into the layout
        holder.tv_hour.setText(String.valueOf(hour));
        holder.tv_email.setText(email);
        holder.tv_score.setText(String.valueOf(score));
        holder.tv_timeLeft.setText(String.valueOf(timeLeft));

    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class AdapterGamesViewHolder extends RecyclerView.ViewHolder {
        //Layout items
        private TextView tv_hour, tv_email,tv_score,tv_timeLeft;

        //ViewHolder constructor
        public AdapterGamesViewHolder(View itemView) {
            super(itemView);

            //The context is given the correct value
            context = itemView.getContext();

            //Layout items initialization
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);
            tv_hour = (TextView) itemView.findViewById(R.id.tv_hour);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            tv_timeLeft = (TextView) itemView.findViewById(R.id.tv_timeLeft);
        }
    }
}

