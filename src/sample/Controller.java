package sample;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import sample.datamodel.Note;
import sample.datamodel.NoteData;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane bpMain;
    @FXML
    private ComboBox<Note> cbNotes;
    @FXML
    private TextArea taNoteText;
    private NoteData data;
    @FXML
    private Button btDeleteNote;
    @FXML
    private Button btCopyNoteName;
    @FXML
    private Button btSaveNote;
    @FXML
    private MenuItem miSave;
    @FXML
    private MenuItem miDelete;
    @FXML
    private MenuItem miAdd;
    @FXML
    private Label lbLastSaved;
    private PauseTransition hideLabel;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
    private final LocalDateTime now = LocalDateTime.now();

    @FXML
    public void initialize(){
        data = new NoteData();
        data.loadNotes();

        //Populate Combobox with items from load
        cbNotes.setItems(data.getNotes());
        if (cbNotes.getItems().size() > 0) {
            cbNotes.setValue(cbNotes.getItems().get(0));
            updateNoteText();
        }

        // Updates text area when new item is selected
        cbNotes.setOnAction(event -> updateNoteText());

        //Checks if changes were made to text are. Will tell user if not yet saved.
        taNoteText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

                try {
                    if (!cbNotes.getSelectionModel().getSelectedItem().getNoteText().equals(newValue)) {
                        lbLastSaved.setVisible(true);
                        lbLastSaved.setText("Changes not yet saved");
                    } else lbLastSaved.setVisible(false);
                } catch (NullPointerException e) {
                    return;
                }
            }
        });

        if (cbNotes.getValue() == null) {
            handleButtonProperty(true);
        } else taNoteText.setDisable(false);

        //Event handler for Label
        hideLabel = new PauseTransition(
                Duration.seconds(5)
        );
        hideLabel.setOnFinished(
                event -> lbLastSaved.setVisible(false)
        );

        //Keyboard shortcuts
        miSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        miDelete.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        miAdd.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+A"));
    }

    //Add Item dialog opens when user clicks "Add" button
    @FXML
    public void showAddItemDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(bpMain.getScene().getWindow());
        dialog.setTitle("Add New Contact");
        dialog.setHeaderText("Enter name of new Note");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addnotedialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Unable to load dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        //Add entered item in dialog to ComboBox list
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddNoteController addNoteController = fxmlLoader.getController();
            Note newNote = addNoteController.getNewNote();

            if (newNote != null) {
                data.addNoteItem(cbNotes, newNote);
                data.saveNotes();
                if (taNoteText.isDisabled()) {
                    handleButtonProperty(false);
                }
            }
        }
    }

    //Gets String value from TextArea and saves it to existing Note item
    @FXML
    public void saveNoteText(){
        Note currentNote = cbNotes.getSelectionModel().getSelectedItem();
        String noteText = taNoteText.getText();

        if (noteText.isEmpty()) {
            currentNote.setNoteText("Type your notes here...");
        } else currentNote.setNoteText(noteText);

        lbLastSaved.setVisible(true);
        lbLastSaved.setText("Last saved: " + dtf.format(now));
        hideLabel.play();
        data.saveNotes();
    }

    //Updates TextArea when a Combobox Item is selected
    @FXML
    public void updateNoteText(){

        //try-catch to check for deleted item if it's the first item
        try {
            if (cbNotes.getItems().size() > 0) {
                String savedText = cbNotes.getSelectionModel().getSelectedItem().getNoteText();
                taNoteText.setText(savedText);
            } else taNoteText.clear();
        } catch (NullPointerException e) {
            return;
        }



    }

    //Delete Combobox Item when "Delete" button is clicked
    @FXML
    public void removeNoteItem(){

        Note currentNote = cbNotes.getSelectionModel().getSelectedItem();


        //Shows alert when user attempts to delete an item
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Note");
        alert.setHeaderText("Deleting '" + currentNote.getNoteName() + "'");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            data.deleteNoteItem(cbNotes,currentNote);
        }

        //WIP - setting the index to zero every time an item is deleted
        //Issue - when first item is deleted, it doesn't setValue to next item
        if (cbNotes.getItems().size() > 0) {
        cbNotes.setValue(cbNotes.getItems().get(0));
        updateNoteText();
        } else {
            handleButtonProperty(true);
        }
        lbLastSaved.setVisible(false);
        data.saveNotes();
    }

    //Copies name of current Combobox Item
    @FXML
    public void copyNoteItem(){

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(cbNotes.getSelectionModel().getSelectedItem().getNoteName());
        clipboard.setContent(content);
    }

    @FXML
    public void handleExit(){

        Platform.exit();
    }

    //Visits Github page when "About" Menu Item is clicked
    @FXML
    public void handleAbout() throws URISyntaxException, IOException {

        Desktop.getDesktop().browse(new URI("https://github.com/marwhindc/mini-note-manager"));
    }

    //Handles button state to avoid exceptions
    @FXML
    private void handleButtonProperty(boolean isDisabled){

        taNoteText.setDisable(isDisabled);
        btDeleteNote.setDisable(isDisabled);
        btCopyNoteName.setDisable(isDisabled);
        btSaveNote.setDisable(isDisabled);
        miDelete.setDisable(isDisabled);
        miSave.setDisable(isDisabled);
    }
}
