package wanted.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import wanted.model.LoanBook;
import wanted.model.ReadOnlyLoanBook;
import wanted.model.loan.*;
import wanted.model.tag.Tag;

/**
 * Contains utility methods for populating {@code LoanBook} with sample data.
 */
public class SampleDataUtil {
    public static Loan[] getSamplePersons() {
        return new Loan[] {
            new Loan(new Name("Alex Yeoh"), new Amount("87.43"), new Date("25th Feb 2024"),
                getTagSet("friends")),
            new Loan(new Name("Bernice Yu"), new Amount("99.27"), new Date("28th Dec 2024"),
                getTagSet("colleagues", "friends")),
            new Loan(new Name("Charlotte Oliveiro"), new Amount("102.83"), new Date("14 Feb 2025"),
                getTagSet("neighbours")),
            new Loan(new Name("David Li"), new Amount("910.31"), new Date("12th March 2025"),
                getTagSet("family")),
            new Loan(new Name("Irfan Ibrahim"), new Amount("91.31"), new Date("12th March 2025"),
                getTagSet("classmates")),
            new Loan(new Name("Roy Balakrishnan"), new Amount("92624.42"), new Date("17th January 2025"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyLoanBook getSampleAddressBook() {
        LoanBook sampleAb = new LoanBook();
        for (Loan samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
