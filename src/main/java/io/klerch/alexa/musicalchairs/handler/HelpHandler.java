package io.klerch.alexa.musicalchairs.handler;

import io.klerch.alexa.state.utils.AlexaStateException;
import io.klerch.alexa.tellask.model.AlexaInput;
import io.klerch.alexa.tellask.model.AlexaOutput;
import io.klerch.alexa.tellask.schema.AlexaIntentHandler;
import io.klerch.alexa.tellask.schema.annotation.AlexaIntentListener;
import io.klerch.alexa.tellask.schema.type.AlexaIntentType;
import io.klerch.alexa.tellask.util.AlexaRequestHandlerException;

@AlexaIntentListener(builtInIntents = {AlexaIntentType.INTENT_HELP})
public class HelpHandler implements AlexaIntentHandler {
    @Override
    public boolean verify(final AlexaInput input) {
        return true;
    }

    @Override
    public AlexaOutput handleRequest(final AlexaInput input) throws AlexaRequestHandlerException, AlexaStateException {
        return AlexaOutput.ask("SayHelp").build();
    }

    @Override
    public AlexaOutput handleError(final AlexaRequestHandlerException exception) {
        // stop anyway and hide error
        return AlexaOutput.tell("SaySorry").build();
    }
}
