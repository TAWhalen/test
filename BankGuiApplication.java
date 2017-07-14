//*****************************************
//   Name: Thomas Whalen
//   CTP-150-877
//   Lab Assignment 11
//*****************************************

import java.text.DecimalFormat; // Imported for better dollar formatting.
import java.io.*; // Imported for input and output to and from files.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
   The BankAccount class is an abstract class which can keep track of the current balance, number of deposits and
   withdrawals, the annual interest, and the service charge for an account. 
*/
abstract class BankAccount implements Serializable 
{
   private double balance;
   private int deposits; // Holds the number of deposits made.
   private int withdrawals; // Holds the number of withdrawals made.
   private double annualInt;
   private double serviceCharge;
   
   /**
     The 2-parameter constructor. This constructor is used to initialize the balance and annual interest.
     @param balance The value used to set the starting balance.
     @param annualInt The value used to determine the annual interest rate.
     @exception InvalidDepositAmount Thrown when deposit amount is invalid.
   */
   public BankAccount(double balance, double annualInt) throws InvalidDepositAmount
   {
      this.deposits = 0; // These values are also declared with the "this" prefix for the sake of consistency. 
      this.withdrawals = 0;
      deposit(balance);
      this.annualInt = annualInt;
      this.serviceCharge = 0.0;
   }
   
   /**
     The 3-parameter constructor. This constructor initializes the same values as the 2-parameter constructor,
     but it can also initialize the service charge variable. 
     @param balance The value used to set the starting balance.
     @param annualInt The value used to set the annual interest rate.
     @param serviceCharge The value used to set the service charge.
     @exception InvalidDepositAmount Thrown when deposit amount is invalid.
   */
   public BankAccount(double balance, double annualInt, double serviceCharge) throws InvalidDepositAmount
   {
      this.deposits = 0; // Again, these are declared with "this" purely for the sake of consistency. 
      this.withdrawals = 0;
      deposit(balance);
      this.annualInt = annualInt;
      this.serviceCharge = serviceCharge;
   }
   
   /**
     The setAnnualInt method is the mutator for the annualInt variable.
     @param newInt The value used to set the new interest rate.
   */
   public void setAnnualInt(double newInt)
   {
      annualInt = newInt;
   }
   
   /**
     The setServiceCharge method is the mutator for the serviceCharge variable.
     @param newCharge The value used to set the new interest rate.
   */
   public void setServiceCharge(double newCharge)
   {
      serviceCharge = newCharge;
   }
   
   /**
     The addServiceCharge method is similar to the setServiceCharge method, but is slightly different.
     It does not replace the current value, but instead adds to the current value. This is primarily useful if a subclass
     is supposed to add to the serviceCharge value, but does not want to completely overwrite it.
     @param addition The value which is added to the serviceCharge value.
   */
   public void addServiceCharge(double addition)
   {
      serviceCharge += addition;
   }
   
   /**
     The getBalance method is the accessor for the balance variable. 
     @return The value of the balance variable.
   */
   public double getBalance()
   {
      return balance;
   }
   
   /**
     The getAnnualInt method is the accessor for the annualInt variable.
     @return The value of the annualInt variable. 
   */
   public double getAnnualInt()
   {
      return annualInt;
   }
   
   /**
     The getServiceCharge method is the accessor for the serviceCharge variable. 
     @return The value of the serviceCharge variable.
   */
   public double getServiceCharge()
   {
      return serviceCharge;
   }
   
   /**
     The getNumDeposits method is the accessor for the deposits variable.
     @return The value of the deposits variable. 
   */
   public int getNumDeposits()
   {
      return deposits;
   }
   
   /**
     The getNumWithdrawals method is the accessor for the withdrawals variable.
     @return The value of the withdrawals method.
   */
   public int getNumWithdrawals()
   {
      return withdrawals;
   }
   
   /**
     The deposit method increase the balance value by the amount specified.
     @param amount The amount to "deposit" into the balance.
     @exception InvalidDepositAmount Thrown when a deposit amount is invalid.
   */
   public void deposit(double amount) throws InvalidDepositAmount
   {
      if(amount > 10000 || amount <= 0)
      {
         throw new InvalidDepositAmount();
      }
      else
      {
         balance += amount;
         deposits++;
      }
   }
   
   /**
     The withdraw method decreases the balance value by the amount specified.
     @param amount The amount to "withdraw" from the balance. 
     @exception InvalidWithdrawalAmount Thrown when a withdrawal amount is invalid.
   */
   public void withdraw(double amount) throws InvalidWithdrawalAmount
   {
      if(amount > getBalance() || amount <= 0 || amount > 10000)
      {
         throw new InvalidWithdrawalAmount();
      }
      else
      {
         balance -= amount;
         withdrawals++;
      }
   }
   
