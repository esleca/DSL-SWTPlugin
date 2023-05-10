// Generation date: 2023-05-10T15:53:20.738048200.
// Input language: CSHARP | Output language: C#.

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using com.bank.atm.dal;

namespace com.bank.atm.dal.tests
{
  [TestClass]
    public class AccountRepository_Tests
  {

    [TestMethod]
    public void getBalance_fail_test()
    {
      int account = 144;
      int expected = 0;
      AccountRepository sut = new AccountRepository();
      int result = sut.getBalance(account);
      Assert.AreEqual(expected, result);
    }

    [TestMethod]
    public void getBalance_pass_test()
    {
      int account = 1;
      int expected = 0;
      AccountRepository sut = new AccountRepository();
      int result = sut.getBalance(account);
      Assert.AreNotEqual(expected, result);
    }

    [TestMethod]
    public void makeDeposit_fail_test()
    {
      int account = 1111;
      int amount = 200;
      String expected = "No se ha podido procesar la transacción";
      AccountRepository sut = new AccountRepository();
      String result = sut.makeDeposit(account, amount);
      Assert.AreEqual(expected, result);
    }

    [TestMethod]
    public void makeDeposit_pass_test()
    {
      int account = 1;
      int amount = 200;
      String expected = "Transacción procesada correctamente";
      AccountRepository sut = new AccountRepository();
      String result = sut.makeDeposit(account, amount);
      Assert.AreEqual(expected, result);
    }

    [TestMethod]
    public void makeWithdrawal_fail_test()
    {
      int account = 1;
      int amount = 2000;
      String expected = "Fondos insuficientes";
      AccountRepository sut = new AccountRepository();
      String result = sut.makeWithdrawal(account, amount);
      Assert.AreEqual(expected, result);
    }

    [TestMethod]
    public void makeWithdrawal_pass_test()
    {
      int account = 1;
      int amount = 200;
      String expected = "Por favor tome el dinero";
      AccountRepository sut = new AccountRepository();
      String result = sut.makeWithdrawal(account, amount);
      Assert.AreEqual(expected, result);
    }

  }

}
