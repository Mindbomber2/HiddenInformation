package com.evacipated.cardcrawl.mod.hiddeninfo

import basemod.*
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import kotlin.math.max
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

fun createSettingsPanel(): ModPanel {
    return object: ModPanel() {
        override fun update() {
            super.update()
            if (!BaseMod.modSettingsUp) {
                HiddenConfig.save()
            }
        }
    }.apply {
        column {
            label("Cards")
            indent {
                checkbox(HiddenConfig::cardTitles)
                checkbox(HiddenConfig::cardDescriptions)
                checkbox(HiddenConfig::cardArt)
                checkbox(HiddenConfig::cardCosts)
            }
            vspace()
            label("Relics")
            indent {
                checkbox(HiddenConfig::relicNames)
                checkbox(HiddenConfig::relicDescriptions)
                checkbox(HiddenConfig::relicCounters)
                checkbox(HiddenConfig::relicFlavor)
                checkbox(HiddenConfig::relicArt)
            }
            vspace()
            label("Potions")
            indent {
                checkbox(HiddenConfig::potionNames)
                checkbox(HiddenConfig::potionDescriptions)
                checkbox(HiddenConfig::potionArt)
            }
        }

        column {
            label("Enemies")
            indent {
                checkbox(HiddenConfig::enemy)
                checkbox(HiddenConfig::enemyHP)
                checkbox(HiddenConfig::enemyBlock)
                checkbox(HiddenConfig::enemyIntentDamage)
                indent(32f) {
                    checkbox(HiddenConfig::enemyIntentDamageImg, disableIf = not(HiddenConfig::enemyIntentDamage))
                }
                checkbox(HiddenConfig::damageNumbers)
                checkbox(HiddenConfig::enemyPowerAmount)
                checkbox(HiddenConfig::enemyPowerNames)
                checkbox(HiddenConfig::enemyPowerDescriptions)
            }
        }

        column {
            label("Player")
            indent {
                checkbox(HiddenConfig::playerHP)
                checkbox(HiddenConfig::playerBlock)
                checkbox(HiddenConfig::playerPowerAmount)
                checkbox(HiddenConfig::playerPowerNames)
                checkbox(HiddenConfig::playerPowerDescriptions)
                checkbox(HiddenConfig::playerEnergy)
                checkbox(HiddenConfig::orbNumbers)
            }
            vspace()
            label("Gold")
            indent {
                checkbox(HiddenConfig::playerGold)
                checkbox(HiddenConfig::rewardGold)
                checkbox(HiddenConfig::shopPrices)
            }
        }

        column {
            label("Map")
            indent {
                checkbox(HiddenConfig::mapNodeType)
                checkbox(HiddenConfig::bossIcon)
            }
            vspace()
            label("Events")
            indent {
                checkbox(HiddenConfig::eventNames)
                checkbox(HiddenConfig::eventText)
                checkbox(HiddenConfig::eventOptions)
                indent(32f) {
                    checkbox(HiddenConfig::eventOptionsEffect, disableIf = HiddenConfig::eventOptions)
                }
                checkbox(HiddenConfig::eventArt)
            }
        }
    }
}

private var x = 360f
private var y = 750f
private var width = 0f
private var indent = 0f

private inline fun column(rPad: Float = 20f, size: Float = -1f, block: () -> Unit) {
    val ySave = y
    block.invoke()
    x += if (size > 0) { size } else { width } + rPad
    width = 0f
    y = ySave
}

private inline fun indent(size: Float = 16f, block: () -> Unit) {
    indent += size
    x += size
    block.invoke()
    x -= size
    indent -= size
}

private fun vspace(size: Float = 16f) {
    y -= size
}

private fun ModPanel.label(text: String) {
    val font = FontHelper.buttonLabelFont
    ModLabel(text, x, y, Settings.CREAM_COLOR, font, this) {
    }.let {
        this.addUIElement(it)
        y -= font.lineHeight / Settings.scale
        val w = FontHelper.getWidth(it.font, it.text, 1f)
        width = max(width, w / Settings.scale + indent)
    }
}

private fun ModPanel.checkbox(kprop: KMutableProperty0<Boolean>, vararg nothings: MyNothing, disableIf: () -> Boolean = {false}) {
    val text = HiddenConfig._strings[kprop.name] ?: kprop.name
    val tooltip = HiddenConfig._strings["${kprop.name}_tooltip"]
    ModDisableLabeledToggleButton(text, tooltip, x, y, Settings.CREAM_COLOR, FontHelper.tipBodyFont, kprop.get(), this, disableIf, {}) {
        kprop.set(it.enabled)
    }.let {
        this.addUIElement(it)
        kprop.isAccessible = true
        (kprop.getDelegate() as? HiddenConfig.Companion.Setting)?.setModToggleButton(it.toggle)
        y -= ImageMaster.OPTION_TOGGLE.height
        val hb = ReflectionHacks.getPrivate<Hitbox>(it.toggle, ModToggleButton::class.java, "hb")
        width = max(width, hb.width / Settings.scale + indent)
    }
}

private fun not(kprop: KProperty0<Boolean>): () -> Boolean =
    { !kprop.get() }

private class MyNothing private constructor() {}
