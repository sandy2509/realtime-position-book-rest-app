package edu.book.position.dao;

import edu.book.position.exception.TradeOrderException;
import edu.book.position.model.TradeOrder;

import java.util.List;

public interface PositionBookDao {

    /**
     * Record Trade Transaction
     *
     * @param tradeOrder TradeOrder object encapsulating Trade done
     * @return
     */
    TradeOrder saveTradeEvent(TradeOrder tradeOrder) throws TradeOrderException;

    List<TradeOrder> getTradeDetails(String accountId, String securityId);

    List<TradeOrder> getTradeDetailsByTradeId(long tradeId);
}

