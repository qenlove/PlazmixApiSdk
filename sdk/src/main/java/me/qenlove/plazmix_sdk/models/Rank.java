package me.qenlove.plazmix_sdk.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Rank {

    DEFAULT(0, "Игрок", RankType.DEFAULT),
    STAR(0, "Star", RankType.DONATE),
    COSMO(0, "Cosmo", RankType.DONATE),
    GALAXY(0, "Galaxy", RankType.DONATE),
    UNIVERSE(0, "Universe", RankType.DONATE),
    YOUTUBE(0, "YouTube", RankType.MEDIA),
    YOUTUBE_PLUS(0, "YouTube+", RankType.MEDIA),
    TESTER(0, "QA", RankType.STAFF),
    ART(0, "ART", RankType.STAFF),
    BUILDER(0, "Строитель", RankType.STAFF),
    BUILDER_PLUS(0, "Ст. Строитель", RankType.STAFF),
    JUNIOUR(0, "Мл. Модератор", RankType.STAFF),
    MODERATOR(0, "Модератор", RankType.STAFF),
    MODERATOR_PLUS(0, "Ст. Модератор", RankType.STAFF),
    DEVELOPER(0, "Разработчик", RankType.STAFF),
    ADMINISTRATOR(0, "Администратор", RankType.STAFF),
    OWNER(0, "Владелец", RankType.STAFF);

    int priority;
    String name;
    RankType rankType;

    public boolean isHigherThan(Rank rank) {
        return this.priority > rank.getPriority();
    }

    public boolean isHigherOrEqualTo(Rank rank) {
        return this.priority >= rank.getPriority();
    }

    public boolean isLowerThan(Rank rank) {
        return this.priority < rank.getPriority();
    }

    public boolean isLowerOrEqualTo(Rank rank) {
        return this.priority <= rank.getPriority();
    }

}
