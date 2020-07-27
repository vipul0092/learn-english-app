package io.vgaur.vidya;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.vgaur.vidya.resources.AuthResource;

public class VidyaServiceApplication extends Application<VidyaServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new VidyaServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "Learning App Backend Service";
    }

    @Override
    public void initialize(final Bootstrap<VidyaServiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final VidyaServiceConfiguration configuration,
                    final Environment environment) {

        environment.jersey().register(new AuthResource());
    }

}
