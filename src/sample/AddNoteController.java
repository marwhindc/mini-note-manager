package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.datamodel.Note;

public class AddNoteController {

    @FXML
    private TextField tfNewItem;


    public Note getNewNote(){

        String noteName = tfNewItem.getText();
        Note newNote = new Note(noteName);
        return  newNote;
    }
}
