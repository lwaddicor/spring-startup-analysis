package pd.treemap;

/**
 *
 * Interface representing an object that can be placed
 * in a treemap layout.
 * <p>
 * The properties are:
 * <ul>
 * <li> size: corresponds to area in map.</li>
 * <li> order: the sort order of the item. </li>
 * <li> depth: the depth in hierarchy. </li>
 * <li> bounds: the bounding rectangle of the item in the map.</li>
 * </ul>
 *
 */
public interface Mappable
{
    public double getSize();
    public String getName();
    public void setName(String name);
    public long getOriginalSize();
    public void setOriginalSize(long name);
    public void   setSize(double size);
    public Rect   getBounds();
    public void   setBounds(Rect bounds);
    public void   setBounds(double x, double y, double w, double h);
    public int    getOrder();
    public void   setOrder(int order);
    public int    getDepth();
    public void   setDepth(int depth);
}
