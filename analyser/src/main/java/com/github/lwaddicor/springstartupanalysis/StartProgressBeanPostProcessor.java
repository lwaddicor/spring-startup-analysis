package com.github.lwaddicor.springstartupanalysis;

import com.github.lwaddicor.springstartupanalysis.dto.StartTimeStatisticDto;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class monitors the start progress of the spring instance
 */
@Component
public class StartProgressBeanPostProcessor implements BeanPostProcessor {

    private List<StartTimeStatisticDto> times = new LinkedList<>();
    private Map<String, StopWatch> beanStopWatchMap = new HashMap<>();

    // Hopefully the logic to this makes sense. Some beans such as
    // tomcatEmbeddedServletContainerFactory seem to create beans inside them.
    // By having this, I can ignore any of these parent beans.
    private boolean inFactoryBean;

    /**
     * Called before a bean is constructed. Saves bean creation details.
     *
     * @param bean The bean being created.
     * @param beanName The name of the bean being created.
     * @return The bean being created.
     * @throws BeansException
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        beanStopWatchMap.put(beanName, stopWatch);
        inFactoryBean = false;
        return bean;
    }

    /**
     * Called after a bean is constructed. Saves bean creation details.
     *
     * @param bean The bean being created.
     * @param beanName The name of the bean being created.
     * @return The bean being created.
     * @throws BeansException
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        StopWatch stopWatch = beanStopWatchMap.get(beanName);
        if (stopWatch != null) {
            stopWatch.stop();

            if (!inFactoryBean) {
                long deltaT = stopWatch.getTime();
                StartTimeStatisticDto bss = new StartTimeStatisticDto(bean.getClass().toString(), deltaT);
                times.add(bss);
            }
        }

        inFactoryBean = true;
        return bean;
    }

    /**
     * Gets the bean startup times as a list
     *
     * @return a list of startup times
     */
    public List<StartTimeStatisticDto> getTimesAsArray() {
        return times;
    }

    /**
     * Gets the total time to start all the beans
     */
    public long getTotalTime(){
        return times.stream().mapToLong(t -> t.getTime()).sum();
    }
}