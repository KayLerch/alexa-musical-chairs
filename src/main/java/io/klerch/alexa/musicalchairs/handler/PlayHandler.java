package io.klerch.alexa.musicalchairs.handler;

import io.klerch.alexa.musicalchairs.model.Jukebox;
import io.klerch.alexa.state.handler.AWSDynamoStateHandler;
import io.klerch.alexa.state.handler.AlexaStateHandler;
import io.klerch.alexa.state.utils.AlexaStateException;
import io.klerch.alexa.tellask.model.AlexaInput;
import io.klerch.alexa.tellask.model.AlexaOutput;
import io.klerch.alexa.tellask.schema.AlexaIntentHandler;
import io.klerch.alexa.tellask.schema.annotation.AlexaIntentListener;
import io.klerch.alexa.tellask.schema.type.AlexaOutputFormat;
import io.klerch.alexa.tellask.util.AlexaRequestHandlerException;

import java.util.Random;

@AlexaIntentListener(customIntents = "PlayIntent")
public class PlayHandler implements AlexaIntentHandler {
    @Override
    public boolean verify(final AlexaInput input) {
        return true;
    }

    @Override
    public AlexaOutput handleRequest(final AlexaInput input) throws AlexaRequestHandlerException, AlexaStateException {
        return play(input);
    }

    public static AlexaOutput play(final AlexaInput input) throws AlexaRequestHandlerException, AlexaStateException {
        final AlexaStateHandler handler = new AWSDynamoStateHandler(input.getSessionStateHandler().getSession());
        final Jukebox jukebox = handler.readModel(Jukebox.class).orElse(handler.createModel(Jukebox.class));
        // interrupt on 10% of all playbacks
        final int interrupt = new Random().nextInt(10);

        return AlexaOutput.tell(interrupt == 1 ? "SayPlayWithInterruption" : "SayPlay")
                .putSlot("audio", jukebox.getRandomMp3(), AlexaOutputFormat.AUDIO)
                .putSlot("audio2", jukebox.getFollowUpMp3(), AlexaOutputFormat.AUDIO)
                .putState(jukebox)
                .build();
    }

    @Override
    public AlexaOutput handleError(final AlexaRequestHandlerException exception) {
        return AlexaOutput.tell("SaySorry").build();
    }
}
