package main.note;

import javafx.scene.control.Button;

import java.util.HashMap;

/**
 * Manages the controls and management of themes
 */
public class ThemeControls {
    /** Mappings of Colors to a color set object **/
    public static HashMap<ThemeColor, String> ColorMapping;

    /** Default theme color when creating a new note **/
    public static final ThemeColor DEFAULT_COLOR
        = ThemeColor.BLUE;

    public static final String BUTTON_SELECTED
        = "⚫";

    public static final String BUTTON_UNSELECTED
        = "⚫";

    /** The parent note **/
    private Note _parent;

    /** Blue toggle button **/
    public Button ButtonBlue;

    /** Yellow toggle button **/
    public Button ButtonYellow;

    /** Dark toggle button **/
    public Button ButtonDark;

    static {
        ColorMapping = new HashMap<>();
        ColorMapping.put(ThemeColor.BLUE, "main/note/theme/blue.css");
        ColorMapping.put(ThemeColor.YELLOW, "main/note/theme/red.css");
        ColorMapping.put(ThemeColor.DARK, "main/note/theme/dark.css");
    }

    /**
     * Creates a new Theme class with the required buttons and parent
     * @param parent Parent note to link to
     * @param buttonBlue Button to toggle blue theme
     * @param buttonYellow Button to toggle yellow theme
     * @param buttonDark Button to toggle dark theme
     */
    public ThemeControls(Note parent, Button buttonBlue, Button buttonYellow, Button buttonDark) {
        _parent = parent;
        ButtonBlue = buttonBlue;
        ButtonYellow = buttonYellow;
        ButtonDark = buttonDark;

        SetButtonEvents();
        ApplyAll();
    }

    /**
     * Sets all the button events
     */
    public void SetButtonEvents() {
        ButtonBlue.setId("btn-blue");
        ButtonDark.setId("btn-dark");
        ButtonYellow.setId("btn-red");

        ButtonBlue.setOnAction(actionEvent -> {
            _parent.set_Color(ThemeColor.BLUE);
            ApplyAll();
        });

        ButtonYellow.setOnAction(actionEvent -> {
            _parent.set_Color(ThemeColor.YELLOW);
            ApplyAll();
        });

        ButtonDark.setOnAction(actionEvent -> {
            _parent.set_Color(ThemeColor.DARK);
            ApplyAll();
        });
    }

    /**
     * Applies all states on the note
     */
    public void ApplyAll() {
        ApplyCurrentButton();
        ApplyCurrentColor();
    }

    /**
     * Applies the current button state to the button
     */
    public void ApplyCurrentButton() {
        ButtonBlue.setText(BUTTON_UNSELECTED);
        ButtonYellow.setText(BUTTON_UNSELECTED);
        ButtonDark.setText(BUTTON_UNSELECTED);

        switch (_parent.get_Color()) {
            case BLUE:
                ButtonBlue.setText(BUTTON_SELECTED);
                break;
            case DARK:
                ButtonDark.setText(BUTTON_SELECTED);
                break;
            case YELLOW:
                ButtonYellow.setText(BUTTON_SELECTED);
                break;
        }
    }

    /**
     * Applies the current color to the note
     */
    public void ApplyCurrentColor() {
        _parent.get_Stage().getScene().getStylesheets().remove(1);
        _parent.get_Stage().getScene().getStylesheets().add(
            ColorMapping.get(_parent.get_Color())
        );
    }
}
