package main.note;

import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.Rect;

import javax.sound.sampled.Clip;


/**
 * Manages events and states related to a Note's controls
 */
public class Controls {
    /** Minimum width of the note **/
    public static final double MIN_WIDTH = 200;

    /** Maximum width of the note **/
    public static final double MIN_HEIGHT = 150;

    /** Close button for a Note **/
    public Button close;

    /** Create button for a Note **/
    public Button create;

    /** Text area for a Note **/
    public TextArea text;

    /** Parent note **/
    private Note _parent;

    /** Stage drag x-offset used when dragging the stage **/
    private double _stageDragOffsetX
        = 0;

    /** Stage drag y-offset used when dragging the stage **/
    private double _stageDragOffsetY
        = 0;

    /** Stage size x-offset used when resizing the stage **/
    private double _stageResizeOffsetX
            = 0;

    /** Stage size y-offset used when resizing the stage **/
    private double _stageResizeOffsetY
            = 0;

    /** Stage drag x-offset used when resizing the stage **/
    private double _stageResizePositionOffsetX
            = 0;

    /** Stage drag y-offset used when resizing the stage **/
    private double _stageResizePositionOffsetY
            = 0;

    /** Margin to use for the resize trigger hooks **/
    private int _resizeMargin
        = 5;

    /** If the left horizontal resize state is set **/
    private boolean _resizeLeftH
        = false;

    /** If the right horizontal resize state is set **/
    private boolean _resizeRightH
        = false;

    /** If the top vertical resize state is set **/
    private boolean _resizeTopV
        = false;

    /** If the bottom vertical resize state is set **/
    private boolean _resizeBottomV
        = false;

    /** Initial drag state when dragging the window **/
    private Rect _initDragState;

    /** Context menu for the control pane **/
    private ContextMenu _controlsContextMenu;

    /** About button for the context menu **/
    private MenuItem _controlsContextMenuPaste;

    /** About button for the context menu **/
    private MenuItem _controlsContextMenuCut;

    /** About button for the context menu **/
    private MenuItem _controlsContextMenuCopy;

    /** About button for the context menu **/
    private MenuItem _controlsContextMenuAbout;

    /** About button for the context menu **/
    private MenuItem _controlsContextMenuExit;

    /** Theme controller **/
    private ThemeControls _theme;


    /** Creates a new Controls object **/
    public Controls(Note parent, Button close, Button create, TextArea text) {
        this._parent = parent;
        this.close = close;
        this.create = create;
        this.text = text;

        _theme = new ThemeControls(parent, new Button("A"), new Button("B"), new Button("C"));

        text.setWrapText(true);

        close.setId("button-close");
        create.setId("button-create");
        text.setId("textarea-main");

        create.setOnAction(actionEvent -> {
            Note n = new Note(_parent.get_Dimensions(), "", Properties.NONE, _parent.get_Color());
            n.TranslateNextTo(_parent.get_Dimensions());
        });

        close.setOnAction(actionEvent -> _parent.Close());

        SetWindowScaleEvent();
        SetTextareaContextMenu();
        SetStageDragEvent();
    }

    /**
     * Sets the scaling events to scale the window
     */
    public void SetWindowScaleEvent() {
        _parent.get_AppPane().setOnMousePressed(mouseEvent -> {
            _initDragState = _parent.get_Dimensions();

            _stageResizePositionOffsetX = _parent.get_Stage().getX() - mouseEvent.getScreenX();
            _stageResizePositionOffsetY = _parent.get_Stage().getY() - mouseEvent.getScreenY();

            _stageResizeOffsetX = mouseEvent.getScreenX() + _parent.get_Dimensions().Width;
            _stageResizeOffsetY = mouseEvent.getScreenY() + _parent.get_Dimensions().Height;
        });

        _parent.get_ButtonRightTile().setOnMouseEntered(mouseEvent -> _parent.get_Stage().getScene().setCursor(Cursor.DEFAULT));

        create.setOnMouseEntered(mouseEvent -> _parent.get_Stage().getScene().setCursor(Cursor.DEFAULT));

        _parent.get_AppPane().setOnMouseDragged(mouseEvent -> {
            if (_resizeRightH) {
                _parent.get_Dimensions().Width = mouseEvent.getX();
                if (_parent.get_Dimensions().Width < MIN_WIDTH) _parent.get_Dimensions().Width = MIN_WIDTH;
            }

            if (_resizeBottomV) {
                _parent.get_Dimensions().Height = mouseEvent.getY();
                if (_parent.get_Dimensions().Height < MIN_HEIGHT) _parent.get_Dimensions().Height = MIN_HEIGHT;
            }

            if (_resizeLeftH) {
                _parent.get_Dimensions().Width = _stageResizeOffsetX - mouseEvent.getScreenX();
                _parent.get_Dimensions().PositionX = mouseEvent.getScreenX() + _stageResizePositionOffsetX;

                if (_parent.get_Dimensions().Width < MIN_WIDTH) _parent.get_Dimensions().Width = MIN_WIDTH;
            }

            if (_resizeTopV) {
                _parent.get_Dimensions().Height = _stageResizeOffsetY - mouseEvent.getScreenY();
                _parent.get_Dimensions().PositionY = mouseEvent.getScreenY() + _stageResizePositionOffsetY;

                if (_parent.get_Dimensions().Height < MIN_HEIGHT) _parent.get_Dimensions().Height = MIN_HEIGHT;
            }

            _parent.ApplyNoteDimensions();
        });

        _parent.get_AppPane().setOnMouseMoved(mouseEvent -> {
            _resizeLeftH = mouseEvent.getX() < _resizeMargin;
            _resizeRightH = mouseEvent.getX() > _parent.get_Dimensions().Width - _resizeMargin;
            _resizeTopV = mouseEvent.getY() < _resizeMargin;
            _resizeBottomV = mouseEvent.getY() > _parent.get_Dimensions().Height - _resizeMargin;

            _parent.get_Stage().getScene().setCursor(Cursor.DEFAULT);

            if (_resizeTopV || _resizeBottomV) _parent.get_Stage().getScene().setCursor(Cursor.V_RESIZE);

            if (_resizeLeftH) {
                _parent.get_Stage().getScene().setCursor(Cursor.H_RESIZE);

                if (_resizeTopV) _parent.get_Stage().getScene().setCursor(Cursor.NW_RESIZE);
                if (_resizeBottomV) _parent.get_Stage().getScene().setCursor(Cursor.SW_RESIZE);
            }

            if (_resizeRightH) {
                _parent.get_Stage().getScene().setCursor(Cursor.H_RESIZE);

                if (_resizeTopV) _parent.get_Stage().getScene().setCursor(Cursor.NE_RESIZE);
                if (_resizeBottomV) _parent.get_Stage().getScene().setCursor(Cursor.SE_RESIZE);
            }
        });

        _parent.get_AppPane().setOnMouseExited(mouseEvent -> _parent.get_Stage().getScene().setCursor(Cursor.DEFAULT));
    }

