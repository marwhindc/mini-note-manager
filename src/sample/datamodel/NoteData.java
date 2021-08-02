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

    public void addNoteItem(ComboBox<Note> comboBox, Note item){
        notes.add(item);
        comboBox.getItems().add(item);
        comboBox.setValue(item);

    }

    public void deleteNoteItem(ComboBox<Note> comboBox, Note item){
        notes.remove(item);
        comboBox.getItems().remove(item);
    }

//    public Note getNote(Note item){
//
//
//        for (Note n : notes) {
//            if (n.equals(item)) {
//                return n;
//            }
//        }
//
//        return null;
//    }
}
