package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.tinylog.Logger;

import model.CSVExporter;
import model.CSVImporter;
import model.ExpenseTrackerModel;
import model.InputValidation;
import model.Transaction;
import view.ExpenseTrackerView;

/**
 * Provides the application programming layer to support the
 * following interface: addTransaction, delete, import, export.
 * 
 * NOTE) Represents the Controller in the MVC architecture pattern.
 */
public class ExpenseTrackerController {
	private ExpenseTrackerModel model = new ExpenseTrackerModel();    
    private ExpenseTrackerView view = new ExpenseTrackerView(model);
    
    public ExpenseTrackerController() {
    	super();
    	
    	// Hook up the view and controller
    	
        // Handle add transaction button clicks
        view.getDataPanelView().getAddTransactionBtn().addActionListener(e -> {
        	addTransaction();
        });
        
        // Handle "Delete" menu item clicks
        view.getDeleteMenuItem().addActionListener(e -> {
        	delete();
        });
        
        // Handle "Open File..." menu item clicks
        view.getOpenFileMenuItem().addActionListener(e -> {
        	openFile();
        });
        
        // Handle "Save" menu item clicks
        view.getSaveAsMenuItem().addActionListener(e -> {	  
        	saveAs();
        });
        
        // Handle "Analyze" button clicks
        view.getAnalysisPanelView().getAnalyzeButton().addActionListener(e -> {
        	performDataAnalysis();
        });
        
        // Initialize view
        view.setVisible(true);
    }
    
    public ExpenseTrackerModel getModel() {
    	// For testing purposes
    	return this.model;
    }
    
    public ExpenseTrackerView getView() {
    	// For testing purposes
    	return this.view;
    }
    
    public void addTransaction() { 
    	try {
    		// Get transaction data from view
    		double amount = view.getDataPanelView().getAmount(); 
    		String category = view.getDataPanelView().getCategory();
    		Logger.info("Adding transaction: amount={}, category={}", amount, category);

    		// Create transaction object
    		Transaction t = new Transaction(amount, category);

    		// Call controller to add transaction
    		model.addTransaction(t);
    		view.refresh();
    		Logger.info("Transaction added successfully. Total transactions: {}", model.getTransactions().size());
    	}
    	catch (NumberFormatException nfe) {
    		Logger.warn("Invalid amount format: {}", nfe.getMessage());
    		view.displayErrorMessage("The amount cannot be parsed as a double number.");
    	}
    	catch (IllegalArgumentException iae) {
    		Logger.warn("Invalid transaction input: {}", iae.getMessage());
    		view.displayErrorMessage(iae.getMessage());
    	}
    }
    
    public void delete() {
        int selectedTransactionID = view.getDataPanelView().getSelectedTransactionID();
        Logger.info("Deleting transaction at index: {}", selectedTransactionID);
    	boolean removed = model.removeTransaction(selectedTransactionID);
    	if (! removed) {
    		Logger.warn("Delete failed: invalid transaction ID {}", selectedTransactionID);
    		view.displayErrorMessage("A valid transaction was not selected to be removed.");
    	}
    	else {
    		Logger.info("Transaction deleted successfully. Remaining transactions: {}", model.getTransactions().size());
    		view.refresh();
    	}
    }
    
    public void openFile() {
    	String inputFileName = view.showFileChooser(true);
    	if (inputFileName != null) {
    		Logger.info("Opening file: {}", inputFileName);
    		int transactionCount = model.getTransactions().size();
    		Logger.debug("Clearing {} existing transactions before import", transactionCount);
    		for (int i = 0; i < transactionCount; i++) {
    			model.removeTransaction(0);
    		}

    		try {
    			CSVImporter csvImporter = new CSVImporter();
    			List<Transaction> importedTransactionsList = csvImporter.importTransactions(inputFileName);
    			for (Transaction importedTransaction : importedTransactionsList) {				
    				model.addTransaction(importedTransaction);
    			}
    			Logger.info("Successfully imported {} transactions from {}", importedTransactionsList.size(), inputFileName);
    		}
    		catch (IOException ioe) {
    			Logger.error("Failed to import file {}: {}", inputFileName, ioe.getMessage());
    			view.displayErrorMessage(ioe.getMessage());
    		}
    		view.refresh();
    	}
    }
    
    public void saveAs() {
    	String outputFileName = view.showFileChooser(false);
    	if (outputFileName != null) {
    		Logger.info("Saving transactions to: {}", outputFileName);
    		CSVExporter csvExporter = new CSVExporter();
    		String errorMessage = csvExporter.exportTransactions(model.getTransactions(), outputFileName);
    		if (errorMessage != null) {
    			Logger.error("Failed to save file {}: {}", outputFileName, errorMessage);
    			view.displayErrorMessage(errorMessage);
    		}
    		else {
    			Logger.info("Successfully saved {} transactions to {}", model.getTransactions().size(), outputFileName);
    		}
    	}
    }
    
    public void performDataAnalysis() {
    	Logger.info("Performing data analysis with {} transactions", model.getTransactions().size());
    	view.getAnalysisPanelView().performDataAnalysis(model);
    }
}
