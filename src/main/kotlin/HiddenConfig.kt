package com.evacipated.cardcrawl.mod.hiddeninfo

import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.makeID
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils
import com.google.gson.GsonBuilder
import com.megacrit.cardcrawl.core.CardCrawlGame
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
    var eventNames: Boolean = true,
    var eventText: Boolean = true,
    var eventArt: Boolean = true,
) {
    companion object {
        @Transient private val _strings: Map<String, String> = CardCrawlGame.languagePack.getUIString("HiddenConfig".makeID())?.TEXT_DICT ?: emptyMap()
        @Transient private var _dirty: Boolean = false

        private lateinit var _INSTANCE: HiddenConfig

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
                return realProp.get(_INSTANCE)
            }

            operator fun setValue(thisRef: Companion, property: KProperty<*>, value: Any?) {
                if (value is Boolean) {
                    init(property.name)
                    if (realProp.get(_INSTANCE) != value) _dirty = true
                    realProp.set(_INSTANCE, value)
                }
            }
        }

        var enemyHP: Boolean by Setting()
        var enemyBlock: Boolean by Setting()
        var enemyIntentDamage: Boolean by Setting()
        var enemyIntentDamageImg: Boolean by Setting()
        var enemyPowerAmount: Boolean by Setting()
        var enemyPowerDescriptions: Boolean by Setting()
        var enemyPowerNames: Boolean by Setting()
        var enemy: Boolean by Setting()

        var playerHP: Boolean by Setting()
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
        var eventNames: Boolean by Setting()
        var eventText: Boolean by Setting()
        var eventArt: Boolean by Setting()

        fun load() {
            val configPath = Paths.get(ConfigUtils.CONFIG_DIR, "Hidden Information", "config.json")
            val configFile = Gdx.files.absolute(configPath.toString())

            if (configFile.exists()) {
                val gson = GsonBuilder()
                    .create()
                _INSTANCE = gson.fromJson(configFile.reader(), HiddenConfig::class.java)
            }
        }

        fun save() {
            if (!_dirty) {
                return
            }

            val configPath = Paths.get(ConfigUtils.CONFIG_DIR, "Hidden Information", "config.json")
            val configFile = Gdx.files.absolute(configPath.toString())
            configFile.parent().mkdirs()

            val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()
            val json = gson.toJson(_INSTANCE)
            configFile.writeString(json, false)

            _dirty = false
        }

        internal fun imgui() {
            if (ImGui.begin("Hidden Information")) {
                if (ImGui.collapsingHeader("Cards")) {
                    makeCheckbox(::cardTitles)
                    makeCheckbox(::cardDescriptions)
                    makeCheckbox(::cardArt)
                    makeCheckbox(::cardCosts)
                }

                if (ImGui.collapsingHeader("Enemies")) {
                    makeCheckbox(::enemy)
                    makeCheckbox(::enemyHP)
                    makeCheckbox(::enemyBlock)
                    makeCheckbox(::enemyIntentDamage)
                    ImGui.beginDisabled(!enemyIntentDamage)
                    ImGui.indent()
                    makeCheckbox(::enemyIntentDamageImg)
                    ImGui.unindent()
                    ImGui.endDisabled()
                    makeCheckbox(::enemyPowerAmount)
                    makeCheckbox(::enemyPowerNames)
                    makeCheckbox(::enemyPowerDescriptions)
                }

                if (ImGui.collapsingHeader("Player")) {
                    makeCheckbox(::playerHP)
                    makeCheckbox(::playerBlock)
                    makeCheckbox(::playerPowerAmount)
                    makeCheckbox(::playerPowerNames)
                    makeCheckbox(::playerPowerDescriptions)
                }

                if (ImGui.collapsingHeader("Gold")) {
                    makeCheckbox(::playerGold)
                    makeCheckbox(::shopPrices)
                }

                if (ImGui.collapsingHeader("Relics")) {
                    makeCheckbox(::relicNames)
                    makeCheckbox(::relicDescriptions)
                    makeCheckbox(::relicFlavor)
                    makeCheckbox(::relicArt)
                }

                if (ImGui.collapsingHeader("Potions")) {
                    makeCheckbox(::potionNames)
                    makeCheckbox(::potionDescriptions)
                    makeCheckbox(::potionArt)
                }

                if (ImGui.collapsingHeader("Orbs")) {
                    makeCheckbox(::orbNumbers)
                }

                if (ImGui.collapsingHeader("Events")) {
                    makeCheckbox(::eventOptions)
                    ImGui.beginDisabled(eventOptions)
                    ImGui.indent()
                    makeCheckbox(::eventOptionsEffect)
                    ImGui.unindent()
                    ImGui.endDisabled()
                    makeCheckbox(::eventNames)
                    makeCheckbox(::eventText)
                    makeCheckbox(::eventArt)
                }

                if (ImGui.collapsingHeader("All Settings")) {
                    Companion::class.declaredMemberProperties
                        .filter { !it.name.startsWith("_") }
                        .filter { it.returnType == Boolean::class.createType() }
                        .filterIsInstance<KMutableProperty1<Companion, Boolean>>()
                        .forEach { makeCheckbox(this, it) }
                }
            }
            ImGui.end()
        }

        private fun makeCheckbox(kprop: KMutableProperty0<Boolean>) {
            val text = _strings[kprop.name]?.let {
                "$it###${kprop.name}"
            } ?: kprop.name
            if (ImGui.checkbox(text, kprop.get())) {
                kprop.set(!kprop.get())
            }
        }

        private fun makeCheckbox(thisRef: Companion, kprop: KMutableProperty1<Companion, Boolean>) {
            if (ImGui.checkbox(kprop.name, kprop.get(thisRef))) {
                kprop.set(thisRef, !kprop.get(thisRef))
            }
        }
    }
}
