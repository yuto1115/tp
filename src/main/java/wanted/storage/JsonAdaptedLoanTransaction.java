package wanted.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.commons.exceptions.IllegalValueException;
import wanted.logic.parser.ParserUtil;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.LoanDate;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.LoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;
import wanted.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Tag}.
 */
class JsonAdaptedLoanTransaction {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Loan's %s field is missing!";

    private final String type;
    private final String amount;
    private final String date;

    /**
     * Constructs a {@code JsonAdaptedLoanTransaction} with the given loan transaction details.
     */
    @JsonCreator
    public JsonAdaptedLoanTransaction(@JsonProperty("type") String type,
                                      @JsonProperty("amount") String amount,
                                      @JsonProperty("date") String date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Converts a given {@code LoanTransaction} into this class for Jackson use.
     */
    public JsonAdaptedLoanTransaction(LoanTransaction source) {
        if (source instanceof AddLoanTransaction) {
            this.type = "add";
        } else if (source instanceof RepayLoanTransaction) {
            this.type = "repay";
        } else {
            throw new RuntimeException("Unknown LoanTransaction type");
        }
        this.amount = source.getAmount().getStringRepresentationWithFixedDecimalPoint();
        this.date = source.getDate().toString();
    }

    /**
     * Converts this Jackson-friendly adapted loan transaction object into the model's {@code LoanTransaction} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public LoanTransaction toModelType() throws IllegalValueException {
        if (amount == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "amount"));
        }
        final MoneyInt modelAmount;
        try {
            modelAmount = ParserUtil.parseMoneyAmount(amount);
        } catch (ParseException e) {
            throw new IllegalValueException(ParserUtil.MESSAGE_INVALID_MONEY_AMOUNT);
        }

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }
        if (!LoanDate.isValidLoanDate(date)) {
            throw new IllegalValueException(LoanDate.MESSAGE_CONSTRAINTS);
        }
        final LoanDate modelDate = new LoanDate(date);

        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "type"));
        }
        switch (type) {
        case "add":
            return new AddLoanTransaction(modelAmount, modelDate);
        case "repay":
            return new RepayLoanTransaction(modelAmount, modelDate);
        default:
            throw new IllegalValueException("Unknown LoanTransaction type: " + type);
        }
    }

}
