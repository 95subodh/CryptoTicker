package com.apps.sky.cryptoticker.GlobalFunctions;

import com.apps.sky.cryptoticker.StockPage.AddToMyPortfolioForm.TradeObject;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 15/10/17.
 */

public class CryptoTradeObject {

    private String crypto;
    private ArrayList<TradeObject> trades = new ArrayList<TradeObject>();

    public void setCrypto (String crypto) { this.crypto = crypto; }

    public String getCrypto () { return this.crypto; }

    public void setTrades (ArrayList<TradeObject> trades) { this.trades = trades; }

    public ArrayList<TradeObject> getTrades () { return this.trades; }

}
