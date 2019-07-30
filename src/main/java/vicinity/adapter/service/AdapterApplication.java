package vicinity.adapter.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import vicinity.adapter.service.resources.*;

public class AdapterApplication extends Application {
    private static final String OBJECTS = "/objects";
    private static final String GET_SET_PROPERTY = "/objects/{oid}/properties/{pid}";
    private static final String EVENT_CHANNEL = "/objects/{iid}/events/{eid}";

    public AdapterApplication() {
    }

    private ChallengeAuthenticator createApiGuard(Restlet next) {
        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(this.getContext(), ChallengeScheme.HTTP_BASIC, "realm");
        apiGuard.setNext(next);
        apiGuard.setOptional(true);
        return apiGuard;
    }

    private Router createApiRouter() {
        Router apiRouter = new Router(this.getContext());

        apiRouter.attach(OBJECTS, ObjectsResource.class); //GET http request
        apiRouter.attach(GET_SET_PROPERTY, GetSetPropertyResource.class); //GET & POST http request
        apiRouter.attach(EVENT_CHANNEL + "/activate", EventChannelResource.class); //POST http request
        apiRouter.attach(EVENT_CHANNEL + "/deactivate", EventChannelResource.class); //DELETE http request
        apiRouter.attach(EVENT_CHANNEL + "/publish", EventChannelResource.class); //PUT http request

        return apiRouter;
    }

    public Restlet createInboundRoot() {
        Router apiRouter = this.createApiRouter();
        ChallengeAuthenticator guard = this.createApiGuard(apiRouter);
        return guard;
    }
}
