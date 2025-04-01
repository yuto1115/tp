[![CI Status](https://github.com/AY2425S2-CS2103T-F11-4/tp/workflows/Java%20CI/badge.svg)](https://github.com/AY2425S2-CS2103T-F11-4/tp/actions)
[![codecov](https://codecov.io/gh/AY2425S2-CS2103T-F11-4/tp/branch/master/graph/badge.svg?token=SbojDIOfdX)](https://codecov.io/gh/AY2425S2-CS2103T-F11-4/tp)

![Ui](docs/images/Ui.png)

* CS2103T tP
* This is **a loan manager to help users track and improve their lending habits**.<br>
  Example usages:
  * Track individuals' loans and due dates to manage repayments effectively.
  * Identify and list individuals with overdue loans to improve lending decisions
* `Wanted` is adapted from an ongoing software project for a desktop application (called _AddressBook_) to instead manage loans given to friends.
  * It is **written in OOP fashion**. It provides a **reasonably well-written** code base **bigger** (around 6 KLoC) than what students usually write in beginner-level SE modules, without being overwhelmingly big.
  * It comes with a **reasonable level of user and developer documentation**.
* It is named `Wanted`  because it aims to display a list of individuals who have not returned their loans.
* For the detailed documentation of this project, see the **[Wanted_Product_Website](https://ay2425s2-cs2103t-f11-4.github.io/tp/)**.

* Features:
  * **Add command**: Add a new entry to the loan list. Format:` add [d/DATE] [n/NAME] [l/AMOUNT_LOANED]` (E.g: `add d/10th Dec 2024 n/John Doe l/10.10`)
  * **Repay loan**: Modify one entry from the list to represent full/partial payment of a loan. Format:` repay [ID] [amount_returned]` (E.g: `repay 2 10.00`)
  * **List command**: List all the entries from the loan list. Format: `list`
  * **Exit command**: Exit the program. Format: `exit`
  * **Sort command**: Sort loans by amount value in descending order. Format: `sort`


This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org)
