package wanted.model.loan;

import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.commons.util.ToStringBuilder;
import wanted.model.tag.Tag;

/**
 * Represents a Loan in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 * Due to the immutability of the class, methods that modify its state will return a new Loan.
 */
public class Loan {
    // Identity fields
    private final Name name;
    private final Amount amount;
    // LoanDate should be moved to History
    // private final LoanDate loanDate;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Loan(Name name, Amount amount, Set<Tag> tags) {
        //requireAllNonNull(name, amount, date, tags);
        requireAllNonNull(name, amount, tags);
        this.name = name;
        this.amount = amount;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Amount getAmount() {
        return amount;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same loan.
     * This defines a weaker notion of equality between two loans.
     */
    public boolean isSameLoan(Loan otherLoan) {
        if (otherLoan == this) {
            return true;
        }

        return otherLoan != null
                && otherLoan.hashCode() == hashCode();
    }

    /**
     * Returns true if both loans have the same identity and data fields.
     * This defines a stronger notion of equality between two loans.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Loan otherLoan)) {
            return false;
        }

        return name.equals(otherLoan.name)
                && amount.equals(otherLoan.amount)
                && tags.equals(otherLoan.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, amount, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("amount", amount)
                .add("tags", tags)
                .toString();
    }

    /**
     * Factory method to create a new Loan for a new Name.
     *
     * @param name Name of the loanee.
     * @param tags Tags associated with the loanee.
     * @return New Loan with no History and no Amount loaned.
     */
    public static Loan createNewLoan(Name name, Set<Tag> tags) {
        return new Loan(name, new Amount("0.00"), tags);
    }

    /**
     * Performs a new transaction of loaning money with additional History of the specified Amount being loaned out.
     *
     * @param loaned Amount given to loanee in this transaction.
     * @return New Loan after the loaning transaction.
     */
    public Loan loanAmount(MoneyInt loaned) {
        Amount newAmount = this.amount.addAmount(loaned);
        // TODO: update History
        return new Loan(this.name, newAmount, this.tags);
    }

    /**
     * Performs a new transaction of repaying money with additional History of the specified Amount being repaid.
     *
     * @param repaid Amount repaid from loanee in this transaction.
     * @return New Loan after the repaying transaction.
     */
    public Loan repayAmount(MoneyInt repaid) {
        Amount newAmount = this.amount.repayAmount(repaid);
        // TODO: update History
        return new Loan(this.name, newAmount, this.tags);
    }

    /**
     * Returns a Loan with the new Name provided, all other fields unchanged.
     *
     * @param newName New name.
     * @return Loan with the new Name provided, all other fields unchanged
     */
    public Loan editName(Name newName) {
        return new Loan(newName, this.amount, this.tags);
    }


}
