package com.damdino.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cpiz.android.bubbleview.BubbleTextView;

import java.util.ArrayList;

/**
 * Created by damdino on 9.7.2017.
 */

public class CustomAdapter extends ArrayAdapter<ChatModel> {


    public CustomAdapter(Context context, ArrayList<ChatModel> chatList) {
        super(context, 0, chatList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }


        TextView tarih = (TextView) convertView.findViewById(R.id.tarih);
        TextView email = (TextView) convertView.findViewById(R.id.email);
        BubbleTextView mesaj = (BubbleTextView) convertView.findViewById(R.id.mesaj);

        ChatModel chatMesaj = getItem(position);

        tarih.setText(String.valueOf(chatMesaj.getTarih()));
        email.setText(chatMesaj.getEmail());
        mesaj.setText(chatMesaj.getMesaj());

        return convertView;

    }
}


