package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import ktx.app.KtxScreen
import nl.bijster.kotlin.schmup.Shmup
import nl.bijster.kotlin.schmup.player.Player
import nl.bijster.kotlin.schmup.types.HiScoreTable

class GameScreen(private val shmup: Shmup) : KtxScreen {

    val player: Player = Player(3)

    // load the images for the droplet & bucket, 64x64 pixels each
    private val dropImage: Texture = Texture(Gdx.files.internal("images/drop.png"))

    // load the drop sound effect and the rain background music
    private val dropSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"))
    private val rainMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("music/rain.mp3")).apply {
        isLooping = true
    }

    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera: OrthographicCamera = OrthographicCamera().apply {
        setToOrtho(false, 800f, 480f)
    }




    // create the raindrops array and spawn the first raindrop
    private var raindrops: Array<Rectangle> = Array<Rectangle>()

    private var lastDropTime: Long = 0L
    private var score: Int = 0

    private fun spawnRaindrop() {
        raindrops.add(Rectangle(MathUtils.random(0f, 800f - 64f), 480f, 64f, 64f))
        lastDropTime = TimeUtils.nanoTime()
    }

    override fun render(delta: Float) {
        // Alle updates
        player.update(delta)

        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        shmup.batch.projectionMatrix = camera.combined

        // begin a new batch and draw the bucket and all drops
        shmup.batch.begin()

        // 0. Text
        shmup.font.draw(shmup.batch, "Nr. Lives: ${player.nrOfLives} \t Drops Avoided: $score", 0f, 480f)

        // 1. Draw player first
        player.draw(shmup.batch)

        // 2. Attachwaves
        for (raindrop in raindrops) {
            shmup.batch.draw(dropImage, raindrop.x, raindrop.y)
        }
        // 3. Bullets
        // 4. Player Hitbox

        shmup.batch.end()


        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000L)
            spawnRaindrop()

        // move the raindrops, remove any that are beneath the bottom edge of the
        //    screen or that hit the bucket.  In the latter case, play back a sound
        //    effect also
        val iter = raindrops.iterator()
        while (iter.hasNext()) {
            val raindrop = iter.next()
            raindrop.y -= 200 * Gdx.graphics.deltaTime
            if (raindrop.y + 64 < 0) {
                iter.remove()
                score++
            }
            if (raindrop.overlaps(player.playerRectangle)) {
                dropSound.play()
                iter.remove()

                val gameOver = player.die()
                if (gameOver) {
                    switchScreens()
                    HiScoreTable.add(score, 1.0)
                }
            }
        }
    }

    private fun switchScreens() {
        shmup.addScreen(MainMenuScreen(shmup))
        shmup.setScreen<MainMenuScreen>()
        shmup.removeScreen<GameScreen>()
        dispose()
    }

    override fun show() {
        shmup.font.data.setScale(3f)

        // start the playback of the background music when the screen is shown
        rainMusic.play()
    }

    override fun dispose() {
        dropImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()

        player.dispose()
    }
}
