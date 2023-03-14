package com.evacipated.cardcrawl.mod.hiddeninfo.patches.minty

import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.mod.hiddeninfo.patches.HideShopPrices
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatch2(
    cls = "mintySpire.patches.ui.PurgeCostDisplayPatches\$RenderCardPurgeCost",
    method = "patch",
    requiredModId = "mintyspire"
)
object HideMintyPurseCost {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(StringBuilder::class, "append") && m.signature == "(I)Ljava/lang/StringBuilder;") {
                    m.replace(
                        "if (${HideShopPrices::class.qualifiedName}.hide()) {" +
                                "\$_ = \$0.append(\"?\");" +
                                "} else {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}"
                    )
                }
            }
        }
}
