package com.example.appachetestwork.paint

import android.graphics.Color

class DrawViewHelper {
    private val _paths: MutableList<Stroke> = mutableListOf()
    val paths: List<Stroke>
        get() = _paths.toList()

    private val _stashPaths: MutableList<Stroke> = mutableListOf()
    val stashPaths: List<Stroke>
        get() = _stashPaths.toList()

    private var _currentColor = Color.BLACK
    val currentColor: Int
        get() = _currentColor

    private var _strokeWidth = 30
    val strokeWidth: Int
        get() = _strokeWidth

    fun setWidth(width: Int) {
        _strokeWidth = width
    }

    fun setColor(color: Int) {
        _currentColor = color
    }

    fun addToPath(fp: Stroke) = _paths.add(fp)
    fun addToStash(fp: Stroke) = _stashPaths.add(fp)
    fun removeLastFromPath() = _paths.removeLast()
    fun removeLastFromStash() = _stashPaths.removeLast()
    fun clearStash() = _stashPaths.clear()

    //TODO убрать костыли при переходе на Dagger2 вытащить в активити скоуп
    init {
        clear()
    }

    private fun clear() {
        _paths.clear()
        _stashPaths.clear()
        _currentColor = Color.BLACK
        _strokeWidth = 30
    }
}