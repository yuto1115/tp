package wanted.logic.commands;

import wanted.commons.core.datatypes.Index;
import wanted.model.loan.Loan;
import wanted.model.loan.Name;

import static java.util.Objects.requireNonNull;
import static wanted.logic.commands.EditCommand.MESSAGE_EDIT_PERSON_SUCCESS;
import static wanted.logic.parser.CliSyntax.PREFIX_NAME;
import static wanted.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;


/**
 * Edits the name of an existing person in the loanbook
 */
public class RenameCommand extends Command{

    public static final String COMMAND_WORD = "rename";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the loan identified "
            + "by the index number used in the displayed loan list. "
            + "Existing name will be overwritten by the input name.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] ";

    public static final String MESSAGE_RENAME_SUCCESS = "Edited loan name: %1$s";
    public static final String MESSAGE_NOT_EDITED = "The name field must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the loan book.";

    private final Index index;
    private final BaseEdit.EditLoanDescriptor editLoanDescriptor;

    /**
     * Constructor
     */
    public RenameCommand(Index index, BaseEdit.EditLoanDescriptor editLoanDescriptor) {
        requireNonNull(index);

        this.index = index;
        this.editLoanDescriptor = editLoanDescriptor;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }

        Loan personToEdit = lastShownList.get(index.getZeroBased());
        Loan editedPerson = BaseEdit.createEditedLoan(personToEdit, editLoanDescriptor);

        if (!personToEdit.equals(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_RENAME_SUCCESS, Messages.format(editedPerson)));
    }

}
