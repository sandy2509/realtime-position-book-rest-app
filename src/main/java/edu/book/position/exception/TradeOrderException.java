package edu.book.position.exception;

public class TradeOrderException extends Throwable{

	private static final long serialVersionUID = 1L;

	public TradeOrderException(String msg) {
		super(msg);
	}
}