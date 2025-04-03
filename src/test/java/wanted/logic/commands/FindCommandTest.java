package wanted.logic.commands;

import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.util.Arrays;
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

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
