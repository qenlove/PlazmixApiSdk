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
    STAR(1, "Star", RankType.DONATE),
    COSMO(2, "Cosmo", RankType.DONATE),
    GALAXY(3, "Galaxy", RankType.DONATE),
    UNIVERSE(4, "Universe", RankType.DONATE),
    YOUTUBE(5, "YouTube", RankType.MEDIA),
    YOUTUBE_PLUS(6, "YouTube+", RankType.MEDIA),
    TESTER(7, "QA", RankType.STAFF),
    ART(8, "ART", RankType.STAFF),
    BUILDER(9, "Строитель", RankType.STAFF),
    BUILDER_PLUS(10, "Ст. Строитель", RankType.STAFF),
    JUNIOUR(11, "Мл. Модератор", RankType.STAFF),
    MODERATOR(12, "Модератор", RankType.STAFF),
    MODERATOR_PLUS(13, "Ст. Модератор", RankType.STAFF),
    DEVELOPER(14, "Разработчик", RankType.STAFF),
    ADMINISTRATOR(15, "Администратор", RankType.STAFF),
    OWNER(16, "Владелец", RankType.STAFF);

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
