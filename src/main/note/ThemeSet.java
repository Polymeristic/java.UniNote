package main.note;

/**
 * Helper class to store information about a color set
 */
public class ThemeSet {
    /** Theme color categorisation **/
    public ThemeColor Color;

    /** Base color of the note **/
    public String BaseColor;

    /** Header color of the note **/
    public String HeaderColor;

    /** Text color of the note **/
    public String TextColor;

    /**
     * Creates a new Theme set object
     * @param base Base color of the note
     * @param header Header color of the note
     * @param text Text color of the note
     */
    public ThemeSet(ThemeColor color, String base, String header, String text) {
        Color = color;
        BaseColor = base;
        HeaderColor = header;
        TextColor = text;
    }
}
