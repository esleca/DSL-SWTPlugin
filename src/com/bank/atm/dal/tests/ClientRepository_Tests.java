// Generation date: 2023-05-10T14:20:25.264677800.
// Input language: JAVA | Output language: Java.

package com.bank.atm.dal.tests;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import com.bank.atm.dal.ClientRepository;

public class ClientRepository_Tests {

  @Test
  public void isUserAuthorized_fail_test() {
    int account = 1;
    int pin = 23;
    boolean expected = false;
    ClientRepository sut = new ClientRepository();
    boolean result = sut.isUserAuthorized(account, pin);
    assertEquals(expected, result);
  }

  @Test
  public void isUserAuthorized_pass_test() {
    int account = 1;
    int pin = 123;
    boolean expected = true;
    ClientRepository sut = new ClientRepository();
    boolean result = sut.isUserAuthorized(account, pin);
    assertEquals(expected, result);
  }

}

