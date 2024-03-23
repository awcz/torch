import korlibs.image.color.Colors
import korlibs.korge.Korge
import korlibs.korge.scene.sceneContainer
import korlibs.math.geom.Size

suspend fun main() = Korge(
    title = "Torch",
    windowSize = Size(800, 250),
    backgroundColor = Colors["#000222"],
) {
    println("Hello KorGE")
    sceneContainer().changeTo {
        Scene1()
    }
}