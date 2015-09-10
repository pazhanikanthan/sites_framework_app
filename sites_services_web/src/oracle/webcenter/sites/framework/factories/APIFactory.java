package oracle.webcenter.sites.framework.factories;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.webcenter.sites.framework.model.RESTApi;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class APIFactory implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private static APIFactory instance = null;
    
    private transient RequestMappingHandlerMapping requestMappingHandlerMapping = null;
    
    private List <RESTApi> apis = new ArrayList <RESTApi> ();
    
    private APIFactory () {
        
    }
    
    public static synchronized APIFactory getInstance () {
        if (instance == null) {
            synchronized (APIFactory.class) {
                instance = new APIFactory ();
            }
        }
        return instance;
    }
    
    public void setRequestMappingHandlerMapping (RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        Map handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Iterator iterator = handlerMethods.keySet().iterator();
        while (iterator.hasNext()) {
            RESTApi restApi = new RESTApi ();
            RequestMappingInfo requestMappingInfo = (RequestMappingInfo) iterator.next();
            Set <RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
            restApi.setMethod(methods.iterator().next().name());
            Set<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
            restApi.setPath(patterns.iterator().next());
            apis.add(restApi);
        }
    }

    public List<RESTApi> getApis() {
        return apis;
    }

    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return requestMappingHandlerMapping;
    }

}
