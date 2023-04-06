package nl.bijster.kotlin.schmup.types

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon


open class GameObject(private val texture: Texture) {
    var sprite: Sprite = Sprite(texture).apply {
        setOriginCenter()
    }

    private val baseVertices = floatArrayOf(
        1f, 1f,                         // Left bottom
        sprite.width, 1f,               // right bottom
        sprite.width, sprite.height,    // right top
        1f, sprite.height               // left top
    )
    var hitbox: Polygon = Polygon(baseVertices).apply {
        setOrigin(texture.width / 2f, texture.height / 2f)
    }

    var isVisible = true
    var collidedWith: MutableList<GameObject> = mutableListOf()

    fun draw(rectBatch: ShapeRenderer) {
        if (showBoundingBoxes) rectBatch.polygon(hitbox.transformedVertices)
    }

    fun draw(batch: SpriteBatch) {
        this.sprite.draw(batch)
    }

    companion object {
        var showBoundingBoxes: Boolean = false
    }
}
