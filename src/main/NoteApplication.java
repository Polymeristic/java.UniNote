package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.note.Note;



/**
 * Primary controller for initiating the Notes application
 */
public class NoteApplication extends Application {

    /** Current note application **/
    private static NoteApplication current;

    /**
     * Class initializer
     */
    public NoteApplication() {
        current = this;
    }

    @Override
    public void start(Stage stage) {
        new Note();
    }

    /** Gets the currently running note application **/
    public static NoteApplication get_CurrentNoteApplication() {
        return current;
    }
}