   /**
     The calcInterest method is used to calculate the monthly interest, based on the annual interest.
     It then adds the monthly interest accrued to the balance.
   */
   public void calcInterest()
   {
      double monthlyRate = (annualInt / 12);
      double monthlyInt = balance * monthlyRate;
      balance = balance + monthlyInt;
   }
   
   /**
     The monthlyProcess method is used to update and reset certain values, 
     similar to a monthly period in an actual bank.
   */
   public void monthlyProcess()
   {
      balance -= serviceCharge;
      
      calcInterest();
      
      withdrawals = 0;
      deposits = 0;
      serviceCharge = 0.0;
   }
   
   /**
      The transfer method transfers money from the current BankAccount to another.
      @param account The BankAccount object which money will be transfer to.
      @param amount The amount which will be transfered if conditions are met.
      @exception InvalidWithdrawalAmount Thrown when the amount to be withdrawn is invalid.
      @exception InvalidDepositAmount Thrown when the amount to be deposited is invalid.
   */
   public void transfer(BankAccount account, double amount) throws InvalidWithdrawalAmount, InvalidDepositAmount
   {
      withdraw(amount);
      account.deposit(amount);
   }
} // End of Abstract Class

/**
  The SavingsAccount class is an extension of the abstract BankAccount class. 
*/
class SavingsAccount extends BankAccount implements Serializable
{
   private boolean activeStatus = false;
   private final double SERVICE_ADDITION = 1.0;
   private final int ACTIVE_BAL = 25;
   
   /**
     2-parameter constructor which works with the 2-parameter constructor of the superclass.
     @param balance The value used to set the balance of the account.
     @param annualInt The value used to set the annual interest rate of the account.
     @exception InvalidDepositAmount Thrown by super if amount is invalid.
   */
   public SavingsAccount(double balance, double annualInt) throws InvalidDepositAmount
   {
      super(balance, annualInt);
      updateStatus(); // Determines if the balance added will activate the account.
   }
   
   /**
     3-parameter constructor which works with the 3-parameter constructor of the superclass.
     @param balance The value used to set the balance of the account.
     @param annualInt The value used to set the annual interest rate of the account.
     @param serviceCharge The value used to set the service charge rate for the account.
     @exception InvalidDepositAmount Thrown by super if amount is invalid.
   */
   public SavingsAccount(double balance, double annualInt, double serviceCharge) throws InvalidDepositAmount
   {
      super(balance, annualInt, serviceCharge);
      updateStatus();
   }
   
   /**
     The updateStatus method is used to update the activeStatus variable. This function is
     given its own method to avoid repeating the same code every time an update should be performed.
   */
   public void updateStatus()
   {
      if(getBalance() >= ACTIVE_BAL)
      {
         activeStatus = true;
      } 
      else
      {
         activeStatus = false;
      }
   }
   
   /**
     The getStatus method is the accessor for the activeStatus variable.
     @return The value of the activeStatus variable.
   */
   public boolean getStatus()
   {
      return activeStatus;
   }
   
   /**
     The deposit method checks if an account is active, then deposits the value into it.
     @param amount The value which will be deposited.
     @exception InvalidDepositAmount Thrown by superclass when a deposit amount is invalid.
   */
   public void deposit(double amount) throws InvalidDepositAmount
   {      
      super.deposit(amount); // An inactive account can still have money deposited into it. 
      updateStatus(); // If the account is inactive, an update is performed in case the deposit is enough to re-activate it.
   }
   
   /**
     The withdraw method "withdraws" some "money" out of the account. 
     @param amount The amount to be withdrawn.
     @exception InvalidWithdrawalAmount Thrown by superclass when the withdrawal is invalid.
   */
   public void withdraw(double amount) throws InvalidWithdrawalAmount
   {
      if(getStatus() == true) 
      {
         super.withdraw(amount); // If the account is active, then money can be withdrawn.
         updateStatus();
         if(getStatus() == false)
         { 
            JOptionPane.showMessageDialog(null, "Balance is now less than $" + ACTIVE_BAL + ". The account is now inactive.");
         }// After an update is performed, if the withdrawn amount sets the account below the "active" threshold,
          // then the account is set to inactive, so that more money cannot be withdrawn.
      }
      else
      {
         JOptionPane.showMessageDialog(null, "Withdrawals cannot be made from an inactive account."); 
      }  // The above message is a new addition from the original service class.
   }     // It was added so that when a withdrawal is denied, the user is told why.
      
