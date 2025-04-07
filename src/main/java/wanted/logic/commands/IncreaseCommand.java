package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;

/**
 * Increases the value of an existing loan
 */

public class IncreaseCommand extends Command {
    public static final String COMMAND_WORD = "increase";
    public static final String MESSAGE_INCREASE_SUCCESS = "Loan successfully updated: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Increases the loan of the person identified "
            + "by the index number used in the displayed persons list.\n"
            + "Parameters:\n"
            + "    [ID] (must be a positive integer)\n"
            + "    l/[AMOUNT] (must be a non-negative numeric amount with 2 decimal places)\n"
            + "    d/[DATE] (must be in the YYYY-MM-DD format)\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "l/30.10 " //whitespace before d
            + "d/2024-08-10";


    private final Index index;
    private final MoneyInt amount;
    private final LoanDate date;

    /**
     * Creates a {@code IncreaseCommand} object
     * @param targetIndex the index of a person
     * @param increaseAmount the amount their loan increases
     * @param date           date of the transaction
     */
    public IncreaseCommand(Index targetIndex, MoneyInt increaseAmount, LoanDate date) {
        requireAllNonNull(targetIndex, increaseAmount, date);
        this.index = targetIndex;
        this.amount = increaseAmount;
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Loan> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan loanIdentified = lastShownList.get(index.getZeroBased());
        Loan newLoan = loanIdentified.addLoan(this.amount, this.date);
        model.setPerson(loanIdentified, newLoan);

        return new CommandResult(String.format(MESSAGE_INCREASE_SUCCESS, Messages.format(newLoan)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof IncreaseCommand)) {
            return false;
        }

        IncreaseCommand e = (IncreaseCommand) other;
        return index.equals(e.index)
                && amount.equals(e.amount);
    }
}
