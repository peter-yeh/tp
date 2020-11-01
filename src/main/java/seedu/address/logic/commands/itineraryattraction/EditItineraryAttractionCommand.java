package seedu.address.logic.commands.itineraryattraction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_ITINERARY_NOT_SELECTED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY_VISITING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.attraction.EditAttractionCommand.EditAttractionDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attraction.Address;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.attraction.Email;
import seedu.address.model.attraction.Location;
import seedu.address.model.attraction.OpeningHours;
import seedu.address.model.attraction.Phone;
import seedu.address.model.attraction.PriceRange;
import seedu.address.model.attraction.Rating;
import seedu.address.model.attraction.Visited;
import seedu.address.model.commons.Description;
import seedu.address.model.commons.Name;
import seedu.address.model.itinerary.Day;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryAttraction;
import seedu.address.model.itinerary.ItineraryTime;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing attraction in the itinerary.
 */
public class EditItineraryAttractionCommand extends Command {

    public static final String COMMAND_WORD = "edit-itinerary-attraction";
    public static final String MESSAGE_EDIT_ATTRACTION_SUCCESS = "Edited Attraction: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_ATTRACTION = "This attraction already exists in Itinerary.";
    public static final String MESSAGE_TIMING_CLASH = "The timing clashes with another attraction in the itinerary";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the itinerary attraction "
            + "identified by the name of the itinerary attraction displayed in the itinerary"
            + "Parameters: INDEX " + PREFIX_DAY_VISITING + "DAY VISITING " + "[" + PREFIX_START_TIME + "START_TIME] "
            + "[" + PREFIX_END_TIME + "END_TIME] \n"
            + "Example: " + COMMAND_WORD + " 2 " + PREFIX_DAY_VISITING + "2 " + PREFIX_START_TIME + "10000 "
            + PREFIX_DAY_VISITING + "4";

    private final Index index;
    private final Index dayVisiting;
    private final EditItineraryAttractionDescriptor editIaDescriptor;

    /**
     * @param index            of the itinerary attraction to edit
     * @param editIaDescriptor details to edit the itinerary attraction with
     */
    public EditItineraryAttractionCommand(Index index, Index dayVisiting,
                                          EditItineraryAttractionDescriptor editIaDescriptor) {
        requireNonNull(index);
        requireNonNull(editIaDescriptor);

        this.index = index;
        this.dayVisiting = dayVisiting;
        this.editIaDescriptor = new EditItineraryAttractionDescriptor(editIaDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getCurrentItinerary() == null) {
            throw new CommandException(MESSAGE_ITINERARY_NOT_SELECTED);
        }

        Itinerary itinerary = model.getCurrentItinerary();
        Day day = itinerary.getDay(dayVisiting);
        List<ItineraryAttraction> itineraryAttractionsThatDay = day.getItineraryAttractions();

        if (index.getZeroBased() >= itineraryAttractionsThatDay.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX);
        }

        ItineraryAttraction itineraryAttractionToEdit = itineraryAttractionsThatDay.get(index.getZeroBased());
        ItineraryAttraction editedItineraryAttraction = createEditedItineraryAttraction(itineraryAttractionToEdit,
                editIaDescriptor);


        if (itineraryAttractionToEdit.equals(editedItineraryAttraction)) {
            throw new CommandException(MESSAGE_DUPLICATE_ATTRACTION);
        }

        // checks if there is a timing clash with an existing itinerary attraction
        if (itinerary.getDay(dayVisiting).hasTimingClash(editedItineraryAttraction, itineraryAttractionToEdit)) {
            throw new CommandException(MESSAGE_TIMING_CLASH);
        }

        itinerary.editItineraryAttraction(itineraryAttractionToEdit, editedItineraryAttraction, dayVisiting);

