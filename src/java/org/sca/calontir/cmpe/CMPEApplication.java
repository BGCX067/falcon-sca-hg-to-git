/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe;

import java.io.File;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

/**
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

        return router;
    }
}
