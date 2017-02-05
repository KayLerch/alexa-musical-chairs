package io.klerch.alexa.musicalchairs;

import com.amazon.speech.ui.SsmlOutputSpeech;
import io.klerch.alexa.test.asset.AlexaAssertion;
import io.klerch.alexa.test.asset.AlexaAsset;
import io.klerch.alexa.test.client.AlexaClient;
import io.klerch.alexa.test.client.endpoint.AlexaEndpoint;
import io.klerch.alexa.test.client.endpoint.AlexaLambdaEndpoint;
import io.klerch.alexa.test.client.endpoint.AlexaRequestStreamHandlerEndpoint;
import io.klerch.alexa.test.response.AlexaResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class MusicalChairsSpeechletHandlerTest {
    @Test
    public void launch() throws Exception {
        final AlexaEndpoint endpoint = AlexaRequestStreamHandlerEndpoint
                .create(MusicalChairsSpeechletHandler.class)
                .build();

        final AlexaClient client = AlexaClient.create(endpoint)
                .withApplicationId("amzn1.ask.skill.11d65861-9acf-4646-8a41-2d79b8d9315a")
                .withLocale(Locale.US)
                .build();

        final List<String> answers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final AlexaResponse r1 = client.startSession().launch()
                    .assertTrue(AlexaAssertion.HasOutputSpeechIsSsml)
                    .assertSessionEnded();

            answers.add(((SsmlOutputSpeech)r1.getResponseEnvelope().getResponse().getOutputSpeech()).getSsml());
        }

        answers.forEach(System.out::println);
    }

}