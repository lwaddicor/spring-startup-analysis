package pd.treemap;

/**
 * Model object used by MapLayout to represent
 * data for a treemap.
 */
public interface MapModel
{
    /**
     * Get the list of items in this model.
     *
     * @return An array of the Mappable objects in this MapModel.
     */
    public Mappable[] getItems();
}