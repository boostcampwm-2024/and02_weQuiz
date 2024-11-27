package kr.boostcamp_2024.course.domain.model

import kr.boostcamp_2024.course.domain.enum.BlankContentType
import kr.boostcamp_2024.course.domain.enum.toBlankContentType
import kotlin.math.min

class BlankQuestionManager(
    private val updateCallback: () -> Unit,
) {
    lateinit var contents: List<Map<String, Any>?>
    lateinit var blankWords: List<Map<String, Any>>

    fun setNewQuestions(
        questionContents: List<Map<String, Any>>,
        isOwner: Boolean = false,
    ) {
        val currentContents = mutableListOf<Map<String, Any>?>()
        val currentBlankWords = mutableListOf<Map<String, Any>>()
        questionContents.forEach {
            val contentType = (it["type"] as? String)?.toBlankContentType()
            when (contentType) {
                BlankContentType.Text -> {
                    currentContents.add(it)
                }

                BlankContentType.Blank -> {
                    if (isOwner) {
                        currentContents.add(it)
                    } else {
                        currentContents.add(null)
                        currentBlankWords.add(it.initBlankQuestionContent())
                    }
                }

                null -> throw Exception("Invalid content type")
            }
        }
        contents = currentContents
        blankWords = currentBlankWords.shuffled()
    }

    fun addBlankContent(mapIdx: Int) {
        val targetIdx = getNullContentMinIndex()
        updateBlankWords(mapIdx, true)
        updateContents(targetIdx, blankWords[mapIdx])
        updateCallback()
    }

    fun removeBlankContent(listIdx: Int) {
        val targetContent = contents[listIdx] ?: return
        val mapIdx = targetContent["index"] as? Int ?: return
        updateContents(listIdx, null)
        updateBlankWords(mapIdx, false)
        updateCallback()
    }

    fun getNullContentMinIndex(
        contents: List<Map<String, Any>?> = this.contents,
    ): Int {
        val deque = ArrayDeque<Pair<Int, Int>>()
        deque.add(0 to contents.size - 1)
        var minIdx = contents.size + 1

        while (deque.isNotEmpty()) {
            val (start, end) = deque.removeFirst()
            if (start > end || start >= contents.size || end >= contents.size) continue

            val mid = (start + end) / 2
            if (contents[mid] == null) {
                minIdx = min(minIdx, mid)
                deque.add(start to mid - 1)
            } else {
                deque.add(start to mid - 1)
                deque.add(mid + 1 to end)
            }
        }

        return minIdx
    }

    fun getAnswer(): Map<String, String?> {
        val blanks = contents.filter { it == null || it["type"] == "blank" }
        return blanks.mapIndexed { index, content ->
            index.toString() to content?.get("text") as? String
        }.toMap()
    }

    private fun updateBlankWords(index: Int, isUsed: Boolean) {
        blankWords = blankWords.toMutableList().apply {
            val mapIdx = if (isUsed) index else -1
            this[index] = this[index].setUsed(mapIdx, isUsed)
        }
    }

    private fun updateContents(index: Int, content: Map<String, Any>?) {
        contents = contents.toMutableList().apply {
            this[index] = content
        }
    }
}

fun Map<String, Any>.setUsed(mapIdx: Int, isUsed: Boolean): Map<String, Any> =
    this.toMutableMap().apply {
        this["index"] = mapIdx
        this["isUsed"] = isUsed
    }

fun Map<String, Any>.initBlankQuestionContent(): Map<String, Any> =
    this.toMutableMap().apply {
        this["isUsed"] = false
        this["index"] = -1
    }

fun Map<String, Any>.setIndex(index: Int): Map<String, Any> =
    this.toMutableMap().apply {
        this["index"] = index
    }
