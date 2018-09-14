package edu.book.position.model;

import java.util.Objects;

public class TradeOrder {
    private long tradeId;
    private String securityId;
    private String accountId;
    private long quantity;
    private TradeEventType tradeEventType;

    private TradeOrder(long tradeId, String securityId, String accountId,long quantity,TradeEventType tradeEventType) {
        this.tradeId = tradeId;
        this.securityId = securityId;
        this.accountId = accountId;
        this.quantity = quantity;
        this.tradeEventType = tradeEventType;
    }

    public long getTradeId() {
        return tradeId;
    }

    public String getSecurityId() {
        return securityId;
    }

    public String getAccountId() {
        return accountId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public TradeEventType getTradeEventType() {
        return tradeEventType;
    }

    public static class TradeOrderBuilder {
        private long tradeId;
        private String securityId;
        private String accountId;
        private long quantity;
        private TradeEventType tradeEventType;

        public TradeOrderBuilder addTradeId(long tradeId) {
            this.tradeId = tradeId;
            return this;
        }

        public TradeOrderBuilder addSecurityId(String securityId) {
            this.securityId = securityId;
            return this;
        }

        public TradeOrderBuilder addAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public TradeOrderBuilder addQuantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public TradeOrderBuilder addTradeEventType(TradeEventType tradeEventType) {
            this.tradeEventType = tradeEventType;
            return this;
        }

        public TradeOrder build() {
            return new TradeOrder(tradeId,securityId,accountId,quantity,tradeEventType);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeOrder that = (TradeOrder) o;
        return tradeId == that.tradeId &&
                quantity == that.quantity &&
                Objects.equals(securityId, that.securityId) &&
                Objects.equals(accountId, that.accountId) &&
                tradeEventType == that.tradeEventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, securityId, accountId, quantity, tradeEventType);
    }

    @Override
	public String toString() {
		return "TradeOrder{" +
				"tradeId=" + tradeId +
				", securityId='" + securityId + '\'' +
				", accountId='" + accountId + '\'' +
				", quantity=" + quantity +
				", tradeEventType=" + tradeEventType +
				'}';
	}
}
