package wanted.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import wanted.commons.exceptions.IllegalValueException;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.Name;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.LoanTransaction;
import wanted.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Loan}.
 */
class JsonAdaptedLoan {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Loan's %s field is missing!";
    public static final String LOAN_EXCESS_REPAYMENT_MESSAGE = "Loan transactions violate the constraint that "
            + "the remaining loan amount should never be negative.";

    private final String name;
    private final List<JsonAdaptedLoanTransaction> transactions = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedLoan} with the given loan details.
     */
    @JsonCreator
    public JsonAdaptedLoan(@JsonProperty("name") String name,
                           @JsonProperty("transactions") List<JsonAdaptedLoanTransaction> transactions,
                           @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        if (transactions != null) {
            this.transactions.addAll(transactions);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Loan} into this class for Jackson use.
     */
    public JsonAdaptedLoan(Loan source) {
        name = source.getName().fullName;

        transactions.addAll(source.getLoanAmount().getTransactionHistoryCopy().stream()
                        .map(JsonAdaptedLoanTransaction::new)
                        .toList());

        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .toList());
    }

    /**
     * Converts this Jackson-friendly adapted loan object into the model's {@code Loan} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted loan.
     */
    public Loan toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        final ArrayList<LoanTransaction> modelTransactions = new ArrayList<>();
        for (JsonAdaptedLoanTransaction transaction : transactions) {
            modelTransactions.add(transaction.toModelType());
        }

        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        final Set<Tag> modelTags = new HashSet<>(personTags);

        try {
            return new Loan(modelName, new LoanAmount(modelTransactions), modelTags);
        } catch (ExcessRepaymentException e) {
            throw new IllegalValueException(LOAN_EXCESS_REPAYMENT_MESSAGE);
        }
    }
}
