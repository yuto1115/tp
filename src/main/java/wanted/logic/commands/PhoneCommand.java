package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.Phone;
import wanted.model.loan.exceptions.PhoneUnchangedException;

/**
 * Command to edit or delete phone number
 */
public class PhoneCommand extends Command {
    public static final String COMMAND_WORD = "phone";
    public static final String MESSAGE_UPDATED_SUCCESS = "Phone number successfully updated: %1$s";
    public static final String MESSAGE_DELETED_SUCCESS = "Phone number sucessfully deleted: %1$s";
    public static final String MESSAGE_DUPLICATE_PHONE = "New phone number must be different than the old one";
    public static final String DELETE_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            +
            ": Update or delete phone number of the loan identified by the index number."
            + "\n" + "Parameters: INDEX (must be a positive integer)\n"
            + "          "
            + PREFIX_PHONE
            + "NEW_PHONE_NUMBER "
            + "\n"
            + "Example (Update new phone number): " + COMMAND_WORD + " 1 " + PREFIX_PHONE + "88888888"
            + "\n"
            + "Example (Delete current phone number): " + COMMAND_WORD + " 1 " + PREFIX_PHONE + DELETE_WORD;
    private final Index targetIndex;
    private final Phone updatedPhone;

    /**
     * Constructor for PhoneCommand
     *
     * @param targetIndex  index of loan
     * @param updatedPhone new phone info
     */
    public PhoneCommand(Index targetIndex, Phone updatedPhone) {
        this.targetIndex = targetIndex;
        this.updatedPhone = updatedPhone;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan loanToUpdate = lastShownList.get(targetIndex.getZeroBased());
        Loan newLoan;
        try {
            newLoan = loanToUpdate.changePhone(this.updatedPhone);
        } catch (PhoneUnchangedException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PHONE);
        }
        model.setPerson(loanToUpdate, newLoan);
        return this.updatedPhone.equals(Phone.EMPTY_PHONE)
                ? new CommandResult(String.format(MESSAGE_DELETED_SUCCESS, Messages.format(newLoan)))
                : new CommandResult(String.format(MESSAGE_UPDATED_SUCCESS, Messages.format(newLoan)));
    }
}
