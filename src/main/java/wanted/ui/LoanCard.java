package wanted.ui;

import java.util.Comparator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import wanted.model.loan.Loan;
import wanted.model.loan.Phone;
import wanted.model.loan.transaction.LoanTransaction;

/**
 * An UI component that displays information of a {@code Loan}.
 */
public class LoanCard extends UiPart<Region> {

    private static final String FXML = "LoanListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on LoanBook level 4</a>
     */

    public final Loan loan;
    private boolean isReturned;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane tags;
    @FXML
    private Label amount;
    @FXML
    private Label totalAmount;
    @FXML
    private Label phone;
    @FXML
    private Label date;
    @FXML
    private Label status;
    @FXML
    private VBox transactionsBox;
    @FXML
    private ScrollPane entryScrollPane;

    private ObservableList<LoanTransaction> transactions;

    /**
     * Creates a {@code PersonCode} with the given {@code Loan} and index to display.
     */
    public LoanCard(Loan loan, int displayedIndex) {
        super(FXML);
        //display fields
        this.loan = loan;
        isReturned = this.loan.getLoanAmount().isRepaid();
        id.setText(displayedIndex + ". ");
        status.setText(getStatus());
        name.setText(loan.getName().fullName);
        amount.setText("Remaining Loan Amount: " + loan.getLoanAmount().getRemainingAmount()
                .getStringRepresentationWithFixedDecimalPoint());
        totalAmount.setText("Total Loaned Amount: " + loan.getLoanAmount().getTotalAmount()
                .getStringRepresentationWithFixedDecimalPoint());
        transactions = FXCollections.observableArrayList(loan.getLoanAmount().getTransactionHistoryCopy());

        if (loan.getPhone() != null && !loan.getPhone().equals(Phone.EMPTY_PHONE)) {
            this.phone.setText("Phone number: " + loan.getPhone().getValue());
        } else {
            this.phone.setText("No phone number available");
        }

        // Sort tags alphabetically and display them
        loan.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        updateBackground();
        updateTransactionList(transactions);
    }

    private void updateBackground() {
        if (isReturned) {
            cardPane.setStyle("-fx-background-image: url('/images/papyrus.png');"
                    + "-fx-background-size: cover;");
        } else {
            cardPane.setStyle("-fx-background-image: url('/images/blood_splatter_background.png');"
                    + "-fx-background-size: cover;");
        }
    }

    private String getStatus() {
        status.getStyleClass().removeAll("status-wanted", "status-not-wanted");

        if (isReturned) {
            status.getStyleClass().add("status-not-wanted");
            return "Not Wanted";
        } else {
            status.getStyleClass().add("status-wanted");
            return "Wanted";
        }
    }

    private void updateTransactionList(ObservableList<LoanTransaction> transactions) {
        transactionsBox.getChildren().clear(); // Clear old transactions
        transactionsBox.getStyleClass().add("transaction-box");
        //pls work
        transactions.stream()
                .map(this::createTransactionLabel).forEach(transactionsBox.getChildren()::add);
        Platform.runLater(() -> entryScrollPane.setVvalue(1.0));
    }

    private Label createTransactionLabel(LoanTransaction transaction) {
        int index = transactionsBox.getChildren().size() + 1;
        Label txnLabel = new Label(index + ". " + transaction.getExplanation());
        txnLabel.getStyleClass().add("transaction-label");
        return txnLabel; //same code logic as before
    }
}
