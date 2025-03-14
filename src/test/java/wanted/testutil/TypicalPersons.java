package wanted.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wanted.model.LoanBook;
import wanted.model.loan.Loan;

import static wanted.logic.commands.CommandTestUtil.*;

/**
 * A utility class containing a list of {@code Loan} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Loan ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withAmount("20.00")
            .withLoanDate("24th January 2025")
            .withTags("friends").build();
    public static final Loan BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withAmount("19.43")
            .withLoanDate("11th Nov 2024")
            .withTags("owesMoney", "friends").build();
    public static final Loan CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").withAmount("2990.23")
            .withLoanDate("9th Nov 2004").build();
    public static final Loan DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street")
            .withAmount("178.23")
            .withLoanDate("13th Jan 2024")
            .withTags("friends").build();
    public static final Loan ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").withAmount("132.23")
            .withLoanDate("18th Feb 2024").build();
    public static final Loan FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo").withAmount("0.23")
            .withLoanDate("13th March 2025")
            .build();
    public static final Loan GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").withAmount("9.23")
            .withLoanDate("19th July 2024")
            .build();

    // Manually added
    public static final Loan HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").withAmount("20.00")
            .withLoanDate("24th January 2025")
            .build();
    public static final Loan IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").withAmount("20.00")
            .withLoanDate("24th January 2025")
            .build();

    // Manually added - Loan's details found in {@code CommandTestUtil}
    public static final Loan AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withAmount(VALID_AMOUNT_AMY)
            .withLoanDate(VALID_DATE_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Loan BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withAmount(VALID_AMOUNT_BOB)
            .withLoanDate(VALID_DATE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code LoanBook} with all the typical persons.
     */
    public static LoanBook getTypicalAddressBook() {
        LoanBook ab = new LoanBook();
        for (Loan person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Loan> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
