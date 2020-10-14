package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showAttractionAtIndex;
import static seedu.address.testutil.TypicalAttractions.getTypicalAttractionList;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ATTRACTION;
import static seedu.address.testutil.TypicalItineraries.getTypicalItineraryList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAttractionList(), getTypicalItineraryList(), new UserPrefs());
        expectedModel = new ModelManager(model.getAttractionList(), model.getItineraryList(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showAttractionAtIndex(model, INDEX_FIRST_ATTRACTION);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