    /**
     * Sets the context menu for the controls pane
     */
    public void SetTextareaContextMenu() {
        _controlsContextMenu = new ContextMenu();

        _controlsContextMenuPaste = new MenuItem("Paste");
        _controlsContextMenuCopy = new MenuItem("Copy");
        _controlsContextMenuCut = new MenuItem("Cut");
        _controlsContextMenuAbout = new MenuItem("About");
        _controlsContextMenuExit = new MenuItem("Close");

        // Add all context menu items to the menu
        _controlsContextMenu.getItems().addAll(
            _controlsContextMenuCopy,
            _controlsContextMenuPaste,
            _controlsContextMenuCut,
            _controlsContextMenuAbout,
            _controlsContextMenuExit
        );

        // Consume text area context menu
        text.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);

        // Set the context menu creator
        text.setOnMouseClicked(mouseEvent -> {
            _controlsContextMenu.hide();

            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                _controlsContextMenuPaste.setDisable(!Clipboard.getSystemClipboard().hasString());
                _controlsContextMenu.show(_parent.get_ControlPane(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });

        // Sets the paste event
        _controlsContextMenuPaste.setOnAction(actionEvent -> {
            if (!Clipboard.getSystemClipboard().hasString()) return;

            int c = text.getCaretPosition();
            String start = text.getText().substring(0, c);
            String clip = Clipboard.getSystemClipboard().getString();
            String end = text.getText(c, text.getLength());

            text.setText(start + clip + end);
            text.positionCaret(start.length() + clip.length());
        });

        // Sets the copy event
        _controlsContextMenuCopy.setOnAction(actionEvent -> {
            String sel = text.getSelectedText();

            SetClipboardText(sel);
        });

        // Sets the cut event
        _controlsContextMenuCut.setOnAction(actionEvent -> {
            int i = text.getSelection().getStart();

            String sel = text.getSelectedText();

            String start = text.getText().substring(0, i);
            String end = text.getText().substring(text.getSelection().getEnd(), text.getLength());

            text.setText(start + end);
            text.positionCaret(i);

            SetClipboardText(sel);
        });

        // Sets the about event
        _controlsContextMenuAbout.setOnAction(actionEvent -> CreateAboutDialog());

        // Sets the exit event
        _controlsContextMenuExit.setOnAction(actionEvent -> _parent.Close());
    }

    /**
     * Creates the about dialog box
     */
    public void CreateAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        HBox pane = new HBox();

        alert.setWidth(400);

        ImageView img = new ImageView(new Image("main/resources/images/logo-large.png"));
        Text txt = new Text("Digital Post-It-Note using JavaFX\nVersion 1.0\nAuthor: Oliver Mitchell (mitoj001)\nCopyright (c) 2020");

        txt.setTextAlignment(TextAlignment.RIGHT);
        img.setFitWidth(200);
        img.setFitHeight(200);

        pane.setSpacing(25);

        pane.getChildren().addAll(img, txt);

        alert.setTitle("About java.UniNote");
        alert.setHeaderText("java.UniNote Post-it-Note application");

        alert.getDialogPane().setContent(pane);

        alert.show();
    }

    /**
     * Sets the stage drag events associated with the control pane
     */
    public void SetStageDragEvent() {
        _parent.get_ControlPane().setOnMouseReleased(mouseEvent -> {
            // Makes note not get hidden when going too high
            if (_parent.get_Stage().getY() < 0) _parent.get_Dimensions().PositionY = 0;

            _parent.ApplyNoteDimensions();
        });
        _parent.get_ControlPane().setOnMouseDragged(mouseEvent -> {
            _parent.get_Dimensions().PositionX = mouseEvent.getScreenX() + _stageDragOffsetX;
            _parent.get_Dimensions().PositionY = mouseEvent.getScreenY() + _stageDragOffsetY;

            _parent.ApplyNoteDimensions();
        });
        _parent.get_ControlPane().setOnMousePressed(mouseEvent -> {
            _stageDragOffsetX = _parent.get_Stage().getX() - mouseEvent.getScreenX();
            _stageDragOffsetY = _parent.get_Stage().getY() - mouseEvent.getScreenY();
        });
    }

    /**
     * Sets the clipboard text
     * @param text Text to set on clipboard
     */
    public void SetClipboardText(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * Gets the ThemeControls class for this Control
     * @return ThemControls
     */
    public ThemeControls get_ThemeControls() {
        return _theme;
    }
}