   /**
     The monthlyProcess method checks if a withdrawal penalty amount needs to applied,
     then it proceeds to call the monthlyProcess method in the superclass. 
   */
   public void monthlyProcess()
   {
      int over = 0;
      double penalty = 0.0;
      
      if(getNumWithdrawals() > 4)
      {
         over = (getNumWithdrawals() - 4);
         penalty = SERVICE_ADDITION * over;
         penalty += getServiceCharge();
         setServiceCharge(penalty);
         super.monthlyProcess();
      }
      else
      {
         super.monthlyProcess(); // If a penalty does not need to be added, the superclass method is called as-is.
      }
      updateStatus(); // Updates the status in case the service charge(s) put the account into the "inactive" range.
   }
   
   /**
     This is the toString method for this class. It provides
     a small list of important information for the object,
     such as balance, interest rate, and account status.
     Service charge and number of withdrawals/deposits are not included,
     as they are cleared by the monthlyProcess method.
     @return The string of account information.
   */
   public String toString()
   {
      DecimalFormat dollars = new DecimalFormat("$#,##0.00"); // Formatted to look prettier.
      String str = "Balance: " + dollars.format(getBalance());
      str = str + "\nInterest Rate: " + getAnnualInt() + "%";
      
      if(activeStatus == true)
      {
         str = str + "\nAccount is: Active";
      }
      else
      {
         str = str + "\nAccount is: Inactive";
      }
      
      return str;
   }
} // End of Service Classes

/**
  The Exception class InvalidDepositAmount.
  Thrown when a deposit amount is invalid due to one or more reasons. 
*/
class InvalidDepositAmount extends Exception
{
   /**
     Constructor. Sends an error message to its superclass.
   */
   public InvalidDepositAmount()
   {
      super("ERROR: Invalid deposit amount: You cannot deposit less than $0.01, or greater than $10,000.");
   }
}

/**
  The Exception class InvalidWithdrawalAmount.
  Thrown when a withdrawal is invalid due to one or more reasons. 
*/
class InvalidWithdrawalAmount extends Exception
{
   /**
     Constructor. Sends an error message to its superclass.
   */
   public InvalidWithdrawalAmount()
   {
      super("ERROR: Invalid withdrawal amount: You cannot withdraw less than $0.01, " + 
                           "\n greater than $10,000, or greater than the current balance.");
   }
}

/**
  The CheckingAccount class is an extension of the BankAccount class.
  It keeps track of transactions and adds a fee if a user makes
  more than three transactions. It also implements the Serializable interface.
*/
class CheckingAccount extends BankAccount implements Serializable
{
   public static final double CHARGE_FEE = 2.00;
   public static final int FREE_TRANS = 3;
   private int transactions;
   private double currentFees;
   private double totalFees;
   
   /**
     The default, no argument constructor.
     @exception InvalidDepositAmount Thrown by super when the balance to deposited is invalid.
   */
   public CheckingAccount() throws InvalidDepositAmount
   {
      super(1, 0); // Balance set to 1 to avoid exception
      transactions = 0;
      totalFees = 0.0;
   }
   
   /**
     The multi-argument constructor.
     @param balance The balance sent to the super.
     @param interest The annual interest rate sent to the super.
     @param serviceCharge The service charge value sent to the constructor.
     @exception InvalidDepositAmount Thrown by super when the balance to be deposited is invalid.
   */
   public CheckingAccount(double balance, double interest, double serviceCharge) throws InvalidDepositAmount
   {
      super(balance, interest, serviceCharge);
      transactions = 0;
      totalFees = 0.0;
   }
   
   /**
     The deposit method. It accepts an argument for the amount to deposit.
     @param amount The amount to be deposited.
     @exception InvalidDepositAmount Thrown by super when deposit amount is invalid.
   */
   public void deposit(double amount) throws InvalidDepositAmount
   {
      super.deposit(amount);
   }
   
   /**
     The withdraw method. It accepts an argument for the amount to withdraw.
     It also keeps track of transactions (since a transaction requires a withdrawal)
     and adds a fee if the user performs more than three transactions.
     @param amount The amount to be withdrawn.
     @exception InvalidWithdrawalAmount Thrown by super when withdrawal amount is invalid.
   */
   public void withdraw(double amount) throws InvalidWithdrawalAmount
   {
      super.withdraw(amount);
      transactions ++;
      
      if(transactions > FREE_TRANS)
      {// deductFees is not called here as it would also reset the current number of transactions.
         int over = transactions - FREE_TRANS;
         currentFees = CHARGE_FEE * over;
      }
   }
   
