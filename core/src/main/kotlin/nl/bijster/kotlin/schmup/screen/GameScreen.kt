package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxScreen
import ktx.graphics.use
import nl.bijster.kotlin.schmup.Shmup
import nl.bijster.kotlin.schmup.attackwave.AttackWave
import nl.bijster.kotlin.schmup.attackwave.drops.GreenDrops
import nl.bijster.kotlin.schmup.attackwave.drops.PurpleDrops
import nl.bijster.kotlin.schmup.player.Player
import nl.bijster.kotlin.schmup.scores.HiScoreTable
import nl.bijster.kotlin.schmup.scores.Score


class GameScreen(private val shmup: Shmup) : KtxScreen {

    private val player: Player = Player(3)
    private val greenDrops: AttackWave = GreenDrops()
    private val purpleDrops: AttackWave = PurpleDrops()

    // load the drop sound effect and the rain background music
    private val rainMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("music/rain.mp3")).apply {
        isLooping = true
    }

    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera: OrthographicCamera = OrthographicCamera().apply {
        setToOrtho(false, 800f, 600f)
    }



    override fun render(delta: Float) {
        // Alle updates

        // 1. Player first!!
        player.update(delta)

        // 2. Player Bullets

        // 3. Attack-waves
        greenDrops.update(delta)
        purpleDrops.update(delta)

        greenDrops.detectAndHandleCollision(player.playerGameObject)
        purpleDrops.detectAndHandleCollision(player.playerGameObject)

        if (player.playerGameObject.hasCollided) {
            val gameOver = player.die()
            if (gameOver) {
                switchScreens()
                HiScoreTable += Pair(Score.score, 1.0)
                Score.score = 0
            }

        }

        shmup.rectBatch.begin(ShapeRenderer.ShapeType.Line)

        // begin a new batch and draw all
        shmup.batch.use {
            // 0. Text
            shmup.font.draw(it, "Score: ${Score.score}", 0f, 600f)
            shmup.font.draw(it, "Nr. Lives: ${player.nrOfLives}", 500f, 600f)
            // 1. Draw player first
            player.draw(it, shmup.rectBatch)

            greenDrops.draw(it, shmup.rectBatch)
            purpleDrops.draw(it, shmup.rectBatch)

            // 3. Bullets

            // 4. Player Hit-box, must be upper-level!
        }

        shmup.rectBatch.end()

    }

    private fun switchScreens() {
        shmup.addScreen(MainMenuScreen(shmup))
        shmup.setScreen<MainMenuScreen>()
        shmup.removeScreen<GameScreen>()
        dispose()
    }

    override fun show() {

        player.init()
        greenDrops.init()
        purpleDrops.init()

        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        shmup.batch.projectionMatrix = camera.combined
        shmup.rectBatch.projectionMatrix = camera.combined


        shmup.font.data.setScale(3f)

        // start the playback of the background music when the screen is shown
        // TODO: rainMusic.play()
    }

    override fun dispose() {
        rainMusic.dispose()

        player.dispose()
        greenDrops.cleanup()
        purpleDrops.cleanup()
    }
}
