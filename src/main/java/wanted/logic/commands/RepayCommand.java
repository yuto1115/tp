package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.CollectionUtil.requireAllNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;

/**
 * Repay a loan identified using its displayed index, with particular amount return
 */
public class RepayCommand extends Command {
    public static final String COMMAND_WORD = "repay";
    public static final String MESSAGE_REPAID_SUCCESS = "Loan successfully updated: %1$s";
    public static final String MESSAGE_REPAID_ALL_SUCCESS = "Loan successfully repaid entirely: %1$s";
    public static final String MESSAGE_EXCEED_AMOUNT_RETURNED =
            "Amount repaid should be less than or equal to the current amount of loan";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            +
            ": Repay the loan identified by the index number used in the displayed loan list, with an amount to repay."
            + "\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "l/ [AMOUNT] (must be a positive double with exactly two digits after the decimal point)\n"
            + "d/ [DATE]\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_AMOUNT + " 10.00 " + PREFIX_DATE + " 21st January 2025";
    private final Index targetIndex;
    private final MoneyInt returnedAmount;
    private final LoanDate date;

    /**
     * Constructor for RepayCommand
     * @param targetIndex    index of loan to repay
     * @param amountReturned returned amount
     * @param date           date of the transaction
     */
    public RepayCommand(Index targetIndex, MoneyInt amountReturned, LoanDate date) {
        requireAllNonNull(targetIndex, amountReturned, date);
        this.targetIndex = targetIndex;
        this.returnedAmount = amountReturned;
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan loanToRepay = lastShownList.get(targetIndex.getZeroBased());
        Loan newLoan;
        try {
            newLoan = loanToRepay.repayLoan(this.returnedAmount, this.date);
        } catch (ExcessRepaymentException e) {
            throw new CommandException(MESSAGE_EXCEED_AMOUNT_RETURNED);
        }
        model.setPerson(loanToRepay, newLoan);

        /*
        If current amount value equals to amount returned values, then repay the loan entirely
         */
        if (newLoan.getLoanAmount().isRepaid()) {
            return new CommandResult(String.format(MESSAGE_REPAID_ALL_SUCCESS, Messages.format(newLoan)));
        }
        return new CommandResult(String.format(MESSAGE_REPAID_SUCCESS, Messages.format(newLoan)));
    }
}
