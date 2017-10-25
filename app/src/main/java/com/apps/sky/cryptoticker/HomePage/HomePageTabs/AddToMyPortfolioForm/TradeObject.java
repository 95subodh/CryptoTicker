package com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class TradeObject {

    private String tradeNumber, cost, quantity, cryptoID;

    public String getCryptoID() {
        return cryptoID;
    }

    public void setCryptoID(String cryptoID) {
        this.cryptoID = cryptoID;
    }

    public String getTradeNumber() { return tradeNumber; }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) { this.cost = cost; }

    public String getQuantity() { return quantity; }

    public void setQuantity(String quantity) { this.quantity = quantity; }

}
