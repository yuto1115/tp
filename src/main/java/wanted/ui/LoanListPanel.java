package wanted.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import wanted.model.loan.Loan;

/**
 * Panel containing the list of persons.
 */
public class LoanListPanel extends UiPart<Region> {
    private static final String FXML = "LoanListPanel.fxml";
    //private final Logger logger = LogsCenter.getLogger(LoanListPanel.class);

    @FXML
    private ListView<Loan> loanListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public LoanListPanel(ObservableList<Loan> loanList) {
        super(FXML);
        loanListView.setItems(loanList);
        loanListView.setCellFactory(listView -> new LoanListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Loan} using a {@code LoanCard}.
     */
    class LoanListViewCell extends ListCell<Loan> {
        @Override
        protected void updateItem(Loan loan, boolean empty) {
            super.updateItem(loan, empty);

            if (empty || loan == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new LoanCard(loan, getIndex() + 1).getRoot());
            }
        }
    }

}
