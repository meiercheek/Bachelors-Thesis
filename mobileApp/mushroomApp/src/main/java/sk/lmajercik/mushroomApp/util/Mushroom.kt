package sk.lmajercik.mushroomApp.util

import android.os.Parcel
import android.os.Parcelable


data class Mushroom (
    val id: String?,
    val match: String?,
    val edibility: String?,
    val description: String?,
    val image: String?,
    val author: String?,
    val license: String?,
    val link: String?,
    val wikiLink: String?,
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(match)
        parcel.writeString(edibility)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(author)
        parcel.writeString(license)
        parcel.writeString(link)
        parcel.writeString(wikiLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mushroom> {
        override fun createFromParcel(parcel: Parcel): Mushroom {
            return Mushroom(parcel)
        }

        override fun newArray(size: Int): Array<Mushroom?> {
            return arrayOfNulls(size)
        }
    }
}