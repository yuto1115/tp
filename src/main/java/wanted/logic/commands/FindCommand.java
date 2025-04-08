package wanted.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.loan.Loan;
import wanted.model.loan.NameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in loan book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final boolean IS_ENABLED = true;

    public static final String COMMAND_WORD = "find";
    public static final String MESSAGE_SUCCESS = "Loan(s) found";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all persons whose names contain any specified keywords (case-insensitive).\n"
            + "Wanted list will be sorted with matches to the keywords (both fully and partially) at the top\n"
            + "Output message will indicate the number of names that fully match keyword entered.\n"
            + "Parameters:\n"
            + "    [KEYWORD]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    /**
     * Constructs a {@code FindCommand} with the specified name-matching predicate.
     *
     * @param predicate The predicate used to evaluate if a loan's name matches any of the given keywords.
     *                  Must not be null.
     */
    public FindCommand(NameContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        LoanBook loanBook = (LoanBook) model.getLoanBook();
        ObservableList<Loan> originalList = loanBook.getPersonList();
        List<Loan> sortedList = IntStream.range(0, originalList.size())
                .mapToObj(i -> new IndexedLoan(originalList.get(i), i))
                .sorted(Comparator
                        .comparingInt((IndexedLoan il) -> getMatchScore(il.loan))
                        .reversed()
                        .thenComparing(il -> il.index))
                .map(il -> il.loan)
                .collect(Collectors.toList());

        assert sortedList.size() == originalList.size() : "New list must include all original items";

        loanBook.setPersons(sortedList);

        // TO find number of matches found
        long matchCount = sortedList.stream()
                        .filter(predicate)
                        .count();
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_FOUND_OVERVIEW, matchCount));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindCommand
                && predicate.equals(((FindCommand) other).predicate));
    }

    /**
     * Returns how many keywords are found in the loan's full name (case-insensitive).
     */
    private int getMatchScore(Loan loan) {
        String lowerName = loan.getName().fullName.toLowerCase();
        return (int) predicate.keywords().stream()
                .filter(keyword -> lowerName.contains(keyword.toLowerCase()))
                .count();
    }

    /**
     * Helper class to track original index for stable sorting
     * Immutable data structure.
     */
    private record IndexedLoan(Loan loan, int index) {
        private IndexedLoan {
            requireNonNull(loan);
        }
    }
}
