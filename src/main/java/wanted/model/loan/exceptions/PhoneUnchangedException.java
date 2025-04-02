package wanted.model.loan.exceptions;

/**
 * Exception for duplicate phone number
 */
public class PhoneUnchangedException extends Exception {
    public PhoneUnchangedException() {
        super("New phone number is same with old phone number!");
    }
}
