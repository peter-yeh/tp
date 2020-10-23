package seedu.address.logic.commands.itineraryattraction;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAttractionList;
import seedu.address.model.ReadOnlyItineraryList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.testutil.ItineraryBuilder;

import java.nio.file.Path;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAttractions.getTypicalAttractionList;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;
import static seedu.address.testutil.TypicalItineraries.getTypicalItineraryList;


/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteAttractionCommand}.
 */
public class DeleteItineraryAttractionCommandTest {

    private Model model = new ModelManager(getTypicalAttractionList(), getTypicalItineraryList(), new UserPrefs());

    @Test
    public void execute_invalidAttractionIndex_throwsCommandException() {
        DeleteItineraryAttractionCommand delIaCommand = new DeleteItineraryAttractionCommand(INDEX_THIRD, INDEX_FIRST);

        ModelStubWithItinerary modelWithItinerary = new ModelStubWithItinerary(ItineraryBuilder.)

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX,
                () -> delIaCommand.execute(modelWithItinerary));
    }


    @Test
    public void equals() {
        DeleteItineraryAttractionCommand deleteFirstCommand;
        DeleteItineraryAttractionCommand deleteSecondCommand;
        deleteFirstCommand = new DeleteItineraryAttractionCommand(INDEX_FIRST, INDEX_THIRD);
        deleteSecondCommand = new DeleteItineraryAttractionCommand(INDEX_FIRST, INDEX_THIRD);

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // diff index -> return false
        deleteSecondCommand = new DeleteItineraryAttractionCommand(INDEX_SECOND, INDEX_THIRD);
        assertNotEquals(deleteFirstCommand, deleteSecondCommand);

        // diff day visiting -> return false
        deleteFirstCommand = new DeleteItineraryAttractionCommand(INDEX_SECOND, INDEX_FIRST);
        assertNotEquals(deleteFirstCommand, deleteSecondCommand);

        // different types -> returns false
        assertNotEquals(deleteFirstCommand, 1);

        // null -> returns false
        assertNotEquals(deleteFirstCommand, null);
    }

    /**
     * A default model stub that throws exception for all method calls.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAttractionListFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAttractionListFilePath(Path attractionListFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getItineraryListFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setItineraryListFilePath(Path itineraryListFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAttraction(Attraction attraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAttractionList(ReadOnlyAttractionList newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAttractionList getAttractionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasAttraction(Attraction attraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteAttraction(Attraction target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAttraction(Attraction target, Attraction editedAttraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void markVisitedAttraction(Attraction target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Attraction> getFilteredAttractionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredAttractionList(Predicate<Attraction> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setItineraryList(ReadOnlyItineraryList newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyItineraryList getItineraryList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteItinerary(Itinerary target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setItinerary(Itinerary target, Itinerary editedItinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Itinerary> getFilteredItineraryList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredItineraryList(Predicate<Itinerary> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Itinerary getCurrentItinerary() {
            throw new AssertionError("This methods should not be called.");
        }
    }

    /**
     * A Model stub that contains a itinerary in itineraryList.
     */
    private class ModelStubWithItinerary extends ModelStub {
        private final Itinerary itinerary;

        public ModelStubWithItinerary(Itinerary itinerary) {
            this.itinerary = itinerary;
        }

        @Override
        public Itinerary getCurrentItinerary() {
            return this.itinerary;
        }
    }

}