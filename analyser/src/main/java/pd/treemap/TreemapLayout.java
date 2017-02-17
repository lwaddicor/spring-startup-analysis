package pd.treemap;

/**
 * Implements the Squarified Treemap layout published by
 * Mark Bruls, Kees Huizing, and Jarke J. van Wijk
 *
 * Squarified Treemaps
 * https://www.win.tue.nl/~vanwijk/stm.pdf
 *
 */
public class TreemapLayout implements Layout {

    private int mid = 0;

    public void layout(MapModel model, Rect bounds) {
        layout(model.getItems(), bounds);
    }

    public void layout(Mappable[] items, Rect bounds)
    {
        layout(sortDescending(items),0,items.length-1,bounds);
    }

    public Mappable[] sortDescending(Mappable[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        Mappable[] inputArr = new Mappable[items.length];
        System.arraycopy(items, 0, inputArr, 0, items.length);
        int length = inputArr.length;

        quickSortDesc(inputArr, 0, length - 1);

        return inputArr;
    }

    public void layout(Mappable[] items, int start, int end, Rect bounds)
    {
        if (start>end) {
            return;
        }
        if(start == end) {
            items[start].setBounds(bounds);
        }

        this.mid = start;
        while (mid < end) {
            if (highestAspect(items, start, mid, bounds) > highestAspect(items, start, mid+1, bounds) ) {
                mid++;
            } else {
                Rect newBounds = layoutRow(items, start, mid, bounds);
                layout(items, mid+1, end, newBounds);
            }
        }
    }

    public double highestAspect(Mappable[] items, int start, int end, Rect bounds) {
        layoutRow(items, start, end, bounds);
        double max = Double.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            if (items[i].getBounds().aspectRatio() > max) {
                max = items[i].getBounds().aspectRatio();
            }
        }
        return max;
    }

    public Rect layoutRow(Mappable[] items, int start, int end, Rect bounds) {
        boolean isHorizontal = bounds.w > bounds.h;
        double total = bounds.w * bounds.h; //totalSize(items, 0, items.length-1);
        double rowSize = totalSize(items, start, end);
        double rowRatio = rowSize / total;
        double offset=0;

        for (int i = start; i <= end; i++) {
            Rect r=new Rect();
            double ratio=items[i].getSize() / rowSize;

            if (isHorizontal) {
                r.x = bounds.x;
                r.w = bounds.w * rowRatio;
                r.y = bounds.y + bounds.h * offset;
                r.h = bounds.h * ratio;
            } else {
                r.x = bounds.x + bounds.w * offset;
                r.w = bounds.w * ratio;
                r.y = bounds.y;
                r.h = bounds.h * rowRatio;
            }
            items[i].setBounds(r);
            offset+=ratio;
        }
        if (isHorizontal) {
            return new Rect(bounds.x + bounds.w * rowRatio, bounds.y, bounds.w - bounds.w * rowRatio, bounds.h);
        } else {
            return new Rect(bounds.x, bounds.y + bounds.h * rowRatio, bounds.w, bounds.h - bounds.h * rowRatio);
        }
    }


    public static double totalSize(Mappable[] items) {
        return totalSize(items, 0, items.length - 1);
    }

    public static double totalSize(Mappable[] items, int start, int end) {
        double sum = 0;
        for (int i = start; i <= end; i++)
            sum += items[i].getSize();
        return sum;
    }

    private void quickSortDesc(Mappable[] inputArr, int lowerIndex, int higherIndex) {

       int i = lowerIndex;
       int j = higherIndex;
       // calculate pivot number
       double pivot = inputArr[lowerIndex+(higherIndex-lowerIndex)/2].getSize();
       // Divide into two arrays
       while (i <= j) {
           /**
            * In each iteration, we will identify a number from left side which
            * is greater then the pivot value, and also we will identify a number
            * from right side which is less then the pivot value. Once the search
            * is done, then we exchange both numbers.
            */
           while (inputArr[i].getSize() > pivot) {
               i++;
           }
           while (inputArr[j].getSize() < pivot) {
               j--;
           }
           if (i <= j) {
               Mappable temp = inputArr[i];
               inputArr[i] = inputArr[j];
               inputArr[j] = temp;
               //move index to next position on both sides
               i++;
               j--;
           }
       }
       // call quickSort() method recursively
       if (lowerIndex < j)
           quickSortDesc(inputArr, lowerIndex, j);
       if (i < higherIndex)
           quickSortDesc(inputArr, i, higherIndex);
   }
}
