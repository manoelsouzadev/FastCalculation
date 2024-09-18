package br.edu.scl.ifsp.sdm.fastcalculation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Settings(
    val playerName: String = "",
    val rounds: Int = 0,
    val roundsInterval: Long = 0
): Parcelable
