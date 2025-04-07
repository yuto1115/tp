package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.logic.commands.CommandTestUtil.assertCommandFailure;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.logic.commands.CommandTestUtil.showPersonAtIndex;
import static wanted.logic.commands.TagCommand.MESSAGE_DUPLICATE_TAG;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.tag.Tag;
import wanted.testutil.EditLoanDescriptorBuilder;
import wanted.testutil.PersonBuilder;

/**
 * Tests based on rename command test
 */
public class TagCommandTest {

    private Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws ExcessRepaymentException {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Loan lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson).withTags(VALID_TAG_HUSBAND);
        Loan editedPerson = personInList.withTags(VALID_TAG_HUSBAND).build();
        BaseEdit.EditLoanDescriptor descriptor =
                new EditLoanDescriptorBuilder().withTags(VALID_TAG_HUSBAND).build();

        TagCommand retagCommand = new TagCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new LoanBook(model.getLoanBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(retagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        //correctly adds a tag to the list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Loan personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> currTags = new HashSet<>(personInFilteredList.getTags());
        List<String> tagStr = currTags.stream().map(Tag::getTagName).collect(Collectors.toList());
        tagStr.add(VALID_TAG_FRIEND);
        String[] tagArray = tagStr.toArray(new String[0]);
        Loan editedPerson = new PersonBuilder(personInFilteredList).withTags(tagArray).build();
        TagCommand retagCommand = new TagCommand(INDEX_FIRST_PERSON,
                new EditLoanDescriptorBuilder().withTags(VALID_TAG_FRIEND).build());

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new LoanBook(model.getLoanBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(retagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        BaseEdit.EditLoanDescriptor descriptor = new EditLoanDescriptorBuilder().withTags(VALID_TAG_HUSBAND).build();
        TagCommand renameCommand = new TagCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(renameCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getLoanBook().getPersonList().size());

        TagCommand renameCommand = new TagCommand(outOfBoundIndex,
                new EditLoanDescriptorBuilder().withTags(VALID_TAG_HUSBAND).build());

        assertCommandFailure(renameCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateTagFilteredList_failure() {
        //identifies duplicate tag
        Loan personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> currTags = new HashSet<>(personInFilteredList.getTags());
        List<String> tagStr = currTags.stream().map(Tag::getTagName).collect(Collectors.toList());
        tagStr.add(tagStr.get(0));
        String[] tagArray = tagStr.toArray(new String[0]);
        TagCommand retagCommand = new TagCommand(INDEX_FIRST_PERSON,
                new EditLoanDescriptorBuilder().withTags(tagArray).build());

        assertCommandFailure(retagCommand, model, MESSAGE_DUPLICATE_TAG);
    }

    @Test
    public void equals() {
        TagCommand tagCommand1 = new TagCommand(INDEX_FIRST_PERSON, new EditLoanDescriptorBuilder().build());
        TagCommand tagCommand2 = new TagCommand(INDEX_FIRST_PERSON, new EditLoanDescriptorBuilder().build());
        TagCommand tagCommand3 = new TagCommand(INDEX_FIRST_PERSON,
                new EditLoanDescriptorBuilder().withTags("example").build());
        TagCommand tagCommand4 = new TagCommand(INDEX_SECOND_PERSON, new EditLoanDescriptorBuilder().build());

        // same object -> returns true
        assertEquals(tagCommand1, tagCommand1);

        // same value -> return true
        assertEquals(tagCommand1, tagCommand2);

        // different value -> returns false (editLoanDescriptor is different)
        assertNotEquals(tagCommand1, tagCommand3);

        //  different index -> returns false
        assertNotEquals(tagCommand1, tagCommand4);
    }
}
