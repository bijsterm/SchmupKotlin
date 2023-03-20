package nl.bijster.kotlin.schmup.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import nl.bijster.kotlin.schmup.types.GameObject

class Player(var nrOfLives: Int) : GameObject() {

    private val playerImage: Texture = Texture(Gdx.files.internal("images/bucket.png"))

    // create a Rectangle to logically represent the player
    val playerRectangle: Rectangle = Rectangle().apply {
        x = 800f / 2f - 64f / 2f // center the bucket horizontally
        y = 20f // bottom left bucket corner is 20px above
        width = 64f
        height = 64f
    }

    fun die(): Boolean {
        nrOfLives -= 1
        return (nrOfLives == 0)
    }

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // getDeltaTime returns the time passed between the last and the current frame in seconds
            playerRectangle.x -= 300 * deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerRectangle.x += 300 * deltaTime
        }

        // make sure the bucket stays within the screen bounds
        playerRectangle.x = MathUtils.clamp(playerRectangle.x, 0f, 800f - 64f)

    }

    override fun draw(batch: SpriteBatch) {
        batch.draw(playerImage, playerRectangle.x, playerRectangle.y, playerRectangle.width, playerRectangle.height)
    }

    override fun dispose() {
        playerImage.dispose()
    }
}
