package wanted.testutil;

import static wanted.logic.commands.CommandTestUtil.VALID_AMOUNT_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_AMOUNT_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_DATE_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wanted.model.LoanBook;
import wanted.model.loan.Loan;

/**
 * A utility class containing a list of {@code Loan} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Loan ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAmount("20.00")
            .withLoanDate("24th January 2025")
            .withTags("friends").build();
    public static final Loan BENSON = new PersonBuilder().withName("Benson Meier")
            .withAmount("19.43")
            .withLoanDate("11th Nov 2024")
            .withTags("owesMoney", "friends").build();
    public static final Loan CARL = new PersonBuilder().withName("Carl Kurz").withAmount("2990.23")
            .withLoanDate("9th Nov 2004").build();
    public static final Loan DANIEL = new PersonBuilder().withName("Daniel Meier")
            .withAmount("178.23")
            .withLoanDate("13th Jan 2024")
            .withTags("friends").build();
    public static final Loan ELLE = new PersonBuilder().withName("Elle Meyer").withAmount("132.23")
            .withLoanDate("18th Feb 2024").build();
    public static final Loan FIONA = new PersonBuilder().withName("Fiona Kunz").withAmount("0.23")
            .withLoanDate("13th March 2025")
            .build();
    public static final Loan GEORGE = new PersonBuilder().withName("George Best").withAmount("9.23")
            .withLoanDate("19th July 2024")
            .build();

    // Manually added
    public static final Loan HOON = new PersonBuilder().withName("Hoon Meier").withAmount("20.00")
            .withLoanDate("24th January 2025")
            .build();
    public static final Loan IDA = new PersonBuilder().withName("Ida Mueller").withAmount("20.00")
            .withLoanDate("24th January 2025")
            .build();

    // Manually added - Loan's details found in {@code CommandTestUtil}
    public static final Loan AMY = new PersonBuilder().withName(VALID_NAME_AMY).withAmount(VALID_AMOUNT_AMY)
            .withLoanDate(VALID_DATE_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Loan BOB = new PersonBuilder().withName(VALID_NAME_BOB).withAmount(VALID_AMOUNT_BOB)
            .withLoanDate(VALID_DATE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code LoanBook} with all the typical persons.
     */
    public static LoanBook getTypicalLoanBook() {
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
