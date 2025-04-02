package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static wanted.logic.commands.CommandTestUtil.DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.DESC_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.EditCommand.EditPersonDescriptor;
import wanted.testutil.EditPersonDescriptorBuilder;

//This is actually for EditPersonDescriptor, hence renamed
public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        assumeTrue(EditCommand.IS_ENABLED);
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different amount -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                // .withAmount(VALID_AMOUNT_BOB)
                .build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different date -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withLoanDate(VALID_DATE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", tags="
                + editPersonDescriptor.getTags().orElse(null) + ", amount="
                + editPersonDescriptor.getAmount().orElse(null) + ", date="
                + editPersonDescriptor.getDate().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
