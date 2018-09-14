package edu.book.position.service.impl;

import edu.book.position.dao.PositionBookDao;
import edu.book.position.exception.TradeOrderException;
import edu.book.position.logger.Logger;
import edu.book.position.model.TradeEventType;
import edu.book.position.model.TradeOrder;
import edu.book.position.model.TradeOrderResponse;
import edu.book.position.service.PositionBookService;
import edu.book.position.validator.TradeOrderValidator;

import java.util.List;
import java.util.stream.Collectors;

public class PositionBookServiceImpl implements PositionBookService {

    private PositionBookDao positionBookDao;
    private TradeOrderValidator tradeOrderValidator;

    public PositionBookServiceImpl(PositionBookDao positionBookDao, TradeOrderValidator tradeOrderValidator) {
        this.positionBookDao = positionBookDao;
        this.tradeOrderValidator = tradeOrderValidator;
    }

    @Override
    public TradeOrder executeTradeEvent(TradeOrder tradeOrder) throws TradeOrderException {
        tradeOrderValidator.validateTradeOrderModel(tradeOrder);
        TradeOrder tradeOrderResponse = positionBookDao.saveTradeEvent(tradeOrder);

        Logger.logMessage(tradeOrder.getTradeEventType().name() + " order with Quantity " + tradeOrderResponse.getQuantity() + " executed successfully for " + tradeOrder.getSecurityId() + " and accountId " + tradeOrder.getAccountId());
        return tradeOrderResponse;
    }

    @Override
    public TradeOrder getTradeEventDetailsByTradeId(long tradeId) throws TradeOrderException {
        tradeOrderValidator.validateTradeId(tradeId);

        List<TradeOrder> tradeOrders = positionBookDao.getTradeDetailsByTradeId(tradeId);

        if (!tradeOrders.isEmpty() && tradeOrders.size()==1)
            return tradeOrders.get(0);
        else{
            Logger.logMessage("TradeDetails are invalid for the trade Id");
            throw new TradeOrderException("Trade Details are invalid for the trade Id");
        }
    }

    @Override
    public TradeOrderResponse getRealTimePositionOfTradeEvent(String accountId, String securityId) throws TradeOrderException {
        List<TradeOrder> totalTradeOrders = positionBookDao.getTradeDetails(accountId, securityId);

        Long buyOrder = calculateOrderQuantityByTradeType(totalTradeOrders, TradeEventType.BUY);
        Long sellOrder = calculateOrderQuantityByTradeType(totalTradeOrders, TradeEventType.SELL);

        Long remainingQuantity = buyOrder - sellOrder;

        Logger.logMessage("Realtime position for Account ID: " + accountId + " and security ID: " + securityId + " is " + remainingQuantity);

        return new TradeOrderResponse(remainingQuantity, totalTradeOrders);
    }

    private Long calculateOrderQuantityByTradeType(List<TradeOrder> filteredTradeOrder, TradeEventType tradeEventType) {
        return filteredTradeOrder.stream().filter(order -> tradeEventType == order.getTradeEventType())
                .collect(Collectors.summingLong(TradeOrder::getQuantity));
    }
}