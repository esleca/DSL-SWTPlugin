package com.bank.atm.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.atm.resources.Constants;

public class ClientRepository {

	public boolean isUserAuthorized(int account, int pin) {
		
		try (Connection conn = DriverManager.getConnection(Constants.url, Constants.user, Constants.pass)) {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(
					"SELECT EXISTS(SELECT * FROM bank_db.accounts where account_number = " + account + " and pin = " + pin +") as 'CHECK';");
			
			if(rs.next()) {
				Boolean check = rs.getBoolean("CHECK");
				conn.close();
				if(check)
					return true;
				else
					return false;
			}
			else
				return false;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
