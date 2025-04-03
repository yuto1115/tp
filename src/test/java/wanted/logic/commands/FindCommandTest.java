package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.NameContainsKeywordsPredicate;
import wanted.testutil.LoanBookBuilder;
import wanted.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {

    private static Model createSampleModel() {
        return new ModelManager(
                new LoanBookBuilder()
                        .withPerson(new PersonBuilder().withName("Alex").build())
                        .withPerson(new PersonBuilder().withName("Alex Yeoh").build())
                        .withPerson(new PersonBuilder().withName("Benedict").build())
                        .withPerson(new PersonBuilder().withName("Elisa").build())
                        .build(),
                new UserPrefs()
        );
    }

    @Test
    public void findCommandConstructor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindCommand(null));
    }

    @Test
    public void executeEmptyKeyword_throwsCommandException() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of(""));
        FindCommand command = new FindCommand(predicate);
        Model model = createSampleModel();
        assertThrows(IllegalArgumentException.class, () -> command.execute(model));
    }

    @Test
    public void executeMultipleKeywords_success() throws CommandException {
        //TODO: Write test case for successful check with multiple keywords in find
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(List.of("Alex", "Yeoh"));
        FindCommand command = new FindCommand(predicate);
        Model model = createSampleModel();

        CommandResult result = command.execute(model);

        List<Loan> filtered = model.getFilteredPersonList();

        // assert that the size of the list should not have changed after find command
        assertEquals(4, filtered.size());

        // The most relevant match "Alex Yeoh" should appear first, followed by "Alex"
        assertEquals("Alex Yeoh", filtered.get(0).getName().fullName);
        assertEquals("Alex", filtered.get(1).getName().fullName);
    }

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(List.of("alex"));
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(List.of("yeoh"));

        FindCommand command1 = new FindCommand(firstPredicate);
        FindCommand command2 = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        FindCommand command1Copy = new FindCommand(firstPredicate);
        assertTrue(command1.equals(command1Copy));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different predicates -> returns false
        assertFalse(command1.equals(command2));
    }
}
