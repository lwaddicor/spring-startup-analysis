package com.github.lwaddicor.springstartupanalysis.web;

import com.github.lwaddicor.springstartupanalysis.TreeMapModel;
import com.github.lwaddicor.springstartupanalysis.StartProgressBeanPostProcessor;
import com.github.lwaddicor.springstartupanalysis.dto.StartTimeSquareDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import pd.treemap.Rect;
import pd.treemap.TreemapLayout;

/**
 * This controller creates the endpoints which allow the display of the
 * graphic representing the spring context start time.
 */
@Controller
@ResponseBody
@RequestMapping(value = "/spring-startup")
public class SpringStartupController implements ApplicationListener<ContextRefreshedEvent> {

    private static final int imageWidth = 1200;
    private static final int imageHeight = 800;

    private BufferedImage image = new BufferedImage(imageWidth, imageHeight,
            BufferedImage.TYPE_BYTE_INDEXED);

    private String html;

    private StartProgressBeanPostProcessor processor;

    @Autowired
    public SpringStartupController(StartProgressBeanPostProcessor processor){
        this.processor = processor;
    }

    /**
     * Gets called when the application becomes ready
     *
     * @param applicationReadyEvent The application event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationReadyEvent) {

        Rect bounds = new Rect(0, 0, imageWidth, imageHeight);
        TreemapLayout algorithm = new TreemapLayout();
        TreeMapModel mapModel = new TreeMapModel(processor.getTimesAsArray(), imageWidth, imageHeight);

        algorithm.layout(mapModel, bounds);

        List<StartTimeSquareDto> items = mapModel.getSquareItems();

        items.sort((a,b)-> (int)(b.getTime() - a.getTime()));

        Graphics2D g2d = image.createGraphics();

        for(StartTimeSquareDto square : items) {
            g2d.setColor(Color.white);
            g2d.drawRect(square.getLeft(), square.getTop(), square.getWidth(), square.getHeight());
            g2d.setColor(Color.gray);
            g2d.drawRect(square.getLeft() + 1, square.getTop() + 1, square.getWidth() - 2, square.getHeight() - 2);
        }

        g2d.dispose();

        STGroup hello = new STGroupFile("com/github/lwaddicor/springstartupanalysis/web/StartupTemplate.stg", '$', '$');
        ST st = hello.getInstanceOf("t1");
        st.add("items", items);
        st.add("totaltime", processor.getTotalTime());

        html = st.render();
    }

    /**
     * Gets the html content for the page.
     *
     * @return The html content
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getHtml() {
        return html;
    }

    /**
     * Gets the image
     *
     * @param response The response to populate.
     * @throws IOException Thrown when there is an error writing the image.
     */
    @RequestMapping(value = "/img.jpeg", method = RequestMethod.GET)
    public void showImage(HttpServletResponse response) throws IOException {
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "jpeg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        byte[] imgByte = jpegOutputStream.toByteArray();

        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(imgByte);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

}