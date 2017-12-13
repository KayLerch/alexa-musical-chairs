package io.klerch.alexa.musicalchairs;

import com.amazon.speech.ui.SsmlOutputSpeech;
import io.klerch.alexa.test.asset.AlexaAssertion;
import io.klerch.alexa.test.asset.AlexaAsset;
import io.klerch.alexa.test.client.AlexaClient;
import io.klerch.alexa.test.client.endpoint.AlexaEndpoint;
import io.klerch.alexa.test.client.endpoint.AlexaRequestStreamHandlerEndpoint;
import io.klerch.alexa.test.response.AlexaResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MusicalChairsSpeechletHandlerTest {
    @Test
    public void launchUS() throws Exception {
        launch("en-US", ".*Welcome.*");
    }

    @Test
    public void launchUK() throws Exception {
        launch("en-GB", ".*Welcome.*");
    }

    @Test
    public void launchDE() throws Exception {
        launch("de-DE", ".*Willkommen.*");
    }

    private void launch(final String locale, final String welcome) throws Exception {
        final AlexaEndpoint endpoint = AlexaRequestStreamHandlerEndpoint
                .create(MusicalChairsSpeechletHandler.class)
                .build();

        final AlexaClient client = AlexaClient.create(endpoint)
                .withApplicationId(SkillConfig.getAlexaAppId())
                .withLocale(locale)
                .build();

        client.startSession()
                .launch()
                    .assertMatches(AlexaAsset.OutputSpeechSsml, welcome)
                    .assertSessionStillOpen()
                    .done()
                .yes()
                    .assertTrue(AlexaAssertion.HasOutputSpeechIsSsml)
                    .assertSessionEnded();

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