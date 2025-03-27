package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Amount;
import wanted.model.loan.Loan;

/**
 * Increases the value of an existing loan
 */

public class IncreaseCommand extends Command {
    public static final String COMMAND_WORD = "increase";
    public static final String MESSAGE_INCREASE_SUCCESS = "Loan successfully updated: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Increases the loan the identified person has taken out "
            + "by the index number used in the last person listing. "
            + "Existing loan will be increased by the amount input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "l/ [AMOUNT]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "l/ 30.10 .";


    private final Index index;
    private final Amount amount;

    /**
     * Creates a {@code IncreaseCommand} object
     * @param targetIndex the index of a person
     * @param increaseAmount the amount their loan increases
     */
    public IncreaseCommand(Index targetIndex, Amount increaseAmount) {
        requireAllNonNull(targetIndex, increaseAmount);
        this.index = targetIndex;
        this.amount = increaseAmount;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Loan> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan personIdentified = lastShownList.get(index.getZeroBased());

        Loan newLoan = createUpdatedLoan(personIdentified);
        model.setPerson(personIdentified, newLoan);

        return new CommandResult(String.format(MESSAGE_INCREASE_SUCCESS, Messages.format(newLoan)));
    }

    /**
     * Creates a new {@code Loan} with the updated amount value
     * @param loanToIncrease old loan
     * @return updated loan
     * @throws CommandException handle invalid case
     */
    private Loan createUpdatedLoan(Loan loanToIncrease) throws CommandException {
        Amount updatedAmount = getUpdatedAmount(loanToIncrease);
        return new Loan(loanToIncrease.getName(), updatedAmount, loanToIncrease.getTags());
    }

    /**
     * get updated amount
     *
     * @param loanToIncrease old loan
     * @return updated amount
     * @throws CommandException exception for invalid case
     */
    private Amount getUpdatedAmount(Loan loanToIncrease) throws CommandException {
        Amount currentAmount = loanToIncrease.getAmount();
        int amountAddedTimesOneHundred = this.amount.remainingValue.getValueTimesOneHundred();
        int currentAmountValueTimesOneHundred = currentAmount.remainingValue.getValueTimesOneHundred();

        MoneyInt updatedAmountValue = MoneyInt.fromCent(currentAmountValueTimesOneHundred + amountAddedTimesOneHundred);
        return new Amount(loanToIncrease.getAmount().totalValue, updatedAmountValue);
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
