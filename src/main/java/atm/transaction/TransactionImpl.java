package atm.transaction;

import bank.exception.AccountNotFoundException;
import bank.exception.InsufficientFundsException;
import bank.exception.InvalidPINException;
import java.lang.Exception.*;
import java.sql.PreparedStatement;

public class TransactionImpl implements Transaction{
	
	public static bank.dao.AccountDao accountDao=new bank.dao.AccountDaoImpl();

	public boolean authenticate(String accountID, String accountPin)
			throws AccountNotFoundException, InvalidPINException {
		boolean status = false;
		bank.model.Account account = accountDao.getAccountByID(accountID);
		if(!account.getAccountPin().equals(accountPin)){
			throw new bank.exception.InvalidPINException("Invalid PIN");
		}
		return true;
	}

	public double deposit(String accountID, double amount) {
		try{
			bank.model.Account account = accountDao.getAccountByID(accountID);
			java.sql.Connection connection = bank.util.DBUtil.getConnection();
			String sql="UPDATE Account SET Account_Balance=? WHERE Account_Id=?";
			double accountBalance = account.getAccountBalance() + amount;
			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, accountBalance);
			statement.setString(2, accountID);
			statement.executeUpdate();
			connection.close();
		}catch(ClassNotFoundException cnfe){
			System.err.println(cnfe.getMessage());
		}catch(java.sql.SQLException sqle){
			System.err.println(sqle.getMessage());
		} catch (AccountNotFoundException e) {
			e.printStackTrace();
		}
		return amount;
	}

	public double withdraw(String accountID, double amount) throws InsufficientFundsException{
		double accountBalance=0;
		try{
			bank.model.Account account = accountDao.getAccountByID(accountID);
			String accountType = account.getAccountType();
			accountBalance = account.getAccountBalance() - amount;
			
			if(accountBalance - amount<0){
				throw new InsufficientFundsException("Insufficient Funds :(");
			}
			java.sql.Connection connection=bank.util.DBUtil.getConnection();
			String sql="UPDATE Account SET Account_Balance=? WHERE Account_Id=?";
			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, accountBalance);
			statement.setString(2, accountID);
			statement.executeUpdate();
			connection.close();
		}catch(ClassNotFoundException cnfe){
			System.err.println(cnfe.getMessage());
		}catch(java.sql.SQLException sqle){
			System.err.println(sqle.getMessage());
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountBalance;
	}

	public double getBalance(String accountID) {
		double accountBalance = 0;
		try{
		bank.model.Account account = accountDao.getAccountByID(accountID);
		accountBalance= account.getAccountBalance();
		}catch(AccountNotFoundException anfe){
			System.out.println(anfe.getMessage());
		}
		return accountBalance;
	}

	public boolean changePin(String accountID, String oldPin, String newPin) throws InvalidPINException {
		boolean success=false;
		try{
			bank.model.Account account = accountDao.getAccountByID(accountID);
			String accountPin=account.getAccountPin();
			if(!accountPin.equals(oldPin)){
				throw new InvalidPINException("Old pin doesn't match");
			}if(oldPin.equals(newPin)){
				throw new InvalidPINException("Old pin and the new pin are the same");
			}if(newPin.length()!=4){
				throw new InvalidPINException("The pin has to have a lenght of 4 digits");
			}if(new StringBuilder(newPin).reverse().equals(newPin)){
				throw new InvalidPINException("The pin cannot be a palindrome");
			}
			java.sql.Connection connection = bank.util.DBUtil.getConnection();
			String sql="UPDATE Account SET Account_pin=? WHERE Account_ID=?";
			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, newPin);
			statement.setString(2,accountID);
			statement.executeUpdate();
			connection.close();
			success=true;
		}catch(ClassNotFoundException cnfe){
			System.err.println(cnfe.getMessage());
		}catch(java.sql.SQLException sqle){
			System.err.println(sqle.getMessage());
		} catch (AccountNotFoundException e) {
			e.printStackTrace();
		}	
		return  success;
	}
}
