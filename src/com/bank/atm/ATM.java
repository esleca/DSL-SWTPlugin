package com.bank.atm;

//import required classes and packages   
import java.util.Scanner;

import com.bank.atm.dal.AccountRepository;
import com.bank.atm.dal.ClientRepository;

//create ATMExample class to implement the ATM functionality  
public class ATM {
	// main method starts
	public static void main(String args[]) {
		// create scanner class object to get choice of user
		Scanner sc = new Scanner(System.in);
		ClientRepository repo = new ClientRepository();

		System.out.println("Cajero automático");
		System.out.println("Digite su número de cuenta");
		int account = sc.nextInt();
		System.out.println("Digite su pin");
		int pin = sc.nextInt();
		if (repo.isUserAuthorized(account, pin)) {
			while (true) {
				AccountRepository accountRepo = new AccountRepository();
				System.out.println("Digite 1 para retiro");
				System.out.println("Digite 2 para depósito");
				System.out.println("Digite 3 para conocer su balance");
				System.out.println("Digite 4 para salir");
				System.out.print("Escoja la operación que desea realizar:");

				// get choice from user
				int choice = sc.nextInt();
				int balance, amount;
				String message;
				switch (choice) {
				case 1:
					System.out.print("Digite el mónto a retirar:");
					amount = sc.nextInt();
					message = accountRepo.makeWithdrawal(account, amount);
					System.out.println(message);
					System.out.println("");
					break;

				case 2:

					System.out.print("Digite mónto a ser depositado:");
					amount = sc.nextInt();
					message = accountRepo.makeDeposit(account, amount);
					System.out.println(message);
					System.out.println("");
					break;

				case 3:
					balance = accountRepo.getBalance(account);
					System.out.println("Balance : " + balance);
					System.out.println("");
					break;

				case 4:
					// exit from the menu
					System.exit(0);
				}
			}
		} else {
			System.out.println("No autorizado");
		}

	}
}