   /**
     The deductFees method. It accepts no arguments.
     This method is called when the accumulated fees are to be deducted,
     such as at the end of the month. 
     @exception InvalidWithdrawalAmount Thrown by the super when the fee amount to be withdrawn is invalid.
   */
   public void deductFees() throws InvalidWithdrawalAmount
   {
      if(currentFees != 0)
      {
         super.withdraw(currentFees);
      }
      totalFees += currentFees;
      transactions = 0;
      currentFees = 0.0;
   }
   
   /**
     The toString method. It returns a string of information about the account,
     including balance, interest, number of transactions, and total fees paid.
     @return The string of account information. 
   */
   public String toString()
   {
      DecimalFormat dollars = new DecimalFormat("$#,##0.00");
      
      String str = "Balance: " + dollars.format(getBalance());
      str = str + "\nInterest Rate: " + getAnnualInt() + "%";
      str = str + "\nNumber of Transactions: " + transactions;
      str = str + "\nTotal Fees Paid: " + dollars.format(totalFees);
      return str;
   }
}

/**
  The BankGuiApplication creates a graphical interface for the user to manage two BankAccounts.
  The BankAccount objects are a SavingsAccount and a CheckingAccount. The class then saves the data to
  a file. Additionally, if a save file already exists, it will load that data.
  Finally, it implements the ActionListener interface for button presses.
*/
public class BankGuiApplication implements ActionListener
{
   static DecimalFormat dollars = new DecimalFormat("$#,##0.00"); // Used for formatting display
   
   // Window declarations
   static JFrame manualWin;
   static JFrame depositWin;
   static JFrame withdrawWin;
   static JFrame transferWin;
   
   // Button declarations
   // (window name)Btn is used for the main window, (window name)WinBtn is used by the window called.
   static JButton depositBtn;
   static JButton depositWinBtn;
   static JButton withdrawBtn;
   static JButton withdrawWinBtn;
   static JButton transferBtn;
   static JButton transferWinBtn;
   static JButton exitBtn;
   static JButton backBtn1;// Each panel is given its own back button.
   static JButton backBtn2;// This is done to avoid bugs cause by having
   static JButton backBtn3;// the same component in multiple panels.
   
   // Text field declarations
   static JTextField depositField;
   static JTextField withdrawField;
   static JTextField transferField;
   
   // Label declarations
   static JLabel depositLabel;
   static JLabel withdrawLabel;
   static JLabel transferLabel;
   static JLabel transferSavingsLabel;
   static JLabel transferCheckingLabel;
   
   // Combobox declarations.
   // Four boxes are made, one for each window. 
   // This is to avoid overlapping components in the different windows.
   static JComboBox<String> accounts1;
   static JComboBox<String> accounts2;
   static JComboBox<String> accounts3;
   static JComboBox<String> accounts4;
   
   // Objects declared but not initialized, so that other methods may access them.
   static SavingsAccount savingsAcct;
   static CheckingAccount checkingAcct;
   
