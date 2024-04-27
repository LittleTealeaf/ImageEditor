package edu.quinnipiac.imageeditor

import android.graphics.Bitmap
import android.graphics.Color

class ImgBlurUtil {
    companion object {
        fun applyBoxBlur(image: Bitmap, blurRadius: Int): Bitmap {
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

        fun invert(bitmap: Bitmap): Bitmap {
            val width = bitmap.width
            val height = bitmap.height

            val invertedBitmap = Bitmap.createBitmap(width, height, bitmap.config)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    val pixel = bitmap.getPixel(x, y)

                    // Extracting RGB components
                    val alpha = pixel shr 24 and 0xFF
                    val red = 255 - (pixel shr 16 and 0xFF)
                    val green = 255 - (pixel shr 8 and 0xFF)
                    val blue = 255 - (pixel and 0xFF)

                    // Combining inverted RGB components into a new pixel value
                    val invertedPixel = (alpha shl 24) or (red shl 16) or (green shl 8) or blue

                    // Setting the inverted pixel in the new bitmap
                    invertedBitmap.setPixel(x, y, invertedPixel)
                }
            }

            return invertedBitmap
        }


    }
}
