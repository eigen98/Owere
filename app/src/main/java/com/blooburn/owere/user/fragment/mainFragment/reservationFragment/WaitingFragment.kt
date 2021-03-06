package com.blooburn.owere.user.fragment.mainFragment.reservationFragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R

import com.blooburn.owere.databinding.WaitingFragmentLayoutBinding
import com.blooburn.owere.user.adapter.userReservation.UserReservationListAdapter
import com.blooburn.owere.user.item.ReservationListItem
import com.blooburn.owere.util.CustomDividerDecoration
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//예정된 예약
//대기중 예약
class WaitingFragment : Fragment(R.layout.waiting_fragment_layout) {

    private var binding: WaitingFragmentLayoutBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var reservationsReferencePath: String


    private lateinit var waitingAdapter: UserReservationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        reservationsReferencePath = "UserReservation/${auth?.currentUser?.uid}/"


        //바인딩 초기화
        val waitingFragmentLayoutBinding = WaitingFragmentLayoutBinding.bind(view)
        binding = waitingFragmentLayoutBinding


        //확정된 예약 리사이클러뷰 초기화
        waitingAdapter = UserReservationListAdapter()
        binding?.recyclerviewWaiting?.adapter = waitingAdapter
        binding?.recyclerviewWaiting?.layoutManager = LinearLayoutManager(requireContext())


//        initReservationsRecyclerView(
//            binding?.recyclerviewConfirmed!!,
//            confirmededAdapter
//        )

        databaseInstance.reference.child(reservationsReferencePath)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var confirmedReservation = mutableListOf<ReservationListItem>()

                    snapshot.children.forEach { reservationSnapshot ->
                        var reservation =
                            reservationSnapshot.getValue(ReservationListItem::class.java)

                        val currentTime = System.currentTimeMillis()
                        val reservationStart = reservation!!.startTime * 1000
                        val reservationEnd = reservation!!.endTime * 1000


                        // 끝나는 시간이 현재 시간을 지나지 않았을 때 && 디자이너의 수락 기다림
                        //currentTime > reservationEnd &&
                        if (currentTime < reservationEnd && reservation.accepted == 0) {
                            confirmedReservation.add(reservation)
                        }
                    }
                    binding?.waitingReservationTotalNumberText?.text = "전체 ${confirmedReservation.size} 건"
                    waitingAdapter.setData(confirmedReservation)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    /**
     * 예약 리사이클러뷰 초기화
     */
    private fun initReservationsRecyclerView(
        recyclerView: RecyclerView,
        _adapter: UserReservationListAdapter
    ) {
        recyclerView.apply {
            // custom divider 적용
            addItemDecoration(
                CustomDividerDecoration(1f, ContextCompat.getColor(this.context, R.color.gray_939393))
            )

            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }
    }
}
