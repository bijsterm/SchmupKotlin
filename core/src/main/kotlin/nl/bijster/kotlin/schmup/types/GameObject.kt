package nl.bijster.kotlin.schmup.types

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

open class GameObject {
    lateinit var sprite: Sprite
    var isVisible = true
    var hasCollided = false

    fun draw(rectBatch: ShapeRenderer) {
        if (showBoundingBoxes) rectBatch.rect(
            sprite.boundingRectangle.x,
            sprite.boundingRectangle.y,
            sprite.boundingRectangle.width,
            sprite.boundingRectangle.height
        )
    }

    fun draw(batch: SpriteBatch) {
        this.sprite.draw(batch)
    }

    companion object {
        var showBoundingBoxes: Boolean = false
    }
}
