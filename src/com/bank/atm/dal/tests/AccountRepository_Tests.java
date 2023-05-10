// Generation date: 2023-05-10T15:53:20.697383100.
// Input language: JAVA | Output language: Java.

package com.bank.atm.dal.tests;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import com.bank.atm.dal.AccountRepository;

public class AccountRepository_Tests {

  @Test
  public void getBalance_fail_test() {
    int account = 144;
    int expected = 0;
    AccountRepository sut = new AccountRepository();
    int result = sut.getBalance(account);
    assertEquals(expected, result);
  }

  @Test
  public void getBalance_pass_test() {
    int account = 1;
    int expected = 0;
    AccountRepository sut = new AccountRepository();
    int result = sut.getBalance(account);
    assertNotEquals(expected, result);
  }

  @Test
  public void makeDeposit_fail_test() {
    int account = 1111;
    int amount = 200;
    String expected = "No se ha podido procesar la transacción";
    AccountRepository sut = new AccountRepository();
    String result = sut.makeDeposit(account, amount);
    assertEquals(expected, result);
  }

  @Test
  public void makeDeposit_pass_test() {
    int account = 1;
    int amount = 200;
    String expected = "Transacción procesada correctamente";
    AccountRepository sut = new AccountRepository();
    String result = sut.makeDeposit(account, amount);
    assertEquals(expected, result);
  }

  @Test
  public void makeWithdrawal_fail_test() {
    int account = 1;
    int amount = 2000;
    String expected = "Fondos insuficientes";
    AccountRepository sut = new AccountRepository();
    String result = sut.makeWithdrawal(account, amount);
    assertEquals(expected, result);
  }

  @Test
  public void makeWithdrawal_pass_test() {
    int account = 1;
    int amount = 200;
    String expected = "Por favor tome el dinero";
    AccountRepository sut = new AccountRepository();
    String result = sut.makeWithdrawal(account, amount);
    assertEquals(expected, result);
  }

}

