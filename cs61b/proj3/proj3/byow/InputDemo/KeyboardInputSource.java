package byow.InputDemo;

/**
 * Created by hug.
 */
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;
    public KeyboardInputSource() {
    }

    public char getNextKey() {
        if (StdDraw.hasNextKeyTyped()) {
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (PRINT_TYPED_KEYS) {
                System.out.print(c);
            }
            return c;
        }
        return ' ';
    }

    public boolean possibleNextInput() {
        return true;
    }
}