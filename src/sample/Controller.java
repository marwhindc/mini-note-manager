package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import sample.datamodel.Note;
import sample.datamodel.NoteData;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane bpMain;
    @FXML
    private ComboBox<String> cbNotes;
    private NoteData data;



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
            cbNotes.getItems().add(newNote.getNoteName());
            cbNotes.setValue(newNote.getNoteName());
        }
    }
}
