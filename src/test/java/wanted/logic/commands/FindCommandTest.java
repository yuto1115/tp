package wanted.logic.commands;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static wanted.logic.Messages.MESSAGE_PERSONS_FOUND_OVERVIEW;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

//import java.util.List;
import org.junit.jupiter.api.Test;

import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
//import wanted.model.loan.Loan;
//import wanted.model.loan.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {

    private final Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());

    @Test
    public void execute_zeroKeywords_noMatches() throws CommandException {
        //TODO: Write test case for zero keywords keyed in

        // NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Nonexistent"));
        //FindCommand command = new FindCommand(predicate);
        // CommandResult result = command.execute(model);

      //assertEquals(String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, 0), result.getFeedbackToUser());
      //assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_multipleKeywords_success() throws CommandException {
        //TODO: Write test case for successful check with multiple keywords in find
        //NameContainsKeywordsPredicate predicate =
        // new NameContainsKeywordsPredicate(List.of("Alice", "Benson", "Carl"));
        //FindCommand command = new FindCommand(predicate);

        //CommandResult result = command.execute(model);
        //List<Loan> filtered = model.getFilteredPersonList();

        // We expect at least one match from the sample keywords
        //assertTrue(filtered.size() > 0);
        //assertEquals(String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, filtered.size()), result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        //TODO: FindCommandTest equals method
        //NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(List.of("alice"));
        //NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(List.of("bob"));

        //FindCommand findFirstCommand = new FindCommand(firstPredicate);
        //FindCommand findFirstCommandCopy = new FindCommand(List.of("alice")
        //        .equals(List.of("alice")) ? new NameContainsKeywordsPredicate(List.of("alice")) : null);
        //FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        //assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        //assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        //assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        //assertFalse(findFirstCommand.equals(null));

        // different predicates -> returns false
        //assertFalse(findFirstCommand.equals(findSecondCommand));
    }
}
