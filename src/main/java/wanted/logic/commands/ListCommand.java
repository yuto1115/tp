package wanted.logic.commands;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.loan.Loan;

import java.util.Comparator;
import java.util.List;

/**
 * Lists all persons in the loan book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        LoanBook loanBook = (LoanBook) model.getLoanBook();
        ObservableList<Loan> oldList = loanBook.getPersonList();
        Comparator<Loan> comparator =
                Comparator.comparing(l -> l.getName().toString().toLowerCase());
        List<Loan> sortedList = oldList.sorted(comparator);
        loanBook.setPersons(sortedList);
        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }
}
