package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.badlogic.gdx.graphics.Texture
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.map.Legend
import com.megacrit.cardcrawl.rooms.AbstractRoom

object HideMapNodeType {
    @SpirePatch2(
        clz = AbstractRoom::class,
        method = "getMapImg"
    )
    object Img {
        @JvmStatic
        fun Prefix(): SpireReturn<Texture> {
            if (HiddenConfig.mapNodeType) {
                return SpireReturn.Return(ImageMaster.MAP_NODE_EVENT)
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = AbstractRoom::class,
        method = "getMapImgOutline"
    )
    object Outline {
        @JvmStatic
        fun Prefix(): SpireReturn<Texture> {
            if (HiddenConfig.mapNodeType) {
                return SpireReturn.Return(ImageMaster.MAP_NODE_EVENT_OUTLINE)
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatches2(
        SpirePatch2(
            clz = Legend::class,
            method = "update"
        ),
        SpirePatch2(
            clz = Legend::class,
            method = "render"
        )
    )
    object DisableLegend {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.mapNodeType) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }
}
