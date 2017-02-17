package com.github.lwaddicor.springstartupanalysis;

import com.github.lwaddicor.springstartupanalysis.dto.StartTimeSquareDto;
import com.github.lwaddicor.springstartupanalysis.dto.StartTimeStatisticDto;

import java.util.LinkedList;
import java.util.List;

import pd.treemap.MapItem;
import pd.treemap.MapModel;
import pd.treemap.Mappable;


public class TreeMapModel implements MapModel {

    private Mappable[] items;

    /**
     * Creates a Map Model instance based on the relative size of the mappable items
     * and the frame size.
     *
     * @param timesAsArray List of items holding the times
     * @param width Width of the display area
     * @param height Height of the display area
     */
    public TreeMapModel(List<StartTimeStatisticDto> timesAsArray, int width, int height) {
        this.items = new MapItem[timesAsArray.size()];
        double totalArea = width * height;
        double sum = timesAsArray.stream().mapToLong(StartTimeStatisticDto::getTime).sum();

        for (int i = 0; i < items.length; i++) {
            items[i] = new MapItem(totalArea / sum * timesAsArray.get(i).getTime(), 0);
            items[i].setName(timesAsArray.get(i).getName());
            items[i].setOriginalSize(timesAsArray.get(i).getTime());
        }
    }

    /**
     * Gets the items converted to the SquareItem Type
     * @return
     */
    public List<StartTimeSquareDto> getSquareItems() {

        List<StartTimeSquareDto> list = new LinkedList<>();
        for(Mappable item : items){
            StartTimeSquareDto square = new StartTimeSquareDto();
            square.setName(item.getName());
            square.setTime(item.getOriginalSize());
            square.setLeft((int) item.getBounds().x);
            square.setRight((int) (item.getBounds().x + item.getBounds().w));
            square.setTop((int) item.getBounds().y);
            square.setBottom((int) (item.getBounds().y + item.getBounds().h));
            square.setWidth((int) (item.getBounds().w));
            square.setHeight((int) (item.getBounds().h));
            list.add(square);
        }

        return list;
    }


    @Override
    public Mappable[] getItems() {
        return items;
    }


}
