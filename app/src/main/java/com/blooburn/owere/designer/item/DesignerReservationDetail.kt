package com.blooburn.owere.designer.item

import com.blooburn.owere.util.TypeOfReservation

// DesignerReservationDetailActivity 에서 사용
data class DesignerReservationDetail(
    override val userName: String,
    override val shop: String,
    override val startTime: Long,
    override val endTime: Long,
    override var type: Int,
    val designerName: String,
    var menuList: MutableList<String>,    // 시술할 메뉴들
    var priceList: MutableList<Int>,         // 메뉴에 따른 가격들
    val request: String

) : DesignerReservation() {
    constructor() : this("", "", 0, 0,
        TypeOfReservation.SCHEDULED.value, "", mutableListOf(), mutableListOf(), "")
}
