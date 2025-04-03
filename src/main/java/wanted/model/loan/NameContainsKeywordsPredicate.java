package wanted.model.loan;

import java.util.List;
import java.util.function.Predicate;

import wanted.commons.util.StringUtil;
import wanted.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Loan}'s {@code Name} matches any of the keywords given.
 */
public record NameContainsKeywordsPredicate(List<String> keywords) implements Predicate<Loan> {

    @Override
    public boolean test(Loan person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
