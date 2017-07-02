package io.klerch.alexa.musicalchairs.handler;

import io.klerch.alexa.musicalchairs.SkillConfig;
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

import java.util.Optional;
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
        final Optional<Jukebox> jukebox = handler.readModel(Jukebox.class);

        if (!jukebox.isPresent()) {
            // newcomers get a welcome message with some guidance
            return AlexaOutput.ask("SayWelcome")
                    .putState(handler.createModel(Jukebox.class))
                    .withReprompt(true).build();
        } else {
            // interrupt on x% of all playbacks
            final int interrupt = new Random().nextInt(1000 / SkillConfig.getInterruptProbabilityPercent()) / 10;

            return AlexaOutput.tell(interrupt == 0 ? "SayPlayWithInterruption" : "SayPlay")
                    .putSlot("audio", jukebox.get().getRandomMp3(), AlexaOutputFormat.AUDIO)
                    .putSlot("audio2", jukebox.get().getFollowUpMp3(), AlexaOutputFormat.AUDIO)
                    .putState(jukebox.get())
                    .build();
        }
    }

    @Override
    public AlexaOutput handleError(final AlexaRequestHandlerException exception) {
        return AlexaOutput.tell("SaySorry").build();
    }
}
