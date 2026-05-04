package model;

import java.util.ArrayList;
import java.util.List;

import org.tinylog.Logger;

/**
 * Represents the data model as a list of transactions.
 * 
 * NOTE) Represents the Model in the MVC architecture pattern.
 */
public class ExpenseTrackerModel {

	private List<Transaction> transactions = new ArrayList<>();
	
	public ExpenseTrackerModel() {
		super();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(Transaction t) {
		transactions.add(t);
		Logger.debug("Transaction added: amount={}, category={}, timestamp={}", t.getAmount(), t.getCategory(), t.getTimestamp());
	}

	public boolean removeTransaction(int transactionID) {
  	  // Perform input validation
  	  if ((transactionID < 0) || (transactionID > this.getTransactions().size() - 1)) {
  		  Logger.debug("Remove failed: transaction ID {} is out of range [0, {}]", transactionID, this.getTransactions().size() - 1);
  		  return false;
  	  }
  	  else {
  		  Transaction removed = transactions.get(transactionID);
  		  Logger.debug("Removing transaction at index {}: amount={}, category={}", transactionID, removed.getAmount(), removed.getCategory());
  		  transactions.remove(transactionID);
  		  return true;
  	  }
	}

	public double computeTransactionsTotalCost() {
		double totalCost=0;
		for(Transaction t : transactions) {
			totalCost+=t.getAmount();
		}
		Logger.trace("Computed total cost: {}", totalCost);
		return totalCost;
	}
}
