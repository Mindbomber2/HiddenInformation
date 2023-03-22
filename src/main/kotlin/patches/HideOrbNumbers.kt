package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.Loader
import com.evacipated.cardcrawl.modthespire.ModInfo
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.orbs.AbstractOrb
import javassist.ClassPool
import javassist.CtBehavior
import javassist.NotFoundException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import org.clapper.util.classutil.*
import java.io.File
import java.net.URISyntaxException

object HideOrbNumbers {
    @SpirePatch2(
        clz = AbstractOrb::class,
        method = "renderText"
    )
    object PassiveEvokeText {
        @JvmStatic
        fun Raw(ctBehavior: CtBehavior) {
            val pool = ctBehavior.declaringClass.classPool
            val finder = ClassFinder()
            finder.add(File(Loader.STS_JAR))
            finder.add(Loader.MODINFOS
                .mapNotNull(ModInfo::jarURL)
                .mapNotNull {
                    try {
                        it.toURI()
                    } catch (ignore: URISyntaxException) {
                        null

                    }
                }
                .map { File(it) }
            )

            val filter = AndClassFilter(
                NotClassFilter(InterfaceOnlyClassFilter()),
                Class.forName("basemod.patches.whatmod.PotionTips\$SuperClassFilter")
                    .getConstructor(ClassPool::class.java, Class::class.java)
                    .also { it.isAccessible = true }
                    .newInstance(pool, AbstractOrb::class.java) as ClassFilter
            )
            val foundClasses = arrayListOf<ClassInfo>()
            finder.findClasses(foundClasses, filter)

            val ctSpriteBatch = pool.get(SpriteBatch::class.java.name)

            foundClasses.forEach { classInfo ->
                val ctClass = pool.get(classInfo.className)
                try {
                    val renderText = ctClass.getDeclaredMethod("renderText", arrayOf(ctSpriteBatch))
                    renderText.insertBefore(
                        "if (${HideOrbNumbers::class.qualifiedName}.hide()) { return; }"
                    )
                } catch (ignored: NotFoundException) {}
            }
        }
    }

    @SpirePatch2(
        clz = AbstractOrb::class,
        method = "update"
    )
    object TooltipNumber {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(TipHelper::class, "renderGenericTip")) {
                        m.replace(
                            "if (${HideOrbNumbers::class.qualifiedName}.hide()) {" +
                                    "\$4 = ${HiddenInfoMod.Statics::class.qualifiedName}.replaceNumbers(\$4);" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.orbNumbers
}
