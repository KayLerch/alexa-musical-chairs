package io.klerch.alexa.musicalchairs.handler;

import io.klerch.alexa.musicalchairs.SkillConfig;
import io.klerch.alexa.musicalchairs.model.Jukebox;
import io.klerch.alexa.state.handler.AWSDynamoStateHandler;
import io.klerch.alexa.state.handler.AlexaStateHandler;
import io.klerch.alexa.state.utils.AlexaStateException;
import io.klerch.alexa.tellask.model.AlexaInput;
import io.klerch.alexa.tellask.model.AlexaOutput;
import io.klerch.alexa.tellask.schema.AlexaLaunchHandler;
import io.klerch.alexa.tellask.schema.annotation.AlexaLaunchListener;
import io.klerch.alexa.tellask.util.AlexaRequestHandlerException;

@AlexaLaunchListener
public class LaunchHandler implements AlexaLaunchHandler {

    @Override
    public AlexaOutput handleRequest(final AlexaInput input) throws AlexaRequestHandlerException, AlexaStateException {
        return PlayHandler.play(input);
    }

    @Override
    public AlexaOutput handleError(final AlexaRequestHandlerException exception) {
        return AlexaOutput.tell("SaySorry").build();
    }
}
