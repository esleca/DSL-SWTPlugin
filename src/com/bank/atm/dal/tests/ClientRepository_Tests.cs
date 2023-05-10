// Generation date: 2023-05-10T14:20:25.280727900.
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
    public class ClientRepository_Tests
  {

    [TestMethod]
    public void isUserAuthorized_fail_test()
    {
      int account = 1;
      int pin = 23;
      boolean expected = false;
      ClientRepository sut = new ClientRepository();
      boolean result = sut.isUserAuthorized(account, pin);
      Assert.AreEqual(expected, result);
    }

    [TestMethod]
    public void isUserAuthorized_pass_test()
    {
      int account = 1;
      int pin = 123;
      boolean expected = false;
      ClientRepository sut = new ClientRepository();
      boolean result = sut.isUserAuthorized(account, pin);
      Assert.AreEqual(expected, result);
    }

  }

}
