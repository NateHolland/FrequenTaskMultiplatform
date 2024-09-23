package com.nate.frequentask.data

import com.nate.frequentask.isDue
import com.nate.frequentask.isLate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.days

@Serializable
data class Theme(
    val name: String,
    val id: String = randomUUID(),
    val active: Boolean,
    val created: Long = Clock.System.now().toEpochMilliseconds(),
    val tasks: List<Task>
) {
    @Serializable
    data class Task(
        val name: String,
        val id: String = randomUUID(),
        val description: String,
        val note: String,
        val frequency: Long,
        val nextDueOn: Long,
        val lastCompletedOn: Long? = null
    )
}

fun Theme.Task.updateFrequency(it: Number): Theme.Task {
    val freq = it.toLong()
    val next = lastCompletedOn?.let { lastCompletedOn ->
        Instant.fromEpochMilliseconds(lastCompletedOn).plus(freq.days).toEpochMilliseconds()
    }?:nextDueOn
    return copy(frequency = freq, nextDueOn = next)
}
fun Theme.Task.updateLastCompleted(it: Long): Theme.Task {
    return copy(lastCompletedOn = it, nextDueOn = Instant.fromEpochMilliseconds(it).plus(frequency.days).toEpochMilliseconds())
}
fun Theme.Task.complete(note: String): Theme.Task {
    val now = Clock.System.now()
    return copy(lastCompletedOn = now.toEpochMilliseconds(), nextDueOn = now.plus(frequency.days).toEpochMilliseconds(), note = note)
}
fun Theme.Task.isDue(): Boolean {
    return nextDueOn.isDue()
}

fun Theme.Task.isLate(): Boolean {
    return nextDueOn.isLate()
}

expect fun randomUUID(): String

@Serializable
data class ThemeList(val themes: List<Theme>)