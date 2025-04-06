package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.Messages.MESSAGE_EXCESS_REPAYMENT_IN_HISTORY;
import static wanted.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static wanted.logic.Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX;
import static wanted.logic.commands.EdithistCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS;
import static wanted.logic.commands.EdithistCommand.MESSAGE_NOT_EDITED;
import static wanted.logic.commands.EdithistCommand.MESSAGE_UNCHANGED_AMOUNT;
import static wanted.logic.commands.EdithistCommand.MESSAGE_UNCHANGED_DATE;
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
import wanted.logic.commands.EdithistCommand.EditTransactionDescriptor;
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

/**
 * Contains integration tests (interaction with the Model) and unit tests for EdithistCommand.
 */
public class EdithistCommandTest {

    private static final LoanAmount sampleAmount;
    private static final LoanAmount[] sampleAmountFirstTransactionEdited;

    private static final EditTransactionDescriptor[] editTransactionDescriptor;

    static {
        try {
            sampleAmount = new LoanAmount(new ArrayList<>(Arrays.asList(
                    new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("1st Jan 2020")),
                    new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2020")),
                    new RepayLoanTransaction(MoneyInt.fromCent(800), new LoanDate("1st Jan 2020"))
            )));
            boolean[] doesEditAmount = new boolean[]{true, false, true};
            boolean[] doesEditDate = new boolean[]{false, true, true};
            sampleAmountFirstTransactionEdited = new LoanAmount[3];
            editTransactionDescriptor = new EditTransactionDescriptor[3];
            for (int i = 0; i < 3; i++) {
                sampleAmountFirstTransactionEdited[i] = new LoanAmount(new ArrayList<>(Arrays.asList(
                        new AddLoanTransaction(MoneyInt.fromCent(doesEditAmount[i] ? 300 : 500),
                                new LoanDate(doesEditDate[i] ? "1st Jan 2010" : "1st Jan 2020")),
                        new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2020")),
                        new RepayLoanTransaction(MoneyInt.fromCent(800), new LoanDate("1st Jan 2020"))
                )));
                editTransactionDescriptor[i] = new EditTransactionDescriptor();
                if (doesEditAmount[i]) {
                    editTransactionDescriptor[i].setAmount(MoneyInt.fromCent(300));
                }
                if (doesEditDate[i]) {
                    editTransactionDescriptor[i].setDate(new LoanDate("1st Jan 2010"));
                }
            }
        } catch (ExcessRepaymentException e) {
            throw new RuntimeException(e);
        }
    }


    // Recreate sample model everytime since model is modified everytime we execute the command
    private static Model createSampleModel() {
        return new ModelManager(
                new LoanBookBuilder()
                        .withPerson(ALICE)
                        .withPerson(new Loan(BOB.getName(), sampleAmount, BOB.getTags()))
                        .build(),
                new UserPrefs()
        );
    }

    private static Model[] createSampleModelFirstTransactionEdited() {
        Model[] models = new Model[sampleAmountFirstTransactionEdited.length];
        for (int i = 0; i < models.length; i++) {
            models[i] = new ModelManager(
                    new LoanBookBuilder()
                            .withPerson(ALICE)
                            .withPerson(new Loan(BOB.getName(), sampleAmountFirstTransactionEdited[i], BOB.getTags()))
                            .build(),
                    new UserPrefs());
        }
        return models;
    }

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new EdithistCommand(null, INDEX_FIRST_PERSON, editTransactionDescriptor[0]));
        assertThrows(NullPointerException.class, () ->
                new EdithistCommand(INDEX_FIRST_PERSON, null, editTransactionDescriptor[0]));
        assertThrows(NullPointerException.class, () ->
                new EdithistCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, null));
        assertThrows(NullPointerException.class, () ->
                new EdithistCommand(null, null, null));
    }

    @Test
    public void execute_success() throws Exception {
        for (int i = 0; i < editTransactionDescriptor.length; i++) {
            EdithistCommand edithistCommand =
                    new EdithistCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON, editTransactionDescriptor[i]);
            Model sampleModel = createSampleModel();
            CommandResult result = edithistCommand.execute(sampleModel);
            assertEquals(createSampleModelFirstTransactionEdited()[i], sampleModel);
            assertEquals(new CommandResult(String.format(MESSAGE_EDIT_TRANSACTION_SUCCESS,
                    Messages.format(sampleModel
                            .getLoanBook()
                            .getPersonList()
                            .get(INDEX_SECOND_PERSON.getZeroBased())))), result);
        }
    }

    @Test
    public void execute_noEdition_throwsCommandException() throws Exception {
        Model sampleModel = createSampleModel();
        EdithistCommand edithistCommand =
                new EdithistCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, new EditTransactionDescriptor());
        assertThrows(CommandException.class, MESSAGE_NOT_EDITED, () -> edithistCommand.execute(sampleModel));
    }

    @Test
    public void execute_unchangedAmount_throwsCommandException() {
        Model sampleModel = createSampleModel();
        EditTransactionDescriptor descriptor = new EditTransactionDescriptor(editTransactionDescriptor[2]);
        descriptor.setAmount(sampleModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()).getLoanAmount()
                .getTransactionHistoryCopy().get(INDEX_FIRST_PERSON.getZeroBased()).getAmount());
        EdithistCommand edithistCommand =
                new EdithistCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON, descriptor);
        assertThrows(CommandException.class, MESSAGE_UNCHANGED_AMOUNT, () -> edithistCommand.execute(sampleModel));
    }

    @Test
    public void execute_unchangedDate_throwsCommandException() {
        Model sampleModel = createSampleModel();
        EditTransactionDescriptor descriptor = new EditTransactionDescriptor(editTransactionDescriptor[2]);
        descriptor.setDate(sampleModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()).getLoanAmount()
                .getTransactionHistoryCopy().get(INDEX_FIRST_PERSON.getZeroBased()).getDate());
        EdithistCommand edithistCommand =
                new EdithistCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON, descriptor);
        assertThrows(CommandException.class, MESSAGE_UNCHANGED_DATE, () -> edithistCommand.execute(sampleModel));
    }

    // both unchanged -> point out unchanged amount
    @Test
    public void execute_unchangedAmountAndDate_throwsCommandException() {
        Model sampleModel = createSampleModel();
        EditTransactionDescriptor descriptor = new EditTransactionDescriptor(editTransactionDescriptor[2]);
        descriptor.setAmount(sampleModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()).getLoanAmount()
                .getTransactionHistoryCopy().get(INDEX_FIRST_PERSON.getZeroBased()).getAmount());
        descriptor.setDate(sampleModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()).getLoanAmount()
                .getTransactionHistoryCopy().get(INDEX_FIRST_PERSON.getZeroBased()).getDate());
        EdithistCommand edithistCommand =
                new EdithistCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON, descriptor);
        assertThrows(CommandException.class, MESSAGE_UNCHANGED_AMOUNT, () -> edithistCommand.execute(sampleModel));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        EdithistCommand edithistCommand =
                new EdithistCommand(INDEX_THIRD_PERSON, INDEX_FIRST_PERSON, editTransactionDescriptor[0]);
        assertThrows(NullPointerException.class, () -> edithistCommand.execute(null));
    }

    @Test
    public void execute_invalidLoanIndex_throwsCommandException() {
        EdithistCommand edithistCommand =
                new EdithistCommand(INDEX_THIRD_PERSON, INDEX_FIRST_PERSON, editTransactionDescriptor[0]);
        Model sampleModel = createSampleModel();
        assertThrows(CommandException.class, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ()
                -> edithistCommand.execute(sampleModel));
    }

    @Test
    public void execute_invalidTransactionIndex_throwsCommandException() {
        EdithistCommand edithistCommand =
                new EdithistCommand(INDEX_SECOND_PERSON, Index.fromOneBased(4),
                        editTransactionDescriptor[0]);
        Model sampleModel = createSampleModel();
        assertThrows(CommandException.class, MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX, ()
                -> edithistCommand.execute(sampleModel));
    }

    @Test
    public void execute_excessRepayment_throwsExcessRepaymentException() {
        EditTransactionDescriptor invalidEdit = new EditTransactionDescriptor();
        invalidEdit.setAmount(MoneyInt.fromCent(1501));
        EdithistCommand edithistCommand = new EdithistCommand(INDEX_SECOND_PERSON, INDEX_THIRD_PERSON, invalidEdit);
        Model sampleModel = createSampleModel();
        assertThrows(CommandException.class, MESSAGE_EXCESS_REPAYMENT_IN_HISTORY, ()
                -> edithistCommand.execute(sampleModel));
    }

    @Test
    public void equals() {
        EdithistCommand command11 =
                new EdithistCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, editTransactionDescriptor[0]);
        EdithistCommand command12 =
                new EdithistCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON, editTransactionDescriptor[0]);
        EdithistCommand command21 =
                new EdithistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, editTransactionDescriptor[0]);

        // same object -> returns true
        assertTrue(command11.equals(command11));

        // same values -> returns true
        assertTrue(command11.equals(
                new EdithistCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, editTransactionDescriptor[0])));

        // different types -> returns false
        assertFalse(command11.equals(1));

        // null -> returns false
        assertFalse(command11.equals(null));

        // different index -> returns false
        assertFalse(command11.equals(command12));
        assertFalse(command11.equals(command21));

        // different transaction -> returns false
        assertFalse(command11.equals(new EdithistCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                editTransactionDescriptor[1])));
    }

}
