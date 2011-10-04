package org.sca.calontir.cmpe;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * REST configuration file.
 *
 * @author rik
 */
public class CMPEApplication extends Application {

    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

        router.attach("/fighter", FighterServerResource.class);
        router.attach("/fighter/{fighterkey}", FighterServerResource.class);
        router.attach("/authType", AuthTypeServerResource.class);
        router.attach("/authType/{authTypekey}", AuthTypeServerResource.class);
        router.attach("/scaGroup", ScaGroupServerResource.class);
        router.attach("/scaGroup/{scaGroupkey}", ScaGroupServerResource.class);

        return router;
    }
}
