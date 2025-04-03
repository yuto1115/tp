package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.CollectionUtil.requireAllNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.exceptions.ExcessRepaymentException;

/**
 * Deletes a transaction from a loan identified using it's displayed index from the address book.
 */
public class DelhistCommand extends Command {

    public static final String COMMAND_WORD = "delhist";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a transaction in the history of the person "
            + "identified by the index number used in the displayed persons list.\n"
            + "The deleted transaction is identified by the index number used in the history of the identified person.\n"
            + "Parameters:\n"
            + "    [ID]\n"
            + "    " + PREFIX_INDEX + "[TRANSACTION ID] (both must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_INDEX + "3";

    public static final String MESSAGE_DELETE_TRANSACTION_SUCCESS = "Loan successfully updated: %1$s";

    private final Index loanIndex;
    private final Index transactionIndex;

    /**
     * Constructs a {@code DelhistCommand} with the given loan index and transaction index.
     */
    public DelhistCommand(Index loanIndex, Index transactionIndex) {
        requireAllNonNull(loanIndex, transactionIndex);
        this.loanIndex = loanIndex;
        this.transactionIndex = transactionIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getFilteredPersonList();

        if (loanIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan loanToModify = lastShownList.get(loanIndex.getZeroBased());

        if (transactionIndex.getZeroBased() >= loanToModify.getLoanAmount().getTransactionsCount()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
        }

        Loan newLoan;
        try {
            newLoan = loanToModify.deleteTransaction(transactionIndex);
        } catch (ExcessRepaymentException e) {
            throw new CommandException(Messages.MESSAGE_EXCESS_REPAYMENT_IN_HISTORY);
        }

        model.setPerson(loanToModify, newLoan);

        return new CommandResult(String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, Messages.format(newLoan)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DelhistCommand)) {
            return false;
        }

        DelhistCommand otherDelhistCommand = (DelhistCommand) other;
        return loanIndex.equals(otherDelhistCommand.loanIndex)
                && transactionIndex.equals(otherDelhistCommand.transactionIndex);
    }
}
