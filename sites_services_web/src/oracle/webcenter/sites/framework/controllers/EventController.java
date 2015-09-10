package oracle.webcenter.sites.framework.controllers;

import javax.ws.rs.Produces;

import oracle.webcenter.sites.framework.context.APIContextAware;
import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.model.APIResponse;
import oracle.webcenter.sites.framework.model.Attachment;
import oracle.webcenter.sites.framework.model.AuditRecord;
import oracle.webcenter.sites.framework.services.AuditService;

import oracle.webcenter.sites.framework.services.EventService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class EventController extends AbstractController {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());

    private static final String EVENT_CREATE_ATTACHMENT    =   "/event/create-attachment";

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = EVENT_CREATE_ATTACHMENT, method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<APIResponse> create (@RequestBody Attachment attachment) throws Exception {
        HttpEntity<APIResponse> apiResponse = null;
        logger.info("Entered");
        try {
            getEventService().create(attachment); 
            apiResponse = new HttpEntity<APIResponse> (new APIResponse("success", null), getHttpHeaders());
        }
        catch (APIException expAPI) {
            apiResponse = new HttpEntity<APIResponse> (new APIResponse("error", expAPI.getErrorMessage()), getHttpHeaders());    
        }
        
        logger.info("Leaving");
        return apiResponse;
    }
    
    private EventService getEventService () {
        return APIContextAware.getEventService();
    }
}
