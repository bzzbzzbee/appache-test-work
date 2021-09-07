package com.example.appachetestwork.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.appachetestwork.databinding.DrawingScreenBinding
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.appachetestwork.R
import com.example.appachetestwork.ui.DrawingScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import petrov.kristiyan.colorpicker.ColorPicker
import java.io.File
import java.nio.ByteBuffer
import petrov.kristiyan.colorpicker.ColorPicker.OnFastChooseColorListener


@AndroidEntryPoint
class DrawingScreenFragment : Fragment() {
    private var _binding: DrawingScreenBinding? = null
    private val binding get() = _binding!!

    private val args: DrawingScreenFragmentArgs by navArgs()

    private val viewModel: DrawingScreenViewModel by viewModels()

    private var projectName = "Мой проект"

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DrawingScreenBinding.inflate(inflater, container, false)

        val drawView = binding.drawView
        val screenWidth = requireActivity().windowManager.currentWindowMetrics.bounds.width()
        drawView.width = screenWidth

        val vto: ViewTreeObserver = drawView.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                drawView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width: Int = drawView.measuredWidth
                val height: Int = drawView.measuredHeight
                if (args.projectId == "new_project") {
                    projectName += " 1"
                    drawView.init(height, width)
                } else {
                    val project = viewModel.getProject(args.projectId)
                    try {
                        val file = File(requireContext().filesDir, project?.name)
                        val bitmap = BitmapFactory.decodeFile(file.path)
                        projectName += " ${project?.id?.plus(1)}"
                        drawView.init(height, width, bitmap)
                    } catch (t: Throwable) {
                        Log.e("File open ex: ", t.message.toString())
                        showToast(R.string.cant_open_project)
                        projectName += " 1"
                        drawView.init(height, width)
                    }
                }
            }
        })

        binding.btnBrush.setOnClickListener {
            //TODO on hold & swipe changing value
        }

        binding.btnColor.setOnClickListener {
            val colorPicker = ColorPicker(requireActivity())
            colorPicker.setOnFastChooseColorListener(object : OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    drawView.setColor(color)
                }

                override fun onCancel() {
                    colorPicker.dismissDialog()
                }
            })
                .setColumns(5)
                .setDefaultColorButton(Color.parseColor("#000000"))
                .show()
        }

        binding.btnSave.setOnClickListener {
            val file = File(projectName)
            if (file.exists())
                file.delete()
            val bitmap = drawView.save()
            val fileContents = bitmap.toByteArray()
            requireContext().openFileOutput(projectName, Context.MODE_PRIVATE).use {
                it.write(fileContents)
            }
        }

        binding.btnUndo.setOnClickListener {
            drawView.undo()
        }

        binding.btnRepeat.setOnClickListener {
            drawView.repeat()
        }

        return binding.root
    }

    private fun showToast(text: Int) = Toast.makeText(
        requireContext(), text,
        Toast.LENGTH_SHORT
    ).show()

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
