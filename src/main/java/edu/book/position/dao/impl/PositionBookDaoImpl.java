package edu.book.position.dao.impl;

import edu.book.position.dao.PositionBookDao;
import edu.book.position.exception.TradeOrderException;
import edu.book.position.model.TradeEventType;
import edu.book.position.model.TradeOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PositionBookDaoImpl implements PositionBookDao {

    private List<TradeOrder> tradeOrders;

    public PositionBookDaoImpl() {
        tradeOrders = new ArrayList();
    }

    @Override
    public TradeOrder saveTradeEvent(TradeOrder tradeOrder) throws TradeOrderException {
        if (tradeOrder.getTradeEventType() == TradeEventType.CANCEL && tradeOrders.stream().anyMatch(trade -> trade.getTradeId() == tradeOrder.getTradeId())) {
            tradeOrders.replaceAll(trade -> {
                trade.setQuantity(trade.getQuantity() - tradeOrder.getQuantity());
                return trade;
            });
        }
        tradeOrders.add(tradeOrder);
        return tradeOrder;
    }

    @Override
    public List<TradeOrder> getTradeDetailsByTradeId(long tradeId) {

        return tradeOrders.stream().filter(trades -> tradeId == trades.getTradeId())
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeOrder> getTradeDetails(String accountId, String securityId) {

        List<TradeOrder> tradeOrderList = new ArrayList();

        tradeOrderList.addAll(tradeOrders);

        return tradeOrderList.stream().filter(trades -> accountId.equalsIgnoreCase(trades.getAccountId()))
                .filter(trades -> securityId.equalsIgnoreCase(trades.getSecurityId()))
                .collect(Collectors.toList());
    }
}