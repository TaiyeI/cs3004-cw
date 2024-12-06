import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class SharedBankState {
	
	private SharedBankState mySharedObj;
	private String myThreadName;
	
	private boolean isLocked=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers

	private HashMap<String, Double> mySharedVariable;
	
	//Constructor
	SharedBankState(HashMap<String, Double> bankAccounts){
		mySharedVariable = bankAccounts;
		
	}
	
	
	//search syncrhronized
	public synchronized void acquireLock() throws InterruptedException{
		Thread me = Thread.currentThread();
		System.out.println(me.getName() + "is attempting to acquire a lock!");
		++threadsWaiting;
	    while (isLocked) {  // while someone else is accessing or threadsWaiting > 0
	      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
	      //wait for the lock to be released - see releaseLock() below
	      wait();
	    }
	    // nobody has got a lock so get one
	    --threadsWaiting;
	    isLocked = true;
	    System.out.println(me.getName()+" got a lock!");
	}
	
	public synchronized void releaseLock() {
		isLocked = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a reference to the current thread
	     System.out.println(me.getName()+" released a lock!");
	}
	
	
	public synchronized String processInput(String myThreadName, String theInput) {
		System.out.println(myThreadName + " received "+ theInput);
		String[] inputArray;
		
		try {
			inputArray = theInput.split(" ");
		}
		catch(Exception e) {
			return theInput;
		}
		
		String theOutput = null;
		double inputValue = 0.0;
		double newfunds = (double) mySharedVariable.get(myThreadName);
		//the input format
		// Operation Amount Account2(optional)
		
		try {
			inputValue = Double.parseDouble(inputArray[1]);
		}
		catch (Exception e){
			System.out.println("invalid fund amount");
			return theInput;
		}
		
		switch(inputArray[0]) {
			case "Add":
				System.out.print("Adding");
				//try convert inputArray[1] to a double
				newfunds += inputValue;
				mySharedVariable.replace(myThreadName, newfunds);
				System.out.println(myThreadName + mySharedVariable.get(myThreadName));
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
				
			case "Subtract":
				newfunds -= inputValue;
				System.out.print("Subtracting");
				mySharedVariable.replace(myThreadName, newfunds);
				System.out.println(myThreadName + mySharedVariable.get(myThreadName));
				break;
				
			case "Transfer":
				mySharedVariable.replace(myThreadName, mySharedVariable.get(myThreadName)-inputValue);
				mySharedVariable.replace(inputArray[2], mySharedVariable.get(inputArray[2])+inputValue);
		}
		
		System.out.println("ClientA: " + mySharedVariable.get("ClientA"));
		System.out.println("ClientB: " + mySharedVariable.get("ClientB"));
		System.out.println("ClientC: " + mySharedVariable.get("ClientC"));
		
		return theInput;
	}
	
}
