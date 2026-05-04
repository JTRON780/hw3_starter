package model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.tinylog.Logger;

/**
 * CSV (Comma Separated Value) implementation of {@link TransactionExporter}.
 */
public class CSVExporter implements TransactionExporter, CSVConstants {
  
  @Override
  public String exportTransactions(List<Transaction> txns, String filename) {
    if (txns == null) return TRANSACTION_LIST_ERROR_MESSAGE;
    if (!InputValidation.isValidFilename(filename)) return FILENAME_ERROR_MESSAGE;

    Logger.info("Exporting {} transactions to: {}", txns.size(), filename);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
      bw.write(CSV_HEADERS);
      bw.newLine();
      for (Transaction t : txns) {
        String line = String.format("%s" + COMMA_SEPARATOR + "%s" + COMMA_SEPARATOR + "%s", Double.toString(t.getAmount()), t.getCategory(), t.getTimestamp());
        bw.write(line);
        bw.newLine();
      }
      bw.flush();
      Logger.info("Export complete: {} transactions written to {}", txns.size(), filename);
      return null;
    } catch (IOException e) {
      Logger.error("Export failed for {}: {}", filename, e.getMessage());
      return e.getMessage();
    }
  }

}