package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.itinerary.Itinerary;

/**
 * Represents the in-memory model of the TrackPad data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AttractionList attractionList;
    private final ItineraryList itineraryList;
    private final UserPrefs userPrefs;
    private final FilteredList<Attraction> filteredAttractions;
    private final FilteredList<Itinerary> filteredItineraries;

    /**
     * Initializes a ModelManager with the given attractionList, itineraryList and userPrefs.
     */
    public ModelManager(ReadOnlyAttractionList attractionList, ReadOnlyItineraryList itineraryList,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(attractionList, itineraryList, userPrefs);

        logger.fine("Initializing with attractionList: " + attractionList + " itineraryList: "
                + itineraryList + " and user prefs " + userPrefs);

        this.attractionList = new AttractionList(attractionList);
        this.itineraryList = new ItineraryList(itineraryList);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredAttractions = new FilteredList<>(this.attractionList.getAttractionList());
        filteredItineraries = new FilteredList<>(this.itineraryList.getItineraryList());
    }

    public ModelManager() {
        this(new AttractionList(), new ItineraryList(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAttractionListFilePath() {
        return userPrefs.getAttractionListFilePath();
    }

    @Override
    public void setAttractionListFilePath(Path attractionListFilePath) {
        requireNonNull(attractionListFilePath);
        userPrefs.setAttractionListFilePath(attractionListFilePath);
    }

    @Override
    public Path getItineraryListFilePath() {
        return userPrefs.getItineraryListFilePath();
    }

    @Override
    public void setItineraryListFilePath(Path itineraryListFilePath) {
        requireNonNull(itineraryListFilePath);
        userPrefs.setItineraryListFilePath(itineraryListFilePath);
    }

    //=========== AttractionList ================================================================================

    public void setAttractionList(ReadOnlyAttractionList attractionList) {
        this.attractionList.resetData(attractionList);
    }

    @Override
    public ReadOnlyAttractionList getAttractionList() {
        return attractionList;
    }

    @Override
    public boolean hasAttraction(Attraction attraction) {
        requireNonNull(attraction);
        return attractionList.hasAttraction(attraction);
    }

    @Override
    public void deleteAttraction(Attraction target) {
        attractionList.removeAttraction(target);
    }

    @Override
    public void addAttraction(Attraction attraction) {
        attractionList.addAttraction(attraction);
        updateFilteredAttractionList(PREDICATE_SHOW_ALL_ATTRACTIONS);
    }

    @Override
    public void setAttraction(Attraction target, Attraction editedAttraction) {
        requireAllNonNull(target, editedAttraction);

        attractionList.setAttraction(target, editedAttraction);
    }

    //=========== Filtered Attraction List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Attraction} backed by the internal list of
     * {@code versionedTrackPad}
     */
    @Override
    public ObservableList<Attraction> getFilteredAttractionList() {
        return filteredAttractions;
    }

    @Override
    public void updateFilteredAttractionList(Predicate<Attraction> predicate) {
        requireNonNull(predicate);
        filteredAttractions.setPredicate(predicate);
    }

    //=========== ItineraryList ================================================================================

    @Override
    public void setItineraryList(ReadOnlyItineraryList itineraryList) {
        this.itineraryList.resetData(itineraryList);
    }

    @Override
    public ReadOnlyItineraryList getItineraryList() {
        return itineraryList;
    }

    @Override
    public boolean hasItinerary(Itinerary itinerary) {
        requireNonNull(itinerary);
        return itineraryList.hasItinerary(itinerary);
    }

    @Override
    public void deleteItinerary(Itinerary target) {
        itineraryList.removeItinerary(target);
    }

    @Override
    public void addItinerary(Itinerary itinerary) {
        itineraryList.addItinerary(itinerary);
        updateFilteredItineraryList(PREDICATE_SHOW_ALL_ITINERARIES);
    }

    @Override
    public void setItinerary(Itinerary target, Itinerary editedItinerary) {
        requireAllNonNull(target, editedItinerary);

        itineraryList.setItinerary(target, editedItinerary);
    }

    @Override
    public Itinerary getCurrentItinerary() {
        return itineraryList.getCurrentItinerary();
    }

    //=========== Filtered Itinerary List Accessors ==============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Itinerary} backed by the internal list of
     * {@code versionedTrackPad}
     */
    @Override
    public ObservableList<Itinerary> getFilteredItineraryList() {
        return filteredItineraries;
    }

    @Override
    public void updateFilteredItineraryList(Predicate<Itinerary> predicate) {
        requireNonNull(predicate);
        filteredItineraries.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return attractionList.equals(other.attractionList)
                && userPrefs.equals(other.userPrefs)
                && filteredAttractions.equals(other.filteredAttractions)
                && filteredItineraries.equals(other.filteredItineraries);
    }

}
