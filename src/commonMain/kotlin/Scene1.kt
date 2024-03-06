import korlibs.image.color.Colors
import korlibs.korge.box2d.registerBodyWithFixture
import korlibs.korge.input.onClick
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size
import org.jbox2d.dynamics.BodyType

class Scene1 : Scene() {
    override suspend fun SContainer.sceneMain() {
        fixedSizeContainer(Size(stage!!.width, stage!!.height)) {
            solidRect(780, 30, Colors.DARKGREEN).position(10, 300).registerBodyWithFixture(
                type = BodyType.STATIC,
                friction = 1
            )
            onClick {
                val position = it.currentPosLocal
                fastRoundRect(Size(10, 10), RectCorners(1, 2), Colors.RED)
                    .position(position.x, position.y)
                    .registerBodyWithFixture(type = BodyType.DYNAMIC)
            }
        }
    }
}
