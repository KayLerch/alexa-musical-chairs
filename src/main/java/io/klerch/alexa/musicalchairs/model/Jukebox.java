package io.klerch.alexa.musicalchairs.model;

import io.klerch.alexa.musicalchairs.SkillConfig;
import io.klerch.alexa.state.model.AlexaScope;
import io.klerch.alexa.state.model.AlexaStateModel;
import io.klerch.alexa.state.model.AlexaStateSave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Jukebox extends AlexaStateModel {
    @AlexaStateSave(Scope = AlexaScope.USER)
    private List<String> lastPlayed = new ArrayList<>();

    private int lastPlayTime = 0;

    public Jukebox() {
    }

    public List<String> getLastPlayed() {
        return lastPlayed;
    }

    public String getFollowUpMp3() {
        int playTime;

        do {
            // find random playtime for follow-up mp3
            playTime = (new Random().nextInt(5) + 3) * 5;
        }
        // until overall playtime does not exceed 90 seconds (maximum)
        while (lastPlayTime + playTime > 90);

        return SkillConfig.getS3BucketUrl() + lastPlayed.get(lastPlayed.size() - 1) + "-" + playTime + ".mp3";
    }

    public String getRandomMp3() {
        lastPlayTime = (new Random().nextInt(10) + 3) * 5;

        // get all themes
        final String[] songs = SkillConfig.getSongs();
        // remove those played recently
        final List<String> onlySongs = Arrays.stream(songs)
                .filter(theme -> !lastPlayed.contains(theme))
                .collect(Collectors.toList());

        String song = "";
        // if no theme left over as all of them were played recently
        if (onlySongs.size() == 0) {
            // pick the one on the bottom of lastPlayed and remove rest
            song = lastPlayed.get(0);
            lastPlayed = lastPlayed.subList((lastPlayed.size() / 3) + 1, (lastPlayed.size()));
            //lastPlayed.clear();
        } else {
            // pick random from themes left over
            song = onlySongs.get(new Random().nextInt(onlySongs.size()));
        }

        // add current theme to end of list
        lastPlayed.add(song);

        return SkillConfig.getS3BucketUrl() + song + "-" + lastPlayTime + ".mp3";
    }
}
