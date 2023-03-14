package com.evacipated.cardcrawl.mod.hiddeninfo.patches.minty

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object HideMintyIncomingDamage {
    @SpirePatch2(
        cls = "mintySpire.patches.ui.RenderIncomingDamagePatches\$TIDHook",
        method = "hook",
        requiredModId = "mintyspire"
    )
    object Number {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(FontHelper::class, "renderFontCentered")) {
                        m.replace(
                            "if (${HideMintyIncomingDamage::class.qualifiedName}.hide()) {" +
                                    "\$3 = \"\";" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    } else if (m.iz(FontHelper::class, "getSmartWidth")) {
                        m.replace(
                            "if (${HideMintyIncomingDamage::class.qualifiedName}.hide()) {" +
                                    "\$2 = \"\";" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }

        @JvmStatic
        @SpirePrefixPatch
        fun updateImg() {
            hiddenImgTimer -= Gdx.graphics.deltaTime
            if (hiddenImgTimer <= 0f) {
                hiddenImgTimer = 0.3f

                var newImg: Int
                do {
                    newImg = (0..6).random()
                } while (newImg == hiddenImg)
                hiddenImg = newImg
            }
        }
    }

    private var hiddenImg = 0
    private var hiddenImgTimer = 0f

    @SpirePatch2(
        cls = "mintySpire.patches.ui.RenderIncomingDamagePatches",
        method = "getAttackIntent",
        requiredModId = "mintyspire"
    )
    object Image {
        @JvmStatic
        fun Prefix(): SpireReturn<Texture> {
            if (HiddenConfig.enemyIntentDamage && HiddenConfig.enemyIntentDamageImg) {
                return SpireReturn.Return(arrayOf(
                    ImageMaster.INTENT_ATK_TIP_1,
                    ImageMaster.INTENT_ATK_TIP_2,
                    ImageMaster.INTENT_ATK_TIP_3,
                    ImageMaster.INTENT_ATK_TIP_4,
                    ImageMaster.INTENT_ATK_TIP_5,
                    ImageMaster.INTENT_ATK_TIP_6,
                    ImageMaster.INTENT_ATK_TIP_7,
                )[hiddenImg])
            }
            return SpireReturn.Continue()
        }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.enemyIntentDamage
}
