package steganosaurus.Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Abstract class representing actions in the Steganosaurus application.
 * This class is generic and can be extended for specific action types.
 *
 * @param <E> the type of the action enum
 */
public abstract class Actions<E extends Enum<E>> implements ActionListener {

    protected E actionType;

    /**
     * Constructor for Actions.
     *
     * @param actionType the type of action this instance represents
     */
    public Actions(E actionType) {
        this.actionType = actionType;
    }

    /**
     * Gets the action type of this instance.
     *
     * @return the action type
     */
    public E getActionType() {
        return actionType;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute();
    }

    /**
     * Executes the action represented by this instance.
     * This method should be implemented by subclasses to define specific behaviour.
     */
    public abstract void execute();
}
