package steganosaurus.Controllers;

public class BackendActions extends Actions<BackendEnum> {

    /**
     * Constructor for BackendActions.
     *
     * @param actionType the type of backend action this instance represents
     */
    public BackendActions(BackendEnum actionType) {
        super(actionType);
    }

    @Override
    public void execute() {
        switch (actionType) {
            case SWITCH_MODE:
                // Logic to switch modes
                break;
            case SELECT_FILE:
                // Logic to select a file
                break;
            case ADD_CARRIERS:
                // Logic to add carriers
                break;
            case REMOVE_CARRIER:
                // Logic to remove a carrier
                break;
            case CLEAR_CARRIERS:
                // Logic to clear carriers
                break;
            case RUN:
                // Logic to run the backend process
                break;
            default:
                throw new UnsupportedOperationException("Action not supported: " + actionType);
        }
    }

}
