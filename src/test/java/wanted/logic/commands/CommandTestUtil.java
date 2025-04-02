package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_NAME;
import static wanted.logic.parser.CliSyntax.PREFIX_TAG;
import static wanted.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.LoanDate;
import wanted.model.loan.NameContainsKeywordsPredicate;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.LoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;
import wanted.testutil.EditLoanDescriptorBuilder;
import wanted.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    // needs to be updated here

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_AMOUNT_AMY = "10.10";
    public static final String VALID_AMOUNT_BOB = "1230.97";
    public static final String VALID_DATE_AMY = "10th December 2024";
    public static final String VALID_DATE_BOB = "19th July 2024";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String AMOUNT_DESC_AMY = " " + PREFIX_AMOUNT + VALID_AMOUNT_AMY;
    public static final String AMOUNT_DESC_BOB = " " + PREFIX_AMOUNT + VALID_AMOUNT_BOB;
    public static final String DATE_DESC_AMY = " " + PREFIX_DATE + VALID_DATE_AMY;
    public static final String DATE_DESC_BOB = " " + PREFIX_DATE + VALID_DATE_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_AMOUNT_DESC = " " + PREFIX_AMOUNT + "10";
    public static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "Febru@ry";
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                // .withAmount(VALID_AMOUNT_AMY)
                .withLoanDate(VALID_DATE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                // .withAmount(VALID_AMOUNT_BOB)
                .withLoanDate(VALID_DATE_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    public static final BaseEdit.EditLoanDescriptor NEW_DESC_AMY;
    //public static final BaseEdit.EditLoanDescriptor NEW_DESC_BOB;

    static {
        ArrayList<LoanTransaction> history = new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(1250), new LoanDate("3rd Jan 2024"))
        ));

        try {
            NEW_DESC_AMY = new EditLoanDescriptorBuilder().withName(VALID_NAME_AMY)
                    .withLoanAmount(new LoanAmount(history)).withTags(VALID_TAG_FRIEND).build();
        } catch (ExcessRepaymentException e) {
            throw new AssertionError("ExcessRepaymentException");
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered loan list and selected loan in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        LoanBook expectedAddressBook = new LoanBook(actualModel.getLoanBook());
        List<Loan> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getLoanBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the loan at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Loan person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
