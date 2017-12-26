package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 15/10/17.
 */

public class CryptoTradeObject {

    private String cryptoID;
    private ArrayList<TradeObject> trades = new ArrayList<>();

    public void setCryptoID(String cryptoID) {
        this.cryptoID = cryptoID;
    }

    public String getCryptoID() {
        return this.cryptoID;
    }

    public void setTrades(ArrayList<TradeObject> trades) {
        this.trades = trades;
    }

    public ArrayList<TradeObject> getTrades() {
        return this.trades;
    }

}
