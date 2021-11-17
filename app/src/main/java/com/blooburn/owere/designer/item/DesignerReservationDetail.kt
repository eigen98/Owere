package com.blooburn.owere.designer.item

import com.blooburn.owere.user.item.MenuItem

// DesignerReservationDetailActivity 에서 사용
data class DesignerReservationDetail(
    override val userName: String,
    override val shop: String,
    override val startTime: Long,
    override val endTime: Long,
    val menuList: MutableList<String>,    // 시술할 메뉴들
    val priceList: MutableList<Int>         // 메뉴에 따른 가격들

) : DesignerReservation() {
    constructor() : this("", "", 0, 0, mutableListOf(), mutableListOf())
}