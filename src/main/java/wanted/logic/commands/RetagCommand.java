package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_TAG;
import static wanted.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;


/**
 * Edits the tags of an existing person in the loanbook
 */
public class RetagCommand extends Command {

    public static final String COMMAND_WORD = "retag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the tags of the loan identified "
            + "by the index number used in the displayed loan list. "
            + "Existing list of tags will be overwritten by the input tags.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "[TAG]...\n";

    public static final String MESSAGE_RENAME_SUCCESS = "Edited loan name: %1$s";
    public static final String MESSAGE_NOT_EDITED = "The tag field must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the loan book.";
    //see what happens with duplicate person
    private final Index index;
    private final BaseEdit.EditLoanDescriptor editLoanDescriptor;

    /**
     * Creates a RetagCommand to edit the specified {@code Loan}
     */
    public RetagCommand(Index index, BaseEdit.EditLoanDescriptor editLoanDescriptor) {
        requireNonNull(index);

        this.index = index;
        this.editLoanDescriptor = editLoanDescriptor;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan personToEdit = lastShownList.get(index.getZeroBased());
        Loan editedPerson = BaseEdit.createEditedLoan(personToEdit, editLoanDescriptor);

        //removed check for isSamePerson since name identity remains the same

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_RENAME_SUCCESS, Messages.format(editedPerson)));
    }

}

