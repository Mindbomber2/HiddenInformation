package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.rewards.RewardItem
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess

@SpirePatch2(
    clz = RewardItem::class,
    method = "render"
)
object HideRewardGold {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(f: FieldAccess) {
                if (f.iz(RewardItem::class, "text") && f.isReader) {
                    f.replace(
                        "if (${HideRewardGold::class.qualifiedName}.hide()) {" +
                                "if (type == ${RewardItem.RewardType::class.qualifiedName}.GOLD) {" +
                                "\$_ = TEXT[1].trim();" +
                                "} else if (type == ${RewardItem.RewardType::class.qualifiedName}.STOLEN_GOLD) {" +
                                "\$_ = TEXT[0].trim();" +
                                "} else {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}" +
                                "} else {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.rewardGold
}
