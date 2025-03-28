package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;

/**
 * Repay a loan identified using its displayed index, with particular amount return
 */
public class RepayCommand extends Command {
    public static final String COMMAND_WORD = "repay";
    public static final String MESSAGE_REPAID_SUCCESS = "Loan successfully updated: %1$s";
    public static final String MESSAGE_REPAID_ALL_SUCCESS = "Loan successfully repaid entirely: %1$s";
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
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_AMOUNT + "10.00";
    private final Index targetIndex;
    private final MoneyInt returnedAmount;

    /**
     * Constructor for RepayCommand     *
     * @param targetIndex    index of loan to repay
     * @param amountReturned returned amount
     */
    public RepayCommand(Index targetIndex, MoneyInt amountReturned) {
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
        Loan newLoan = loanToRepay.repayAmount(this.returnedAmount);
        model.setPerson(loanToRepay, newLoan);

        /*
        If current amount value equals to amount returned values, then repay the loan entirely
         */
        if (newLoan.getAmount().isRepaid()) {
            return new CommandResult(String.format(MESSAGE_REPAID_ALL_SUCCESS, Messages.format(loanToRepay)));
        }
        return new CommandResult(String.format(MESSAGE_REPAID_SUCCESS, Messages.format(newLoan)));
    }
}
