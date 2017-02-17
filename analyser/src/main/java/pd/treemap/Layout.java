package pd.treemap;

/**
* The interface for the treemap layout algorithm.
*/
public interface Layout
{
   /**
    * Arrange the items in the given MapModel to fill the given rectangle.
    *
    * @param model The MapModel.
    * @param bounds The bounding rectangle for the layout.
    */
   public void layout(MapModel model, Rect bounds);
 }