package wanted.testutil;

import java.util.HashSet;
import java.util.Set;

import wanted.model.loan.Address;
import wanted.model.loan.Amount;
import wanted.model.loan.Email;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;
import wanted.model.loan.Name;
import wanted.model.loan.Phone;
import wanted.model.tag.Tag;
import wanted.model.util.SampleDataUtil;

/**
 * A utility class to help with building Loan objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    private static final String DEFAULT_AMOUNT = "10.10";
    private static final String DEFAULT_DATE = "24th December 2024";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Amount amount;
    private LoanDate loanDate;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        amount = new Amount(DEFAULT_AMOUNT);
        loanDate = new LoanDate(DEFAULT_DATE);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Loan personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
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
     * Sets the {@code Address} of the {@code Loan} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Loan} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Loan} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
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
        return new Loan(name, phone, email, address, amount, loanDate, tags);
    }

}
