package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.app.KtxScreen
import ktx.graphics.use
import nl.bijster.kotlin.schmup.Shmup
import nl.bijster.kotlin.schmup.types.HiScoreTable

/** First screen of the application. Displayed after the application is created.  */
class MainMenuScreen(private val shmup: Shmup) : KtxScreen {

    private val camera = OrthographicCamera().apply {
        setToOrtho(false, 800f, 480f)
    }

    override fun render(delta: Float) {

        val scores = HiScoreTable.get().map { (score, _) ->
            score
        }

        shmup.batch.use {
            shmup.font.draw(it, "Welcome to Drop!!! ", 100f, 200f)
            shmup.font.draw(it, "Hi-scores: $scores ", 100f, 150f)
            shmup.font.draw(it, "Tap anywhere or press any key to begin!", 100f, 100f)
        }

        if (Gdx.input.isTouched || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            shmup.addScreen(GameScreen(shmup))
            shmup.setScreen<GameScreen>()
            shmup.removeScreen<MainMenuScreen>()
            dispose()
        }
    }

    override fun show() {
        camera.update()
        shmup.batch.projectionMatrix = camera.combined

        shmup.font.data.setScale(1f)
    }

}
