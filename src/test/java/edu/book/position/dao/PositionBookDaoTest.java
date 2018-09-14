package edu.book.position.dao;

import edu.book.position.dao.impl.PositionBookDaoImpl;
import edu.book.position.exception.TradeOrderException;
import edu.book.position.logger.Logger;
import edu.book.position.model.TradeEventType;
import edu.book.position.model.TradeOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class PositionBookDaoTest {

	private PositionBookDao underTest;

	@Before
	public void setUp(){
		underTest = new PositionBookDaoImpl();
	}

    @Test
    public void shouldSaveBuyTradeEvent() throws TradeOrderException {
        long buyOrderQuantity = 600L;
        int expectedOrderCountAfterTrade = 1;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(0L)
                .addAccountId("ABC1")
                .addSecurityId("XYZ1")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        try {
            tradeOrder = underTest.saveTradeEvent(tradeOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }


        List<TradeOrder> tradeOrderResponse = underTest.getTradeDetails("ABC1", "XYZ1");

        assertEquals(expectedOrderCountAfterTrade, tradeOrderResponse.size());
        assertSame(tradeOrder,tradeOrderResponse.get(0));
    }

	@Test
	public void shouldGetTradeDetailsByTradeId(){
		long buyOrderQuantity = 600L;
		long tradeId = 99L;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("ACC2")
                .addSecurityId("SEC2")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

		try {
			tradeOrder = underTest.saveTradeEvent(tradeOrder);
		} catch (TradeOrderException e) {
			Logger.logMessage("Message : " + e.getMessage());
			assertNull(e.toString(), e);
		}

		List<TradeOrder> tradeOrderDetails = underTest.getTradeDetailsByTradeId(tradeId);

		assertEquals(1,tradeOrderDetails.size());
		assertSame(tradeOrder,tradeOrderDetails.get(0));
	}

    @Test
    public void shouldFailToGetTradeDetailsByInvalidTradeId(){
        long buyOrderQuantity = 600L;
        long tradeId = 100L;
        long invalidTradeId = 101L;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("ACC2")
                .addSecurityId("SEC2")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        try {
            tradeOrder = underTest.saveTradeEvent(tradeOrder);
        } catch (TradeOrderException e) {
            Logger.logMessage("Message : " + e.getMessage());
            assertNull(e.toString(), e);
        }

        List<TradeOrder> tradeOrderDetails = underTest.getTradeDetailsByTradeId(invalidTradeId);

        assertNotEquals(1,tradeOrderDetails.size());
        assertEquals(0,tradeOrderDetails.size());
    }
}