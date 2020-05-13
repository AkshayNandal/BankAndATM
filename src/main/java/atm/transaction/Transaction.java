package atm.transaction;

import bank.exception.*;

public interface Transaction {
	public boolean authenticate(String accountID, String accountPin) throws AccountNotFoundException, InvalidPINException;
	public double deposit(String accountID, double amount);
	public double withdraw(String accountID, double amount) throws InsufficientFundsException;
	public double getBalance(String accountID);
	public boolean changePin(String accountID, String oldPin, String newPin) throws InvalidPINException;
}
