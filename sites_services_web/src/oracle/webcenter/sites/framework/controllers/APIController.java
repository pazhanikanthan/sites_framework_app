package oracle.webcenter.sites.framework.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.Produces;

import oracle.webcenter.sites.framework.factories.APIFactory;
import oracle.webcenter.sites.framework.model.Info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
public class APIController extends AbstractController {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    private static final String INFO = "/info";
    
    @Autowired
    public APIController(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        APIFactory.getInstance().setRequestMappingHandlerMapping(requestMappingHandlerMapping);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = INFO, method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<Info> getInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Info info = new Info();
        info.setVersion("1.0");
        info.addAuthor("Paz Periasamy");
        info.addAuthor("Rodney Sowirono");
        info.setLastUpdatedDate(new Date());
        return new HttpEntity<Info>(info, getHttpHeaders());
    }

}

