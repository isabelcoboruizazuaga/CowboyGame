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
import com.example.cowboygame.Models.Player;
import com.example.cowboygame.PlayerGames;
import com.example.cowboygame.R;

import java.util.ArrayList;

public class AdapterPlayers extends RecyclerView.Adapter<AdapterPlayers.AdapterPlayerViewHolder> {
    private ArrayList<Player> players;
    private Context context;

    //AdapterPlayers's constructor
    public AdapterPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    @NonNull
    @Override
    public AdapterPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The view is inflated
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);

        //The view holder is created
        AdapterPlayerViewHolder avh = new AdapterPlayerViewHolder((itemView));
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlayerViewHolder holder, int position) {
        Player playerItem = players.get(position);

        String email= playerItem.getEmail();
        String name= playerItem.getName();
        String phone = playerItem.getPhone();

        //The player data are put into the layout
        holder.tv_email.setText(email);
        holder.tv_name.setText(name);
        holder.tv_phone.setText(phone);

        //Each item will have an OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When an item is pressed an option menu will be showed
                showDialog(v, playerItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class AdapterPlayerViewHolder extends RecyclerView.ViewHolder {
        //Layout items
        private TextView tv_email, tv_name,tv_phone;

        //ViewHolder constructor
        public AdapterPlayerViewHolder(View itemView) {
            super(itemView);

            //The context is given the correct value
            context = itemView.getContext();

            //Layout items initialization
            tv_email = (TextView) itemView.findViewById(R.id.tv_player_email);
            tv_name = (TextView) itemView.findViewById(R.id.tv_player_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_player_phone);
        }
    }


    //OptionDialog creation method
    //jugar, llamarlo por teléfono, enviarle un email o ver sus mejores partidas
    private void showDialog(View view,Player playerItem){
        //Initialization
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(context);
        optionDialog.setTitle(playerItem.getName());

        //Options creation
        CharSequence opciones[] = {"Jugar","Ver partidas","Llamar","Enviar email"};
        //OnClickMethod for each option
        optionDialog.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item){
                    case 0:
                        //Play
                        Intent intent= new Intent(context, MainActivity.class);
                        intent.putExtra("player",playerItem);
                        context.startActivity(intent);
                        break;
                    case 1:
                        //Stats
                        Intent intent2= new Intent(context, PlayerGames.class);
                        intent2.putExtra("player",playerItem);
                        context.startActivity(intent2);
                        break;

                    case 2:
                        //Call
                        Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + playerItem.getPhone()));
                        context.startActivity(intentCall);
                        break;
                    case 3:
                        //Email
                        Intent intentEmail = new Intent(Intent.ACTION_SEND);
                        intentEmail.setType("text/plain");
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Game");
                        intentEmail.putExtra(Intent.EXTRA_TEXT, "Hello");
                        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {playerItem.getEmail() });
                        context.startActivity(intentEmail);
                        break;
                }
            }
        });
        //Dialog creation
        AlertDialog alertDialog = optionDialog.create();
        alertDialog.show();
    }
}
