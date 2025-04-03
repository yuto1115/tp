package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.CollectionUtil.requireAllNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.commons.util.CollectionUtil;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.LoanTransaction;

/**
 * Edits the details of an existing loan in the loan book.
 */
public class EdithistCommand extends Command {

    public static final String COMMAND_WORD = "edithist";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits a transaction in the history of the loan "
            + "identified by the index number used in the displayed loan list.\n"
            + "The edited transaction is identified by the index number used in the history of the identified loan.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: LOAN_ID "
            + PREFIX_INDEX + "[TRANSACTION_ID] (both must be positive integers)\n"
            + "    (" + PREFIX_AMOUNT + "[AMOUNT]) "
            + "(" + PREFIX_DATE + "[DATE])\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_INDEX + "3 "
            + PREFIX_AMOUNT + "30.10 "
            + PREFIX_DATE + "10th August 2024";

    public static final String MESSAGE_EDIT_TRANSACTION_SUCCESS = "Loan successfully updated: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    private final Index loanIndex;
    private final Index transactionIndex;
    private final EditTransactionDescriptor editTransactionDescriptor;

    /**
     * Constructs a {@code EdithistCommand} with the given loan index, transaction index and
     * a description of how it is edited.
     */
    public EdithistCommand(Index loanIndex, Index transactionIndex,
                           EditTransactionDescriptor editTransactionDescriptor) {
        requireAllNonNull(loanIndex, transactionIndex, editTransactionDescriptor);

        this.loanIndex = loanIndex;
        this.transactionIndex = transactionIndex;
        this.editTransactionDescriptor = new EditTransactionDescriptor(editTransactionDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!editTransactionDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        List<Loan> lastShownList = model.getFilteredPersonList();

        if (loanIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan loanToEdit = lastShownList.get(loanIndex.getZeroBased());

        if (transactionIndex.getZeroBased() >= loanToEdit.getLoanAmount().getTransactionsCount()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
        }

        LoanTransaction loanTransactionToEdit =
                loanToEdit.getLoanAmount().getTransactionHistoryCopy().get(transactionIndex.getZeroBased());

        LoanTransaction editedLoanTransaction =
                createEditedTransaction(loanTransactionToEdit, editTransactionDescriptor);

        Loan editedLoan;
        try {
            editedLoan = loanToEdit.replaceTransaction(transactionIndex, editedLoanTransaction);
        } catch (ExcessRepaymentException e) {
            throw new CommandException(Messages.MESSAGE_EXCESS_REPAYMENT_IN_HISTORY);
        }

        model.setPerson(loanToEdit, editedLoan);

        return new CommandResult(String.format(MESSAGE_EDIT_TRANSACTION_SUCCESS, Messages.format(editedLoan)));
    }

    /**
     * Creates and returns a {@code Loan} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static LoanTransaction createEditedTransaction(LoanTransaction transactionToEdit,
                                                           EditTransactionDescriptor editTransactionDescriptor) {
        assert transactionToEdit != null;

        MoneyInt updatedAmount = editTransactionDescriptor.getAmount().orElse(transactionToEdit.getAmount());
        LoanDate updatedDate = editTransactionDescriptor.getDate().orElse(transactionToEdit.getDate());

        return transactionToEdit.getNewTransactionOfSameType(updatedAmount, updatedDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EdithistCommand)) {
            return false;
        }

        EdithistCommand otherEdithistCommand = (EdithistCommand) other;
        return loanIndex.equals(otherEdithistCommand.loanIndex)
                && transactionIndex.equals(otherEdithistCommand.transactionIndex)
                && editTransactionDescriptor.equals(otherEdithistCommand.editTransactionDescriptor);
    }

    /**
     * Stores the details to edit the loan with. Each non-empty field value will replace the
     * corresponding field value of the loan.
     */
    public static class EditTransactionDescriptor {
        private MoneyInt amount;
        private LoanDate date;

        public EditTransactionDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTransactionDescriptor(EditTransactionDescriptor toCopy) {
            setAmount(toCopy.amount);
            setDate(toCopy.date);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(amount, date);
        }

        public void setAmount(MoneyInt amount) {
            this.amount = amount;
        }

        public Optional<MoneyInt> getAmount() {
            return Optional.ofNullable(amount);
        }

        public void setDate(LoanDate date) {
            this.date = date;
        }

        public Optional<LoanDate> getDate() {
            return Optional.ofNullable(date);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTransactionDescriptor)) {
                return false;
            }
            //in the future we can discuss whether we want to use a hashcode
            EditTransactionDescriptor otherEditTransactionDescriptor = (EditTransactionDescriptor) other;
            return Objects.equals(amount, otherEditTransactionDescriptor.amount)
                    && Objects.equals(date, otherEditTransactionDescriptor.date);
        }
    }
}
