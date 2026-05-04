package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.tinylog.Logger;


public class CSVImporter implements CSVConstants {
	public List<Transaction> importTransactions(String inputFileName) throws IOException {
		List<Transaction> importedTransactionsList = null;
		if (inputFileName != null) {
			Logger.info("Importing transactions from: {}", inputFileName);
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			importedTransactionsList = new ArrayList<Transaction>();
			
			String currentLine = null;
			int currentLineNumber = 1;
			while ((currentLine = reader.readLine()) != null) {
				// Skip the headers
				if (currentLineNumber > 1) {
					StringTokenizer tokenizer = new StringTokenizer(currentLine, COMMA_SEPARATOR);
					String currentAmountString = tokenizer.nextToken();
					double currentAmount = Double.parseDouble(currentAmountString);
					String currentCategory = tokenizer.nextToken();
					String currentDate = tokenizer.nextToken();
					Logger.debug("Parsed line {}: amount={}, category={}, date={}", currentLineNumber, currentAmount, currentCategory, currentDate);

					importedTransactionsList.add(new Transaction(currentAmount, currentCategory, currentDate));
				}
				currentLineNumber++;
			}

			reader.close();
			Logger.info("Import complete: {} transactions loaded from {}", importedTransactionsList.size(), inputFileName);
		}
		
		return importedTransactionsList;
	}
}
