package edu.book.position.validator;


import java.util.Objects;
import java.util.function.Supplier;

public interface ValidatorUtil {
    static <T> void required(T properties, Supplier<? extends RuntimeException> exceptionProducer) {
        if (Objects.isNull(properties))
            throw exceptionProducer.get();
    }

    static void requireNumber(Number number, Supplier<? extends RuntimeException> exceptionProducer) {
        if (Objects.isNull(number))
            throw exceptionProducer.get();
    }

    static void notBlank(String properties, Supplier<? extends RuntimeException> exceptionProducer) {
        if (Objects.isNull(properties) || properties.isEmpty())
            throw exceptionProducer.get();
    }

    static void validateIfTradeIdIsNegative(long tradeId, Supplier<? extends RuntimeException> exceptionProducer) {
        if (tradeId < 0) {
            throw exceptionProducer.get();
        }
    }
}
