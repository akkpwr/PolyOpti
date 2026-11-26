package org.butbetter.polyopti.client;

import net.fabricmc.api.ClientModInitializer;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public class PolyOptiClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PolyOptiConfig.INSTANCE.preload();
        PolyOptiRuntime.onInitialize();
    }
}

