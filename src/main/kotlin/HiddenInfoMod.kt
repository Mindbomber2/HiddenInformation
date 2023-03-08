package com.evacipated.cardcrawl.mod.hiddeninfo

import basemod.BaseMod
import basemod.ModPanel
import basemod.interfaces.EditStringsSubscriber
import basemod.interfaces.ImGuiSubscriber
import basemod.interfaces.PostInitializeSubscriber
import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.assetPath
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.localization.UIStrings
import imgui.ImGui

@SpireInitializer
class HiddenInfoMod :
    PostInitializeSubscriber,
    EditStringsSubscriber,
    ImGuiSubscriber
{
    companion object Statics {
        val ID: String = "hiddeninfo"
        val NAME: String = "Hidden Information"

        @Suppress("unused")
        @JvmStatic
        fun initialize() {
            BaseMod.subscribe(HiddenInfoMod())
        }

        fun makeID(id: String): String {
            return "$ID:$id"
        }
        fun assetPath(path: String): String {
            return "${ID}Assets/$path"
        }
    }

    override fun receivePostInitialize() {
        HiddenConfig.load()
        HiddenConfig.save()

        // Make settings panel
        val settingsPanel = ModPanel()

        BaseMod.registerModBadge(
            ImageMaster.loadImage("images/modBadge.png".assetPath()),
            NAME,
            "kiooeht",
            "TODO",
            settingsPanel
        )
    }

    override fun receiveImGui() {
        HiddenConfig.imgui()
    }

    private fun makeLocPath(language: Settings.GameLanguage, filename: String): String {
        val langPath = language.name.toLowerCase()
        return "localization/$langPath/$filename.json".assetPath()
    }

    private fun loadLocFile(language: Settings.GameLanguage, stringType: Class<*>) {
        val locPath = makeLocPath(language, stringType.simpleName)
        if (Gdx.files.internal(locPath).exists()) {
            BaseMod.loadCustomStringsFile(stringType, locPath)
        }
    }

    private fun loadLocFiles(language: Settings.GameLanguage) {
        loadLocFile(language, UIStrings::class.java)
    }

    override fun receiveEditStrings() {
        loadLocFiles(Settings.GameLanguage.ENG)
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadLocFiles(Settings.language)
        }
    }
}
