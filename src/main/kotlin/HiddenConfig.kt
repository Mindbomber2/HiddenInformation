package com.evacipated.cardcrawl.mod.hiddeninfo

import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils
import com.google.gson.GsonBuilder
import imgui.ImGui
import java.nio.file.Paths
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties

data class HiddenConfig(
    var enemyHP: Boolean = true,
    var enemyBlock: Boolean = true,
    var enemyIntentDamage: Boolean = true,
    var enemyIntentDamageImg: Boolean = true,
    var enemyPowerAmount: Boolean = true,
    var enemyPowerDescriptions: Boolean = true,
    var enemyPowerNames: Boolean = true,
    var enemy: Boolean = true,
    var playerHP: Boolean = true,
    var playerBlock: Boolean = true,
    var playerPowerAmount: Boolean = true,
    var playerPowerDescriptions: Boolean = true,
    var playerPowerNames: Boolean = true,
    var playerEnergy: Boolean = true,
    var playerGold: Boolean = true,
    var shopPrices: Boolean = true,
    var damageNumbers: Boolean = true,
    var mapNodeType: Boolean = true,
    var bossIcon: Boolean = true,
    var cardDescriptions: Boolean = true,
    var cardTitles: Boolean = true,
    var cardCosts: Boolean = true,
    var cardArt: Boolean = true,
    var relicNames: Boolean = true,
    var relicDescriptions: Boolean = true,
    var relicFlavor: Boolean = true,
    var relicArt: Boolean = true,
    var orbNumbers: Boolean = true,
) {
    companion object {
        @Transient private var dirty: Boolean = false

        private lateinit var INSTANCE: HiddenConfig

        var enemyHP: Boolean
            get() = INSTANCE.enemyHP
            set(value) {
                if (INSTANCE.enemyHP != value) dirty = true
                INSTANCE.enemyHP = value
            }
        var enemyBlock: Boolean
            get() = INSTANCE.enemyBlock
            set(value) {
                if (INSTANCE.enemyBlock != value) dirty = true
                INSTANCE.enemyBlock = value
            }
        var enemyIntentDamage: Boolean
            get() = INSTANCE.enemyIntentDamage
            set(value) {
                if (INSTANCE.enemyIntentDamage != value) dirty = true
                INSTANCE.enemyIntentDamage = value
            }
        var enemyIntentDamageImg: Boolean
            get() = INSTANCE.enemyIntentDamageImg
            set(value) {
                if (INSTANCE.enemyIntentDamageImg != value) dirty = true
                INSTANCE.enemyIntentDamageImg = value
            }
        var enemyPowerAmount: Boolean
            get() = INSTANCE.enemyPowerAmount
            set(value) {
                if (INSTANCE.enemyPowerAmount != value) dirty = true
                INSTANCE.enemyPowerAmount = value
            }
        var enemyPowerDescriptions: Boolean
            get() = INSTANCE.enemyPowerDescriptions
            set(value) {
                if (INSTANCE.enemyPowerDescriptions != value) dirty = true
                INSTANCE.enemyPowerDescriptions = value
            }
        var enemyPowerNames: Boolean
            get() = INSTANCE.enemyPowerNames
            set(value) {
                if (INSTANCE.enemyPowerNames != value) dirty = true
                INSTANCE.enemyPowerNames = value
            }
        var enemy: Boolean
            get() = INSTANCE.enemy
            set(value) {
                if (INSTANCE.enemy != value) dirty = true
                INSTANCE.enemy = value
            }

        var playerHP: Boolean
            get() = INSTANCE.playerHP
            set(value) {
                if (INSTANCE.playerHP != value) dirty = true
                INSTANCE.playerHP = value
            }
        var playerBlock: Boolean
            get() = INSTANCE.playerBlock
            set(value) {
                if (INSTANCE.playerBlock != value) dirty = true
                INSTANCE.playerBlock = value
            }
        var playerPowerAmount: Boolean
            get() = INSTANCE.playerPowerAmount
            set(value) {
                if (INSTANCE.playerPowerAmount != value) dirty = true
                INSTANCE.playerPowerAmount = value
            }
        var playerPowerDescriptions: Boolean
            get() = INSTANCE.playerPowerDescriptions
            set(value) {
                if (INSTANCE.playerPowerDescriptions != value) dirty = true
                INSTANCE.playerPowerDescriptions = value
            }
        var playerPowerNames: Boolean
            get() = INSTANCE.playerPowerNames
            set(value) {
                if (INSTANCE.playerPowerNames != value) dirty = true
                INSTANCE.playerPowerNames = value
            }
        var playerEnergy: Boolean
            get() = INSTANCE.playerEnergy
            set(value) {
                if (INSTANCE.playerEnergy != value) dirty = true
                INSTANCE.playerEnergy = value
            }
        var playerGold: Boolean
            get() = INSTANCE.playerGold
            set(value) {
                if (INSTANCE.playerGold != value) dirty = true
                INSTANCE.playerGold = value
            }
        var shopPrices: Boolean
            get() = INSTANCE.shopPrices
            set(value) {
                if (INSTANCE.shopPrices != value) dirty = true
                INSTANCE.shopPrices = value
            }

        var damageNumbers: Boolean
            get() = INSTANCE.damageNumbers
            set(value) {
                if (INSTANCE.damageNumbers != value) dirty = true
                INSTANCE.damageNumbers = value
            }

        var mapNodeType: Boolean
            get() = INSTANCE.mapNodeType
            set(value) {
                if (INSTANCE.mapNodeType != value) dirty = true
                INSTANCE.mapNodeType = value
            }
        var bossIcon: Boolean
            get() = INSTANCE.bossIcon
            set(value) {
                if (INSTANCE.bossIcon != value) dirty = true
                INSTANCE.bossIcon = value
            }

        var cardDescriptions: Boolean
            get() = INSTANCE.cardDescriptions
            set(value) {
                if (INSTANCE.cardDescriptions != value) dirty = true
                INSTANCE.cardDescriptions = value
            }
        var cardTitles: Boolean
            get() = INSTANCE.cardTitles
            set(value) {
                if (INSTANCE.cardTitles != value) dirty = true
                INSTANCE.cardTitles = value
            }
        var cardCosts: Boolean
            get() = INSTANCE.cardCosts
            set(value) {
                if (INSTANCE.cardCosts != value) dirty = true
                INSTANCE.cardCosts = value
            }
        var cardArt: Boolean
            get() = INSTANCE.cardArt
            set(value) {
                if (INSTANCE.cardArt != value) dirty = true
                INSTANCE.cardArt = value
            }

        var relicNames: Boolean
            get() = INSTANCE.relicNames
            set(value) {
                if (INSTANCE.relicNames != value) dirty = true
                INSTANCE.relicNames = value
            }
        var relicDescriptions: Boolean
            get() = INSTANCE.relicDescriptions
            set(value) {
                if (INSTANCE.relicDescriptions != value) dirty = true
                INSTANCE.relicDescriptions = value
            }
        var relicFlavor: Boolean
            get() = INSTANCE.relicFlavor
            set(value) {
                if (INSTANCE.relicFlavor != value) dirty = true
                INSTANCE.relicFlavor = value
            }
        var relicArt: Boolean
            get() = INSTANCE.relicArt
            set(value) {
                if (INSTANCE.relicArt != value) dirty = true
                INSTANCE.relicArt = value
            }

        var orbNumbers: Boolean
            get() = INSTANCE.orbNumbers
            set(value) {
                if (INSTANCE.orbNumbers != value) dirty = true
                INSTANCE.orbNumbers = value
            }

        fun load() {
            val configPath = Paths.get(ConfigUtils.CONFIG_DIR, "Hidden Information", "config.json")
            val configFile = Gdx.files.absolute(configPath.toString())

            if (configFile.exists()) {
                val gson = GsonBuilder()
                    .create()
                INSTANCE = gson.fromJson(configFile.reader(), HiddenConfig::class.java)
            }
        }

        fun save() {
            if (!dirty) {
                return
            }

            val configPath = Paths.get(ConfigUtils.CONFIG_DIR, "Hidden Information", "config.json")
            val configFile = Gdx.files.absolute(configPath.toString())
            configFile.parent().mkdirs()

            val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()
            val json = gson.toJson(INSTANCE)
            configFile.writeString(json, false)

            dirty = false
        }

        internal fun imgui() {
            if (ImGui.begin("Hidden Information")) {
                HiddenConfig::class.declaredMemberProperties.forEach { kprop ->
                    if (kprop is KMutableProperty1 && kprop.returnType == Boolean::class.createType()) {
                        if (ImGui.checkbox(kprop.name, kprop.get(INSTANCE) as Boolean)) {
                            (kprop as KMutableProperty1<HiddenConfig, Boolean>).set(INSTANCE, !kprop.get(INSTANCE))
                        }
                    }
                }
            }
            ImGui.end()
        }
    }
}