   /**
     The main method of the application class.
     It checks for the saved data files, then loads them. If one or
     both are not found, two new ones are created.
     @param args The args parameter is not used by this class.
   */
   public static void main(String[] args)
   {
      double savingBal = 1;
      double savingInt = 0;
      double savingCharge = 0;
      double checkingBal = 1;
      double checkingInt = 0;
      double checkingCharge = 0;
      String inputString;
      boolean good = false; // Used for checking if new accounts need to be made.
      
      try
      {
         JOptionPane.showMessageDialog(null, "Reading data from saved data files...");
         savingsAcct = (SavingsAccount)readObj("Savings.dat");
         checkingAcct = (CheckingAccount)readObj("Checking.dat");
         JOptionPane.showMessageDialog(null, "Reading complete.");
         good = true;
      }
      catch(FileNotFoundException e)
      {// If this exception is thrown, then one or more saved files are not present. Thus, new ones are made.
         JOptionPane.showMessageDialog(null, "No saved data file found for one or both accounts. New ones will be created.");
         inputString = JOptionPane.showInputDialog("Please enter the balance of the savings account.");
         savingBal = Double.parseDouble(inputString);
         inputString = JOptionPane.showInputDialog("Please enter the annual interest for the savings account.");
         savingInt = Double.parseDouble(inputString);
         inputString = JOptionPane.showInputDialog("Please enter the service charge amount for the savings account.");
         savingCharge = Double.parseDouble(inputString);
      
         inputString = JOptionPane.showInputDialog("Please enter the balance of the checking account.");
         checkingBal = Double.parseDouble(inputString);
         inputString = JOptionPane.showInputDialog("Please enter the annual interest for the checking account.");
         checkingInt = Double.parseDouble(inputString);
         inputString = JOptionPane.showInputDialog("Please enter the service charge for the checking account.");
         checkingCharge = Double.parseDouble(inputString);
         good = false;
      }
      catch(ClassNotFoundException e)
      {
         JOptionPane.showMessageDialog(null, "An internal error has occured.");
      }
      catch(IOException e)
      {
         JOptionPane.showMessageDialog(null, "An error has occurred with input or output.");
      }
      
      if(!good)
      {
         try
         {
            savingsAcct = new SavingsAccount(savingBal, savingInt, savingCharge);
            checkingAcct = new CheckingAccount(checkingBal, checkingInt, checkingCharge);
            good = true;
         }
         catch(InvalidDepositAmount e)
         {// If the user enters an invalid balance, then they must re-enter the balances.
            JOptionPane.showMessageDialog(null, e.getMessage());
            inputString = JOptionPane.showInputDialog("Please enter a new balance for the savings account.");
            savingBal = Double.parseDouble(inputString);
            inputString = JOptionPane.showInputDialog("Please enter a new balance for the checking account.");
            checkingBal = Double.parseDouble(inputString);
            good = false;
         }
      }
      
      // Calls window building methods, then enables the main window.
      buildMain();
      buildDeposit();
      buildWithdraw();
      buildTransfer();
      manualWin.setVisible(true);
   }
   
   /**
     The buildMain method builds the main window.
     The main window has buttons for deposit, withdraw, transfer, and exit.
   */
   private static void buildMain()
   {
      final int MAIN_WIDTH = 350;
      final int MAIN_HEIGHT = 350;
      
      manualWin = new JFrame("Manual Window");
      JPanel mainPanel = new JPanel();
      
      depositBtn = new JButton("Deposit");
      withdrawBtn = new JButton("Withdraw");
      transferBtn = new JButton("Transfer of Funds");
      exitBtn = new JButton("Exit");
      
      depositBtn.addActionListener(new BankGuiApplication());
      withdrawBtn.addActionListener(new BankGuiApplication());
      transferBtn.addActionListener(new BankGuiApplication());
      exitBtn.addActionListener(new BankGuiApplication());
      
      mainPanel.add(depositBtn);
      mainPanel.add(withdrawBtn);
      mainPanel.add(transferBtn);
      mainPanel.add(exitBtn);
      
      manualWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      manualWin.setSize(MAIN_WIDTH, MAIN_HEIGHT);
      manualWin.add(mainPanel);
      manualWin.setLocationRelativeTo(null); // Centers window. Present in all build methods.
   }
   
   /**
     The buildDeposit method builds the deposit window.
     It has options to select an account, input an amount to deposit, and a button to deposit.
   */
   private static void buildDeposit()
   {
      final int DEPOSIT_WIDTH = 350;
      final int DEPOSIT_HEIGHT = 350;
      
      depositWin = new JFrame("Deposit Window");
      JPanel depositPanel = new JPanel();
      depositWinBtn = new JButton("Deposit funds");
      backBtn1 = new JButton("Back");
      depositLabel = new JLabel("Amount to be deposited: ");
      depositField = new JTextField(20);
      accounts1 = new JComboBox<>();
      
      backBtn1.addActionListener(new BankGuiApplication());
      depositWinBtn.addActionListener(new BankGuiApplication());
      
      accounts1.addItem("Savings Account");
      accounts1.addItem("Checking Account");
      
      depositPanel.add(depositLabel);
      depositPanel.add(depositField);
      depositPanel.add(depositWinBtn);
      depositPanel.add(backBtn1);
      
      depositPanel.add(accounts1);
      depositWin.setSize(DEPOSIT_WIDTH, DEPOSIT_HEIGHT);
      depositWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      depositWin.add(depositPanel);
      depositWin.setLocationRelativeTo(null);
   }
   
