package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import sample.datamodel.Note;
import sample.datamodel.NoteData;

import java.io.IOException;
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



    public void initialize(){
        data = new NoteData();
        data.loadNotes();
        cbNotes.setItems(data.getNotes());

        if (cbNotes.getItems().size() > 0) {
            cbNotes.setValue(cbNotes.getItems().get(0));
            updateNoteText();
        }

        cbNotes.setOnAction(event -> updateNoteText());

        if (cbNotes.getValue() == null) {
            handleButtonProperty(true);
        } else taNoteText.setDisable(false);

    }

    //Add item dialog opens when user clicks Add button beside CheckBox
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

    //Gets typed text from Text Area and add it to existing Note item
    @FXML
    public void saveNoteText(){

        String noteText = taNoteText.getText();
        if (noteText.isEmpty()) {
            cbNotes.getSelectionModel().getSelectedItem().setNoteText("Type your notes here...");
        } else cbNotes.getSelectionModel().getSelectedItem().setNoteText(noteText);
        data.saveNotes();
    }

    //Updates Text Area when Note Item is selected
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

    //Remove Combobox item when delete button is pressed
    @FXML
    public void removeNoteItem(){

        Note currentNote = cbNotes.getSelectionModel().getSelectedItem();

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
        data.saveNotes();
    }

    //Copies text for current Combobox value for easy use
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

    //Handles logic for when buttons need to be enabled or disabled for UX purposes
    @FXML
    private void handleButtonProperty(boolean isDisabled){

        taNoteText.setDisable(isDisabled);
        btDeleteNote.setDisable(isDisabled);
        btCopyNoteName.setDisable(isDisabled);
        btSaveNote.setDisable(isDisabled);
    }
}
