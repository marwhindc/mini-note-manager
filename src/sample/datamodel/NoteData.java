package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class NoteData {

    private static final String NOTES_FILE = "notes.xml";

    private static final String NOTE = "note";
    private static final String NOTE_NAME = "note_name";
    private static final String NOTE_TEXT = "note_text";

    private ObservableList<Note> notes;

    public NoteData(){
        notes = FXCollections.observableArrayList();
    }

    public ObservableList<Note> getNotes(){
        return notes;
    }

    public void addNoteItem(ComboBox<Note> comboBox, Note item){
        notes.add(item);
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



    //Handles Save and Load

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    private void saveNote(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Note note)
            throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create contact open tag
        StartElement configStartElement = eventFactory.createStartElement("",
                "", NOTE);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, NOTE_NAME, note.getNoteName());
        createNode(eventWriter, NOTE_TEXT, note.getNoteText());

        eventWriter.add(eventFactory.createEndElement("", "", NOTE));
        eventWriter.add(end);
    }

    public void saveNotes() {

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(new FileOutputStream(NOTES_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement notesStartElement = eventFactory.createStartElement("",
                    "", "notes");
            eventWriter.add(notesStartElement);
            eventWriter.add(end);

            for (Note note: notes) {
                saveNote(eventWriter, eventFactory, note);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "notes"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Problem with Contacts file: " + e.getMessage());
            e.printStackTrace();
        }
        catch (XMLStreamException e) {
            System.out.println("Problem writing contact: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadNotes() {
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(NOTES_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Note note = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a note item, we create a new note
                    if (startElement.getName().getLocalPart().equals(NOTE)) {
                        note = new Note();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(NOTE_NAME)) {
                            event = eventReader.nextEvent();
                            note.setNoteName(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(NOTE_TEXT)) {
                        event = eventReader.nextEvent();
                        note.setNoteText(event.asCharacters().getData());
                        continue;
                    }
                }

                // If we reach the end of a note element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(NOTE)) {
                        notes.add(note);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