   /**
     The buildWithdraw method builds the withdraw window. 
     The withdraw window has options to select the account, input an amount to withdraw, and a button to withdraw.
   */
   private static void buildWithdraw()
   {
      final int WITHDRAW_WIDTH = 250;
      final int WITHDRAW_HEIGHT = 200;
      withdrawWin = new JFrame("Withdraw Window");
      JPanel withdrawPanel = new JPanel();
      withdrawWinBtn = new JButton("Withdraw funds");
      backBtn2 = new JButton("Back");
      withdrawLabel = new JLabel("Amount to withdrawn: ");
      withdrawField = new JTextField(20);
      accounts2 = new JComboBox<>();
      
      withdrawWinBtn.addActionListener(new BankGuiApplication());
      backBtn2.addActionListener(new BankGuiApplication());
      
      accounts2.addItem("Savings Account");
      accounts2.addItem("Checking Account");
      
      withdrawPanel.add(withdrawLabel);
      withdrawPanel.add(withdrawField);
      withdrawPanel.add(withdrawWinBtn);
      withdrawPanel.add(backBtn2);
      
      withdrawPanel.add(accounts2);
      withdrawWin.setSize(WITHDRAW_WIDTH, WITHDRAW_HEIGHT);
      withdrawWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      withdrawWin.add(withdrawPanel);
      withdrawWin.setLocationRelativeTo(null);
   }
   
   /**
     The buildTransfer builds the transfer window.
     The transfer window has options to select an account to transfer from, an account to transfer to,
     input the amount to transfer, and also displays the balance of both accounts.
   */
   private static void buildTransfer()
   {  
      transferWin = new JFrame("Transfer Window");
      JPanel transferPanel1 = new JPanel();
      JPanel transferPanel2 = new JPanel();
      JPanel transferPanel3 = new JPanel();
      JPanel transferPanel4 = new JPanel();
      JPanel transferPanel5 = new JPanel();
      JPanel transferPanel6 = new JPanel();
      transferWinBtn = new JButton("Transfer funds");
      backBtn3 = new JButton("Back");
      transferLabel = new JLabel("Enter amount to Transfer");
      transferField = new JTextField(20);
      transferSavingsLabel = new JLabel("Balance of savings: " + dollars.format(savingsAcct.getBalance()));
      transferCheckingLabel = new JLabel("Balance of checking: " + dollars.format(checkingAcct.getBalance()));
      accounts3 = new JComboBox<>();
      accounts4 = new JComboBox<>();
      
      transferWinBtn.addActionListener(new BankGuiApplication());
      backBtn3.addActionListener(new BankGuiApplication());
      
      // A gridLayout is used to make the buttons and displays clear.
      transferWin.setLayout(new GridLayout(2, 3));
      
      accounts3.addItem("Savings Account");
      accounts3.addItem("Checking Account");
      accounts4.addItem("Savings Account");
      accounts4.addItem("Checking Account");
      
      accounts3.addActionListener(new BankGuiApplication());
      accounts4.addActionListener(new BankGuiApplication());
      
      transferPanel1.add(accounts3);
      transferPanel2.add(transferLabel);
      transferPanel2.add(transferField);
      transferPanel3.add(accounts4);
      transferPanel4.add(transferSavingsLabel);
      transferPanel5.add(transferWinBtn);
      transferPanel5.add(backBtn3);
      transferPanel6.add(transferCheckingLabel);
      
      transferWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      transferWin.add(transferPanel1);
      transferWin.add(transferPanel2);
      transferWin.add(transferPanel3);
      transferWin.add(transferPanel4);
      transferWin.add(transferPanel5);
      transferWin.add(transferPanel6);
      transferWin.pack(); 
      transferWin.setLocationRelativeTo(null);
   }
   
   /**
     The depositWindow method hides the main window and makes the deposit window visible.
   */
   public static void depositWindow()
   {
      manualWin.setVisible(false);
      depositWin.setVisible(true);
   }
   
   /**
     The withdrawWindow method hides the main window and makes the withdraw window visible.
   */
   public static void withdrawWindow()
   {
      manualWin.setVisible(false);
      withdrawWin.setVisible(true);
   }
   
   /**
     The transferWindow method hides the main window and makes the transfer window visible.
   */
   public static void transferWindow()
   {  
      manualWin.setVisible(false);
      transferSavingsLabel.setText("Balance of savings: " + dollars.format(savingsAcct.getBalance()));
      transferCheckingLabel.setText("Balance of checking: " + dollars.format(checkingAcct.getBalance()));
      transferWin.setVisible(true);
   }
   
