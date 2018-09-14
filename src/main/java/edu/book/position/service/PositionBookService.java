package edu.book.position.service;


import edu.book.position.exception.TradeOrderException;
import edu.book.position.model.TradeOrder;
import edu.book.position.model.TradeOrderResponse;

public interface PositionBookService {

    /**
     * Executes a trade event
     * depending on the Trade Type(viz. BUY,SELL or CANCEL)
     *
     * @param tradeOrder
     * @return Response
     * @throws TradeOrderException
     */
    TradeOrder executeTradeEvent(TradeOrder tradeOrder) throws TradeOrderException;

    /**
     * Generate the real time Position
     * of Trade Event based on accountId and SecurityId
     * @param accountId
     * @param securityId
     * @return Response
     * @throws TradeOrderException
     */
    TradeOrderResponse getRealTimePositionOfTradeEvent(String accountId, String securityId) throws TradeOrderException;

    /**
     * Get Trade Details filter by tradeId
     * @param tradeId
     * @return Response
     * @throws TradeOrderException
     */
    TradeOrder getTradeEventDetailsByTradeId(long tradeId) throws TradeOrderException;
}