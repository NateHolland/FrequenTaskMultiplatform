package com.nate.frequentask.data

import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ThemeRepository {
    private val settings: Settings = Settings()

    val themes = mutableStateOf(getThemes())

    private fun getThemes(): List<Theme> {
        return settings.getStringOrNull("themes")?.let {
            Json.decodeFromString<ThemeList>(it).themes
        } ?: emptyList()
    }

    private fun setThemes(themes: List<Theme>) {
        settings["themes"] = Json.encodeToString(ThemeList(themes))
    }

    private fun saveThemes() {
        setThemes(themes.value)
    }

    fun updateThemeName(themeID: String, name: String) {
        themeID.findTheme()?.apply {
            updateTheme(copy(name = name))
        }
    }

    private fun updateTheme(theme: Theme) {
        themes.value = themes.value.filter { it.id != theme.id } + (theme)
        saveThemes()
    }


    private fun String.findTheme(): Theme? {
        return themes.value.find { it.id == this }
    }

    fun addTheme(theme: Theme) {
        themes.value += theme
        saveThemes()
    }

    fun updateTask(themeID: String, task: Theme.Task) {
        themeID.findTheme()?.apply {
            updateTheme(copy(tasks = tasks.filter { it.id != task.id } + task))
        }
    }

    fun addTask(themeID: String, task: Theme.Task) {
        themeID.findTheme()?.apply {
            updateTheme(copy(tasks = tasks + task))
        }
    }

    fun updateTaskName(themeId: String, task: Theme.Task, taskName: String) {
        updateTask(themeId, task.copy(name = taskName))
    }

    fun deleteTask(themeID: String, taskId: String) {
        themeID.findTheme()?.apply {
            updateTheme(copy(tasks = tasks.filter { it.id != taskId }))
        }
    }

    fun deleteTheme(theme: Theme) {
        themes.value = themes.value.filter { it.id != theme.id }
        saveThemes()
    }

    fun setActiveTheme(theme: Theme, active: Boolean) {
        updateTheme(theme.copy(active = active))
    }

}
