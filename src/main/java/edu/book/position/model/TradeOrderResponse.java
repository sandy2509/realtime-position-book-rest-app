package edu.book.position.model;

import java.util.List;
import java.util.Objects;

public class TradeOrderResponse {
    private Long quantity;
    private List<TradeOrder> tradeOrder;

    public Long getQuantity() {
        return quantity;
    }

    public TradeOrderResponse(Long quantity, List<TradeOrder> tradeOrder) {
        this.quantity = quantity;
        this.tradeOrder = tradeOrder;
    }

    public List<TradeOrder> getTradeOrder() {
        return tradeOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeOrderResponse that = (TradeOrderResponse) o;
        return Objects.equals(quantity, that.quantity) &&
                Objects.equals(tradeOrder, that.tradeOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, tradeOrder);
    }

    @Override
    public String toString() {
        return "TradeOrderResponse{" +
                "quantity=" + quantity +
                ", tradeOrder=" + tradeOrder +
                '}';
    }
}
