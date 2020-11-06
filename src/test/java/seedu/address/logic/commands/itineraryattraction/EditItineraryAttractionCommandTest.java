package seedu.address.logic.commands.itineraryattraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ITINERARY_DAY;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_START_TIME;
import static seedu.address.commons.core.Messages.MESSAGE_ITINERARY_NOT_SELECTED;
import static seedu.address.commons.core.Messages.MESSAGE_TIMING_CLASH;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.itineraryattraction.EditItineraryAttractionCommand.EditItineraryAttractionDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAttractionList;
import seedu.address.model.ReadOnlyItineraryAttractionList;
import seedu.address.model.ReadOnlyItineraryList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.itinerary.Day;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryAttraction;
import seedu.address.testutil.AttractionBuilder;
import seedu.address.testutil.EditItineraryAttractionDescriptorBuilder;
import seedu.address.testutil.ItineraryAttractionBuilder;
import seedu.address.testutil.ItineraryBuilder;

public class EditItineraryAttractionCommandTest {
    private final EditItineraryAttractionDescriptor descriptor = new EditItineraryAttractionDescriptorBuilder().build();

    @Test
    public void execute_itineraryNotSelected_throwsCommandException() {
        ModelStubNoItinerarySelected model = new ModelStubNoItinerarySelected();
        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_FIRST, INDEX_FIRST, descriptor);

