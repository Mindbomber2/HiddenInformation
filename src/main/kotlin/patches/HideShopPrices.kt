package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.shop.ShopScreen
import com.megacrit.cardcrawl.shop.StorePotion
import com.megacrit.cardcrawl.shop.StoreRelic
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatches2(
    SpirePatch2(
        clz = ShopScreen::class,
        method = "renderCardsAndPrices"
    ),
    SpirePatch2(
        clz = ShopScreen::class,
        method = "renderPurge"
    ),
    SpirePatch2(
        clz = StoreRelic::class,
        method = "render"
    ),
    SpirePatch2(
        clz = StorePotion::class,
        method = "render"
    )
)
object HideShopPrices {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(FontHelper::class, "renderFontLeftTopAligned")) {
                    m.replace(
                        "if (${HideShopPrices::class.qualifiedName}.hide()) {" +
                                "\$3 = ${HiddenInfoMod.Statics::class.qualifiedName}.replaceNumbers(\$3);" +
                                "}" +
                                "\$_ = \$proceed(\$\$);"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.shopPrices
}