   /**
     The depositAmount method deposits an amount into the selected account.
     @param account The BankAccount object to deposit a value into.
     @param amount The amount to deposit.
     @param selected An int used to determine which account was selected.
   */
   public static void depositAmount(BankAccount account, double amount, int selected)
   {
      boolean good = false;
      while(!good)
      {
         try
         {
            account.deposit(amount);
            good = true;
         }
         catch(InvalidDepositAmount e)
         {
            JOptionPane.showMessageDialog(null, e.getMessage());
            String str = JOptionPane.showInputDialog("Please enter another amount");
            amount = Double.parseDouble(str);
         }
      }
      
      if(selected == 1)
      {
         JOptionPane.showMessageDialog(null, "Amount deposited into savings: " + dollars.format(amount)
                                       + "\nBalance after deposit: " + dollars.format(account.getBalance()));
      }
      else if(selected == 2)
      {
         JOptionPane.showMessageDialog(null, "Amount deposited into checking: " + amount
                                    + "\nBalance after deposit: " + dollars.format(account.getBalance()));
      }
   }
   
   /**
     The withdrawAmount method withdraws an amount from an account.
     @param account The BankAccount object to withdraw a value from.
     @param amount The amount to withdraw.
     @param selected An int value used to determine which account was selected.
   */
   public static void withdrawAmount(BankAccount account, double amount, int selected)
   {
      boolean good = false;
      
      while(!good)
      {
         try
         {
            account.withdraw(amount);
            good = true;
         }
         catch(InvalidWithdrawalAmount e)
         {
            JOptionPane.showMessageDialog(null, e.getMessage());
            String str = JOptionPane.showInputDialog("Please enter another amount");
            amount = Double.parseDouble(str);
         }
      }
      if(selected == 1)
      {
         JOptionPane.showMessageDialog(null, "Amount withdrawn from savings: " + dollars.format(amount) +
                                       "\nBalance after withdrawal: " + dollars.format(account.getBalance()));
      }
      else if(selected == 2)
      {
         JOptionPane.showMessageDialog(null, "Amount withdrawn from checking: " + dollars.format(amount) +
                                              "\nBalance after withdrawal: " + dollars.format(account.getBalance()));
      }
   }
   
   /**
     The transferAmount method transfers a value from one BankAccount object to another.
     It does by withdrawing an amount, then depositing it into another.
     @param account1 The account to withdraw the money from.
     @param account2 The account to deposit money into.
     @param amount The amount to transfer between accounts.
     @param selected An int used to determine which accounts were selected.
   */
   public static void transferAmount(BankAccount account1, BankAccount account2, double amount, int selected)
   {
      boolean good = false;
      String str;
      
      while(!good)
      {
         try
         {
            account1.transfer(account2, amount);
            good = true;
         }
         catch(InvalidWithdrawalAmount | InvalidDepositAmount e)
         {// InvalidWithdrawalAmount will always be thrown first in this situation. However, both exceptions need to be caught.
          // As such, both are caught in the same catch block.
            JOptionPane.showMessageDialog(null, e.getMessage());
            
            str = JOptionPane.showInputDialog("Please enter a new amount.");  
            amount = Double.parseDouble(str);
          }
       }
       
       // These lines update the labels after a transfer
       if(selected == 1)
       {
          transferSavingsLabel.setText("Balance of savings: " + dollars.format(account1.getBalance()));
          transferCheckingLabel.setText("Balance of checking: " + dollars.format(account2.getBalance()));
       }
       else if(selected == 2)
       {
          transferSavingsLabel.setText("Balance of savings: " + dollars.format(account2.getBalance()));
          transferCheckingLabel.setText("Balance of checking: " + dollars.format(account1.getBalance()));
       }
       
       // Determines which message to display.
       if(selected == 1)
       {
          JOptionPane.showMessageDialog(null, "Amount transferred from savings to checking: $" + amount);
       }
       else if(selected == 2)
       {
          JOptionPane.showMessageDialog(null, "Amount transferred from checking to savings: $" + amount);
       }
   }
   
   /**
     The exit method calls the processing methods for the accounts, deducts accumulated fees, 
     saves the accounts to a file, then exits the program.
   */
   public static void exit()
   {
      try
      {
         savingsAcct.monthlyProcess();
         checkingAcct.deductFees(); // Accumulated fees are removed before the monthly proccess
         checkingAcct.monthlyProcess();
         writeObj(savingsAcct, checkingAcct);
         System.exit(0);
      }
      catch(InvalidWithdrawalAmount e)
      {
         JOptionPane.showMessageDialog(null, "An error has occured while deducting fees.");
      }
      catch(FileNotFoundException e)
      { 
         JOptionPane.showMessageDialog(null, "ERROR: File not found");
      }
      catch(IOException e)
      { 
         JOptionPane.showMessageDialog(null, "An error has occured with file input or output.");
      }
   }
   
