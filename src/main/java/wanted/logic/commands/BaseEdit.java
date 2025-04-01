package wanted.logic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import wanted.commons.util.CollectionUtil;
import wanted.commons.util.ToStringBuilder;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.Name;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.LoanTransaction;
import wanted.model.tag.Tag;


/**
 * Serves as a utility class
 */
public final class BaseEdit {
    private BaseEdit() {} //private constructor

    /**
     * Method to edit a loan wrapped in abstraction
     */
    public static Loan createEditedLoan(Loan loanToEdit, EditLoanDescriptor editDescriptor) {
        assert loanToEdit != null;
        Name updatedName = editDescriptor.getName().orElse(loanToEdit.getName());
        LoanAmount updatedAmount = editDescriptor.getAmount().orElse(loanToEdit.getLoanAmount());
        Set<Tag> updatedTags = editDescriptor.getTags().orElse(loanToEdit.getTags());

        return new Loan(updatedName, updatedAmount, updatedTags);
    }

    /**
     * Stores the details to edit the loan with. Each non-empty field value will replace the
     * corresponding field value of the loan.
     */
    public static class EditLoanDescriptor {
        private Name name;
        private Set<Tag> tags;
        private LoanAmount amount;
        //should only return transaction history

        public EditLoanDescriptor() {
        }

        /**
         * A defensive copy of {@code tags} is used internally.
         */
        public EditLoanDescriptor(EditLoanDescriptor toCopy) throws ExcessRepaymentException {
            setName(toCopy.name);
            setTags(toCopy.tags);
            setAmount(toCopy.amount.getTransactionHistoryCopy());
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setAmount(ArrayList<LoanTransaction> amt) throws ExcessRepaymentException {
            this.amount = new LoanAmount(amt);
        }

        public Optional<LoanAmount> getAmount() {
            return Optional.ofNullable(amount);
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
            if (!(other instanceof EditLoanDescriptor)) {
                return false;
            }

            EditLoanDescriptor otherEditPersonDescriptor = (EditLoanDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(amount, otherEditPersonDescriptor.amount);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("tags", tags)
                    .add("amount", amount)
                    .toString();
        }
    }
}
