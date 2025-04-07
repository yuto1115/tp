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
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the tags of the loan identified "
            + "by the index number used in the displayed persons list.\n"
            + "Existing list of tags will be overwritten by the input tags.\n"
            + "Parameters:\n"
            + "    [ID] (must be a positive integer)\n"
            + "    (" + PREFIX_TAG + "[TAG]...)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_TAG_SUCCESS = "Edited loan tags: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "Your requested tag(s) already exist(s) for this person";
    private final Index index;
    private final BaseEdit.EditLoanDescriptor editLoanDescriptor;

    /**
     * Creates a RetagCommand to edit the specified {@code Loan}
     */
    public TagCommand(Index index, BaseEdit.EditLoanDescriptor editLoanDescriptor) {
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
        if (!editedPerson.getTags().isEmpty() && personToEdit.getTags().containsAll(editedPerson.getTags())) {
            throw new CommandException(MESSAGE_DUPLICATE_TAG);
        }

        //removed check for isSamePerson since name identity remains the same

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_TAG_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherCommand = (TagCommand) other;
        return index.equals(otherCommand.index)
                && editLoanDescriptor.equals(otherCommand.editLoanDescriptor);
    }


}

