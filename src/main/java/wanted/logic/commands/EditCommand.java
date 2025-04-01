package wanted.logic.commands;

import static java.util.Objects.requireNonNull;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_NAME;
import static wanted.logic.parser.CliSyntax.PREFIX_TAG;
import static wanted.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import wanted.commons.core.datatypes.Index;
import wanted.commons.util.CollectionUtil;
import wanted.commons.util.ToStringBuilder;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.LoanDate;
import wanted.model.loan.Name;
import wanted.model.tag.Tag;

/**
 * Edits the details of an existing loan in the loan book.
 */
public class EditCommand extends Command {

    public static final boolean IS_ENABLED = false;

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the loan identified "
            + "by the index number used in the displayed loan list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_AMOUNT + "AMOUNT] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "John Doe "
            + PREFIX_AMOUNT + "123.25"
            + PREFIX_DATE + "17th March 2025";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Loan: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This loan already exists in the loan book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the loan in the filtered loan list to edit
     * @param editPersonDescriptor details to edit the loan with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Loan personToEdit = lastShownList.get(index.getZeroBased());
        Loan editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSameLoan(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Loan} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Loan createEditedPerson(Loan loanToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert loanToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(loanToEdit.getName());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(loanToEdit.getTags());
        LoanAmount updatedAmount = editPersonDescriptor.getAmount().orElse(loanToEdit.getLoanAmount());

        return new Loan(updatedName, updatedAmount, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the loan with. Each non-empty field value will replace the
     * corresponding field value of the loan.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Set<Tag> tags;
        private LoanAmount loanAmount;
        private LoanDate date;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setTags(toCopy.tags);
            setAmount(toCopy.loanAmount);
            setDate(toCopy.date);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, tags, loanAmount, date);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setAmount(LoanAmount loanAmount) {
            this.loanAmount = loanAmount;
        }

        public Optional<LoanAmount> getAmount() {
            return Optional.ofNullable(loanAmount);
        }

        public void setDate(LoanDate date) {
            this.date = date;
        }

        public Optional<LoanDate> getDate() {
            return Optional.ofNullable(date);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }
            //in the future we can discuss whether we want to use a hashcode
            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(loanAmount, otherEditPersonDescriptor.loanAmount)
                    && Objects.equals(date, otherEditPersonDescriptor.date);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("tags", tags)
                    .add("amount", loanAmount)
                    .add("date", date)
                    .toString();
        }
    }
}