        return new CommandResult(String.format(MESSAGE_EDIT_ATTRACTION_SUCCESS, editedItineraryAttraction), true);
    }

    /**
     * Creates and returns a {@code Attraction} with the details of {@code attractionToEdit}
     * edited with {@code editItineraryAttractionDescriptor}.
     */
    private ItineraryAttraction createEditedItineraryAttraction(ItineraryAttraction itineraryAttractionToEdit,
                                                                EditItineraryAttractionDescriptor editItiAttrDesc) {
        assert itineraryAttractionToEdit != null;
        Name updatedName = editItiAttrDesc.getName().orElse(itineraryAttractionToEdit.getName());
        Phone updatedPhone = editItiAttrDesc.getPhone().orElse(itineraryAttractionToEdit.getPhone());
        Email updatedEmail = editItiAttrDesc.getEmail().orElse(itineraryAttractionToEdit.getEmail());
        Address updatedAddress = editItiAttrDesc.getAddress().orElse(itineraryAttractionToEdit.getAddress());
        Description updatedDescription = editItiAttrDesc
                .getDescription().orElse(itineraryAttractionToEdit.getDescription());
        Location updatedLocation = editItiAttrDesc.getLocation().orElse(itineraryAttractionToEdit.getLocation());
        OpeningHours updatedOpeningHours = editItiAttrDesc
                .getOpeningHours().orElse(itineraryAttractionToEdit.getOpeningHours());
        PriceRange updatedPriceRange = editItiAttrDesc
                .getPriceRange().orElse(itineraryAttractionToEdit.getPriceRange());
        Rating updatedRating = editItiAttrDesc.getRating().orElse(itineraryAttractionToEdit.getRating());
        Visited updatedVisited = editItiAttrDesc.getVisited().orElse(itineraryAttractionToEdit.getVisited());
        Set<Tag> updatedTags = editItiAttrDesc.getTags().orElse(itineraryAttractionToEdit.getTags());

        ItineraryTime startTime = editItiAttrDesc.getStartTime().orElse(itineraryAttractionToEdit.getStartTime());
        ItineraryTime endTime = editItiAttrDesc.getEndTime().orElse(itineraryAttractionToEdit.getEndTime());

        return new ItineraryAttraction(new Attraction(updatedName, updatedPhone, updatedEmail, updatedAddress,
                updatedDescription, updatedLocation, updatedOpeningHours, updatedPriceRange,
                updatedRating, updatedVisited, updatedTags), startTime, endTime);
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditItineraryAttractionCommand)) {
            return false;
        }

        // state check
        EditItineraryAttractionCommand e = (EditItineraryAttractionCommand) other;
        return index.equals(e.index)
                && editIaDescriptor.equals(e.editIaDescriptor)
                && dayVisiting.equals(e.dayVisiting);
    }

    /**
     * Stores the details to edit the attraction with. Each non-empty field value will replace the
     * corresponding field value of the attraction.
     */
    public static class EditItineraryAttractionDescriptor extends EditAttractionDescriptor {
        private ItineraryTime startTime;
        private ItineraryTime endTime;

        public EditItineraryAttractionDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditItineraryAttractionDescriptor(EditItineraryAttractionDescriptor toCopy) {
            if (toCopy.getName().isPresent()) {
                setName(toCopy.getName().get());
            }

            if (toCopy.getPhone().isPresent()) {
                setPhone(toCopy.getPhone().get());
            }

            if (toCopy.getEmail().isPresent()) {
                setEmail(toCopy.getEmail().get());
            }

            if (toCopy.getAddress().isPresent()) {
                setAddress(toCopy.getAddress().get());
            }

            if (toCopy.getDescription().isPresent()) {
                setDescription(toCopy.getDescription().get());
            }

            if (toCopy.getLocation().isPresent()) {
                setLocation(toCopy.getLocation().get());
            }

            if (toCopy.getOpeningHours().isPresent()) {
                setOpeningHours(toCopy.getOpeningHours().get());
            }

            if (toCopy.getPriceRange().isPresent()) {
                setPriceRange(toCopy.getPriceRange().get());
            }

            if (toCopy.getRating().isPresent()) {
                setRating(toCopy.getRating().get());
            }

            if (toCopy.getVisited().isPresent()) {
                setVisited(toCopy.getVisited().get());
            }

            if (toCopy.getTags().isPresent()) {
                setTags(toCopy.getTags().get());
            }

            setStartTime(toCopy.startTime);
            setEndTime(toCopy.endTime);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return super.isAnyFieldEdited() || CollectionUtil.isAnyNonNull(startTime, endTime);
        }

        public void setStartTime(ItineraryTime startTime) {
            this.startTime = startTime;
        }

        public Optional<ItineraryTime> getStartTime() {
            return Optional.ofNullable(startTime);
        }

        public void setEndTime(ItineraryTime endTime) {
            this.endTime = endTime;
        }

        public Optional<ItineraryTime> getEndTime() {
            return Optional.ofNullable(endTime);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditItineraryAttractionDescriptor)) {
                return false;
            }
            // state check
            EditItineraryAttractionDescriptor e = (EditItineraryAttractionDescriptor) other;

            if (e.getStartTime().isPresent() && !e.getStartTime().get().equals(startTime)) {
                return false;
            }

            if (e.getEndTime().isPresent() && !e.getEndTime().get().equals(endTime)) {
                return false;
            }

            return super.equals(other);
        }
    }
}
