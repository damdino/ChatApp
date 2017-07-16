package com.damdino.chatapp;

/**
 * Created by damdino on 9.7.2017.
 */

public class ChatModel {


    private String tarih;
    private String email;
    private String mesaj;

    public ChatModel() {


    }

    public ChatModel(String tarih, String mesaj, String email) {
        this.tarih = tarih;
        this.mesaj = mesaj;
        this.email = email;
    }


    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }


}
