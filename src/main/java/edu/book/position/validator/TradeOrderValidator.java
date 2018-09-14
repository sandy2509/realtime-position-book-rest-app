package edu.book.position.validator;


import edu.book.position.exception.ValidationException;
import edu.book.position.model.TradeOrder;

import static edu.book.position.validator.ValidatorUtil.*;

public class TradeOrderValidator {
    public void validateTradeOrderModel(TradeOrder tradeOrder) {
        required(tradeOrder, () -> new ValidationException("TradeOrder Request Body is Empty"));
        notBlank(tradeOrder.getAccountId(), () -> new ValidationException("account ID is not provided"));
        notBlank(tradeOrder.getSecurityId(), () -> new ValidationException("Security ID is not provided"));
        required(tradeOrder.getTradeEventType(), () -> new ValidationException("TradeEventType is not provided"));
        validateTradeId(tradeOrder.getTradeId());
        requireNumber(tradeOrder.getQuantity(), () -> new ValidationException("Quantity is not valid or null"));
    }

    public void validateTradeId(long tradeId) {
        requireNumber(tradeId, () -> new ValidationException("TradeId is not valid or null"));
        validateIfTradeIdIsNegative(tradeId, () -> new ValidationException("TradeId cannot be negative"));
    }
}
