import korlibs.image.color.Colors
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.box2d.registerBodyWithFixture
import korlibs.korge.input.onClick
import korlibs.korge.ldtk.view.LDTKWorldView
import korlibs.korge.ldtk.view.readLDTKWorld
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size
import org.jbox2d.dynamics.BodyType

class Scene1 : Scene() {
    override suspend fun SContainer.sceneMain() {


        fixedSizeContainer(Size(stage!!.width, stage!!.height)) {
            solidRect(580, 30, Colors.DARKGREEN).position(110, 300).registerBodyWithFixture(
                type = BodyType.STATIC,
                friction = 1
            )
            onClick {
                val position = it.currentPosLocal
                for (i in 1..10) {
                    fastRoundRect(Size(30, 30), RectCorners(1, 2), Colors.RED)
                        .position(position.x, position.y)
                        .registerBodyWithFixture(type = BodyType.DYNAMIC)
                }
            }
        }
        val world = resourcesVfs["ldtk/torch_map.ldtk"].readLDTKWorld()
        val mapView = LDTKWorldView(world, showCollisions = true)
        this += mapView
    }
}
