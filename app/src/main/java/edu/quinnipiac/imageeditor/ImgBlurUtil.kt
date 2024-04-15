package edu.quinnipiac.imageeditor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import java.io.File
import java.io.FileOutputStream

class ImgBlurUtil(private val context: Context, private val inputImagePath: String, private val outputImagePath: String) {

    fun blurImage(blurRadius: Int) {
        // Load image
        val image = BitmapFactory.decodeFile(inputImagePath)

        // Apply blur
        val blurredImage = applyBoxBlur(image, blurRadius)

        // Save blurred image
        val outputStream = FileOutputStream(File(outputImagePath))
        blurredImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        println("Image blurred and saved as $outputImagePath")
    }

    private fun applyBoxBlur(image: Bitmap, blurRadius: Int): Bitmap {
        val width = image.width
        val height = image.height
        val outputImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                var totalRed = 0
                var totalGreen = 0
                var totalBlue = 0

                var count = 0
                for (dx in -blurRadius..blurRadius) {
                    for (dy in -blurRadius .. blurRadius) {
                        val neighborX = x + dx
                        val neighborY = y + dy
                        if (neighborX in 0 until width && neighborY in 0 until height) {
                            val pixel = image.getPixel(neighborX, neighborY)
                            totalRed += Color.red(pixel)
                            totalGreen += Color.green(pixel)
                            totalBlue += Color.blue(pixel)
                            count++
                        }
                    }
                }

                val avgRed = totalRed / count
                val avgGreen = totalGreen / count
                val avgBlue = totalBlue / count

                val blurredColor = Color.rgb(avgRed, avgGreen, avgBlue)
                outputImage.setPixel(x, y, blurredColor)
            }
        }

        return outputImage
    }
}