   /**
     The writeObj method writes the data from the SavingsAccount and CheckingAccount
     objects to a file. It throws its exceptions as it is intended for the main method to handle them. 
     @param savings The SavingsAccount object to save to the file.
     @param checking The CheckingAccount object to save to the file.
     @exception IOException Thrown when an I/O error occurs.
   */
   public static void writeObj(SavingsAccount savings, CheckingAccount checking) throws IOException   
   {
      JOptionPane.showMessageDialog(null, "Saving data to file..."); // Informs user data is being written.
      
      FileOutputStream savingStream = new FileOutputStream("Savings.dat");
      ObjectOutputStream savOutputFile = new ObjectOutputStream(savingStream);
         
      FileOutputStream checkingStream = new FileOutputStream("Checking.dat");
      ObjectOutputStream checkOutputFile = new ObjectOutputStream(checkingStream);
         
      savOutputFile.writeObject(savings); 
      checkOutputFile.writeObject(checking);
         
      savOutputFile.close();
      checkOutputFile.close();
         
      JOptionPane.showMessageDialog(null, "Saving completed."); // Signifies this method completed properly.
   }
   
   /**
     The readObj method reads serialized BankAccount data from a file,
     then returns the object. This class is primarily used by the main method, as such
     it throws its exceptions rather than handle them. This is because the main method
     determines if new files must be created, or if existing ones can be read from.
     @param fileName The file name to read from.
     @return The derserialized BankAccount object.
     @exception FileNotFoundException Thrown when the file name is not found.
     @exception IOException Thrown when an I/O error occurs. 
     @exception ClassNotFoundException Thrown when a class cannot be found. 
   */
   public static BankAccount readObj(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
   { 
      FileInputStream savInStream = new FileInputStream(fileName);
      ObjectInputStream saveFile = new ObjectInputStream(savInStream);
      BankAccount savedAccount = (BankAccount)saveFile.readObject();
         
      saveFile.close();
      
      return savedAccount;
   }
   
   /**
     The actionPerformed method is the listener method for this class.
     It is called when an action is performed (such as a button click)
     and then reacts accordingly using a long series of if-else-ifs.
     @param e An ActionEvent object which is sent when an action is performed. 
   */
   public void actionPerformed(ActionEvent e)
   {       
      if(e.getSource() == depositBtn)
      {
         depositWindow();
      }
      else if(e.getSource() == depositWinBtn)
      {
         String input = depositField.getText();
         double amount = Double.parseDouble(input);
         
         if(accounts1.getSelectedItem().equals("Savings Account"))
         {
            depositAmount(savingsAcct, amount, 1);
         }
         else if(accounts1.getSelectedItem().equals("Checking Account"))
         {
            depositAmount(checkingAcct, amount, 2);
         }
      }
      else if(e.getSource() == withdrawBtn)
      {
         withdrawWindow();
      }
      else if(e.getSource() == withdrawWinBtn)
      {
         String input = withdrawField.getText();
         double amount = Double.parseDouble(input);
         
         if(accounts2.getSelectedItem().equals("Savings Account"))
         {
            withdrawAmount(savingsAcct, amount, 1);
         }
         else if(accounts2.getSelectedItem().equals("Checking Account"))
         {
            withdrawAmount(checkingAcct, amount, 2);
         }
      }
      else if(e.getSource() == transferBtn)
      {
         transferWindow();
      }
      else if(e.getSource() == transferWinBtn)
      {
         String input = transferField.getText();
         double amount = Double.parseDouble(input);
         
         if(accounts3.getSelectedItem().equals("Savings Account") && accounts4.getSelectedItem().equals("Checking Account"))
         {
            transferAmount(savingsAcct, checkingAcct, amount, 1);
         }
         else if(accounts3.getSelectedItem().equals("Checking Account") && accounts4.getSelectedItem().equals("Savings Account"))
         {
            transferAmount(checkingAcct, savingsAcct, amount, 2);
         }
         else
         {
            JOptionPane.showMessageDialog(null, "You cannot select the same account both times.");
         }
         
      }
      else if(e.getSource() == exitBtn)
      {
         exit();
      }
      else if(e.getSource() == backBtn1 || e.getSource() == backBtn2 || e.getSource() == backBtn3)
      {
         depositWin.setVisible(false);
         withdrawWin.setVisible(false);
         transferWin.setVisible(false);
         manualWin.setVisible(true);
      }
   }
}