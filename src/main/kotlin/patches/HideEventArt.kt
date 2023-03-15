package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.badlogic.gdx.graphics.Texture
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.events.GenericEventDialog

@SpirePatch2(
    clz = GenericEventDialog::class,
    method = "render"
)
object HideEventArt {
    private var imgSave: Texture? = null

    @JvmStatic
    fun Prefix(@ByRef ___img: Array<Texture?>) {
        if (HiddenConfig.eventArt) {
            imgSave = ___img[0]
            ___img[0] = null
        }
    }

    @JvmStatic
    fun Postfix(@ByRef ___img: Array<Texture?>) {
        if (imgSave != null) {
            ___img[0] = imgSave
            imgSave = null
        }
    }
}
