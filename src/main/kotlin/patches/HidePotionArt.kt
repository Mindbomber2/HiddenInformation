package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.vfx.FlashPotionEffect
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess

object HidePotionArt {
    @SpirePatches2(
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "render"
        ),
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "shopRender"
        ),
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "labRender"
        ),
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "renderShiny"
        )
    )
    object Shop {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.iz(AbstractPotion::class, "liquidColor") && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${Color::class.qualifiedName}.DARK_GRAY;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    } else if ((f.iz(AbstractPotion::class, "hybridColor") || f.iz(AbstractPotion::class, "spotsColor")) && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = null;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    } else if (f.iz(AbstractPotion::class, "liquidImg") && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${ImageMaster::class.qualifiedName}.POTION_SPHERE_LIQUID;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    } else if (f.iz(AbstractPotion::class, "containerImg") && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${ImageMaster::class.qualifiedName}.POTION_SPHERE_CONTAINER;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatches2(
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "renderLightOutline",
            paramtypez = [SpriteBatch::class]
        ),
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "renderOutline",
            paramtypez = [SpriteBatch::class]
        ),
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "renderOutline",
            paramtypez = [SpriteBatch::class, Color::class]
        )
    )
    object Outline {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.iz(AbstractPotion::class, "outlineImg") && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${ImageMaster::class.qualifiedName}.POTION_SPHERE_OUTLINE;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = FlashPotionEffect::class,
        method = SpirePatch.CONSTRUCTOR
    )
    object FlashEffectColor {
        @JvmStatic
        fun Postfix(___liquidColor: Color) {
            if (hideArt()) {
                ___liquidColor.set(Color.DARK_GRAY)
            }
        }
    }

    @SpirePatch2(
        clz = FlashPotionEffect::class,
        method = "render",
        paramtypez = [SpriteBatch::class, Float::class, Float::class]
    )
    object FlashEffect {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if ((f.iz(FlashPotionEffect::class, "renderHybrid") || f.iz(FlashPotionEffect::class, "renderSpots")) && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = false;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    } else if (f.iz(FlashPotionEffect::class, "liquidImg") && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${ImageMaster::class.qualifiedName}.POTION_SPHERE_LIQUID;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    } else if (f.iz(FlashPotionEffect::class, "containerImg") && f.isReader) {
                        f.replace(
                            "if (${HidePotionArt::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${ImageMaster::class.qualifiedName}.POTION_SPHERE_CONTAINER;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = AbstractPotion::class,
        method = "generateSparkles"
    )
    object NoSparkles {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (hideArt()) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @JvmStatic
    fun hideArt(): Boolean =
        HiddenConfig.potionArt
}
