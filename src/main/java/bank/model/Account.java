package bank.model;

import bank.dao.AccountDao;
import bank.exception.AccountNotFoundException;
import bank.exception.AccountTypeMismatchException;
import bank.exception.InsufficientFundsException;

public abstract class Account {
	private String accountID;
	private String accountType;
	private String accountName;
	private double accountBalance;
	private String accountPin;
	
	public Account(String accountID, String accountType, String accountName, double accountBalance, String accountPin){
		this.accountID=accountID;
		this.accountType=accountType;
		this.accountName=accountName;
		this.accountBalance=accountBalance;
		this.accountPin=accountPin;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	
	public String getAccountPin() {
		return accountPin;
	}

	public void setAccountPin(short pin) {
		this.accountPin = accountPin;
	}

	@Override
	public String toString(){
		return "Account [ AccountID " + accountID + " Account Type " + accountType + " Account Name " + accountName + " Account Balance "  + accountBalance +
				"Pin " +accountPin+  " ]";
	}
	

}
