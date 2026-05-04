package model;

import java.util.Arrays;

import org.tinylog.Logger;

public class InputValidation {

  public static final String[] VALID_CATEGORIES = {"food", "travel", "bills", "entertainment", "other"};
	
  public static boolean isValidAmount(double amount) {
    
    // Check range
    if(amount >1000) {
      Logger.debug("Amount validation failed: {} exceeds 1000", amount);
      return false;
    }
    if (amount < 0){
      Logger.debug("Amount validation failed: {} is negative", amount);
      return false;
    }
    if (amount == 0){
      Logger.debug("Amount validation failed: amount is zero");
      return false;
    }
    return true;
  }

  public static boolean isValidCategory(String category) {

    if(category == null) {
      Logger.debug("Category validation failed: category is null");
      return false; 
    }
  
    if(category.trim().isEmpty()) {
      Logger.debug("Category validation failed: category is empty");
      return false;
    }

    if(!category.matches("[a-zA-Z]+")) {
      Logger.debug("Category validation failed: '{}' contains non-alphabetic characters", category);
      return false;
    }

    if(!Arrays.asList(VALID_CATEGORIES).contains(category.toLowerCase())) {
      // invalid word  
      Logger.debug("Category validation failed: '{}' is not a valid category", category);
      return false;
    }
  
    return true;
  
  }

  public static boolean isValidFilename(String filename) {
    if (filename == null) return false;
    String trimmed = filename.trim();
    if (trimmed.isEmpty()) return false;
    // Disallow obvious path traversal
    if (trimmed.contains("..")) return false;
    // Use the file name portion for checking the extension so absolute paths are allowed
    java.io.File f = new java.io.File(trimmed);
    String name = f.getName();
    if (name == null || name.trim().isEmpty()) return false;
    String lower = name.toLowerCase();
    if (!lower.endsWith(".csv")) return false;
    // Ensure there's at least one character before the extension
    if (name.length() <= 4) return false;
    return true;
  }
}