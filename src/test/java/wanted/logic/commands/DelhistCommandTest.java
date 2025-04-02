package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.Messages.MESSAGE_EXCESS_REPAYMENT_IN_HISTORY;
import static wanted.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static wanted.logic.Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX;
import static wanted.logic.commands.DelhistCommand.MESSAGE_DELETE_TRANSACTION_SUCCESS;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static wanted.testutil.TypicalPersons.ALICE;
import static wanted.testutil.TypicalPersons.BOB;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;
import wanted.testutil.LoanBookBuilder;

public class DelhistCommandTest {
    private static final LoanAmount sampleAmount;
    private static final LoanAmount sampleAmountFirstTransactionDeleted;

    static {
        try {
            sampleAmount = new LoanAmount(new ArrayList<>(Arrays.asList(
                    new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("1st Jan 2020")),
                    new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2020")),
                    new RepayLoanTransaction(MoneyInt.fromCent(800), new LoanDate("1st Jan 2020"))
            )));
            sampleAmountFirstTransactionDeleted = new LoanAmount(new ArrayList<>(Arrays.asList(
                    new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2020")),
                    new RepayLoanTransaction(MoneyInt.fromCent(800), new LoanDate("1st Jan 2020"))
            )));
        } catch (ExcessRepaymentException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Loan sampleLoan = new Loan(BOB.getName(), sampleAmount, BOB.getTags());
    private static final Loan sampleLoanFirstTransactionDeleted =
            new Loan(BOB.getName(), sampleAmountFirstTransactionDeleted, BOB.getTags());

    // Recreate sample model everytime since model is modified everytime we execute the command
    private static Model createSampleModel() {
        return new ModelManager(
                new LoanBookBuilder()
                        .withPerson(ALICE)
                        .withPerson(sampleLoan)
                        .build(),
                new UserPrefs()
        );
    }

    private static Model createSampleModelFirstTransactionDeleted() {
        return new ModelManager(
                new LoanBookBuilder()
                        .withPerson(ALICE)
                        .withPerson(sampleLoanFirstTransactionDeleted)
                        .build(),
                new UserPrefs()
        );
    }

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DelhistCommand(null, INDEX_FIRST_PERSON));
        assertThrows(NullPointerException.class, () -> new DelhistCommand(INDEX_FIRST_PERSON, null));
        assertThrows(NullPointerException.class, () -> new DelhistCommand(null, null));
    }

    @Test
    public void execute_success() throws Exception {
        DelhistCommand delhistCommand = new DelhistCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);
        Model sampleModel = createSampleModel();
        CommandResult result = delhistCommand.execute(sampleModel);
        assertEquals(createSampleModelFirstTransactionDeleted(), sampleModel);
        assertEquals(result, new CommandResult(String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS,
                Messages.format(sampleModel
                        .getLoanBook()
                        .getPersonList()
                        .get(INDEX_SECOND_PERSON.getZeroBased())))));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        DelhistCommand delhistCommand = new DelhistCommand(INDEX_THIRD_PERSON, INDEX_FIRST_PERSON);
        assertThrows(NullPointerException.class, () -> delhistCommand.execute(null));
    }

    @Test
    public void execute_invalidLoanIndex_throwsCommandException() {
        DelhistCommand delhistCommand = new DelhistCommand(INDEX_THIRD_PERSON, INDEX_FIRST_PERSON);
        Model sampleModel = createSampleModel();
        assertThrows(CommandException.class, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ()
                -> delhistCommand.execute(sampleModel));
    }

    @Test
    public void execute_invalidTransactionIndex_throwsCommandException() {
        DelhistCommand delhistCommand = new DelhistCommand(INDEX_SECOND_PERSON, Index.fromOneBased(4));
        Model sampleModel = createSampleModel();
        assertThrows(CommandException.class, MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX, ()
                -> delhistCommand.execute(sampleModel));
    }

    @Test
    public void execute_excessRepayment_throwsExcessRepaymentException() {
        DelhistCommand delhistCommand = new DelhistCommand(INDEX_SECOND_PERSON, INDEX_SECOND_PERSON);
        Model sampleModel = createSampleModel();
        assertThrows(CommandException.class, MESSAGE_EXCESS_REPAYMENT_IN_HISTORY, ()
                -> delhistCommand.execute(sampleModel));
    }

    @Test
    public void equals() {
        DelhistCommand command11 = new DelhistCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        DelhistCommand command12 = new DelhistCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);
        DelhistCommand command21 = new DelhistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(command11.equals(command11));

        // same values -> returns true
        assertTrue(command11.equals(new DelhistCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON)));

        // different types -> returns false
        assertFalse(command11.equals(1));

        // null -> returns false
        assertFalse(command11.equals(null));

        // different index -> returns false
        assertFalse(command11.equals(command12));
        assertFalse(command11.equals(command21));
    }
}
