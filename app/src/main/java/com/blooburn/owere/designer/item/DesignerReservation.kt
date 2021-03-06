package com.blooburn.owere.designer.item

import android.os.Parcelable
import com.blooburn.owere.util.TypeOfReservation
import kotlinx.android.parcel.Parcelize

@Parcelize
// DesignerConfirmedReservationFragment 에서 사용
open class DesignerReservation(
    open val userName: String,
    val profileImagePath: String,
    open val shop: String,
    open val startTime: Long,
    open val endTime: Long,
    open var type: Int,
    var accepted: Int, // 디자이너 수락여부,
    var userId: String

) : Parcelable {
    constructor() : this("", "", "", 0, 0,
        TypeOfReservation.SCHEDULED.value, 0, "")
}
