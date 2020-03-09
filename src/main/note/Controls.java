package main.note;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import main.Rect;


/**
 * Manages events and states related to a Note's controls
 */
public class Controls {
    /** Minimum width of the note **/
    public static final double MIN_WIDTH = 100;

    /** Maximum width of the note **/
    public static final double MIN_HEIGHT = 100;

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


    /** Creates a new Controls object **/
    public Controls(Note parent, Button close, Button create, TextArea text) {
        this._parent = parent;
        this.close = close;
        this.create = create;
        this.text = text;

        text.setWrapText(true);

        close.setId("button-close");
        create.setId("button-create");
        text.setId("textarea-main");

        create.setOnAction(actionEvent -> {
            Note n = new Note(_parent.get_Dimensions());
            n.TranslateNextTo(_parent.get_Dimensions());
        });
        
        close.setOnAction(actionEvent -> _parent.Close());

        SetWindowScaleEvent();
        SetControlsPaneContextMenu();
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

        close.setOnMouseEntered(mouseEvent -> {
            _parent.get_Stage().getScene().setCursor(Cursor.DEFAULT);
        });

        create.setOnMouseEntered(mouseEvent -> {
            _parent.get_Stage().getScene().setCursor(Cursor.DEFAULT);
        });

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

        _parent.get_AppPane().setOnMouseExited(mouseEvent -> {
            _parent.get_Stage().getScene().setCursor(Cursor.DEFAULT);
        });
    }

    /**
     * Sets the context menu for the controls pane
     */
    public void SetControlsPaneContextMenu() {
        // TODO: Add context menu
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
}
