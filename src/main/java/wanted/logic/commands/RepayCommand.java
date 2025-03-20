package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Amount;
import wanted.model.loan.Loan;

/**
 * Repay a loan identified using it's displayed index, with particular amount return
 */
public class RepayCommand extends Command {
    public static final String COMMAND_WORD = "repay";
    public static final String MESSAGE_REPAID_SUCCESS = "Loan successfully updated:";
    public static final String MESSAGE_REPAID_ALL_SUCCESS = "Loan successfully repaid entirely:";
    public static final String MESSAGE_EXCEED_AMOUNT_RETURNED =
            "Amount returned should be less than or equal current amount of loan";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            +
            ": Repay the loan identified by the index number used in the displayed loan list, with an amount to repay."
            + "\n" + "Parameters: INDEX (must be a positive integer)\n"
            + "          "
            + PREFIX_AMOUNT
            + "AMOUNT_REPAID (must be a positive double with exactly two digits after the decimal point, "
            + "and must be less than or equal current amount of loan.)" + "\n"
            + "Example: " + COMMAND_WORD + " 1" + PREFIX_AMOUNT + "10.00";
    private final Index targetIndex;
    private final Amount returnedAmount;
    private Amount updatedAmount;
    private Loan updatedLoan;

    /**
     * Constructor for RepayCommand
     *
     * @param targetIndex    index of loan to pay
     * @param amountReturned returned amount
     */
    public RepayCommand(Index targetIndex, Amount amountReturned) {
        this.targetIndex = targetIndex;
        this.returnedAmount = amountReturned;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan loanToRepay = lastShownList.get(targetIndex.getZeroBased());
        Amount currentAmount = loanToRepay.getAmount();

        /*
        If current amount value equals to amount returned values, then repay the loan entirely and delete
         */
        if (currentAmount.equals(returnedAmount)) {
            model.deletePerson(loanToRepay);
            return new CommandResult(String.format(MESSAGE_REPAID_ALL_SUCCESS, Messages.format(loanToRepay)));
        }
        Loan newLoan = this.getUpdatedLoan(loanToRepay);
        model.setPerson(loanToRepay, newLoan);

        return new CommandResult(String.format(MESSAGE_REPAID_SUCCESS, Messages.format(newLoan)));
    }

    /**
     * Just leave public for testing, before I find a better way to check the updated amount of loan manually
     * get updated loan
     *
     * @param loanToRepay old loan
     * @return updated loan
     * @throws CommandException handle invalid case
     */
    public Loan getUpdatedLoan(Loan loanToRepay) throws CommandException {
        if (this.updatedLoan != null) {
            return updatedLoan;
        }
        this.updatedLoan = new Loan(loanToRepay.getName(), this.getUpdatedAmount(loanToRepay),
                loanToRepay.getLoanDate(), loanToRepay.getTags());
        return this.updatedLoan;
    }

    /**
     * get updated amount
     *
     * @param loanToRepay old loan
     * @return updated amount
     * @throws CommandException exception for invalid case
     */
    private Amount getUpdatedAmount(Loan loanToRepay) throws CommandException {
        if (this.updatedAmount != null) {
            return this.updatedAmount;
        }
        double updatedAmountValue = getNewAmountValue(loanToRepay);

        String updatedAmountValueString = String.format("%.2f", updatedAmountValue);

        this.updatedAmount = new Amount(updatedAmountValueString);
        return this.updatedAmount;
    }


    /**
     * get updated amount value
     *
     * @param loanToRepay old loan
     * @return updated value of amount
     * @throws CommandException exception for invalid case
     */
    private double getNewAmountValue(Loan loanToRepay) throws CommandException {
        Amount currentAmount = loanToRepay.getAmount();
        int amountReturnedTimesOneHundred = this.returnedAmount.value.getValueTimesOneHundred();
        int currentAmountValueTimesOneHundred = currentAmount.value.getValueTimesOneHundred();

        if (amountReturnedTimesOneHundred > currentAmountValueTimesOneHundred) {
            throw new CommandException(MESSAGE_EXCEED_AMOUNT_RETURNED);
        }
        return ((double) (currentAmountValueTimesOneHundred - amountReturnedTimesOneHundred)) / 100.00;
    }
}
