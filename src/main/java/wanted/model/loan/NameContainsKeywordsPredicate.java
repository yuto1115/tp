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

    /**
     * Returns true if the given name starts with any of the specified keywords (case-insensitive).
     * A match is determined by whether any keyword appears as a whole word at the start of the name.
     *
     * @param name The name to be checked against the keywords. Must not be null.
     * @return True if the name starts with any keyword, false otherwise.
     */
    public boolean startWithKeyword(String name) {
        String lowerName = name.toLowerCase();
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(lowerName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate)) {
            return false;
        }

        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
