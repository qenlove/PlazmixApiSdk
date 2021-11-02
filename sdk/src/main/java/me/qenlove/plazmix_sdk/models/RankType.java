package me.qenlove.plazmix_sdk.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RankType {

    DEFAULT("По умолчанию"), DONATE("Донат"), MEDIA("Медиа"), STAFF("Персонал");

    String name;
        
}