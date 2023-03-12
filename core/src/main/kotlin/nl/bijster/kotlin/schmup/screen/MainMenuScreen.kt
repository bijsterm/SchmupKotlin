package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import nl.bijster.kotlin.schmup.Schmup

/** First screen of the application. Displayed after the application is created.  */
class MainMenuScreen(val game: Schmup) : Screen {
    private var camera: OrthographicCamera = OrthographicCamera()

    init {
        camera.setToOrtho(false, 320f, 240f)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.setProjectionMatrix(camera.combined)

        game.batch.begin()
        game.font.draw(game.batch, "Welcome to Drop!!! ", 100f, 150f)
        game.font.draw(game.batch, "Tap anywhere or press any key to begin!", 100f, 100f)
        game.batch.end()

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(GameScreen(game))
            dispose()
        }
    }

    override fun hide() {
    }

    override fun show() {
        game.font.data.setScale(1f)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }
}
