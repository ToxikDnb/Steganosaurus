package steganosaurus.Settings;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a keybind for a menu item, allowing it to be triggered by a
 * specific key combination.
 * This class manages the key code and any modifiers (like Shift, Ctrl, etc.)
 * that should be used
 * to trigger the menu item.
 */
public class Keybind {
    private JMenuItem attachedItem;
    private int keyCode;
    private List<Integer> modifiers;

    /**
     * Constructs a Keybind with the specified menu item, key code, and modifiers.
     *
     * @param attachedItem the JMenuItem to which this keybind is attached
     * @param keyCode      the key code for the keybind
     * @param modifiers    variable number of modifier masks (e.g.,
     *                     KeyEvent.SHIFT_DOWN_MASK)
     */
    public Keybind(JMenuItem attachedItem, int keyCode, int... modifiers) {
        this.attachedItem = attachedItem;
        this.modifiers = new ArrayList<>();
        updateKeyEvents(keyCode, modifiers);
    }

    /**
     * Updates the key code and modifiers for this keybind.
     *
     * @param keyCode       the new key code for the keybind
     * @param modifierMasks variable number of modifier masks (e.g.,
     *                      KeyEvent.SHIFT_DOWN_MASK)
     */
    public void updateKeyEvents(int keyCode, int... modifierMasks) {
        this.keyCode = keyCode;
        this.modifiers.clear();
        for (int mod : modifierMasks) {
            this.modifiers.add(mod);
        }
        attachAccelerator();
    }

    // #region helper methods
    private void attachAccelerator() {
        if (attachedItem == null)
            return;
        attachedItem.setAccelerator(KeyStroke.getKeyStroke(keyCode, getCombinedModifiers()));
    }

    private int getCombinedModifiers() {
        int combined = 0;
        for (int mod : modifiers) {
            combined |= mod;
        }
        return combined;
    }
    // #endregion
}
