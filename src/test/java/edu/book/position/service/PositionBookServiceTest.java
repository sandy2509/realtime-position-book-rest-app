package edu.book.position.service;

import edu.book.position.dao.PositionBookDao;
import edu.book.position.dao.impl.PositionBookDaoImpl;
import edu.book.position.exception.TradeOrderException;
import edu.book.position.logger.Logger;
import edu.book.position.model.TradeEventType;
import edu.book.position.model.TradeOrder;
import edu.book.position.model.TradeOrderResponse;
import edu.book.position.service.impl.PositionBookServiceImpl;
import edu.book.position.validator.TradeOrderValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionBookServiceTest {

    private PositionBookService underTest;

    @Before
    public void setUp() {

        final PositionBookDao positionBookDao = new PositionBookDaoImpl();
        final TradeOrderValidator tradeOrderValidator = new TradeOrderValidator();

        underTest = new PositionBookServiceImpl(positionBookDao, tradeOrderValidator);
    }

    @Test
    public void shouldGetRealTimePositionOfTradeEvent() throws TradeOrderException {

        long buyOrderQuantity = 600L;
        long sellOrderQuantity = 100L;
        long expectedRemainingQuantity = 500L;
        int expectedOrderCountAfterTrade = 2;

        TradeOrder buyOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(0L)
                .addAccountId("ABC4")
                .addSecurityId("XYZ4")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        try {
            buyOrder = underTest.executeTradeEvent(buyOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }


        TradeOrderResponse buyOrderResponse = underTest.getRealTimePositionOfTradeEvent("ABC4", "XYZ4");

        assertEquals(1, buyOrderResponse.getTradeOrder().size());
        assertSame(buyOrder,buyOrderResponse.getTradeOrder().get(0));

        TradeOrder sellOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(1L)
                .addAccountId("ABC4")
                .addSecurityId("XYZ4")
                .addTradeEventType(TradeEventType.SELL)
                .addQuantity(sellOrderQuantity).build();

        try {
            underTest.executeTradeEvent(sellOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }

        TradeOrderResponse tradeOrderResponse = underTest.getRealTimePositionOfTradeEvent("ABC4", "XYZ4");

        Logger.logMessage("Consolidated Realtime report looks: "+tradeOrderResponse);
        assertEquals(expectedOrderCountAfterTrade, tradeOrderResponse.getTradeOrder().size());
        assertEquals(Long.valueOf(expectedRemainingQuantity), Long.valueOf(tradeOrderResponse.getQuantity()));
    }

    @Test
    public void shouldExecuteValidBuyTradeEvent() throws TradeOrderException {
        long buyOrderQuantity = 600L;
        long expectedRemainingQuantity = 600L;
        int expectedOrderCountAfterTrade = 1;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(0L)
                .addAccountId("ABC1")
                .addSecurityId("XYZ1")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        try {
            tradeOrder = underTest.executeTradeEvent(tradeOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }


        TradeOrderResponse tradeOrderResponse = underTest.getRealTimePositionOfTradeEvent("ABC1", "XYZ1");

        assertEquals(expectedOrderCountAfterTrade, tradeOrderResponse.getTradeOrder().size());
        assertSame(tradeOrder,tradeOrderResponse.getTradeOrder().get(0));
        assertEquals(Long.valueOf(expectedRemainingQuantity), Long.valueOf(tradeOrderResponse.getQuantity()));
    }

    @Test
    public void shouldExecuteValidSellTradeEvent() throws TradeOrderException {
        long sellOrderQuantity = 100L;
        long expectedRemainingQuantity = -100L;//minus denotes short-selling and buy order hasn't been executed
        int expectedOrderCountAfterTrade = 1;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(0L)
                .addAccountId("ABC2")
                .addSecurityId("XYZ2")
                .addTradeEventType(TradeEventType.SELL)
                .addQuantity(sellOrderQuantity).build();

        try {
            tradeOrder = underTest.executeTradeEvent(tradeOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }

        TradeOrderResponse tradeOrderResponse = underTest.getRealTimePositionOfTradeEvent("ABC2", "XYZ2");

        assertEquals(expectedOrderCountAfterTrade, tradeOrderResponse.getTradeOrder().size());
        assertSame(tradeOrder,tradeOrderResponse.getTradeOrder().get(0));
        assertEquals(Long.valueOf(expectedRemainingQuantity), Long.valueOf(tradeOrderResponse.getQuantity()));
    }

    @Test
    public void shouldExecuteCancelEventOnBuyOrder() throws TradeOrderException {
        long buyOrderQuantity = 100L;
        long cancelOrderQuantity = 40L;
        long expectedRemainingQuantity = 60L;
        int expectedOrderCountAfterTrade = 1;

        TradeOrder buyOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(0L)
                .addAccountId("ABC3")
                .addSecurityId("XYZ3")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        try {
            buyOrder = underTest.executeTradeEvent(buyOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }

        TradeOrder tradeOrderByTradeId = underTest.getTradeEventDetailsByTradeId(buyOrder.getTradeId());

        TradeOrder cancelOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeOrderByTradeId.getTradeId())
                .addAccountId(tradeOrderByTradeId.getAccountId())
                .addSecurityId(tradeOrderByTradeId.getSecurityId())
                .addTradeEventType(TradeEventType.CANCEL)
                .addQuantity(cancelOrderQuantity).build();

        try {
            underTest.executeTradeEvent(cancelOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }



        TradeOrderResponse tradeOrderResponse = underTest.getRealTimePositionOfTradeEvent("ABC3", "XYZ3");

        assertEquals(expectedOrderCountAfterTrade, tradeOrderResponse.getTradeOrder().size());
        assertEquals(Long.valueOf(expectedRemainingQuantity), Long.valueOf(tradeOrderResponse.getQuantity()));
    }


    @Test
    public void shouldExecuteCancelEventOnSellOrder() throws TradeOrderException {
        long sellOrderQuantity = 100L;
        long cancelOrderQuantity = 40L;
        long expectedRemainingQuantity = -60L; //minus denotes short-selling and buy order hasn't been executed
        int expectedOrderCountAfterTrade = 1;

        TradeOrder sellOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(0L)
                .addAccountId("ABC5")
                .addSecurityId("XYZ5")
                .addTradeEventType(TradeEventType.SELL)
                .addQuantity(sellOrderQuantity).build();

        try {
            sellOrder = underTest.executeTradeEvent(sellOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }

        TradeOrder tradeOrderByTradeId = underTest.getTradeEventDetailsByTradeId(sellOrder.getTradeId());

        TradeOrder cancelOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeOrderByTradeId.getTradeId())
                .addAccountId(tradeOrderByTradeId.getAccountId())
                .addSecurityId(tradeOrderByTradeId.getSecurityId())
                .addTradeEventType(TradeEventType.CANCEL)
                .addQuantity(cancelOrderQuantity).build();

        try {
            underTest.executeTradeEvent(cancelOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }

        TradeOrderResponse tradeOrderResponse = underTest.getRealTimePositionOfTradeEvent("ABC5", "XYZ5");

        assertEquals(expectedOrderCountAfterTrade, tradeOrderResponse.getTradeOrder().size());
        assertEquals(Long.valueOf(expectedRemainingQuantity), Long.valueOf(tradeOrderResponse.getQuantity()));
    }

    @Test
    public void shouldGetTradeEventDetailsByTradeId() throws TradeOrderException {
        long buyOrderQuantity = 600L;
        long tradeId = 99L;


        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("ACC1")
                .addSecurityId("SEC1")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        try {
            tradeOrder = underTest.executeTradeEvent(tradeOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }


        TradeOrder tradeOrderDetails = underTest.getTradeEventDetailsByTradeId(tradeId);

        assertSame(tradeOrderDetails,tradeOrder);
    }

    @Test(expected = TradeOrderException.class)
    public void shouldFailToGetTradeEventDetailsByInvalidTradeId() throws TradeOrderException {
        long sellOrderQuantity = 600L;
        long tradeId = 100L;
        long invalidTradeId = 101L;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("ACC1")
                .addSecurityId("SEC1")
                .addTradeEventType(TradeEventType.SELL)
                .addQuantity(sellOrderQuantity).build();

        try {
            underTest.executeTradeEvent(tradeOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }

        underTest.getTradeEventDetailsByTradeId(invalidTradeId);
    }
}