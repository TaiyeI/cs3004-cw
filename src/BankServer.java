import java.net.*;
import java.util.HashMap;
import java.io.*;


public class BankServer {
  public static void main(String[] args) throws IOException {

	//Run Bank Server on specified Port
	ServerSocket BankServerSocket = null;
    boolean listening = true;
    String BankServerName = "BankServer";
    int BankServerNumber = 4545;
    
    HashMap<String, Double> bankAccounts = new HashMap<String, Double>();
    bankAccounts.put("ClientA", 1000.0);
    bankAccounts.put("ClientB", 1000.0);
    bankAccounts.put("ClientC", 1000.0);
    
    //convert shared variable to dictionary possibly

    //Create the shared object in the global scope...
    
    SharedBankState ourSharedBankStateObject = new SharedBankState(bankAccounts);
    
        
    // Make the server socket

    try {
      BankServerSocket = new ServerSocket(BankServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + BankServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(BankServerName + " started");

    
    while (listening){
      new BankServerThread(BankServerSocket.accept(), "ClientA", ourSharedBankStateObject).start();
      new BankServerThread(BankServerSocket.accept(), "ClientB", ourSharedBankStateObject).start();
      new BankServerThread(BankServerSocket.accept(), "ClientC", ourSharedBankStateObject).start();
      new BankServerThread(BankServerSocket.accept(), "ClientA", ourSharedBankStateObject).start();
      System.out.println("New " + BankServerName + " thread started.");
    }
    BankServerSocket.close();
  }
}