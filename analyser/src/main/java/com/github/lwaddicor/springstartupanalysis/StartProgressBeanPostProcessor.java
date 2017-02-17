package com.github.lwaddicor.springstartupanalysis;

import com.github.lwaddicor.springstartupanalysis.dto.StartTimeStatisticDto;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * This class monitors the start progress of the spring instance
 */
@Component
public class StartProgressBeanPostProcessor implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    private List<StartTimeStatisticDto> times = new LinkedList<>();
    private StopWatch beanTimeStopWatch = new StopWatch();
    private StopWatch totalTimeStopWatch = new StopWatch();

    /**
     * Constructor for the bean post processor
     */
    @PostConstruct
    public void postConstruct(){
        totalTimeStopWatch.reset();
        totalTimeStopWatch.reset();
        totalTimeStopWatch.start();
        beanTimeStopWatch.start();

        System.out.println("#####################################################################################");
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        beanTimeStopWatch.stop();

        long deltaT = beanTimeStopWatch.getTime();

        StartTimeStatisticDto bss = new StartTimeStatisticDto(bean.getClass().toString(), deltaT);
        times.add(bss);

        beanTimeStopWatch.reset();
        beanTimeStopWatch.start();
        return bean;
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        totalTimeStopWatch.stop();
    }

    public List<StartTimeStatisticDto> getTimesAsArray(){
        return times;
    }

    public long getTotalTime(){
        return totalTimeStopWatch.getTime();
    }
}