package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class NoteData {

    private ObservableList<Note> notes;

    public NoteData(){
        notes = FXCollections.observableArrayList();
    }

    public ObservableList<Note> getNotes(){
        return notes;
    }

    public void addNote(ComboBox<String> comboBox, Note item){
        notes.add(item);
        comboBox.getItems().add(item.getNoteName());
        comboBox.setValue(item.getNoteName());

    }

    public void deleteNote(Note item){
        notes.remove(item);
    }

    public Note getNote(String name){


        for (Note n : notes) {
            if (n.getNoteName().equals(name)) {
                return n;
            }
        }

        return null;
    }
}
