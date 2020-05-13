package com.ibm.reva.atm;

import java.util.Scanner;


import bank.dao.AccountDaoImpl;
import bank.exception.AccountNotFoundException;
import bank.exception.AccountTypeMismatchException;
import bank.exception.InsufficientFundsException;
import bank.model.Account;

public class Bank{
	static Scanner sc=new Scanner(System.in);
	static bank.dao.AccountDao accountDao = new bank.dao.AccountDaoImpl();
		public static void main(String[] args){
			int choice=0;
			while(true){
				System.out.println("1. Create Account");
				System.out.println("2. Get All Accounts");
				System.out.println("3. Get Account by ID");
				System.out.println("4. Update Account");
				System.out.println("5. Delete Account");
				System.out.println("6. Exit");
				System.out.println("Choice?");
				choice=sc.nextInt();
				switch(choice){
				case 1: createAccount(); break;
				case 2: getAllAccounts(); break;
				case 3: getAccountByID(); break;
				case 4: updateAccount(); break;
				case 5: deleteAccount(); break;
				case 6: System.exit(0);
				default: System.out.println("Invalid Choice");
				}
			}
		}
		public static void deleteAccount() {
			System.out.println("Account ID?");
			sc.nextLine();
			String accountID = sc.nextLine();
			try{
				if(accountDao.deleteAccount(accountID)){
					System.out.println("Account ID"+accountID+"Deleted!");
				}
			}catch(bank.exception.AccountNotFoundException anfe){
				System.out.println(anfe.getMessage());
			}
			
		}

		public static void updateAccount() {
			System.out.println("Enter the Account ID:");
			sc.nextLine();
			String accountID=sc.nextLine();
			System.out.println("Account Name?");
			String accountName=sc.nextLine();
			try{
				bank.model.Account account=accountDao.updateAccount(accountID, accountName);
				System.out.println(account+"\n");
			}catch(bank.exception.AccountNotFoundException ane){
				System.out.println("Account not found");
			}
			
		}

		public static void getAccountByID() {
			System.out.println("Enter your ID");
			sc.nextLine();
			String accountID=sc.nextLine();
			try{
				Account account=accountDao.getAccountByID(accountID);
				System.out.println(account);
			}catch(AccountNotFoundException e){
				System.err.println(e.getMessage());
			}
			
		}

		public static void getAllAccounts() {
			java.util.List<bank.model.Account> accounts = accountDao.getAllAccounts();
			for(bank.model.Account account : accounts)
					System.out.println(account);
		}

		private static void createAccount() {
			// TODO Auto-generated method stub
			System.out.println("Account Type");
			sc.nextLine();
			String accountType = sc.next();
			System.out.println("Account Name");
			String accountName = sc.next();
			System.out.println("Account balance");				
			double openingBalance=sc.nextDouble();
				accountDao=new AccountDaoImpl();
			try{
				//double openingBalance = 0;
				String accountID=accountDao.createAccount(accountType, accountName, openingBalance);
				
				String accountPin=accountDao.getAccountByID(accountID).getAccountPin();
				System.out.println("Account created: "+accountID+"\n"+"Your PIN is: "+accountPin);
			}catch(AccountTypeMismatchException amte){
				System.err.println(amte.getMessage());
			} catch (InsufficientFundsException ie) {
				System.err.println(ie.getMessage());
			}catch(AccountNotFoundException anfe){
				System.err.println(anfe.getMessage());
			}
		}
}
