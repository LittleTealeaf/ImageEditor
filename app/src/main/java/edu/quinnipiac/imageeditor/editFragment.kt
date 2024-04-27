package edu.quinnipiac.imageeditor

import android.R.attr.height
import android.R.attr.maxHeight
import android.R.attr.maxWidth
import android.R.attr.width
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import edu.quinnipiac.imageeditor.databinding.FragmentEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt


class editFragment : Fragment() {
    private var _binding:FragmentEditBinding? = null
    private val binding get ()= _binding!!

    private lateinit var imageView: ImageView;
    private lateinit var blur_slider: Slider;
    private lateinit var bitmap: Bitmap;
    private lateinit var apply_button: Button;
    private lateinit var invert_switch: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById<ImageView>(R.id.imageView2)
        blur_slider=  view.findViewById(R.id.edit_blur_slider)
        invert_switch = view.findViewById(R.id.edit_invert)


        apply_button = view.findViewById<Button>(R.id.edit_apply)
        apply_button.setOnClickListener {
            apply_button.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) { // Launch coroutine in lifecycle scope
                Log.v("async", "Start")
                val resultsBitmap = apply_filters()
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(resultsBitmap)
                    apply_button.isEnabled = true // Re-enable button
                    Log.v("async","End")
                }
            }

        }

        var bm = arguments?.getParcelable<Bitmap>("image_bitmap")!!
        var width = bm.width
        var height = bm.height
        val maxWidth = 256
        val maxHeight = 256
        if (width > height) {
            // landscape
            val ratio = width.toFloat() / maxWidth
            width = maxWidth
            height = (height / ratio).toInt()
        } else if (height > width) {
            // portrait
            val ratio = height.toFloat() / maxHeight
            height = maxHeight
            width = (width / ratio).toInt()
        } else {
            // square
            height = maxHeight
            width = maxWidth
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height)

        bm = Bitmap.createScaledBitmap(bm, width, height, true)

        bitmap = bm

        imageView.setImageBitmap(bitmap)
    }




    suspend fun apply_filters(): Bitmap {
        var bm = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val blur_scale = blur_slider.value.roundToInt()
        if (blur_scale > 0) {
            Log.v("Filters", "Blurring")
            bm = ImgBlurUtil.applyBoxBlur(bm, blur_scale)
        }
        if(invert_switch.isActivated) {
            Log.v("Filters", "Inverting")
            bm = ImgBlurUtil.invert(bm)
        }
        return bm
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}