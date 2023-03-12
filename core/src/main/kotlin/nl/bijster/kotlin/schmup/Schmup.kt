package nl.bijster.kotlin.schmup

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import nl.bijster.kotlin.schmup.screen.MainMenuScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Schmup : Game() {
    lateinit var batch: SpriteBatch      // public is the default modifier
    lateinit var font: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        // use LibGDX's default Arial font
        font = BitmapFont()

        this.setScreen(MainMenuScreen(this))
    }

    override fun render() {
        super.render()  // important!
    }

    override fun dispose() {
        this.getScreen().dispose()

        batch.dispose()
        font.dispose()
    }
}
