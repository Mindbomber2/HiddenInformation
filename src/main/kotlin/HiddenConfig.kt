package com.evacipated.cardcrawl.mod.hiddeninfo

import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils
import com.google.gson.GsonBuilder
import imgui.ImGui
import java.nio.file.Paths
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
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
    var potionNames: Boolean = true,
    var potionDescriptions: Boolean = true,
    var potionArt: Boolean = true,
    var orbNumbers: Boolean = true,
    var eventOptionsEffect: Boolean = true,
    var eventOptions: Boolean = false,
) {
    companion object {
        @Transient private var dirty: Boolean = false

        private lateinit var INSTANCE: HiddenConfig

        private class Setting {
            private lateinit var realProp: KMutableProperty1<HiddenConfig, Boolean>

            private fun init(name: String) {
                if (!this::realProp.isInitialized) {
                    realProp = HiddenConfig::class.declaredMemberProperties
                            .filterIsInstance<KMutableProperty1<HiddenConfig, Boolean>>()
                            .firstOrNull { it.name == name } ?: throw NoSuchElementException(name)
                }
            }

            operator fun getValue(thisRef: Companion, property: KProperty<*>): Boolean {
                init(property.name)
                return realProp.get(INSTANCE)
            }

            operator fun setValue(thisRef: Companion, property: KProperty<*>, value: Any?) {
                if (value is Boolean) {
                    init(property.name)
                    if (realProp.get(INSTANCE) != value) dirty = true
                    realProp.set(INSTANCE, value)
                }
            }
        }

        var enemyHP: Boolean by Setting()
        var playerHP: Boolean by Setting()

        var enemyBlock: Boolean by Setting()
        var enemyIntentDamage: Boolean by Setting()
        var enemyIntentDamageImg: Boolean by Setting()
        var enemyPowerAmount: Boolean by Setting()
        var enemyPowerDescriptions: Boolean by Setting()
        var enemyPowerNames: Boolean by Setting()
        var enemy: Boolean by Setting()

        var playerBlock: Boolean by Setting()
        var playerPowerAmount: Boolean by Setting()
        var playerPowerDescriptions: Boolean by Setting()
        var playerPowerNames: Boolean by Setting()
        var playerEnergy: Boolean by Setting()

        var playerGold: Boolean by Setting()
        var shopPrices: Boolean by Setting()

        var damageNumbers: Boolean by Setting()

        var mapNodeType: Boolean by Setting()
        var bossIcon: Boolean by Setting()

        var cardDescriptions: Boolean by Setting()
        var cardTitles: Boolean by Setting()
        var cardCosts: Boolean by Setting()
        var cardArt: Boolean by Setting()

        var relicNames: Boolean by Setting()
        var relicDescriptions: Boolean by Setting()
        var relicFlavor: Boolean by Setting()
        var relicArt: Boolean by Setting()

        var potionNames: Boolean by Setting()
        var potionDescriptions: Boolean by Setting()
        var potionArt: Boolean by Setting()

        var orbNumbers: Boolean by Setting()

        var eventOptionsEffect: Boolean by Setting()
        var eventOptions: Boolean by Setting()

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
