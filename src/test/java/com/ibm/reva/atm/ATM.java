package com.ibm.reva.atm;

import java.util.Scanner;
import bank.exception.*;
import atm.transaction.*;

public class ATM {
	static Scanner in = new Scanner(System.in);
	static atm.transaction.Transaction transaction = new atm.transaction.TransactionImpl();
	static String accountID;
	public static void main(String[] args) throws InsufficientFundsException{
		System.out.println("Welcome"+"\n");
		System.out.println("Account ID?");
		accountID = in.nextLine();
		System.out.println("Account PIN?");
		String accountPin = in.nextLine();
		
		boolean success = false;
		
		try{
			success = transaction.authenticate(accountID,accountPin);
		}catch(AccountNotFoundException anfe){
			System.err.println(anfe.getMessage());
		}catch(InvalidPINException ipe){
			System.err.println(ipe.getMessage());
		}
		
		while(success){
			System.out.println("\n");
			System.out.println("1. Deposit Amount \n");
			System.out.println("2. Withdraw Amount \n");
			System.out.println("3. Get Account Balance \n");
			System.out.println("4. Change PIN \n");
			System.out.println("5. Exit \n");
			System.out.println("Choice? \n");
			int choice = in.nextInt();
			switch(choice){
			case 1: depositAmount(); break;
			case 2: withdrawAmount(); break;
			case 3: getBalance(); break;
			case 4: changePin(); break;
			case 5: System.out.println("Thank You!"); break;
			default: System.out.println("Invalid Choice"); break;
			}
		}
		
	}
	private static void changePin() {
		System.out.println("Enter your old Pin");
		in.nextLine();
		String oldPin=in.nextLine();
		System.out.println("Enter your new pin");
		String newPin=in.nextLine();
		try{
			System.out.println("Pin Update!"+transaction.changePin(accountID, oldPin, newPin));
		}catch(InvalidPINException e){
			System.out.println("You've not followed the norms");
		}
		
	}
	private static void getBalance() {
		System.out.println("Account Balance:"+transaction.getBalance(accountID));
	}
	private static void withdrawAmount() throws InsufficientFundsException {
		System.out.println("Enter the amount you want to withdraw");
		double amount = in.nextDouble();
		double accountBalance=transaction.withdraw(accountID, amount);
		System.out.println("Amount withdrawn");
	}
	private static void depositAmount() {
		System.out.println("Enter the amount to be deposited:");
		double amount = in.nextDouble();
		double accountBalance=transaction.deposit(accountID, amount);
		System.out.println("Amount Deposited! \n"+"Current Balance: "+accountBalance);
	}

}
