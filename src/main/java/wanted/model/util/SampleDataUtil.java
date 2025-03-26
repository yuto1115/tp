package wanted.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import wanted.model.LoanBook;
import wanted.model.ReadOnlyLoanBook;
import wanted.model.loan.Amount;
import wanted.model.loan.Loan;
import wanted.model.loan.Name;
import wanted.model.tag.Tag;

/**
 * Contains utility methods for populating {@code LoanBook} with sample data.
 */
public class SampleDataUtil {
    public static Loan[] getSamplePersons() {
        return new Loan[] {
            new Loan(new Name("Alex Yeoh"),
                        new Amount("10.23"),
                        getTagSet("friends")),
            new Loan(new Name("Bernice Yu"),
                        new Amount("13.23"),
                        getTagSet("colleagues", "friends")),
            new Loan(new Name("Charlotte Oliveiro"),
                        new Amount("100.23"),
                        getTagSet("neighbours")),
            new Loan(new Name("David Li"),
                        new Amount("1.06"),
                        getTagSet("family")),
            new Loan(new Name("Irfan Ibrahim"),
                        new Amount("24.24"),
                        getTagSet("classmates")),
            new Loan(new Name("Roy Balakrishnan"),
                        new Amount("18.93"),
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
