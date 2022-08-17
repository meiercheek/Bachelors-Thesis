package sk.lmajercik.mushroomApp.util

import java.util.*

data class Analysis(val time: Date, val picture: String, val shrooms: Array<Mushroom>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Analysis

        if (time != other.time) return false
        if (picture != other.picture) return false
        if (!shrooms.contentEquals(other.shrooms)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + picture.hashCode()
        result = 31 * result + shrooms.contentHashCode()
        return result
    }
}