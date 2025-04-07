---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Wanted Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is a modification of [AddressBook Level 3](https://github.com/se-edu/addressbook-level3) (AB3). The code structure, models and documentation is reused and maintained in largely the same format as AB3.

--------------------------------------------------------------------------------------------------------------------

## **Setting Up, Getting Started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<box type="info" seamless>

**Note**: In this Developer Guide, the term `loan book` is used with the same meaning as `Wanted list` in the User Guide.
</box>

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `LoanListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Loan` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `LoanBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a loan).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `LoanBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `LoanBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="520" />


The `Model` component,

* stores the loan book data i.e., all `Loan` objects (which are contained in a `UniqueLoanList` object).
* offers a list of all the stored `Loan` objects to outsiders as an unmodifiable `ObservableList<Loan>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

`LoanAmount` is a class to manage the loan amount of an entry. Detailed explanation can be found here.

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `LoanBook`, which `Loan` references. This allows `LoanBook` to only require one `Tag` object per unique tag, instead of each `Loan` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="850" />

The `Storage` component,
* can save both loan book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `LoanBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `wanted.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedLoanBook`. It extends `LoanBook` with an undo/redo history, stored internally as an `loanBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedLoanBook#commit()`—Saves the current loan book state in its history.
* `VersionedLoanBook#undo()`—Restores the previous loan book state from its history.
* `VersionedLoanBook#redo()`—Restores a previously undone loan book state from its history.

These operations are exposed in the `Model` interface as `Model#commitLoanBook()`, `Model#undoLoanBook()` and `Model#redoLoanBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedLoanBook` will be initialized with the initial loan book state, and the `currentStatePointer` pointing to that single loan book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th entry in the loan book. The `delete` command calls `Model#commitLoanBook()`, causing the modified state of the loan book after the `delete 5` command executes to be saved in the `loanBookStateList`, and the `currentStatePointer` is shifted to the newly inserted loan book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David` to add a new entry. The `add` command also calls `Model#commitLoanBook()`, causing another modified loan book state to be saved into the `loanBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitLoanBook()`, so the loan book state will not be saved into the `loanBookStateList`.

</box>

Step 4. The user now decides that adding the loan was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoLoanBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous loan book state, and restores the loan book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial LoanBook state, then there are no previous LoanBook states to restore. The `undo` command uses `Model#canUndoLoanBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoLoanBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the loan book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `loanBookStateList.size() - 1`, pointing to the latest loan book state, then there are no undone LoanBook states to restore. The `redo` command uses `Model#canRedoLoanBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `help`. Commands that do not modify the loan book, such as `help`, will usually not call `Model#commitLoanBook()`, `Model#undoLoanBook()` or `Model#redoLoanBook()`. Thus, the `loanBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitLoanBook()`. Since the `currentStatePointer` is not pointing at the end of the `loanBookStateList`, all loan book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire loan book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the loan being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* loans money to others
* prefers desktop apps over other types
* can type quickly
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage loans much more easily than manual tracking with manual calculations, in a faster and more efficient manner than mouse-based apps

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​            | I want to …​                                                          | So that I can…​                                                        |
|-------|--------------------|-----------------------------------------------------------------------|------------------------------------------------------------------------|
| `* * *` | loan issuer        | keep track of the total amount of money owed to me                    | I can see how much I am due to collect                                 |
| `* * *` | user               | add a new loan                                                        | I can track new loans that are given out                               |
| `* * *` | user               | delete a loan                                                         | remove loans that are no longer relevant                               |
| `* * *` | user               | view current loans                                                    | I can see the full list of loans that need to be returned              |
| `* *` | user               | edit loans                                                            | I can update loan information if necessary                             |
| `* *` | user               | mark loans as returned                                                | I can keep track of whether a loan was repaid                          |
| `* *` | forgetful user     | track the number of days since the loan was given                     | I can remind friends to return their loans                             | |
| `* *` | forgetful user     | add phone numbers of people who owe me money                          | I can contact them if necessary                                        |
| `* *` | frequent loaner    | view a history of all loans that have been fulfilled                  | I can keep track of past lending habits                                | |
| `* *` | new user           | go through a new user guide                                           | I learn how to use the program                                         |
| `* *` | new user           | view example entries                                                  | I can learn how to use from the examples                               |
| `* *` | user               | blacklist people who are always late to return money                  | I can avoid loaning to particular individuals                          |
| `* *` | user               | tag individuals based on the amount of money lent                     | I can prioritise users that have larger ticket size loans              |
| `* *` | user               | tag individuals based on their spending habits                        | I can avoid lending to high risk individuals                           |
| `* *` | user               | view a leaderboard of those with largest loans                        | I can have a visual representation of who needs to be chased for loans |
| `* *` | user               | see past loans categorised by month and loan type                     | I can project loaning for future months                                |
| `* *` | user               | delete all records                                                    | I can purge examples                                                   |
| `* *` | user               | sort records, by loan amount, period, priority etc.                   | I can see the most 'important' records for me                          |
| `*`   | forgetful user     | upload photos of people who owe me money                              | I can match their appearance to their loans                            |
| `*`   | user               | have notifications for those who have loaned for longer than duration | I can prompt them to return that it has been past a grace period       |
| `*`   | user               | send messages to send to people who owe me money                      | I can efficiently prod them to return the money                        |
| `*`   | user               | customize the autogenerated message sent to those who owe money       | I can better persuade them to return my money                          |
| `*`   | cash strapped user | calculate projected returns if everyone was to return loans           | I can see how much I can earn back from chasing people for loans       |

### Use cases

(For all use cases below, the **System** is the `Wanted` application and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - `Add` a New Loan**

**MSS**

1. User requests to add a new loan with borrower's name.
2. System records the new loan in the loan list.
3. System confirms that the loan has been successfully added.

   Use case ends.

**Extensions**

* 1a. System detects an error in the command entered.
  * 1a1. System displays an error message and tells the user the correct format to enter.
  * Use case resumes from step 1.

**Use case: UC02 - `Delete` a Loan**

**MSS**

1. User requests to delete a specific loan.
2. System removes the loan from the loan list.
3. System confirms that the loan has been successfully deleted.

   Use case ends.

**Extensions**

* 1a. The loan does not exist in the system.
  * 1a1. System displays a message indicating that the loan is not found.
  * Use case ends.

**Use case: UC03 - `List` Current Loans**

**MSS**

1. User requests to list all loans by name in alphabetical order.
2. System retrieves and displays the list of loans.

   Use case ends.

**Use case: UC04 - `Sort` Current Loans**

**MSS**

1. User requests to sort all loans by Remaining Loan Amount in decreasing order.
2. System retrieves and displays the list of loans.

   Use case ends.

**Use Case: UC05 - `Increase` a Loan Amount**

**MSS**
1. User requests to add an increase transaction of a specified amount and date to a loan.
2. System creates a new increase transaction.
3. System adds the transaction to the transaction history of the loan.
4. System updates and displays Total Amount Loaned and Remaining Loan Amount of the loan.

   Use case ends.

**Extensions**

* 1a. System detects an error in the command entered.
    * 1a1. System displays an error message and tells the user the correct format to enter.
    * Use case resumes from step 1.

**Use Case: UC06 - `Repay` a Loan Amount in Full or Partially**

**MSS**
1. User requests to add a repayment transaction of a specified amount and date to a loan.
2. System creates a new repayment transaction.
3. System adds the transaction to the loan history of the loan.
4. System updates and displays Total Amount Loaned and Remaining Loan Amount of the loan.

   Use case ends.

**Extensions**

* 1a. System detects an error in the command entered.
    * 1a1. System displays an error message and tells the user the correct format to enter.
    * Use case resumes from step 1.
* 1b. System detects the repayment amount is greater than the Remaining Loan Amount of the loan.
    * 1b1. System displays an error message informing the user to key in a valid repayment amount.
    * Use case resumes from step 1.

**Use Case: UC07 - Editing a Loan Transaction**

**MSS**
1. User requests to modify the amount and/or date of a transaction in a loan.
2. System updates the transaction.
3. System updates and displays the updated transaction history, Total Amount Loaned and Remaining Loan Amount of the loan.

   Use case ends.

**Extensions**

* 1a. System detects an error in the command entered.
    * 1a1. System displays an error message and tells the user the correct format to enter.
    * Use case resumes from step 1.
* 1b. System detects the modified amount will result in a negative Remaining Loan Amount.
    * 1b1. System displays an error message informing the user to key in a valid amount.
    * Use case resumes from step 1.

**Use Case: UC08 - Deleting a Loan Transaction**

**MSS**
1. User requests to delete a transaction in a loan.
2. System deletes the transaction.
3. System updates and displays the updated transaction history, Total Amount Loaned and Remaining Loan Amount of the loan.

   Use case ends.

**Extensions**

* 1a. System detects an error in the command entered.
    * 1a1. System displays an error message and tells the user the correct format to enter.
    * Use case resumes from step 1.
* 1b. System detects the deleted transaction will result in a negative Remaining Loan Amount.
    * 1b1. System displays an error message informing the user to key in a valid amount.
    * Use case resumes from step 1.

**Use Case: UC09 - `Tag` a Loan**

**MSS**
1. User requests to add tag(s) to a loan.
2. System adds the tag(s) to the loan.
3. System displays the updated loan.

   Use case ends.

**Extensions**

* 1a. System detects an error in the command entered.
    * 1a1. System displays an error message and tells the user the correct format to enter.
    * Use case resumes from step 1.
* 1b. User requests to delete all tags from a loan by entering an empty tag.
    * 1b1. System removes all tags from the loan.
    * Use case resumes from step 3.

**Use Case: UC10 - Modifying the `Phone` of a Loan**

**MSS**
1. User requests to modify the phone number of a loan.
2. System modifies the phone number of the loan.
3. System display the updated loan.

   Use case ends.

**Extensions**

* 1a. System detects an error in the command entered.
    * 1a1. System displays an error message and tells the user the correct format to enter.
    * Use case resumes from step 1.

**Use Case: UC11 - `Clear`ing all Loans**

**MSS*
1. User requests to clear all loans from the list.
2. System deletes all loans from the list.

   Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 loans without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Loan**: A group of information associated with a person the user lends to.
* **Loan Issuer**: The loan who lends money or items to others and expects repayment or return.
* **User**: A general term for loan issuers managing their loans.
* **Forgetful User**: A user who needs additional reminders and tracking features to recall outstanding loans.
* **Frequent Loaner**: A user who frequently lends money or items and needs an organized record of past and present loans.
* **Cash-Strapped User**: A user who urgently needs to recover loaned money to maintain financial stability.
* **New User**: Someone who has just started using the system and may require guidance on how to navigate it.
* **Amount**: A sum of money.
* **Total Amount Loaned**: Total amount of money loaned to a person across all transactions associated with them.
* **Remaining Loan Amount**: Amount of money a person is yet to repay across all transactions associated with them.
* **Transaction**: An action to increase or repay the loan owed to the user.
* **Tags**: A method of categorizing individuals based on different attributes such as spending habits, friends, family etc.
* **Leaderboard**: A visual ranking system displaying individuals with the highest loans.
* **Loan History**: A record of all transactions under a current loan, categorized by increase or repay transactions.
* **Clear**: The ability to delete all stored loan data, often used to clear test or example entries.
* **Example Entries**: Pre-filled sample data to help new users understand how the system works.
* **Wanted/Not Wanted**: UI element on each loan entry representing whether the loan is fully returned.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the latest jar file and copy into an empty folder.

   1. Run the jar file with Java 17 (best to be done through command line). The default window size may not be suitable.

1. Saving window preferences

   1. Resize the window to a desired size. Move the window to a different location. Close the window.

   1. Re-launch the app.<br>
       Expected: The most recent window size and location is retained.

### Adding a loan

1. Prerequisites: No entries are in the list, easiest done by clearing all entries from the list by running `clear`.

2. Creating a new entry

   1. Test case: `add n/John`<br>
   Expected: A new entry with name `John` is created in position 1. It should also have the default parameters of $0.00 in loan amount, no tags and no phone number.
   
   2. Test case: `add n/John`<br>
   Expected: No entry is created. Error details shown in status message. Command is not erased from input field.
   
   3.  Test case: `add n/john` <br>
   Expected: A new entry is name `john` is created with default parameters. 

3. Adding a phone number to entries

   1. Test case: `phone 1 p/98765432`
   Expected: Adds the phone number `98765432` to the entry for `John`.
   
   2. Test case: `phone 1 p/delete`
   Expected: Deletes the phone number and removes it from the display for the entry for `John`.
   
   3. Test case: `phone 2 p/abcdefgh`
   Expected: No phone number is updated. Error details shown in status message. Command is not erased from input field.

4. Adding tags to entries

   1. Test case: `tag 1 t/friend t/frequentloaner`
   Expected: Adds the tags `friend` and `frequentloaner` to the entry for `John`.
   
   2. Test case: `tag 1 t/`
   Expected: Removes all tags from the entry for `John`.

5. Adding loans to entries

   1. Test case: `increase 1 l/100.00 d/2025-01-01`<br>
   Expected: The entry for `John` at index 1 has a loan for $100.00 on 2025-01-01 recorded in its transaction history, and loan amount is increased by $100.00. <br>
   The entry should now show "Wanted" instead of "Not Wanted".
   
   2. Test case: `increase 1 l/-100.00 d/2025-02-02`<br>
   Exoected: No transaction is created. Error details shown in the status message. Command is not erased from input field.

### Repaying a loan
1. Prerequisites: The first entry on the currently displayed list has a loan amount of $100.00.

2. Adding loan repayments to entries

   1. Test case: `repay 1 l/60.00 d/2025-01-03`
   Expected: The first entry on the list now has a loan amount of $40.00. The repayment transaction details are added to the transaction history. 

   2. Test case: `repay 1 l/50.00 d/2025-01-04`
   Expected: No transaction is created. Error details shown in the status message. Command is not erased from input field.

   3. Test case: `repay 1 l/40.00 d/2025-01-04`
   Expected: The first entry on the list now has a loan amount of $0.00 and shows "Not Wanted". The repayment transaction details are added to the transaction history.

### Editing loan transaction history
1. Prerequisites: The first entry on the currently displayed list has a transaction history of exactly a $100.00 loan in the first slot and a $50.00 repayment in the second slot.

2. Editing loan transaction history
   
    1. Test case: `edithist 1 i/2 l/60.00 d/2025-01-10`
   Expected: The repayment transaction is increased to $60.00 and the date is changed to 2025-01-10. The loan amount of the entry should be updated to $40.00.
   
   2. Test case: `edithist 1 i/1 l/40.00`
   Expected: No change occurs. Error details shown in the status message. Command is not erased from input field.
   
   3. Test case: `edithist 1 i/1 l/250.00`
   Expected: The loan transaction is increased to $250.00. The date is unchanged. The loan amount of the entry should be updated to $190.00.

   4. Test case: `edithist 1 i/5 l/100.00`
      Expected: No change occurs. Error details shown in the status message. Command is not erased from input field.

### Deleting loan transaction history
1. Prerequisites: The first entry on the currently displayed list has a transaction history of exactly a $100.00 loan in the first slot and a $50.00 repayment in the second slot.
    
    1. Test case: `delhist 1 i/1`
   Expected: No change occurs. Error details shown in the status message. Command is not erased from input field.

   2. Test case: `delhist 1 i/2`
      Expected: The transaction for the repayment is deleted. The loan amount is reverted to $100.00.

### Finding specific entries
1. Prerequisites: There are multiple entries on the list with different names, with at least one entry containing `John` in the name and no entries containing `Jim`.

   1. Test case: `find John`
   Expected: Entries containing the name `John` (case-insensitive) are moved to the top of the list.
   
    2. Test case: `find Jim`
    Expected: No change occurs.

### Displaying all entries by name in alphabetical order
1. Prerequisites: There are multiple entries on the list with different names

1. Test case: `list`<br>
Expected: All entries are sorted in alphabetical order of name.

### Sorting the list
1. Prerequisites: There are multiple entries on the list with different Remaining Loan Amount.

2. Test case: `sort`<br>
Expected: All entries are sorted in decreasing order of Remaining Loan Amount.

### Deleting a loan

1. Prerequisites: Multiple loans in the list.

   1. Test case: `delete 1`<br>
      Expected: First entry is deleted from the list. Details of the deleted entry shown in the status message.

   1. Test case: `delete 0`<br>
      Expected: No loan is deleted. Error details shown in the status message. Command is not erased from input field.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

Team Size: 5 pax

1. Names that do not match exactly, case-sensitive, are currently considered different names and can be added.
As this may often be a result of user error, we plan to add detection for case-insensitive matches and prevent them from being added or renamed.
For instance, `add n/john` should fail if an entry for `John` already exists.
2. Names that contain special characters that are non-alphanumeric are currently not supported.
As such names do exist, we plan to add support for common special characters in names, such as `/` and `-`.
3. There are currently no checks for dates in transaction history, allowing transactions to be edited to be out of order.
We plan to add date checking to ensure transaction editing enforces a sensible timeline.
4. Likewise, as there are no checks for dates in transaction history, transactions may be recorded out of date order.
We plan to make transactions displayed by date order in the transaction lists.
5. Phone numbers are currently required to be alphanumeric.
We plan to add support for allowing international phone numbers with the `+` character for country code.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

This project involved restructuring the model of AB3 associating information with a Person to our model of associating
information with a Loan. 

As part of our objective was to automate the calculation of loan amounts for the user, we needed to implement ways to 
easily track and update information in the Loan. We created several new classes to encompass the new data types of money,
dates, as well as the LoanAmount and LoanTransaction classes to hold information involving how the user interacts with
their loanees.

During the MVP phase of our project, we had initially planned to simply track each loan transaction individually, with
each loan transaction being its own Loan object, allowing our model structure and program to function similarly to AB3. 
However, we discovered that this would make it difficult to track loans associated with a single person to collate
information such as Total Amount Loaned and Remaining Loan Amount, and this would not accurately simulate cases where
a person borrows multiple times before repaying all at once, or partially repaying for multiple loans in amounts that 
did not match the borrowed amounts exactly.

As such, we embarked on significant code restructuring across v1.4 and v1.5 to get to our current model, allowing us to
associate all transactions with a single person under a Loan, which also provided much easier ways to display that
information in a grouped manner that made more realistic sense. This allowed us to then implement easier methods to track
Total Amount Loaned and Remaining Loan Amount, allowing us to display that information at all times up front.
This also required modification of the UI to accommodate the grouping of information.

Additionally, unlike AB3, where all information in an entry could be added or edited in a single command, we chose to 
have each piece of information split out into separate commands. This necessitated creating many additional Command and
CommandParser classes, as well as many additional test cases. 
Doing so benefits the user as it splits up the entering or editing of information into individual
pieces that they are able to perform step-by-step, while also clearly showing the user on the exact pieces of information 
they wished to input being in an invalid format.
