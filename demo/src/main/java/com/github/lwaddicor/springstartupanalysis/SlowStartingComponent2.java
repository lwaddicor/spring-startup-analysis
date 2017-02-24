package com.github.lwaddicor.springstartupanalysis;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Nemmy on 24/02/2017.
 */
@Component
public class SlowStartingComponent2 {

    @PostConstruct
    public void postConstruct() throws InterruptedException {
        Thread.sleep(2000);
    }
}
