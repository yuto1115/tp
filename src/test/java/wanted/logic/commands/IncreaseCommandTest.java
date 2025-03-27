package wanted.logic.commands;

import static wanted.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IncreaseCommandTest {
    //TODO: check for negative amount value
    //TODO: execution of command success
    //TODO: check for invalid index
    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }
}
