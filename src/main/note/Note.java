package main.note;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Rect;
import java.util.ArrayList;


/**
 * Note object that will control a single instance of a Note
 */
public class Note extends Stage {

    /** List of all notes within the application **/
    private static ArrayList<Note> notes
        = new ArrayList<Note>();

    /** Default content used when creating a new note **/
    private static final String DEFAULT_CONTENT
        = "";

    /** Default note properties used when creating a new note **/
    private static final Properties DEFAULT_NOTE_PROPERTIES
            = Properties.NONE;

    private static final Rect DEFAULT_DIMENSIONS
            = new Rect(25, 25, 250, 250);

    /** Position of the note **/
    private Rect _dimensions;

    /** ID for this note **/
    private int _noteID;

    /** Contents of the note **/
    private String _content;

    /** Special properties of the note **/
    private Properties _properties;

    /** Stage of this note **/
    private Stage _stage;

    /** Scene of this note **/
    private Scene _scene;

    /** Overall app pane that contains the entire application **/
    private StackPane _appPane;

    /** Pane that contains the controls of the note **/
    private BorderPane _controlPane;

    /** Pane that contains the content of the note **/
    private BorderPane _contentPane;

    /** Controls associated with the Note **/
    private Controls _controls;

    /**
     * Creates a new Note object
     * @param content       Content of the note
     * @param properties    Special properties for this note
     */
    public Note(Rect dimensions, String content, Properties properties) {
        _dimensions = dimensions;
        _content = content;
        _properties = properties;

        SetUniqueNoteID();
        CreateStage();

        notes.add(this);
    }

    /**
     * Creates a new Note object with empty content
     * @param dimensions Dimensions of the Note
     * @param properties Properties of the Note
     */
    public Note(Rect dimensions, Properties properties) {
        this(dimensions, DEFAULT_CONTENT, properties);
    }

    /**
     * Creates a new Note object with empty content and default properties
     * @param dimensions Dimensions of the Note
     */
    public Note(Rect dimensions) {
        this(dimensions, DEFAULT_CONTENT, DEFAULT_NOTE_PROPERTIES);
    }

    /**
     * Creates a new Note object using all defaults
     */
    public Note() {
        this(DEFAULT_DIMENSIONS, DEFAULT_CONTENT, DEFAULT_NOTE_PROPERTIES);
    }


    /**
     * Creates the stage and sets initial parameters
     */
    public void CreateStage() {
        _stage = new Stage();

        _appPane = new StackPane();
        _contentPane = new BorderPane();
        _controlPane = new BorderPane();

        _scene = new Scene(_appPane, _dimensions.Width, _dimensions.Height, Color.TRANSPARENT);
        _stage.initStyle(StageStyle.TRANSPARENT);

        _scene.getStylesheets().add("main/note/note.css");
        _stage.setScene(_scene);

        _stage.setTitle("Sticky Note (" + (_noteID + 1) + ")");

        _stage.show();
        _stage.setAlwaysOnTop(true);

        _stage.setOnCloseRequest(windowEvent -> Close());

        ApplyNoteDimensions();
        CreateContent();
    }

    /**
     * Creates the content for the application
     */
    public void CreateContent() {
        _appPane.getChildren().add(_contentPane);
        _contentPane.setTop(_controlPane);

        _appPane.setId("app-pane");
        _contentPane.setId("content-pane");
        _controlPane.setId("control-pane");

        _controls = new Controls(this, new Button("×"), new Button("+"), new TextArea());

        _controlPane.setLeft(_controls.create);
        _controlPane.setRight(_controls.close);
        _contentPane.setCenter(_controls.text);
    }

    /**
     * Translates this note to be next to a given rect position. Will adjust as needed if there is not enough room.
     * @param position Position to move next to
     */
    public void TranslateNextTo(Rect position) {
        Rectangle2D r = Screen.getPrimary().getVisualBounds();
        Rect nRect = new Rect(position);

        nRect.PositionX += position.Width + 14;

        if (nRect.PositionX + position.Width + 14 > r.getMaxX()) {
            if (nRect.PositionX + (position.Width * 0.15) > r.getMaxX()) {
                nRect.PositionX = position.PositionX - position.Width - 14;
            } else {
                nRect.PositionX = r.getMaxX() - position.Width - 14;
            }
        }

        _dimensions = nRect;
        ApplyNoteDimensions();
    }

    /**
     * Applies the current dimensions set on the Note to reflect the displayed Note
     */
    public void ApplyNoteDimensions() {
        _stage.setX(_dimensions.PositionX);
        _stage.setY(_dimensions.PositionY);
        _stage.setWidth(_dimensions.Width);
        _stage.setHeight(_dimensions.Height);
    }

    /**
     * Closes this note window
     */
    public void Close() {
        notes.remove(this);
        _stage.close();
    }

    /**
     * Sets a unique note name for this note
     */
    private void SetUniqueNoteID() {
        int uid = 0;

        for (Note note : notes) {
            if (note.get_noteID() == uid) uid++;
            else break;
        }

        _noteID = uid;
    }

    /**
     * Gets the app pane of this Note
     * @return StackPane for the app section
     */
    public StackPane get_AppPane() {
        return _appPane;
    }

    /**
     * Gets the dimensions of this Note
     * @return Dimensions
     */
    public Rect get_Dimensions() {
        return _dimensions;
    }

    /**
     * Gets the Note ID of this note
     * @return Note ID
     */
    public int get_noteID() {
        return _noteID;
    }

    /**
     * Gets the Stage for this note
     * @return Stage of this note
     */
    public Stage get_Stage() {
        return _stage;
    }

    /**
     * Gets the control Pane for this note
     * @return Pane for the control section
     */
    public Pane get_ControlPane() {
        return _controlPane;
    }

    /**
     * Gets the content Pane for this note
     * @return Pane for the content section
     */
    public Pane get_ContentPane() {
        return _contentPane;
    }
}
