package com.etheller.warsmash

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.etheller.warsmash.util.DataSourceFileHandle
import com.etheller.warsmash.util.WarsmashConstants
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.InputStream

fun loadSound(fileHandle: DataSourceFileHandle) =
    Gdx.audio.newSound(Gdx.files.absolute(createFileIfNeeded(fileHandle)))

fun loadMusic(fileHandle: DataSourceFileHandle) =
    Gdx.audio.newMusic(Gdx.files.absolute(createFileIfNeeded(fileHandle)))

fun loadSound(stream: InputStream, pathToFile: String): Sound {
    val absolutePathToFile =
        "${WarsmashConstants.AUDIO_FILES_PATH}/${pathToFile.replace('\\', '/')}"
    val file = File(absolutePathToFile)
    if (!file.exists()) {
        file.parentFile.mkdirs()
        file.writeBytes(IOUtils.toByteArray(stream))
    }
    return Gdx.audio.newSound(Gdx.files.absolute(absolutePathToFile))
}

private fun createFileIfNeeded(fileHandle: DataSourceFileHandle): String {
    val absolutePathToFile =
        "${WarsmashConstants.AUDIO_FILES_PATH}/${fileHandle.path().replace('\\', '/')}"
    val file = File(absolutePathToFile)
    if (!file.exists()) {
        file.parentFile.mkdirs()
        file.writeBytes(IOUtils.toByteArray(fileHandle.read()))
    }
    return absolutePathToFile
}