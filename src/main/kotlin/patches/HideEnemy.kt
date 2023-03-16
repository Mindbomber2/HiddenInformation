package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.assetPath
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.MonsterGroup
import javassist.CtBehavior
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.expr.NewExpr

object HideEnemy {
    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "render"
    )
    object Model {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(SpriteBatch::class, "draw")) {
                        m.replace(
                            "if (img == ${Model::class.qualifiedName}.getMissingno()) {" +
                                    "\$4 = (hb.height / img.getHeight()) * img.getWidth();" +
                                    "\$2 = hb.cX - (\$4 / 2f) + animX;" +
                                    "\$3 = hb.y + animY;" +
                                    "\$5 = hb.height;" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }

        private var atlasSave: TextureAtlas? = null
        private var imgSave: Texture? = null

        @JvmStatic
        @SpireInsertPatch(
            locator = LocatorStart::class
        )
        fun EnabledMissingno(__instance: AbstractMonster, @ByRef ___atlas: Array<TextureAtlas?>, @ByRef ___img: Array<Texture?>) {
            if (hide()) {
                atlasSave = ___atlas[0]
                ___atlas[0] = null
                imgSave = ___img[0]
                ___img[0] = missingno
            }
        }

        @JvmStatic
        @SpireInsertPatch(
            locator = LocatorEnd::class
        )
        fun DisableMissingno(__instance: AbstractMonster, @ByRef ___atlas: Array<TextureAtlas?>, @ByRef ___img: Array<Texture?>) {
            if (atlasSave != null) {
                ___img[0] = null
                ___atlas[0] = atlasSave
            } else if (imgSave != null) {
                ___img[0] = imgSave
            }
            atlasSave = null
            imgSave = null
        }

        class LocatorStart : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior): IntArray {
                val matcher = Matcher.FieldAccessMatcher(AbstractMonster::class.java, "atlas")
                return LineFinder.findInOrder(ctBehavior, matcher)
            }
        }

        class LocatorEnd : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior): IntArray {
                val matcher = Matcher.FieldAccessMatcher(MonsterGroup::class.java, "hoveredMonster")
                return LineFinder.findInOrder(ctBehavior, matcher)
            }
        }

        @JvmStatic
        val missingno by lazy { ImageMaster.loadImage("images/missingno.png".assetPath(), false) }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "dispose"
    )
    object DontDisposeMissingno {
        @JvmStatic
        fun Prefix(@ByRef ___img: Array<Texture?>) {
            if (___img[0] == Model.missingno) {
                ___img[0] = null;
            }
        }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "renderName"
    )
    object Name {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (hide()) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "renderTip"
    )
    object NameInPowerTooltips {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (e.iz(PowerTip::class)) {
                        e.replace(
                            "if (${HideEnemy::class.qualifiedName}.hide()) {" +
                                    "\$2 = ${NameInPowerTooltips::class.qualifiedName}.replaceName(this, \$2);" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }

        @JvmStatic
        fun replaceName(owner: AbstractCreature?, description: String): String {
            val name = owner?.name
            if (name != null) {
                return description
                    .replace(name, "?")
                    .replace(FontHelper.colorString(name, "y"), "#y???")
            }
            return description
        }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.enemy
}
