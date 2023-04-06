package nl.bijster.kotlin.schmup.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
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

    lateinit var playerGameObject: GameObject


    private fun createPlayerGameObject() {
        playerGameObject = GameObject(playerImage).apply {
            sprite.apply {
                x = (SCREEN_WIDTH - width) / 2f // center the bucket horizontally
                y = 20f // bottom left bucket corner is 20px above
            }
            hitbox.setOrigin(sprite.width / 2f, sprite.height / 2f)
            hitbox.setScale(0.1f, 0.1f)
        }
    }

    override fun init() {
        createPlayerGameObject()
    }

    fun die(): Boolean {
        nrOfLives -= 1
        playerGameObject.collidedWith =
            emptyList<GameObject>().toMutableList() // Reset the list of Objects the player has collided with.

        debug { "Nr of lives: $nrOfLives" }
        return (nrOfLives == 0)
    }

    override fun update(deltaTime: Float) {
        val playerSprite = playerGameObject.sprite
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

        playerGameObject.hitbox.setPosition(playerSprite.x, playerSprite.y)
    }

    override fun draw(batch: SpriteBatch, rectBatch: ShapeRenderer) {
        val playerSprite = playerGameObject.sprite
//        batch.draw(playerImage, playerRectangle.x, playerRectangle.y, playerRectangle.width, playerRectangle.height)
//        batch.draw(playerSprite, playerSprite.x, playerSprite.y, 10f, 10f)
        playerSprite.draw(batch)
//        rectBatch.rect(
//            playerSprite.boundingRectangle.x,
//            playerSprite.boundingRectangle.y,
//            playerSprite.boundingRectangle.width,
//            playerSprite.boundingRectangle.height
//        )
        rectBatch.polygon(playerGameObject.hitbox.transformedVertices)

    }

    override fun dispose() {
        playerImage.dispose()
    }
}
