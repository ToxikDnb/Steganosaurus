package steganosaurus.Controllers;

import steganosaurus.GUI.AboutWindow;

public class GUIActions extends Actions<GUIEnum> {

    /**
     * Constructor for GUIActions.
     *
     * @param actionType the type of GUI action this instance represents
     */
    public GUIActions(GUIEnum actionType) {
        super(actionType);
    }

    @Override
    public void execute() {
        switch (actionType) {
            case EXIT:
                System.exit(0);
                break;
            case ABOUT:
                new AboutWindow();
                break;
            default:
                throw new UnsupportedOperationException("Action not supported: " + actionType);
        }
    }

}
