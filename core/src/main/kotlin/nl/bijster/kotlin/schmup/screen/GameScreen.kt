package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import ktx.app.KtxScreen
import nl.bijster.kotlin.schmup.Shmup

class GameScreen(val shmup: Shmup) : KtxScreen {

    // load the images for the droplet & bucket, 64x64 pixels each
    private val dropImage: Texture = Texture(Gdx.files.internal("images/drop.png"))
    private val bucketImage: Texture = Texture(Gdx.files.internal("images/bucket.png"))

    // load the drop sound effect and the rain background music
    private val dropSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"))
    private val rainMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("music/rain.mp3")).apply {
        setLooping(true)
    }

    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera: OrthographicCamera = OrthographicCamera().apply {
        setToOrtho(false, 800f, 480f)
    }

    // create a Rectangle to logically represent the bucket
    private val bucket: Rectangle = Rectangle().apply {
        x = 800f / 2f - 64f / 2f  // center the bucket horizontally
        y = 20f               // bottom left bucket corner is 20px above
        //    bottom screen edge
        width = 64f
        height = 64f
    }

    // create the touchPos to store mouse click position
    private val touchPos: Vector3 = Vector3()

    // create the raindrops array and spawn the first raindrop
    private var raindrops: Array<Rectangle> = Array<Rectangle>()

    private var lastDropTime: Long = 0L
    private var dropsGathered: Int = 0

    private fun spawnRaindrop() {
        raindrops.add(Rectangle(MathUtils.random(0f, 800f - 64f), 480f, 64f, 64f))
        lastDropTime = TimeUtils.nanoTime()
    }

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        shmup.batch.projectionMatrix = camera.combined

        // begin a new batch and draw the bucket and all drops
        shmup.batch.begin()
        shmup.font.draw(shmup.batch, "Drops Collected: " + dropsGathered, 0f, 480f)
        shmup.batch.draw(bucketImage, bucket.x, bucket.y,bucket.width, bucket.height)
        for (raindrop in raindrops) {
            shmup.batch.draw(dropImage, raindrop.x, raindrop.y)
        }
        shmup.batch.end()

        // process user input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat(),0f)
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64f / 2f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // getDeltaTime returns the time passed between the last and the current frame in seconds
            bucket.x -= 200 * Gdx.graphics.getDeltaTime()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 200 * Gdx.graphics.getDeltaTime()
        }

        // make sure the bucket stays within the screen bounds
        bucket.x = MathUtils.clamp(bucket.x, 0f, 800f-64f)

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000L)
            spawnRaindrop()

        // move the raindrops, remove any that are beneath the bottom edge of the
        //    screen or that hit the bucket.  In the latter case, play back a sound
        //    effect also
        val iter = raindrops.iterator()
        while (iter.hasNext()) {
            val raindrop = iter.next()
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime()
            if (raindrop.y + 64 < 0)
                iter.remove()

            if (raindrop.overlaps(bucket)) {
                dropsGathered++
                dropSound.play()
                iter.remove()
            }
        }
    }

    // the following overrides are no-ops, unused in tutorial, but needed in
    //    order to compile a class that implements Screen
    override fun resize(width: Int, height: Int) {}

    override fun hide() {}
    override fun pause() {}
    override fun resume() {}

    override fun show() {
        shmup.font.data.setScale(5f)

        // start the playback of the background music when the screen is shown
        rainMusic.play()
    }

    override fun dispose() {
        dropImage.dispose()
        bucketImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()
    }
}