        assertThrows(CommandException.class, MESSAGE_ITINERARY_NOT_SELECTED, () -> editIaCommand.execute(model));
    }

    @Test
    public void execute_invalidDay_throwsCommandException() {
        Itinerary itinerary = new ItineraryBuilder().build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(itinerary);
        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_FIRST, Index.fromZeroBased(4), descriptor);

        assertThrows(CommandException.class, MESSAGE_INVALID_ITINERARY_DAY, ()
            -> editIaCommand.execute(model));

    }

    @Test
    public void execute_invalidIaIndex_throwsCommandException() {
        Itinerary itinerary = new ItineraryBuilder().withItineraryAttraction(
                new ItineraryAttractionBuilder().build(), INDEX_FIRST).build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(itinerary);
        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_SECOND, INDEX_FIRST, descriptor);

        // tests with 1 ItineraryAttraction in a day
        assertThrows(CommandException.class, MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX, ()
            -> editIaCommand.execute(model));


        Itinerary itinerary2 = new ItineraryBuilder().build();
        ModelStubWithItinerarySelected model2 = new ModelStubWithItinerarySelected(itinerary2);
        EditItineraryAttractionCommand editIaCommand2 = new EditItineraryAttractionCommand(
                INDEX_FIRST, INDEX_FIRST, descriptor);

        // tests with no ItineraryAttraction in a day
        assertThrows(CommandException.class, MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX, ()
            -> editIaCommand2.execute(model2));
    }

    @Test
    public void execute_duplicateItineraryAttraction_throwsCommandException() {
        Attraction attraction = new AttractionBuilder().build();
        ItineraryAttraction itineraryAttraction = new ItineraryAttractionBuilder().withAttraction(attraction).build();
        Itinerary itinerary = new ItineraryBuilder().withItineraryAttraction(itineraryAttraction, INDEX_FIRST).build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(itinerary);
        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_FIRST, INDEX_FIRST, descriptor);

        assertThrows(CommandException.class,
                EditItineraryAttractionCommand.MESSAGE_FIELD_NOT_CHANGED, () -> editIaCommand.execute(model));
    }

    @Test
    public void execute_invalidStartTime_throwsCommandException() {
        // same start time and end time
        ItineraryAttraction ia = new ItineraryAttractionBuilder().build();
        Itinerary itinerary = new ItineraryBuilder().withItineraryAttraction(ia, INDEX_THIRD).build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(itinerary);
        EditItineraryAttractionDescriptor descriptor = new EditItineraryAttractionDescriptorBuilder()
                .withStartTime("1000").withEndTime("1000").build();

        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_FIRST, INDEX_THIRD, descriptor);

        assertThrows(CommandException.class, MESSAGE_INVALID_START_TIME, ()
            -> editIaCommand.execute(model));
    }

    @Test
    public void execute_invalidStartTime2_throwsCommandException() {
        // start time later than end time
        ItineraryAttraction ia = new ItineraryAttractionBuilder().build();
        Itinerary itinerary = new ItineraryBuilder().withItineraryAttraction(ia, INDEX_THIRD).build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(itinerary);
        EditItineraryAttractionDescriptor descriptor = new EditItineraryAttractionDescriptorBuilder()
                .withStartTime("1001").withEndTime("1000").build();

        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_FIRST, INDEX_THIRD, descriptor);

        assertThrows(CommandException.class, MESSAGE_INVALID_START_TIME, ()
            -> editIaCommand.execute(model));
    }

    @Test
    public void execute_timingClash_throwsCommandException() {
        // edits the end time of ia, end time conflicts with the start time of ia2
        ItineraryAttraction ia = new ItineraryAttractionBuilder().withStartTime("0900").withEndTime("1000").build();
        ItineraryAttraction ia2 = new ItineraryAttractionBuilder().withStartTime("1000").withEndTime("1200").build();
        Itinerary itinerary = new ItineraryBuilder().withItineraryAttraction(ia, INDEX_THIRD)
                .withItineraryAttraction(ia2, INDEX_THIRD).build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(itinerary);
        EditItineraryAttractionDescriptor descriptor = new EditItineraryAttractionDescriptorBuilder()
                .withEndTime("1001").build();
        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_FIRST, INDEX_THIRD, descriptor);

        assertThrows(CommandException.class, MESSAGE_TIMING_CLASH, ()
            -> editIaCommand.execute(model));
    }

    @Test
    public void execute_timingClash2_throwsCommandException() {
        // used new test cases as the variables used in lambda must be effectively final
        // edits the start time of ia2, end time conflicts with the start time of ia
        ItineraryAttraction ia = new ItineraryAttractionBuilder().withStartTime("0900").withEndTime("1000").build();
        ItineraryAttraction ia2 = new ItineraryAttractionBuilder().withStartTime("1000").withEndTime("1200").build();
        Itinerary itinerary = new ItineraryBuilder().withItineraryAttraction(ia, INDEX_THIRD)
                .withItineraryAttraction(ia2, INDEX_THIRD).build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(itinerary);
        EditItineraryAttractionDescriptor descriptor = new EditItineraryAttractionDescriptorBuilder()
                .withStartTime("0959").build();
        EditItineraryAttractionCommand editIaCommand = new EditItineraryAttractionCommand(
                INDEX_SECOND, INDEX_THIRD, descriptor);

        assertThrows(CommandException.class, MESSAGE_TIMING_CLASH, ()
            -> editIaCommand.execute(model));
    }

    @Test
    public void execute_fieldSpecified_success() throws Exception {
        Index index = INDEX_FIRST;
        Index day = INDEX_FIRST;

        // to test
        EditItineraryAttractionDescriptor tDescriptor = new EditItineraryAttractionDescriptorBuilder()
                .withEndTime("2359").build();
        ItineraryAttraction validIa = new ItineraryAttractionBuilder().build();
        ModelStubWithItinerarySelected model = new ModelStubWithItinerarySelected(new ItineraryBuilder()
                .withItineraryAttraction(validIa, day).build());

        CommandResult commandResult = new EditItineraryAttractionCommand(index, day, tDescriptor).execute(model);

        // expected
        ItineraryAttraction eItineraryAttraction = new ItineraryAttractionBuilder().withEndTime("2359").build();

        assertEquals(String.format(EditItineraryAttractionCommand.MESSAGE_EDIT_ATTRACTION_SUCCESS,
                eItineraryAttraction), commandResult.getFeedbackToUser());
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
        public void setCurrentItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Itinerary getCurrentItinerary() {
            throw new AssertionError("This methods should not be called.");
        }

        @Override
        public ReadOnlyItineraryAttractionList getItineraryAttractionList() {
            throw new AssertionError("This methods should not be called.");
        }

        @Override
        public ObservableList<ItineraryAttraction> getFilteredItineraryAttractionList() {
            throw new AssertionError("This methods should not be called.");
        }
    }

    /**
     * A Model stub that returns no itinerary selected.
     */
    private class ModelStubNoItinerarySelected extends ModelStub {
        @Override
        public Itinerary getCurrentItinerary() {
            return null;
        }
    }

    /**
     * A Model stub that returns a itinerary selected.
     */
    private class ModelStubWithItinerarySelected extends ModelStub {
        private final Itinerary itinerary;

        public ModelStubWithItinerarySelected(Itinerary itinerary) {
            this.itinerary = itinerary;
        }

        @Override
        public Itinerary getCurrentItinerary() {
            return itinerary;
        }

        private Day getDay(Index index) {
            return itinerary.getDay(index);
        }
    }
}
