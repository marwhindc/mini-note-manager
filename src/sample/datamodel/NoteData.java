package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NoteData {

    private ObservableList<Note> notes;

    public NoteData(){
        notes = FXCollections.observableArrayList();
    }

    public ObservableList<Note> getNotes(){
        return notes;
    }

    public void addNote(Note item){
        notes.add(item);
    }

    public void deleteNote(Note item){
        notes.remove(item);
    }
}
