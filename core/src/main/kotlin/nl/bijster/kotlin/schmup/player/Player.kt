package nl.bijster.kotlin.schmup.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import ktx.log.debug
import nl.bijster.kotlin.schmup.constants.SCREEN_WIDTH
import nl.bijster.kotlin.schmup.types.GameObject
import nl.bijster.kotlin.schmup.types.GameObjectFlow

const val PLAYER_SPEED = 300f

class Player(var nrOfLives: Int) : GameObjectFlow {

    private val playerImage: Texture = Texture(Gdx.files.internal("images/bucket.png"))
// TODO:   private val playerHitboxImage: Texture = Texture(Gdx.files.internal("images/Player_Hitbox.png"))

    lateinit var playerGameObject: GameObject
    lateinit var playerSprite: Sprite
    fun die(): Boolean {
        nrOfLives -= 1
        playerGameObject.hasCollided = false // reset hasCollided
        debug { "Nr of lives: $nrOfLives" }
        return (nrOfLives == 0)
    }

    private fun createPlayerGameObject() {
        playerSprite = Sprite(playerImage).apply {
            x = (SCREEN_WIDTH - width) / 2f // center the bucket horizontally
            y = 20f // bottom left bucket corner is 20px above
            setOriginCenter()
        }
        playerGameObject = GameObject().apply {
            sprite = playerSprite
        }
    }

    override fun init() {
        createPlayerGameObject()
    }

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // getDeltaTime returns the time passed between the last and the current frame in seconds
            playerSprite.x -= PLAYER_SPEED * deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerSprite.x += PLAYER_SPEED * deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // getDeltaTime returns the time passed between the last and the current frame in seconds
            playerSprite.y += PLAYER_SPEED * deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerSprite.y -= PLAYER_SPEED * deltaTime
        }

        // make sure the bucket stays within the screen bounds
        playerSprite.x = MathUtils.clamp(playerSprite.x, 0f, 800 - playerSprite.width)
        playerSprite.y = MathUtils.clamp(playerSprite.y, 0f, 600 - playerSprite.height)

    }

    override fun draw(batch: SpriteBatch, rectBatch: ShapeRenderer) {
//        batch.draw(playerImage, playerRectangle.x, playerRectangle.y, playerRectangle.width, playerRectangle.height)
//        batch.draw(playerSprite, playerSprite.x, playerSprite.y, 10f, 10f)
        playerSprite.draw(batch)
        rectBatch.rect(
            playerSprite.boundingRectangle.x,
            playerSprite.boundingRectangle.y,
            playerSprite.boundingRectangle.width,
            playerSprite.boundingRectangle.height
        )

    }

    override fun dispose() {
        playerImage.dispose()
    }
}
