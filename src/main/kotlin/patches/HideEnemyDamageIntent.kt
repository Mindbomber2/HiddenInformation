package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.monsters.AbstractMonster
import kotlin.random.Random

object HideEnemyDamageIntent {
    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "renderDamageRange"
    )
    object Intent {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.enemyIntentDamage) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "updateIntentTip"
    )
    object Tooltip {
        @JvmStatic
        fun Postfix(__instance: AbstractMonster, ___intentTip: PowerTip) {
            if (HiddenConfig.enemyIntentDamage && __instance.intent.name.contains("ATTACK")) {
                ___intentTip.body = ___intentTip.body.replace(HiddenInfoMod.numberRegex, "?")
            }
        }
    }

    @SpirePatches2(
        SpirePatch2(
            clz = AbstractMonster::class,
            method = "getAttackIntent",
            paramtypez = [Int::class]
        ),
        SpirePatch2(
            clz = AbstractMonster::class,
            method = "getAttackIntent",
            paramtypez = []
        )
    )
    object Img {
        @JvmStatic
        fun Prefix(__instance: AbstractMonster): SpireReturn<Texture> {
            if (HiddenConfig.enemyIntentDamage && HiddenConfig.enemyIntentDamageImg) {
                return SpireReturn.Return(arrayOf(
                    ImageMaster.INTENT_ATK_1,
                    ImageMaster.INTENT_ATK_2,
                    ImageMaster.INTENT_ATK_3,
                    ImageMaster.INTENT_ATK_4,
                    ImageMaster.INTENT_ATK_5,
                    ImageMaster.INTENT_ATK_6,
                    ImageMaster.INTENT_ATK_7,
                )[ImgFields.hiddenImg[__instance]])
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "getAttackIntentTip",
        paramtypez = []
    )
    object TipImg {
        @JvmStatic
        fun Prefix(__instance: AbstractMonster): SpireReturn<Texture> {
            if (HiddenConfig.enemyIntentDamage && HiddenConfig.enemyIntentDamageImg) {
                return SpireReturn.Return(arrayOf(
                    ImageMaster.INTENT_ATK_TIP_1,
                    ImageMaster.INTENT_ATK_TIP_2,
                    ImageMaster.INTENT_ATK_TIP_3,
                    ImageMaster.INTENT_ATK_TIP_4,
                    ImageMaster.INTENT_ATK_TIP_5,
                    ImageMaster.INTENT_ATK_TIP_6,
                    ImageMaster.INTENT_ATK_TIP_7,
                )[ImgFields.hiddenImg[__instance]])
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "updateIntent"
    )
    object ImgUpdate {
        @JvmStatic
        fun Prefix(__instance: AbstractMonster, @ByRef ___intentImg: Array<Texture>, ___intentTip: PowerTip) {
            ImgFields.hiddenImgTimer[__instance] -= Gdx.graphics.deltaTime
            if (ImgFields.hiddenImgTimer[__instance] <= 0f) {
                ImgFields.hiddenImgTimer[__instance] = 0.3f

                var newImg: Int
                do {
                    newImg = (0..6).random()
                } while (newImg == ImgFields.hiddenImg[__instance])
                ImgFields.hiddenImg[__instance] = newImg

                if (__instance.intent.name.contains("ATTACK")) {
                    ___intentImg[0] = ReflectionHacks.privateMethod(
                        AbstractMonster::class.java,
                        "getIntentImg"
                    ).invoke(__instance)

                    ___intentTip.img = ReflectionHacks.privateMethod(
                        AbstractMonster::class.java,
                        "getAttackIntentTip"
                    ).invoke(__instance)
                }
            }
        }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = SpirePatch.CLASS
    )
    private object ImgFields {
        @JvmField
        val hiddenImg: SpireField<Int> = SpireField { (0..6).random() }
        @JvmField
        val hiddenImgTimer: SpireField<Float> = SpireField { Random.nextFloat() * 0.3f }
    }
}
