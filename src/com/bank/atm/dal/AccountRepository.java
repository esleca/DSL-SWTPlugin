package com.bank.atm.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.atm.resources.Constants;

public class AccountRepository {

	public String makeDeposit(int account, int amount) {
		try (Connection conn = DriverManager.getConnection(Constants.url, Constants.user, Constants.pass)) {
			PreparedStatement s = conn
					.prepareStatement("INSERT INTO bank_db.transactions(account_number,type,amount,date) values ("
							+ account + ",'depósito'," + amount + ",now())");
			int rowsInserted = s.executeUpdate();
			conn.close();
			if (rowsInserted > 0) {
				boolean update = updateBalance(account, amount);
				if (update) {
					return Constants.transaction_success;
				} else {
					return Constants.transaction_error;
				}
			} else
				return Constants.transaction_error;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return Constants.transaction_error;
		}
	}

	public String makeWithdrawal(int account, int amount) {
		int balance = getBalance(account);
		// check whether the balance is greater than or equal to the withdrawal amount
		if (balance >= amount) {
			try (Connection conn = DriverManager.getConnection(Constants.url, Constants.user, Constants.pass)) {
				PreparedStatement s = conn
						.prepareStatement("INSERT INTO bank_db.transactions(account_number,type,amount,date) values ("
								+ account + ",'retiro'," + amount + ",now())");
				int rowsInserted = s.executeUpdate();
				conn.close();
				if (rowsInserted > 0) {
					amount *= -1;
					boolean update = updateBalance(account, amount);
					if (update) {
						return "Por favor tome el dinero";
					} else {
						return Constants.transaction_error;
					}
				} else
					return Constants.transaction_error;

			} catch (SQLException ex) {
				ex.printStackTrace();
				return Constants.transaction_error;
			}
		} else {
			// show custom error message
			return "Fondos insuficientes";
		}
	}

	private boolean updateBalance(int account, int amount) {

		try (Connection conn = DriverManager.getConnection(Constants.url, Constants.user, Constants.pass)) {
			int balance = getBalance(account);
			balance = balance + amount;
			PreparedStatement ps = conn
					.prepareStatement("UPDATE bank_db.accounts SET balance = ? WHERE account_number = ?");
			ps.setInt(1, balance);
			ps.setInt(2, account);
			int rowsInserted = ps.executeUpdate();
			conn.close();
			if (rowsInserted > 0) {
				return true;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}

	public int getBalance(int account) {

		try (Connection conn = DriverManager.getConnection(Constants.url, Constants.user, Constants.pass)) {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT balance FROM bank_db.accounts where account_number = " + account);
			if (rs.next()) {
				int balance = rs.getInt("balance");
				conn.close();
				return balance;

			} else {
				conn.close();
				return 0;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
}
