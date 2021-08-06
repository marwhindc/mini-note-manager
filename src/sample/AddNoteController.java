package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import sample.datamodel.Note;

import java.util.Optional;

public class AddNoteController {

    @FXML
    private TextField tfNewItem;


    public Note getNewNote(){

        String noteName = tfNewItem.getText();
        if (noteName.isEmpty() || noteName.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Adding Note");
            alert.setHeaderText("Error");
            alert.setContentText("New note name should not be empty");
            Optional<ButtonType> result = alert.showAndWait();
            return null;
        }
        Note newNote = new Note(noteName);
        return  newNote;
    }
}
