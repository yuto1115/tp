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
@JsonRootName(value = "addressbook")
class JsonSerializableLoanBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate loan(s).";

    private final List<JsonAdaptedLoan> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableLoanBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableLoanBook(@JsonProperty("persons") List<JsonAdaptedLoan> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyLoanBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableLoanBook}.
     */
    public JsonSerializableLoanBook(ReadOnlyLoanBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedLoan::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code LoanBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public LoanBook toModelType() throws IllegalValueException {
        LoanBook addressBook = new LoanBook();
        for (JsonAdaptedLoan jsonAdaptedLoan : persons) {
            Loan person = jsonAdaptedLoan.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }
        return addressBook;
    }

}
