package nl.bijster.kotlin.schmup.types

import com.badlogic.gdx.graphics.g2d.Sprite

open class GameObject {
    lateinit var sprite: Sprite
    var isVisible = true
    var hasCollided = false
}
