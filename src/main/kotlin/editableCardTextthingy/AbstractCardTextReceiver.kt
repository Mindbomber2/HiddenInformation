package com.evacipated.cardcrawl.mod.hiddeninfo.editableCardTextthingy

import basemod.ReflectionHacks
import basemod.interfaces.TextReceiver
import basemod.patches.com.megacrit.cardcrawl.helpers.input.ScrollInputProcessor.TextInput
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.scale
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.localization.CardStrings

class AbstractCardTextReceiver(
    CARD: AbstractCard
) : TextReceiver {
    private val cardStrings: CardStrings
    private val EXTENDED_DESCRIPTION: Array<String>?


    private val card: AbstractCard
    var maybeDescription: CardDescriptionDatabase.CardDescription


    init {
        card = CARD
        maybeDescription = CardDescriptionDatabase.get(card.cardID)
        cardStrings = CardCrawlGame.languagePack.getCardStrings(card.cardID)
        card.rawDescription = CardDescriptionDatabase.get(card.cardID).getRawDescription(card.upgraded)
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION

        CardDescriptionDatabase.cardCreated(card)
    }

    /**@Suppress("unused")
    protected fun finishInit() {
    initializeTitle()
    initializeDescription()

    cost = COST
    costForTurn = cost

    baseDamage = DAMAGE ?: baseDamage
    baseBlock = BLOCK ?: baseBlock
    baseMagicNumber = MAGIC ?: baseMagicNumber
    magicNumber = baseMagicNumber
    baseMagicNumber2 = MAGIC2 ?: baseMagicNumber2
    magicNumber2 = baseMagicNumber2

    if (!CardLibrary.getAllCards().isNullOrEmpty()) {
    CardArtRoller.computeCard(this)
    } else {
    needsArtRefresh = true
    }
    }*/

    /**@SpireOverride
    protected fun getDescFont(): BitmapFont {
    return SpireSuper.call()
    }*/

    var typingBarOn = true
    var typingBarTimer = -1f
    val gl: GlyphLayout = GlyphLayout()
    val TEXT_WIDTH = 250.scale()
    /**
    @SpireOverride
    protected fun renderDescription(sb: SpriteBatch) {
    //        SpireSuper.call<Void>(sb)

    val DESC_OFFSET_Y = if (Settings.BIG_TEXT_MODE) {
    IMG_HEIGHT * 0.24f
    } else {
    IMG_HEIGHT * 0.255f
    }

    val font = getDescFont()
    val origLineHeight = font.lineHeight
    font.data.setLineHeight(font.capHeight * 1.45f / drawScale)

    gl.setText(
    font,
    rawDescription,
    Color.WHITE,
    TEXT_WIDTH * drawScale,
    Align.center,
    true
    )
    val drawX = current_x - TEXT_WIDTH * drawScale / 2f
    val drawY = current_y - IMG_HEIGHT * drawScale / 2f + DESC_OFFSET_Y * drawScale + gl.height / 2f
    font.draw(sb, gl, drawX, drawY)

    font.data.setLineHeight(origLineHeight)

    if (typingBarTimer >= 0f) {
    if (typingBarOn) {
    // render "typing" bar
    gl.runs.lastOrNull()?.let { lastGlyph ->
    var xAdvance = 0f
    for (i in 0 until lastGlyph.xAdvances.size) {
    xAdvance += lastGlyph.xAdvances[i]
    }
    sb.draw(
    ImageMaster.WHITE_SQUARE_IMG,
    if (rawDescription.endsWith('\n')) { current_x } else { drawX + lastGlyph.x + xAdvance },
    drawY - gl.height,
    2f * drawScale,
    font.capHeight
    )
    } ?: run {
    sb.draw(
    ImageMaster.WHITE_SQUARE_IMG,
    current_x,
    drawY - font.capHeight,
    2f * drawScale,
    font.capHeight
    )
    }
    }

    typingBarTimer -= Gdx.graphics.rawDeltaTime
    if (typingBarTimer < 0f) {
    typingBarTimer = 1f
    typingBarOn = !typingBarOn
    }
    }
    }
     **/

    /**override fun update() {
    super.update()
    if (needsArtRefresh) {
    needsArtRefresh = false
    CardArtRoller.computeCard(this)
    }

    if (HitboxRightClick.rightClicked[this.hb]) {
    onRightClick()
    } else if ((InputHelper.justClickedLeft || InputHelper.justClickedRight) && !hb.hovered) {
    onKeyDown(Input.Keys.ESCAPE)
    }
    }*/

    /**override fun upgradeName() {
    super.upgradeName()
    if (UPGRADE_NAME != null) {
    name = UPGRADE_NAME
    initializeTitle()
    }

    rawDescription = maybeDescription.getRawDescription(true)
    initializeDescription()
    }*/


    fun onRightClick() {
        if (!TextInput.isTextInputActive()) {
            TextInput.startTextReceiver(this)
            typingBarTimer = 1f
            typingBarOn = true
        }
    }

    override fun onKeyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE) {
            TextInput.stopTextReceiver(this)
            typingBarTimer = -1f
            CardDescriptionDatabase.saveDescriptions()
            try {
                val input = Gdx.input
                ReflectionHacks.getPrivate<BooleanArray>(
                    input,
                    Input::class.java,
                    "justPressedKeys"
                )[Input.Keys.ESCAPE] = false
            } catch (_: Exception) {
            }
        }
        return false
    }

    override fun onPushEnter(): Boolean {
        return false
    }

    // because basemod is bugged
    override fun onPushTab(): Boolean {
        return false
    }

    override fun getCurrentText(): String {
        return maybeDescription.get(card.upgraded) ?: ""
    }

    override fun setText(updatedText: String) {
        maybeDescription.set(card.upgraded, updatedText)
        CardDescriptionDatabase.update(card.cardID, maybeDescription)
    }

    override fun isDone(): Boolean {
        return false
    }

    override fun acceptCharacter(c: Char): Boolean {
        return when (c) {
            '\n', '\r' -> true
            else -> FontHelper.cardDescFont_N.data.hasGlyph(c)
        }
    }

    override fun getAppendedText(c: Char): String {
        return when (c) {
            '\n', '\r' -> "\n"
            // because basemod is bugged
            '\t' -> ""
            else -> super.getAppendedText(c)
        }
    }
}