package sample.datamodel;


import javafx.beans.property.SimpleStringProperty;

public class Note {

    private SimpleStringProperty noteName = new SimpleStringProperty("");
    private SimpleStringProperty noteText = new SimpleStringProperty("");

    public Note(String noteName){

        this.noteName.set(noteName);
        this.noteText.set("Type your notes here...");
    }

    public Note(){
    }

    public String getNoteName() {
        return noteName.get();
    }

    public void setNoteName(String noteName) {
        this.noteName.set(noteName);
    }

    public String getNoteText() {
        return noteText.get();
    }

    public void setNoteText(String noteText) {
        this.noteText.set(noteText);
    }

    @Override
    public String toString() {
        return this.getNoteName();
    }
}

