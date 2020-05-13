package bank.dao;

import java.sql.SQLException;

import bank.exception.AccountNotFoundException;
import bank.exception.AccountTypeMismatchException;
import bank.exception.InsufficientFundsException;

public class AccountDaoImpl implements AccountDao{
	public String createAccount(String accountType, String accountName, double openingBalance) throws AccountTypeMismatchException, InsufficientFundsException
	{
		String accountID="";
		try{
			if(!(accountType.equalsIgnoreCase("SB")|| accountType.equalsIgnoreCase("CC"))){
				throw new bank.exception.AccountTypeMismatchException("Account is neither Saving nor Current");
			}
			if(accountType.equalsIgnoreCase("SB")&& openingBalance<1000){
				throw new bank.exception.InsufficientFundsException("Insufficient Balance for savings account"+"Minimum balance is 1000");
			}
			if(accountType.equalsIgnoreCase("CC") && openingBalance<5000){
				throw new bank.exception.InsufficientFundsException("Insufficient Balance for current account"+"Minimum balance is 5000");
			}
			java.sql.Connection connection = bank.util.DBUtil.getConnection();
			String sql="SELECT MAX(Account_ID) FROM Account WHERE Account_Type=?";
			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, accountType);
			java.sql.ResultSet result = statement.executeQuery();
			if(result.next()){
				String maxAccountID=result.getString(1);
				maxAccountID=(maxAccountID!=null) ? maxAccountID.substring(2) : "0";
				int accountID1=1+Integer.parseInt(maxAccountID);
				accountID=accountType+String.format("%014d", accountID1);
			}
//			else{
//				//String accountID = accountType+"0000000000001";
//				accountID=(accountType+"0000000000001");
//			}
			String accountPin=generatePin();
			sql = "INSERT INTO Account VALUES(?,?,?,?,?)";
			statement=connection.prepareStatement(sql);
			statement.setString(1, accountID);
			statement.setString(2, accountType);
			statement.setString(3, accountName);
			statement.setDouble(4, openingBalance);
			statement.setString(5, accountPin);
			
			statement.execute();
			
			connection.commit();
			connection.close();
			
		}catch(ClassNotFoundException cnf){
			System.err.print(cnf.getMessage());
		}catch(java.sql.SQLException sqle){
			System.err.println(sqle.getMessage());
		}
		return accountID;
	}
	public java.util.List<bank.model.Account> getAllAccounts(){
		java.util.List<bank.model.Account> accounts = new java.util.ArrayList<bank.model.Account>();
		try {
			java.sql.Connection connection=bank.util.DBUtil.getConnection();
			String sql="SELECT * FROM Account";
			java.sql.Statement statement = connection.createStatement();
			java.sql.ResultSet result = statement.executeQuery(sql);
			while(result.next()){
				String accountID=result.getString(1);
				String accountType=result.getString(2);
				String accountName=result.getString(3);
				double accountBalance=result.getDouble(4);
				String accountPin=result.getString(5);
				bank.model.Account account=null;
				if(accountType.equalsIgnoreCase("SB")){
					account = new bank.model.Saving(accountID, accountType, accountName, accountBalance, accountPin);
				}
				else{
					account = new bank.model.Current(accountID, accountType, accountName, accountBalance, accountPin);
				}
				accounts.add(account);
			}
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return accounts;
	}
	public bank.model.Account getAccountByID(String accountID)throws AccountNotFoundException{
		bank.model.Account account = null;
		try{
			java.sql.Connection connection = bank.util.DBUtil.getConnection();
			String sql = "SELECT * FROM Account WHERE Account_Id=?";
			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, accountID);
			java.sql.ResultSet result = statement.executeQuery();
			if(result.next()){
				String accountType = result.getString(2);
				String accountName = result.getString(3);
				Double accountBalance = result.getDouble(4);
				String accountPin=result.getString(5);
				if(accountType.equalsIgnoreCase("SB")){
					account = new bank.model.Saving(accountID, accountType, accountName, accountBalance, accountPin);
				}else{
					account = new bank.model.Current(accountID, accountType, accountName, accountBalance, accountPin);
				}
			}
			else{
					throw new AccountNotFoundException("The account you're looking for does not exist");
				}
		}catch(SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return account;
	}
	public bank.model.Account updateAccount(String accountID, String accountName) throws AccountNotFoundException{
		//bank.model.Account account=null;
		try{
			java.sql.Connection connection = bank.util.DBUtil.getConnection();
			bank.model.Account account = getAccountByID(accountID);
			if(account==null){
				throw new bank.exception.AccountNotFoundException("Acccount with ID:"+accountID+"not found!");
			}
			String sql="UPDATE Account SET Account_Name=? WHERE Account_ID=?";
			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, accountName);
			statement.setString(2, accountID);
			statement.executeUpdate();
			//connection.commit();
			connection.close();
		}catch(ClassNotFoundException cnfe){
			System.err.println(cnfe.getMessage());
		}catch(java.sql.SQLException sqle){
			System.err.println(sqle.getMessage());
		}
		return getAccountByID(accountID);
	}
	public boolean deleteAccount(String accountID) throws AccountNotFoundException{
		boolean status=false;
		try{
			java.sql.Connection connection = bank.util.DBUtil.getConnection();
			bank.model.Account account = getAccountByID(accountID);
			if(account == null){
				System.out.println("Account not found!");
			}
			String sql="DELETE FROM Account WHERE Account_ID=?";
			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, accountID);
			statement.executeUpdate();
			status=true;
		}catch(ClassNotFoundException cnfe){
			System.err.println(cnfe.getMessage());
		}catch(java.sql.SQLException sqle){
			System.err.println(sqle.getMessage());
		}		
		return status;
	}
	public String generatePin() {
		String accountPin="";
		java.util.Random random = new java.util.Random();
		int firstNumber = (int) (random.nextDouble()*10);
		int secondNumber = (int) (random.nextDouble()*10);
		int thirdNumber = (int) (random.nextDouble()*10);
		int fourthNumber = (int) (random.nextDouble()*10);
		return ""+firstNumber+secondNumber+thirdNumber+fourthNumber;
	}
}
