package com.evacipated.cardcrawl.mod.hiddeninfo

import basemod.ModLabel
import basemod.ModLabeledToggleButton
import basemod.ModPanel
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.isAccessible

fun createSettingsPanel(): ModPanel {
    return ModPanel().apply {
        label("Cards")
        indent {
            checkbox(HiddenConfig::cardTitles)
            checkbox(HiddenConfig::cardDescriptions)
            checkbox(HiddenConfig::cardArt)
            checkbox(HiddenConfig::cardCosts)
        }
    }
}

private var x = 360f
private var y = 750f

private inline fun indent(size: Float = 16f, block: () -> Unit) {
    x += size
    block.invoke()
    x -= size
}

private fun ModPanel.label(text: String) {
    val font = FontHelper.buttonLabelFont
    ModLabel(text, x, y, Settings.CREAM_COLOR, font, this) {
    }.let {
        this.addUIElement(it)
        y -= font.lineHeight
    }
}

private fun ModPanel.checkbox(kprop: KMutableProperty0<Boolean>) {
    val text = HiddenConfig._strings[kprop.name] ?: kprop.name
    ModLabeledToggleButton(text, x, y, Settings.CREAM_COLOR, FontHelper.tipBodyFont, true, this, {}) {
        kprop.set(it.enabled)
    }.let {
        this.addUIElement(it)
        kprop.isAccessible = true
        (kprop.getDelegate() as? HiddenConfig.Companion.Setting)?.setModToggleButton(it.toggle)
        y -= ImageMaster.OPTION_TOGGLE.height
    }
}
