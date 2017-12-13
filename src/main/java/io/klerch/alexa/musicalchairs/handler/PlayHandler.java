package io.klerch.alexa.musicalchairs.handler;

import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.SimpleCard;
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

import java.util.HashMap;
import java.util.Map;
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
            final boolean interrupt = (new Random().nextInt(1000 / SkillConfig.getInterruptProbabilityPercent()) / 10) == 0;
            // return review solicitation on xth playback
            final boolean reviewSolicitation = jukebox.get().getLastPlayed().size() == SkillConfig.getReviewSolicitationAfter();

            final AlexaOutput.AlexaOutputBuilder outputBuilder = AlexaOutput.tell("SayPlay" + (reviewSolicitation ? "AndSolicit" : "") + (interrupt ? "WithInterruption" : ""))
                    .putSlot("audio", jukebox.get().getRandomMp3(), AlexaOutputFormat.AUDIO)
                    .putSlot("audio2", jukebox.get().getFollowUpMp3(), AlexaOutputFormat.AUDIO)
                    .putState(jukebox.get());

            // add review solicitation card or leave it
            return reviewSolicitation ? outputBuilder.withCard(getReviewSolicitationCard(input.getLocale())).build() : outputBuilder.build();
        }
    }

    @Override
    public AlexaOutput handleError(final AlexaRequestHandlerException exception) {
        return AlexaOutput.tell("SaySorry").build();
    }

    private static Card getReviewSolicitationCard(final String locale) {
        final String defaultInstructions = "We would love your feedback. Please write a review for the 'Musical Chairs' skill by doing the following:\n" +
                "1. Click on 'Skills' from the menu\n" +
                "2. Click on 'Your Skills' in the top right-hand corner\n" +
                "3. Click on the 'Musical Chairs' skill\n" +
                "4. Scroll down and click “Write a review”\n";

        final Map<String, String> instructions = new HashMap<>();
        instructions.put("de-DE", "Wir würden uns über dein Feedback freuen. Bitte gebe doch eine Bewertung für 'Reise nach Jerusalem' mit folgenden Schritten:\n" +
                "1. Wähle 'Skills' aus dem Hauptmenü\n" +
                "2. Klicke auf 'Ihre Skills' im oberen rechten Bereich\n" +
                "3. Klicke auf den 'Reise nach Jerusalem' Skill\n" +
                "4. Scrolle herunter und wähle “Schreiben Sie eine Rezension”\n");
        instructions.put("en-US", defaultInstructions);
        instructions.put("en-GB", defaultInstructions);

        final SimpleCard card = new SimpleCard();
        card.setContent(instructions.getOrDefault(locale, defaultInstructions));
        return card;
    }
}
