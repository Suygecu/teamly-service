package com.suygecu.teamly_service.service.jira

enum class ContentType(val contentType: String, val uuidPage: String, ) {
    CONCEPT("Concept", "1-otchet-podrazdeleniya-hudozhestvennoj-grafiki-HeK6oXxWXS  "),
    CHARACTER("3dcharacter", "2-otchet-podrazdeleniya-modelirovaniya-personazhej-ITlSoyqGpm"),
    WEAPON("3dweapon", "3-otchet-podrazdeleniya-modelirovaniya-oruzhiya-Ylqu5Uylps"),
    PROPS("3dprops", "4-otchet-podrazdeleniya-modelirovaniya-propsov-WBMa1Jq7GQ"),
    ANIMATION("Animation", "5-otchet-podrazdeleniya-animirovaniya-trehmernoj-grafiki-ROHq5WtN8Y"),
    EFFECTS("Effects", "6-otchet-podrazdeleniya-vfx-PBT3YuMzJq"),
    SOUND("Sound", "7-otchet-podrazdeleniya-zvukovogo-soprovozhdeniya-uBIImjYPYl"),
    ARMOR_TEXTURING("ArmorTexturing", "8-otchet-podrazdeleniya-teksturirovaniya-modelej-armor-PL8hN8atyB"),
    WEAPON_TEXTURING("WeaponTexturing", "9-otchet-podrazdeleniya-teksturirovaniya-modelej-weapon-o5QgrptTD3"),
    TECHPART("TechPart", "10-otchet-podrazdeleniya-tehnicheskih-hudozhnikov-plKAJaRbja")
}