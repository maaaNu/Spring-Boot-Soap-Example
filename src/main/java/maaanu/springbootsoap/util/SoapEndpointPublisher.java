package maaanu.springbootsoap.util;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SoapEndpointPublisher {
    private static final String WEB_SERVICE_WSDL_SUFFIX = ".wsdl";
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ApplicationContext applicationContext;
    private final SpringBus springBus;

    public SoapEndpointPublisher(ApplicationContext applicationContext, SpringBus springBus) {
        this.applicationContext = applicationContext;
        this.springBus = springBus;
    }

    @PostConstruct
    public void onApplicationEvent() {
        applicationContext.getBeansWithAnnotation(SoapService.class).forEach((beanName, controller) -> {
            String route = returnRouteIfDeclaredWithAnnotation(controller).orElse(controller.getClass().getName());
            createEndpoint(controller, route);
        });
    }

    private Optional<String> returnRouteIfDeclaredWithAnnotation(Object controller) {
        List<String> list = Arrays.stream(controller.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Route.class))
                .map(field -> getRouteValue(controller, field))
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());

        if(list.size() > 1) LOG.error("Multiple Fields are with @Route declared!");
        if(list.size() == 0) {
            LOG.info("No Field is declared @Route - using Classname as route");
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    private String getRouteValue(Object controller, Field field) {
        try {
            return (String) field.get(controller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void createEndpoint(Object object, String route) {
        final EndpointImpl endpoint = new EndpointImpl(springBus, object);
        endpoint.publish(route);
        endpoint.setWsdlLocation(route + WEB_SERVICE_WSDL_SUFFIX);
    }
}
