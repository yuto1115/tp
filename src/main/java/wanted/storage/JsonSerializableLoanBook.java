package wanted.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import wanted.commons.exceptions.IllegalValueException;
import wanted.model.LoanBook;
import wanted.model.ReadOnlyLoanBook;
import wanted.model.loan.Loan;

/**
 * An Immutable LoanBook that is serializable to JSON format.
 */
@JsonRootName(value = "loanbook")
class JsonSerializableLoanBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Loans list contains duplicate loan(s).";

    private final List<JsonAdaptedLoan> loans = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableLoanBook} with the given loans.
     */
    @JsonCreator
    public JsonSerializableLoanBook(@JsonProperty("loans") List<JsonAdaptedLoan> loans) {
        this.loans.addAll(loans);
    }

    /**
     * Converts a given {@code ReadOnlyLoanBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableLoanBook}.
     */
    public JsonSerializableLoanBook(ReadOnlyLoanBook source) {
        loans.addAll(source.getPersonList().stream().map(JsonAdaptedLoan::new).collect(Collectors.toList()));
    }

    /**
     * Converts this loan book into the model's {@code LoanBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public LoanBook toModelType() throws IllegalValueException {
        LoanBook loanBook = new LoanBook();
        for (JsonAdaptedLoan jsonAdaptedLoan : loans) {
            Loan person = jsonAdaptedLoan.toModelType();
            if (loanBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            loanBook.addPerson(person);
        }
        return loanBook;
    }

}
