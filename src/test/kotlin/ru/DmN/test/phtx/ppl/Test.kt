package ru.DmN.test.phtx.ppl

import ru.DmN.phtx.ppl.container.CFourfold
import ru.DmN.phtx.ppl.element.EImage
import ru.DmN.phtx.ppl.container.CPair
import ru.DmN.phtx.ppl.container.CTriple
import ru.DmN.phtx.ppl.page.PageList
import ru.DmN.phtx.ppl.utils.Presentation
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater {
            Presentation("Тестовая Презентация", 1000).apply {
                this += PageList(
                    mutableListOf(
                        CFourfold(
                            EImage(ImageIO.read(Test::class.java.getResourceAsStream("/test/phtx/ppl/img0.jpg"))),
                            EImage(ImageIO.read(Test::class.java.getResourceAsStream("/test/phtx/ppl/img1.jpg"))),
                            EImage(ImageIO.read(Test::class.java.getResourceAsStream("/test/phtx/ppl/img2.jpg"))),
                            EImage(ImageIO.read(Test::class.java.getResourceAsStream("/test/phtx/ppl/img3.jpg")))
                        )
                    )
                )
                //
                this.show()
            }
        }
    }
}