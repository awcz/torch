import korlibs.event.Key
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.input.keys
import korlibs.korge.ldtk.view.LDTKCollisions
import korlibs.korge.ldtk.view.createCollisionMaps
import korlibs.korge.ldtk.view.readLDTKWorld
import korlibs.korge.scene.Scene
import korlibs.korge.ui.uiText
import korlibs.korge.view.SContainer
import korlibs.korge.view.View
import korlibs.korge.view.addFixedUpdater
import korlibs.math.geom.Point
import korlibs.time.hz
import korlibs.time.seconds
import korlibs.korge.ldtk.view.LDTKWorldView

private const val COLLISION_MARKER = 1
private const val GRAVITY = 10
private const val JUMP_STEP_Y = 5
private const val MOVE_STEP_X = 1.5
private const val REFRESHING_HZ = 50

class Scene1 : Scene() {

    private lateinit var collisions: LDTKCollisions
    private lateinit var player: View
    override suspend fun SContainer.sceneMain() {
        val world = resourcesVfs["ldtk/torch_map.ldtk"].readLDTKWorld()
        val mapView = LDTKWorldView(world)
        player = mapView.findViewByName("Player") ?: throw IllegalStateException()
        collisions = world.createCollisionMaps()
        this += mapView
        this += player

        val refreshing = REFRESHING_HZ.hz
        var moveInput = 0.0
        var currentMove = Point(0, 0)
        var health = 100
        val uiText = mapView.uiText("$health / 100")
        addFixedUpdater(refreshing) {
            moveIfPossible(Point(MOVE_STEP_X, 0) * moveInput)
            currentMove += Point(0, GRAVITY) * refreshing.timeSpan.seconds
            if (!moveIfPossible(currentMove)) {
                currentMove = Point.ZERO
            } else {
                health -= 1
            }

            uiText.text = "\uD83D\uDD25 $health"
        }
        mapView.keys {
            val moveSpeed = 1.2
            justDown(Key.LEFT) { moveInput = -MOVE_STEP_X * moveSpeed }
            justDown(Key.RIGHT) { moveInput = MOVE_STEP_X * moveSpeed }
            justDown(Key.SPACE, Key.UP) {
                if (currentMove.y == 0.0)
                    currentMove += Point(0, -JUMP_STEP_Y)
            }
            up(Key.LEFT, Key.RIGHT) { moveInput = 0.0 }
        }
    }

    private fun moveIfPossible(move: Point): Boolean {
        val destination = player.pos + move
        val isValidMove = collisions.getPixel(destination) != COLLISION_MARKER
        if (isValidMove) {
            player.pos = destination
        }
        return isValidMove
    }
}