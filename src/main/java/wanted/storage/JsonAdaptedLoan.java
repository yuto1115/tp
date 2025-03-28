package wanted.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import wanted.commons.exceptions.IllegalValueException;
import wanted.model.loan.Amount;
import wanted.model.loan.Loan;
import wanted.model.loan.Name;
import wanted.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Loan}.
 */
class JsonAdaptedLoan {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Loan's %s field is missing!";

    private final String name;
//    private final String amount;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedLoan} with the given loan details.
     */
    @JsonCreator
    public JsonAdaptedLoan(@JsonProperty("name") String name,
//                           @JsonProperty("amount") String amount,
                           @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
//        this.amount = amount;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Loan} into this class for Jackson use.
     */
    public JsonAdaptedLoan(Loan source) {
        name = source.getName().fullName;
//        amount = source.getAmount().remainingValue.getStringRepresentationWithFixedDecimalPoint();

        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted loan object into the model's {@code Loan} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted loan.
     */
    public Loan toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

//        if (amount == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Amount.class.getSimpleName()));
//        }
//        if (!Amount.isValidAmount(amount)) {
//            throw new IllegalValueException(Amount.MESSAGE_CONSTRAINTS);
//        }
//        final Amount modelAmount = new Amount(amount);

        /*
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LoanDate.class.getSimpleName()));
        }
        if (!LoanDate.isValidLoanDate(date)) {
            throw new IllegalValueException(LoanDate.MESSAGE_CONSTRAINTS);
        }
        final LoanDate modelLoanDate = new LoanDate(date);
        */

        final Set<Tag> modelTags = new HashSet<>(personTags);
//        return new Loan(modelName, modelAmount, modelTags);
        return new Loan(modelName, modelTags);
    }
}
