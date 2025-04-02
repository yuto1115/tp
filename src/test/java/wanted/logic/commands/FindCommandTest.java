package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
import static wanted.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.testutil.TypicalPersons.CARL;
import static wanted.testutil.TypicalPersons.ELLE;
import static wanted.testutil.TypicalPersons.FIONA;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private final Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalLoanBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        assertEquals(findFirstCommand, findFirstCommand);
        assertEquals(findFirstCommand, new FindCommand(firstPredicate));
        assertNotEquals(1, findFirstCommand);
        assertNotEquals(null, findFirstCommand);
        assertNotEquals(findFirstCommand, findSecondCommand);
    }

    @Test
    public void execute_zeroKeywords_noMatches() {
        NameContainsKeywordsPredicate predicate = preparePredicate("   ");
        FindCommand command = new FindCommand(predicate);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        List<Loan> expectedList = model.getLoanBook().getPersonList(); // should remain unchanged

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedList, model.getLoanBook().getPersonList());
    }

    @Test
    public void execute_multipleKeywords_rankedResults() {
        NameContainsKeywordsPredicate predicate = preparePredicate("Elle Carl Fiona");
        FindCommand command = new FindCommand(predicate);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);

        // The model list is sorted in-place — we verify the list order after the command
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // Manually score and sort expected list
        List<Loan> sortedExpected = List.of(CARL, ELLE, FIONA); // Assuming match count = 1 for each
        assertEquals(sortedExpected, model.getLoanBook().getPersonList().subList(0, 3));
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.trim().split("\\s+")));
    }
}
