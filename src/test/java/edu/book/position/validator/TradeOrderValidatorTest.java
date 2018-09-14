package edu.book.position.validator;

import edu.book.position.exception.ValidationException;
import edu.book.position.model.TradeEventType;
import edu.book.position.model.TradeOrder;
import org.junit.Before;
import org.junit.Test;

public class TradeOrderValidatorTest {

    private TradeOrderValidator underTest;

    @Before
    public void setUp() {
        underTest = new TradeOrderValidator();
    }

    @Test(expected = ValidationException.class)
    public void shouldFailToValidateWhenTradeIdIsNegative() {
        long buyOrderQuantity = 600L;
        long tradeId = -100L;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("ACC1")
                .addSecurityId("SEC1")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        underTest.validateTradeOrderModel(tradeOrder);
    }

    @Test(expected = ValidationException.class)
    public void shouldFailToValidateWhenAccountIdIsMissing() {
        long buyOrderQuantity = 600L;
        long tradeId = -100L;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("")
                .addSecurityId("SEC1")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        underTest.validateTradeOrderModel(tradeOrder);
    }

    @Test(expected = ValidationException.class)
    public void shouldFailToValidateWhenSecurityIdIsMissing() {
        long buyOrderQuantity = 600L;
        long tradeId = -100L;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("ACC1")
                .addSecurityId("")
                .addTradeEventType(TradeEventType.BUY)
                .addQuantity(buyOrderQuantity).build();

        underTest.validateTradeOrderModel(tradeOrder);
    }

    @Test(expected = ValidationException.class)
    public void shouldFailToValidateWhenTradeEventTypeIsMissing() {
        long buyOrderQuantity = 600L;
        long tradeId = -100L;

        TradeOrder tradeOrder = new TradeOrder.TradeOrderBuilder()
                .addTradeId(tradeId)
                .addAccountId("ACC1")
                .addSecurityId("SEC1")
                .addTradeEventType(null)
                .addQuantity(buyOrderQuantity).build();

        underTest.validateTradeOrderModel(tradeOrder);
    }

    @Test(expected = ValidationException.class)
    public void shouldFailToValidateWhenTradeOrderIsMissing() {
        underTest.validateTradeOrderModel(null);
    }
}