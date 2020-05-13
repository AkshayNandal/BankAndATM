package bank.dao;
import bank.exception.*;
public interface AccountDao{
		public String createAccount(String accountType, String accountName, double openingBalance)
		throws AccountTypeMismatchException, InsufficientFundsException;
		public java.util.List<bank.model.Account> getAllAccounts();
		public bank.model.Account getAccountByID(String accountID)throws AccountNotFoundException;
		public bank.model.Account updateAccount(String accountID, String accountName) throws AccountNotFoundException;
		public boolean deleteAccount(String accountID) throws AccountNotFoundException;
		public String generatePin();
}
