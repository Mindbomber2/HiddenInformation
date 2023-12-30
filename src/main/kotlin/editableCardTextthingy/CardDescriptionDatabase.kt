package com.evacipated.cardcrawl.mod.hiddeninfo.editableCardTextthingy

import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.megacrit.cardcrawl.cards.AbstractCard
import java.nio.charset.StandardCharsets
import java.util.*

object CardDescriptionDatabase {
    private val descriptions: MutableMap<String, CardDescription> = mutableMapOf()
    private val liveCards: MutableMap<String, WeakHashMap<AbstractCard, Boolean>> = mutableMapOf()

    internal fun loadSavedDescriptions() {
        val gson = Gson()
        val handle = Gdx.files.absolute(SpireConfig.makeFilePath(HiddenInfoMod.ID, "descriptions", "json"))
        if (handle.exists()) {
            val tmpType = object : TypeToken<Map<String, CardDescription>>() {}.type
            gson.fromJson<Map<String, CardDescription>>(handle.reader(), tmpType)?.let {
                descriptions.putAll(it)
            }
        }
    }

    internal fun saveDescriptions() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val handle = Gdx.files.absolute(SpireConfig.makeFilePath(HiddenInfoMod.ID, "descriptions", "json"))
        handle.writeString(gson.toJson(descriptions), false, StandardCharsets.UTF_8.toString())
    }

    fun get(cardId: String): CardDescription {
        return descriptions.getOrPut(cardId) { CardDescription() }
    }

    fun update(cardId: String, description: CardDescription) {
        descriptions[cardId] = description

        liveCards[cardId]?.forEach { (c, _) ->
            c.rawDescription = description.getRawDescription(c.upgraded)
            c.initializeDescription()
        }
    }

    internal fun cardCreated(card: AbstractCard) {
        liveCards.getOrPut(card.cardID) { WeakHashMap() }[card] = true
    }

    data class CardDescription(var description: String? = null, var upgradeDescription: String? = null) {
        fun getRawDescription(upgraded: Boolean): String {
            return get(upgraded) ?: "" //?.replace("\n", " NL ") ?: ""
        }

        fun get(upgraded: Boolean): String? {
            return if (upgraded) {
                upgradeDescription ?: description
            } else {
                description ?: upgradeDescription
            }
        }

        fun set(upgraded: Boolean, text: String) {
            if (upgraded) {
                upgradeDescription = text
            } else {
                description = text
            }
        }
    }
}