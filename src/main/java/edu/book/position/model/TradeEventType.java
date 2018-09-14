package edu.book.position.model;

public enum TradeEventType {
    BUY("B"), SELL("S"),CANCEL("C");

    private String tradeEventType;

    TradeEventType(String tradeEventType) {
        this.setTradeEventType(tradeEventType);
    }

    public String getTradeEventType() {
        return tradeEventType;
    }

    public void setTradeEventType(String tradeEventType) {
        this.tradeEventType = tradeEventType;
    }
}
