package bank.exception;


public class AccountTypeMismatchException extends Exception{
	//String message;
	public AccountTypeMismatchException(String message){
		super(message);
	}
}
