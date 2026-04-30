# hw3_starter

# CS520 Spring 2026 - Homework 3

## Overview and Goal
In this assignment, you will work with an existing implementation of an Expense Tracker application. This application applies the MVC architecture pattern.
Rather than building brand-new user features, your task is to understand, analyze, and restructure the system using sound software engineering principles.

Treat this as a realistic on-boarding task: you are joining a team with shipped software, and your responsibility is to improve understandability, modularity, extensibility readiness, and testability while preserving current behavior. 

## Getting Started
1. Clone the repository: `git clone https://github.com/CS520-Spring2026/hw3_starter`
2. Read this `README.md` file.
3. Build, test, and run the application using the commands below.
4. Explore source code in `src/` and tests in `test/`.

We'll use the ant build tool (`https://ant.apache.org/manual/installlist.html`) to build and run the application.

## Optional Working Files
You may draft your answers in local markdown files (for example under `docs/`) while working, but these files are optional and are not required for grading.
All written graded content must appear in `HW3_answers.pdf`.

## Build and Run

The Expense Tracker application has the following structure:
- `bin/`: Contains the generated class files
- `jdoc/`: Contains the generated javadoc folders/files
- `lib/`: Contains the third party software jar files
- `src/`: Contains the Java folders and source files
- `test/`: Contains the JUnit test suite source files
- `build.xml`: Is the ant build tool input file
- `build/`: Contains the ant build tool output files
- `coverage_report/`: Contains the test coverage report

The build requirements are:
- JDK 21+: Generate API doc (javadoc), compile (javac), run (java)
- Ant 1.10.15+: Build and run the application and test suite(s)

From the root directory (containing the build.xml file):

1. Build app: `ant compile`

<!-- 2. Run the app: `java -cp bin ExpenseTrackerApp` -->
2. Run the app:
   - On Windows: `java -cp "bin;lib\xchart-3.8.8.jar;lib\tinylog-api-2.7.0.jar;lib\tinylog-impl-2.7.0.jar" ExpenseTrackerApp`
   - On Linux/macOS: `java -cp "bin:lib/xchart-3.8.8.jar:lib/tinylog-api-2.7.0.jar:lib/tinylog-impl-2.7.0.jar" ExpenseTrackerApp`

3. Build and run tests: `ant test` (See the build/TEST-*.txt files for more details.)

4. Generate the test coverage report: `ant coverage.report` (See `coverage_report/index.html` for more details.)

5. Generate Javadoc: `ant document`

6. Perform linting `ant lint`

7. Clean generated artifacts (e.g., class files, javadoc files): `ant clean`

# Architecture

The Expense Tracker application applies the MVC architecture pattern as follows:

* **model package:** Contains the data model and business logic.
  - `ExpenseTrackerModel` — Maintains the list of transactions; supports add, remove, and total cost computation.
  - `Transaction` — Represents a single transaction with amount, category, and auto-generated timestamp (format: `dd-MM-yyyy HH:mm`).
  - `InputValidation` — Validates user input for amounts, categories, and filenames.
  - `TransactionExporter` — Strategy interface for exporting transactions to various formats.
  - `CSVExporter` — CSV implementation of `TransactionExporter`; exports transactions to `.csv` files.
  - `CSVImporter` — Imports transactions from `.csv` files.
  - `CSVConstants` — Shared constants for CSV formatting (headers, separators, error messages).

* **view package:** Contains the UI visualizations and user interaction components.
  - `ExpenseTrackerView` — Main application window (JFrame) with a menu bar (File, Edit) and a tabbed panel (Data, Analysis).
  - `DataPanelView` — The "Data" tab; provides input fields for amount/category, an "Add Transaction" button, and a table displaying all transactions with a running total.
  - `AnalysisPanelView` — The "Analysis" tab; provides a time window selector, an "Analyze" button, and displays a bar chart of total cost per category using the XChart library.
  - `DataAnalysisTimeWindow` — Enum defining the available time windows: All, Last year, Last week.
  - `DataVizUtils` — Utility class that computes category-based summaries filtered by the selected time window.

* **controller package:** Contains the application logic that connects user interactions to model operations.
  - `ExpenseTrackerController` — Wires view events (button clicks, menu selections) to model operations (add, delete, import, export, analyze).

## Input Validation Rules

* **Amount:** Must be greater than 0 and less than 1000.
* **Category:** Must be one of the following (case-insensitive): `food`, `travel`, `bills`, `entertainment`, `other`.
* **Filename:** Must be non-empty, must end with `.csv`, and must not contain path traversal sequences (`..`).

## CSV File Format

Transactions are saved and loaded in CSV format with the following columns:

```
Amount,Category,Date
50.0,food,30-04-2026 16:39
133.0,travel,30-04-2026 16:39
```

# Features

* **Add Transaction:**
  In the 'Data' tab, enter a valid amount and category, then click **Add Transaction**.
  The valid transaction appears in the table with an auto-generated timestamp, and the total cost row updates automatically.

* **Delete Transaction:**
  In the 'Data' tab, select a transaction row from the table.
  In the 'Edit' menu, select the 'Delete' menu item.
  The transaction is removed from the table, and the total cost updates automatically.

* **Save the Transaction List:**
  In the 'File' menu, select the 'Save As...' menu item.
  In the Save dialog, select a destination and click the 'Save' button.
  The transactions are exported to a `.csv` file in the format described above.

* **Open a Transaction List:**
  In the 'File' menu, select the 'Open File...' menu item.
  In the Open dialog, select a valid `.csv` file and click the 'Open' button.
  The current transaction list is replaced with the imported transactions.

* **Analyze:**
  Select the 'Analysis' tab.
  Choose a time window from the dropdown: **All**, **Last year**, or **Last week**.
  Click the **Analyze** button.
  If there are transactions within the selected time window, a **bar chart** is displayed showing the **total cost per category** (using the XChart library).
  If there are no transactions in that time window, an error message is displayed.
