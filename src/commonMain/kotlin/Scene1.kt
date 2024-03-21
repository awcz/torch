import korlibs.event.GameButton
import korlibs.image.color.Colors
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.KeepOnReload
import korlibs.korge.box2d.registerBodyWithFixture
import korlibs.korge.input.onClick
import korlibs.korge.ldtk.view.LDTKWorldView
import korlibs.korge.ldtk.view.readLDTKWorld
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.korge.virtualcontroller.virtualController
import korlibs.math.geom.Point
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size
import korlibs.time.hz
import org.jbox2d.dynamics.BodyType

class Scene1 : Scene() {

    @KeepOnReload
    var playerPosition = Point(0, 100)

    private lateinit var player: View
    override suspend fun SContainer.sceneMain() {
        val world = resourcesVfs["ldtk/torch_map.ldtk"].readLDTKWorld()
        val mapView = LDTKWorldView(world, showCollisions = true)
        mapView.scale = 2.0
        this += mapView
        player = this.findViewByName("Player")!!
        this += player

        fixedSizeContainer(Size(stage!!.width, stage!!.height)) {
            onClick {
                val position = it.currentPosLocal
                for (i in 1..10) {
                    fastRoundRect(Size(30, 30), RectCorners(1, 2), Colors.RED)
                        .position(position.x, position.y)
                        .registerBodyWithFixture(type = BodyType.DYNAMIC)
                }
            }
        }

        virtualController().apply {
            down(GameButton.BUTTON_SOUTH) {
                playerPosition = player.pos + Point(0, -10)
            }
            changed(GameButton.LX) {
                playerPosition = player.pos + Point(2.0, 0)
            }
            changed(GameButton.LY) {
                playerPosition = player.pos + Point(-2.0, 0)
            }
        }

        addFixedUpdater(60.hz) {
            run {
                player.x = playerPosition.x
                player.y = playerPosition.y
            }
        }
    }
}