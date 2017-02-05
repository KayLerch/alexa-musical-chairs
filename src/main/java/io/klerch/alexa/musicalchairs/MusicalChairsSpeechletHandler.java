package io.klerch.alexa.musicalchairs;

import io.klerch.alexa.tellask.model.wrapper.AlexaRequestStreamHandler;
import io.klerch.alexa.tellask.schema.UtteranceReader;
import io.klerch.alexa.tellask.util.resource.ResourceUtteranceReader;

import java.util.Collections;
import java.util.Set;

public class MusicalChairsSpeechletHandler extends AlexaRequestStreamHandler {
    @Override
    public Set<String> getSupportedApplicationIds() {
        return Collections.singleton(SkillConfig.getAlexaAppId());
    }

    @Override
    public UtteranceReader getUtteranceReader() {
        return new ResourceUtteranceReader("out/");
    }
}
