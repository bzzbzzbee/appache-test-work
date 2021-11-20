package com.example.appachetestwork.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.appachetestwork.MainActivity
import com.example.appachetestwork.R
import com.example.appachetestwork.databinding.FragmentDrawingBinding
import com.example.appachetestwork.paint.DrawView
import com.example.appachetestwork.ui.DrawingScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import petrov.kristiyan.colorpicker.ColorPicker
import java.nio.ByteBuffer


@AndroidEntryPoint
class DrawingScreenFragment : Fragment() {
    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    //TODO add arg nav arguments
    private val args = "new_project"

    private val viewModel: DrawingScreenViewModel by viewModels()

    private var projectName = "Мой проект"
    private var screenWidth = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO WindowMetrics issue on Redmi Note 4
        val bounds = (requireActivity() as MainActivity).windowManager.currentWindowMetrics.bounds
        screenWidth = minOf(bounds.width(), bounds.height())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)

        val drawView = binding.drawView
        drawView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                drawView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                drawView.init(screenWidth, screenWidth, viewModel.drawViewHelper)
            }
        })

        initButtons(drawView)
        return binding.root
    }

    private fun showToast(text: Int) = Toast.makeText(
        requireContext(), text,
        Toast.LENGTH_SHORT
    ).show()

    private fun initButtons(drawView: DrawView) {
        binding.btnBrush.setOnClickListener {
            drawView.width = 30

        }

        //TODO move to https://github.com/skydoves/ColorPickerView
        binding.btnColor.setOnClickListener {
            val colorPicker = ColorPicker(requireActivity())
            colorPicker.setOnFastChooseColorListener(object :
                ColorPicker.OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    drawView.setColor(color)
                }

                override fun onCancel() {
                    colorPicker.dismissDialog()
                    drawView.setColor(Color.BLACK)
                }
            })
                .setColumns(5)
                .setDefaultColorButton(R.color.black)
                .show()
        }

        binding.btnSave.setOnClickListener {
            //TODO move to separate save handler
            /*val file = File(projectName)
            if (file.exists())
                file.delete()
            val bitmap = drawView.save()
            val fileContents = bitmap.toByteArray()
            requireContext().openFileOutput(projectName, Context.MODE_PRIVATE).use {
                 it.write(fileContents)
            }*/
        }

        binding.btnUndo.setOnClickListener {
            drawView.undo()
        }

        binding.btnRepeat.setOnClickListener {
            drawView.repeat()
        }

        binding.btnClear.setOnClickListener {
            drawView.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun Bitmap.toByteArray(): ByteArray {
    val bytes: Int = byteCount
    val buffer: ByteBuffer = ByteBuffer.allocate(bytes)
    copyPixelsToBuffer(buffer)

    return buffer.array()
}
