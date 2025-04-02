package wanted.model.loan.exceptions;

public class PhoneUnchangedException extends Exception {
    public PhoneUnchangedException() {
        super("New phone number is same with old phone number!");
    }
}
