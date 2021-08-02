package sample;

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




    public void initialize(){
        data = new NoteData();
        cbNotes.setOnAction(event -> updateNoteText());

        if (cbNotes.getValue() == null) {
            taNoteText.setDisable(true);
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
            data.addNoteItem(cbNotes, newNote);
            if (taNoteText.isDisabled()) {
                taNoteText.setDisable(false);
            }
        }
    }

    //Gets typed text from Text Area and add it to existing Note item
    @FXML
    public void saveNoteText(){

        cbNotes.getSelectionModel().getSelectedItem().setNoteText(taNoteText.getText());

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
        data.deleteNoteItem(cbNotes,currentNote);

        //WIP - setting the index to zero every time an item is deleted
        //Issue - when first item is deleted, it doesn't setValue to next item
        if (cbNotes.getItems().size() > 0) {
        cbNotes.setValue(cbNotes.getItems().get(0));
        updateNoteText();
        }

    }

    //Copies text for current Combobox value for easy use
    @FXML
    public void copyNoteItem(){

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(cbNotes.getSelectionModel().getSelectedItem().getNoteName());
        clipboard.setContent(content);
    }
}
