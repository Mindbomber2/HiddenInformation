package com.evacipated.cardcrawl.mod.hiddeninfo

import basemod.ModToggleButton
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
    var enemyHP: Boolean = false,
    var enemyBlock: Boolean = false,
    var enemyIntentDamage: Boolean = false,
    var enemyIntentDamageImg: Boolean = false,
    var enemyPowerAmount: Boolean = false,
    var enemyPowerDescriptions: Boolean = false,
    var enemyPowerNames: Boolean = false,
    var enemy: Boolean = false,
    var playerHP: Boolean = false,
    var playerBlock: Boolean = false,
    var playerPowerAmount: Boolean = false,
    var playerPowerDescriptions: Boolean = false,
    var playerPowerNames: Boolean = false,
    var playerEnergy: Boolean = false,
    var playerGold: Boolean = false,
    var rewardGold: Boolean = false,
    var shopPrices: Boolean = false,
    var damageNumbers: Boolean = false,
    var mapNodeType: Boolean = false,
    var bossIcon: Boolean = false,
    var cardDescriptions: Boolean = false,
    var cardTitles: Boolean = false,
    var cardCosts: Boolean = false,
    var cardArt: Boolean = false,
    var relicNames: Boolean = false,
    var relicDescriptions: Boolean = false,
    var relicCounters: Boolean = false,
    var relicFlavor: Boolean = false,
    var relicArt: Boolean = false,
    var potionNames: Boolean = false,
    var potionDescriptions: Boolean = false,
    var potionArt: Boolean = false,
    var orbNumbers: Boolean = false,
    var eventOptionsEffect: Boolean = false,
    var eventOptions: Boolean = false,
    var eventNames: Boolean = false,
    var eventText: Boolean = false,
    var eventArt: Boolean = false,
) {
    companion object {
        @Transient internal val _strings: Map<String, String> = CardCrawlGame.languagePack.getUIString("HiddenConfig".makeID())?.TEXT_DICT ?: emptyMap()
        @Transient private var _dirty: Boolean = false

        private lateinit var _INSTANCE: HiddenConfig

        internal class Setting {
            private lateinit var realProp: KMutableProperty1<HiddenConfig, Boolean>
            private var modToggleButton: ModToggleButton? = null

            private fun init(name: String) {
                if (!this::realProp.isInitialized) {
                    realProp = HiddenConfig::class.declaredMemberProperties
                            .filterIsInstance<KMutableProperty1<HiddenConfig, Boolean>>()
                            .firstOrNull { it.name == name } ?: throw NoSuchElementException(name)
                }
            }

            fun setModToggleButton(btn: ModToggleButton) {
                modToggleButton = btn
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
                    modToggleButton?.enabled = value
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
        var rewardGold: Boolean by Setting()
        var shopPrices: Boolean by Setting()

        var damageNumbers: Boolean by Setting()

        var mapNodeType: Boolean by Setting()
        var bossIcon: Boolean by Setting()

        var cardDescriptions: Boolean by Setting()
        var cardTitles: Boolean by Setting()
        var cardCosts: Boolean by Setting()
        var cardArt: Boolean by Setting()
        var cardBetaArt: Boolean by Setting()

        var relicNames: Boolean by Setting()
        var relicDescriptions: Boolean by Setting()
        var relicCounters: Boolean by Setting()
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

            _INSTANCE = if (configFile.exists()) {
                val gson = GsonBuilder()
                    .create()
                gson.fromJson(configFile.reader(), HiddenConfig::class.java)
            } else {
                HiddenConfig()
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

        private fun enableDisableAll(enable: Boolean) {
            Companion::class.declaredMemberProperties
                .filter { !it.name.startsWith("_") }
                .filter { it.returnType == Boolean::class.createType() }
                .filterIsInstance<KMutableProperty1<Companion, Boolean>>()
                .forEach { it.set(this, enable) }
        }
        internal fun enableAll() = enableDisableAll(true)
        internal fun disableAll() = enableDisableAll(false)

        internal fun imgui() {
            if (ImGui.begin("Hidden Information")) {
                if (ImGui.collapsingHeader("Cards")) {
                    makeCheckbox(::cardTitles)
                    makeCheckbox(::cardDescriptions)
                    makeCheckbox(::cardArt)
                    ImGui.beginDisabled(cardArt)
                    ImGui.indent()
                    makeCheckbox(::cardBetaArt)
                    ImGui.unindent()
                    ImGui.endDisabled()
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
                    makeCheckbox(::damageNumbers)
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
                    makeCheckbox(::playerEnergy)
                }

                if (ImGui.collapsingHeader("Gold")) {
                    makeCheckbox(::playerGold)
                    makeCheckbox(::rewardGold)
                    makeCheckbox(::shopPrices)
                }

                if (ImGui.collapsingHeader("Relics")) {
                    makeCheckbox(::relicNames)
                    makeCheckbox(::relicDescriptions)
                    makeCheckbox(::relicCounters)
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
                    makeCheckbox(::eventNames)
                    makeCheckbox(::eventText)
                    makeCheckbox(::eventOptions)
                    ImGui.beginDisabled(eventOptions)
                    ImGui.indent()
                    makeCheckbox(::eventOptionsEffect)
                    ImGui.unindent()
                    ImGui.endDisabled()
                    makeCheckbox(::eventArt)
                }

                if (ImGui.collapsingHeader("Map")) {
                    makeCheckbox(::mapNodeType)
                    makeCheckbox(::bossIcon)
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
