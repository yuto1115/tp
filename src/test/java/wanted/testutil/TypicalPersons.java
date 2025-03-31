package wanted.testutil;

import static wanted.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.LoanBook;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;

/**
 * A utility class containing a list of {@code Loan} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Loan ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAmount(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_NOT_FULLY_REPAID)
            .withTags("friends").build();
    public static final Loan BENSON = new PersonBuilder().withName("Benson Meier")
            .withAmount(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_NOT_FULLY_REPAID)
            .withTags("owesMoney", "friends").build();
    public static final Loan CARL = new PersonBuilder().withName("Carl Kurz")
            .withAmount(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_FULLY_REPAID)
            .build();
    public static final Loan DANIEL = new PersonBuilder().withName("Daniel Meier")
            .withAmount(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_FULLY_REPAID)
            .withTags("friends").build();
    public static final Loan ELLE = new PersonBuilder().withName("Elle Meyer")
            .withAmount(TypicalLoanAmount.EMPTY_LOAN_AMOUNT)
            .build();
    public static final Loan FIONA;

    static {
        try {
            FIONA = new PersonBuilder().withName("Fiona Kunz")
                    .withAmount(new LoanAmount(new ArrayList<>(List.of(
                            new AddLoanTransaction(MoneyInt.fromCent(23), new LoanDate("13th March 2025"))
                    ))))
                    .build();
        } catch (ExcessRepaymentException e) {
            throw new RuntimeException(e);
        }
    }

    // Manually added
    public static final Loan HOON;
    public static final Loan IDA;

    static {
        try {
            HOON = new PersonBuilder().withName("Hoon Meier")
                    .withAmount(new LoanAmount(new ArrayList<>(List.of(
                            new AddLoanTransaction(MoneyInt.fromCent(2000), new LoanDate("24th January 2025"))
                    ))))
                    .build();
            IDA = new PersonBuilder().withName("Ida Mueller")
                    .withAmount(new LoanAmount(new ArrayList<>(List.of(
                            new AddLoanTransaction(MoneyInt.fromCent(2000), new LoanDate("24th January 2025"))
                    ))))
                    .build();
        } catch (ExcessRepaymentException e) {
            throw new RuntimeException(e);
        }
    }

    // Manually added - Loan's details found in {@code CommandTestUtil}
    public static final Loan AMY = new PersonBuilder().withName(VALID_NAME_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Loan BOB = new PersonBuilder().withName(VALID_NAME_BOB)
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
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA));
    }
}
