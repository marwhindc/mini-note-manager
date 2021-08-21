package sample;

import com.sun.javafx.scene.control.skin.ContextMenuContent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

public class MainTest extends ApplicationTest{

    //Initializing controls' fx:id to String
    final String MENU_ITEM_FILE = "#miFile";
    final String MENU_ITEM_ADD = "#miAdd";
    final String MENU_ITEM_DELETE = "#miDelete";
    final String MENU_ITEM_SAVE = "#miSave";
    final String BUTTON_ADD_NOTE = "#btAddNote";
    final String BUTTON_DELETE_NOTE = "#btDeleteNote";
    final String BUTTON_SAVE_NOTE = "#btSaveNote";
    final String BUTTON_COPY_NOTE = "#btCopyNoteName";
    final String TEXT_FIELD_NEW_ITEM = "#tfNewItem";
    final String COMBO_BOX_NOTES = "#cbNotes";
    final String TEXT_AREA_NOTES = "#taNoteText";


    @Override
    public void start (Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(Main.class.getResource("main.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp () throws Exception {
        ApplicationTest.launch(Main.class);
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    public <T extends Node> T find(final String query){
        return (T)lookup(query).queryAll().iterator().next();
    }

    /**
     * Test Cases for ADDING ITEM
    */

    // TEST CASE - The product should open add item dialog when Add button is clicked
    @Test
    public void testAddItemDialog_1(){
        clickOn(BUTTON_ADD_NOTE);
        verifyThat("OK", NodeMatchers.isVisible());
        verifyThat("Cancel", NodeMatchers.isVisible());
    }

    // TEST CASE - The product should open add item dialog when Add Item menu item is clicked
    @Test
    public void testAddItemDialog_2(){
        clickOn(MENU_ITEM_FILE);
        clickOn(MENU_ITEM_ADD);
        verifyThat("OK", NodeMatchers.isVisible());
        verifyThat("Cancel", NodeMatchers.isVisible());
    }

    //TEST CASE - The product should be able to type text on text field when add item dialog is open
    @Test
    public void testAddItemDialog_3(){
        String test = "This is a test";
        clickOn(BUTTON_ADD_NOTE);
        clickOn(TEXT_FIELD_NEW_ITEM).write(test);
        verifyThat(TEXT_FIELD_NEW_ITEM, TextInputControlMatchers.hasText(test));
    }

    //TEST CASE - The product should throw an alert when it tried to add an item but no text is typed in the text field of add item dialog
    @Test
    public void testAddItemDialog_4(){
        clickOn(MENU_ITEM_FILE);
        clickOn(MENU_ITEM_ADD);
        press(KeyCode.ENTER);
        //TODO - Add test to check if current popup window is the Alert box
    }

    //TEST CASE - The product should be able to add item when add item dialog is open and text filed is not empty
    @Test
    public void testAddItemDialog_5(){
        clickOn(BUTTON_ADD_NOTE);
        clickOn(TEXT_FIELD_NEW_ITEM).write("This is a test");
        press(KeyCode.ENTER);
        ComboBox cb = find(COMBO_BOX_NOTES);
        assertEquals(1, cb.getItems().size());
    }

    //TEST CASE - The product’s text area should have String "Type your notes here..." when a new combo box item is created
    @Test
    public void testAddItemDialog_6(){
        clickOn(BUTTON_ADD_NOTE);
        clickOn(TEXT_FIELD_NEW_ITEM).write("This is a test");
        press(KeyCode.ENTER);
        TextArea ta = find(TEXT_AREA_NOTES);
        assertEquals("Type your notes here...", ta.getText());
    }

    /**
    * Test Cases for DELETE ITEM
     * Add test item if needed:
       <note>
       	<note_name>This is a test</note_name>
       	<note_text>Type your notes here...</note_text>
       </note>
    */

    //TEST CASE - The product should have Delete button disabled when combo box list is empty
    @Test
    public void testDeleteItem_1(){
        assertTrue(find(BUTTON_DELETE_NOTE).isDisabled());
    }

    //TEST CASE - The product should have Delete menu item disabled when combo box list is empty
    @Test
    public void testDeleteItem_2(){
        assertTrue(find(MENU_ITEM_DELETE).isDisabled());
        //TODO - getting NoSuchElementException when finding Delete Menu Item
    }

    //TEST CASE - The product should have Delete button enabled when combo box has at least one item (Add item to notes.fxml before testing)
    @Test
    public void testDeleteItem_3(){
        assertFalse(find(BUTTON_DELETE_NOTE).isDisabled());
    }

    //TEST CASE - The product should have Delete menu item enabled when combo box has at least one item
    @Test
    public void testDeleteItem_4(){
        assertFalse(find(MENU_ITEM_DELETE).isDisabled());
        //TODO - getting NoSuchElementException when finding Delete Menu Item
    }

    //TEST CASE - The product should throw an alert when Delete button is clicked
    @Test
    public void testDeleteItem_5(){
        //TODO - Add test to check if current popup window is the Alert box
    }

    //TEST CASE - The product should throw an alert when Delete button is clicked
    @Test
    public void testDeleteItem_6(){
        //TODO - Add test to check if current popup window is the Alert box
    }

    //TEST CASE - The product should delete/remove selected combo box item when Ok is clicked from the alert (w/ one item present)
    @Test
    public void testDeleteItem_7(){
        clickOn(BUTTON_DELETE_NOTE);
        press(KeyCode.ENTER);
        ComboBox cb = find(COMBO_BOX_NOTES);
        assertEquals(0, cb.getItems().size());
    }

    // w/ at least 2 items present
    @Test
    public void testDeleteItem_8(){
        clickOn(BUTTON_DELETE_NOTE);
        press(KeyCode.ENTER);
        ComboBox cb = find(COMBO_BOX_NOTES);
        assertEquals(1, cb.getItems().size());
    }
}