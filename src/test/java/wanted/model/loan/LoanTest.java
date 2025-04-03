package wanted.model.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static wanted.testutil.TypicalPersons.ALICE;
import static wanted.testutil.TypicalPersons.BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;
import wanted.model.tag.Tag;
import wanted.testutil.PersonBuilder;
import wanted.testutil.TypicalLoanAmount;

public class LoanTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Loan person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void instantiateDefaultLoan() {
        Loan person = new Loan(new Name("Test Name"));
        assertEquals("Test Name", person.getName().toString());
        assertEquals(new LoanAmount(), person.getLoanAmount());
        assertEquals(new HashSet<>(), person.getTags());
        assertEquals(Phone.EMPTY_PHONE, person.getPhone());
    }

    @Test
    public void isSameLoan() {
        // same object -> returns true
        assertTrue(ALICE.isSameLoan(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameLoan(null));

        //assert true when two loans have the same name
        assertTrue(BOB.isSameLoan(BOB));

        // different name, all other attributes same -> returns false
        Loan editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameLoan(editedAlice));

        // name differs in case, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(ALICE.getName().toString().toLowerCase()).build();
        assertFalse(ALICE.isSameLoan(editedAlice));

        //amount and tags differ but name remains the same
        editedAlice = new PersonBuilder(ALICE)
                .withAmount(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_FULLY_REPAID)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameLoan(editedAlice));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = ALICE.getName().toString() + " ";
        editedAlice = new PersonBuilder(ALICE).withName(nameWithTrailingSpaces).build();
        assertFalse(ALICE.isSameLoan(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Loan aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different loan -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Loan editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different amount -> returns false
        editedAlice = new PersonBuilder(ALICE)
                .withAmount(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_FULLY_REPAID)
                .build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags same name -> returns true
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Loan.class.getCanonicalName() + "{name=" + ALICE.getName()
                + ", amount=" + ALICE.getLoanAmount()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void addLoan() throws Exception {
        Name name = ALICE.getName();
        Set<Tag> tags = ALICE.getTags();
        LoanAmount amount = new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd Jan 2024"))
        )));
        LoanAmount newAmount = new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(2000), new LoanDate("5th Jan 2024"))
        )));

        Loan originalLoan = new Loan(name, amount, tags);
        Loan newLoan = originalLoan.addLoan(MoneyInt.fromCent(2000), new LoanDate("5th Jan 2024"));

        assertEquals(originalLoan, new Loan(name, amount, tags));
        assertEquals(newLoan, new Loan(name, newAmount, tags));
    }

    @Test
    public void repayLoan_success() throws Exception {
        Name name = ALICE.getName();
        Set<Tag> tags = ALICE.getTags();
        LoanAmount amount = new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd Jan 2024"))
        )));
        LoanAmount newAmount = new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("5th Jan 2024"))
        )));

        Loan originalLoan = new Loan(name, amount, tags);
        Loan newLoan = originalLoan.repayLoan(MoneyInt.fromCent(500), new LoanDate("5th Jan 2024"));

        assertEquals(originalLoan, new Loan(name, amount, tags));
        assertEquals(newLoan, new Loan(name, newAmount, tags));
    }

    @Test
    public void repayLoan_excessiveRepayment_throwsExcessRepaymentException() throws Exception {
        Name name = ALICE.getName();
        Set<Tag> tags = ALICE.getTags();
        LoanAmount amount = new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd Jan 2024"))
        )));

        assertThrows(ExcessRepaymentException.class, () ->
                new Loan(name, amount, tags).repayLoan(MoneyInt.fromCent(501), new LoanDate("5th Jan 2024")));
    }

    @Test
    public void deleteTransaction_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ALICE.deleteTransaction(null));
    }

    @Test
    public void deleteTransaction_invalidIndex_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                ALICE.deleteTransaction(Index.fromZeroBased(ALICE.getLoanAmount().getTransactionsCount())));
    }

    @Test
    public void deleteTransaction_success() throws Exception {
        Name name = ALICE.getName();
        Set<Tag> tags = ALICE.getTags();
        Loan original = new Loan(name, new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd Jan 2024"))))), tags);
        Loan expected = new Loan(name, new LoanAmount(new ArrayList<>(List.of(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024"))))), tags);

        assertEquals(expected, original.deleteTransaction(Index.fromZeroBased(1)));
    }

    @Test
    public void deleteTransaction_invalid_throwExcessRepaymentException() throws Exception {
        Name name = ALICE.getName();
        Set<Tag> tags = ALICE.getTags();
        Loan loan = new Loan(name, new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(600), new LoanDate("3rd Jan 2024"))))), tags);

        assertThrows(ExcessRepaymentException.class, () ->
                loan.deleteTransaction(Index.fromZeroBased(0)));
    }

    @Test
    public void replaceTransaction_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ALICE.replaceTransaction(null,
                new AddLoanTransaction(MoneyInt.fromCent(0), new LoanDate("1st Jan 2024"))));
        assertThrows(NullPointerException.class, ()
                -> ALICE.replaceTransaction(INDEX_FIRST_PERSON, null));
        assertThrows(NullPointerException.class, () -> ALICE.replaceTransaction(null, null));
    }

    @Test
    public void replaceTransaction_invalidIndex_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                ALICE.replaceTransaction(Index.fromZeroBased(ALICE.getLoanAmount().getTransactionsCount()),
                        new AddLoanTransaction(MoneyInt.fromCent(0), new LoanDate("1st Jan 2024"))));
    }

    @Test
    public void replaceTransaction_success() throws Exception {
        Name name = ALICE.getName();
        Set<Tag> tags = ALICE.getTags();
        Loan original = new Loan(name, new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("3rd Jan 2024"))))), tags);
        Loan expected = new Loan(name, new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(800), new LoanDate("2nd Feb 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(200), new LoanDate("3rd Mar 2024"))))), tags);

        assertEquals(expected, original
                // replace by a transaction of the same type
                .replaceTransaction(INDEX_SECOND_PERSON,
                        new RepayLoanTransaction(MoneyInt.fromCent(800), new LoanDate("2nd Feb 2024")))
                // replace by a transaction of a different type
                .replaceTransaction(INDEX_THIRD_PERSON,
                        new RepayLoanTransaction(MoneyInt.fromCent(200), new LoanDate("3rd Mar 2024"))));
    }

    @Test
    public void replaceTransaction_invalid_throwExcessRepaymentException() throws Exception {
        Name name = ALICE.getName();
        Set<Tag> tags = ALICE.getTags();
        Loan loan = new Loan(name, new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("3rd Jan 2024"))))), tags);

        assertThrows(ExcessRepaymentException.class, () ->
                loan.replaceTransaction(INDEX_SECOND_PERSON,
                        new RepayLoanTransaction(MoneyInt.fromCent(1100), new LoanDate("2nd Jan 2024"))));
    }
}
