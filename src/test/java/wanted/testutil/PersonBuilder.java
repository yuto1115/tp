package wanted.testutil;

import java.util.HashSet;
import java.util.Set;

import wanted.model.loan.Amount;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;
import wanted.model.loan.Name;
import wanted.model.tag.Tag;
import wanted.model.util.SampleDataUtil;

/**
 * A utility class to help with building Loan objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    private static final String DEFAULT_AMOUNT = "10.10";
    private static final String DEFAULT_DATE = "24th December 2024";

    private Name name;
    private Set<Tag> tags;
    private Amount amount;
    private LoanDate loanDate;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        tags = new HashSet<>();
        amount = new Amount(DEFAULT_AMOUNT);
        loanDate = new LoanDate(DEFAULT_DATE);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Loan personToCopy) {
        name = personToCopy.getName();
        amount = personToCopy.getAmount();
        loanDate = personToCopy.getLoanDate();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Loan} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Loan} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Amount} of the {@code Loan} that we are building.
     */
    public PersonBuilder withAmount(String amount) {
        this.amount = new Amount(amount);
        return this;
    }

    /**
     * Sets the {@code LoanDate} of the {@code Loan} that we are building.
     */
    public PersonBuilder withLoanDate(String date) {
        this.loanDate = new LoanDate(date);
        return this;
    }

    public Loan build() {
        return new Loan(name, amount, loanDate, tags);
    }

}
