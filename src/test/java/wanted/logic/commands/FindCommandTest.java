package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static wanted.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.testutil.TypicalPersons.CARL;
import static wanted.testutil.TypicalPersons.ELLE;
import static wanted.testutil.TypicalPersons.FIONA;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 * This command is disabled in the MVP
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalLoanBook(), new UserPrefs());

//    @Test
//    public void equals() {
//        NameContainsKeywordsPredicate firstPredicate =
//                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
//        NameContainsKeywordsPredicate secondPredicate =
//                new NameContainsKeywordsPredicate(Collections.singletonList("second"));
//
//        FindCommand findFirstCommand = new FindCommand(firstPredicate);
//        FindCommand findSecondCommand = new FindCommand(secondPredicate);
//
//        // same object -> returns true
//        assertTrue(findFirstCommand.equals(findFirstCommand));
//
//        // same values -> returns true
//        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
//        assertTrue(findFirstCommand.equals(findFirstCommandCopy));
//
//        // different types -> returns false
//        assertFalse(findFirstCommand.equals(1));
//
//        // null -> returns false
//        assertFalse(findFirstCommand.equals(null));
//
//        // different loan -> returns false
//        assertFalse(findFirstCommand.equals(findSecondCommand));
//    }

//    @Test
//    public void execute_zeroKeywords_noPersonFound() {
//        assumeTrue(FindCommand.IS_ENABLED);
//        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
//        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
//        FindCommand command = new FindCommand(predicate);
//        expectedModel.updateFilteredPersonList(predicate);
//        assertCommandSuccess(command, model, expectedMessage, expectedModel);
//        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
//    }

//    @Test
//    public void execute_multipleKeywords_multiplePersonsFound() {
//        assumeTrue(FindCommand.IS_ENABLED);
//        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
//        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
//        FindCommand command = new FindCommand(predicate);
//        expectedModel.updateFilteredPersonList(predicate);
//        assertCommandSuccess(command, model, expectedMessage, expectedModel);
//        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
//    }

//    @Test
//    public void toStringMethod() {
//        assumeTrue(FindCommand.IS_ENABLED);
//        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
//        FindCommand findCommand = new FindCommand(predicate);
//        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
//        assertEquals(expected, findCommand.toString());
//    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
