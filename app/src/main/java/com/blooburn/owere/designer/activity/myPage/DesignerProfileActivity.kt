package com.blooburn.owere.designer.activity.myPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ItemPriceMenuBinding
import com.blooburn.owere.designer.adapter.myPage.EditPortfolioSliderAdapter
import com.blooburn.owere.user.item.DesignerItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserReview
import com.blooburn.owere.util.DesignerProfileHandler
import com.blooburn.owere.util.databaseInstance
import com.blooburn.owere.util.storageInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class DesignerProfileActivity : AppCompatActivity(), DesignerProfileHandler {

    private var favoriteCountTextView: TextView? = null
    private val tempDesignerId = "designer0"
    private val DESIGNERS_REFERENCE_PATH = "Designers"
    private val PRICECHART_REFERENCE_PATH = "designerPriceChart"
    private var designerItem: DesignerItem? = null
    private val reviewImagePathList = mutableListOf<String>()
    private val reviewList = mutableListOf<UserReview>()

    private val portfolioSliderAdapter: EditPortfolioSliderAdapter by lazy{
        EditPortfolioSliderAdapter(this)
    }
    private val viewPager: ViewPager2 by lazy{
        findViewById(R.id.view_pager2_designer_profile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_profile)

        initToolbar(findViewById(R.id.toolbar_designer_profile))
        viewPager.adapter = portfolioSliderAdapter

        fetchPortfolioImages()
        getAndSetDesignerProfileFromDB()
        addEditBtnsClickListener()
    }

    private fun getAndSetDesignerProfileFromDB() {
        databaseInstance.reference.child("$DESIGNERS_REFERENCE_PATH/$tempDesignerId")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    designerItem = snapshot.getValue(DesignerItem::class.java)

                    fetchReviewImagePathList(snapshot)    // ???????????? ?????? ????????? ?????? ??????
                    fetchReviewList(snapshot)   // ???????????? ?????? ?????? ??????
                    initDesignerProfile()   // ?????? UI??? ?????????
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun fetchReviewImagePathList(snapshot: DataSnapshot) {
        val genericTypeIndicator = object : GenericTypeIndicator<List<String>>() {}
        snapshot.child("reviewImages").getValue(genericTypeIndicator)?.let {
            reviewImagePathList.addAll(it)
        }
    }

    private fun fetchReviewList(snapshot: DataSnapshot){
        val genericTypeIndicator = object : GenericTypeIndicator<List<UserReview>>() {}
        snapshot.child("reviews").getValue(genericTypeIndicator)?.let {
            reviewList.addAll(it)
        }
    }

    /**
     * TODO UserDesignerProfileActivity?????? ??????
     */
    private fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(false)  // ??? ????????? ??????
            this.setDisplayHomeAsUpEnabled(true)    // ???????????? ??????
        }

        // ???????????? ?????? ?????????
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * ????????? ?????? ?????? ??????
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_designer_profile, menu)
        menu?.let { favoriteCountTextView = findFavoriteCountTextView(it) }

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * ????????? ?????? ????????? ?????? TextView
     */
    private fun findFavoriteCountTextView(menu: Menu) =
        menu.findItem(R.id.action_designer_profile_favorite)?.actionView?.findViewById<TextView>(
            R.id.text_view_favorite_count
        )

    /* TODO ?????? ????????? ?????? ????????? ??????
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
    */

    /**
     * ??????????????? ????????? ????????????(path)??? ?????? ????????????
     */
    private fun fetchPortfolioImages() {

        storageInstance.reference.child("portfolio/$tempDesignerId")
            .listAll().addOnSuccessListener {
                portfolioSliderAdapter.setList(it.items)
                portfolioSliderAdapter.notifyDataSetChanged()
            }
    }

    /**
     * ???????????? ????????? ?????? binding
     */
    private fun initDesignerProfile() {
        if (designerItem == null) {
            return
        }

        val reviewStar = convertRatingToStar(designerItem!!.rating)

        // ???????????? ????????? ????????? ?????????
        bindProfileImage(
            window.decorView,
            findViewById(R.id.img_view_designer_profile_self),
            designerItem!!.profileImagePath,
            true
        )
        findViewById<TextView>(R.id.txt_view_designer_profile_name).text =
            designerItem!!.name
        findViewById<TextView>(R.id.txt_view_designer_profile_area).text =
            designerItem!!.area
        findViewById<TextView>(R.id.txt_view_designer_profile_matching).text =
            getString(R.string.matching_rate, designerItem!!.matchingRate)
        findViewById<TextView>(R.id.txt_view_designer_profile_star).text = reviewStar
        findViewById<TextView>(R.id.txt_view_designer_profile_introduction).text =
            designerItem!!.introduction

        // ?????? ??????, ??????, ??????
        findViewById<TextView>(R.id.txt_view_designer_profile_review_star).text = reviewStar
        findViewById<TextView>(R.id.txt_designer_profile_review_rating).text =
            designerItem!!.rating.toString()
        findViewById<TextView>(R.id.txt_designer_profile_review_count).text =
            designerItem!!.reviewCount.toString()

        initPriceChart()
        initReviewImages()
        initReviewList()
    }

    /**
     * ???????????? ????????? ???????????? ??????
     */
    private fun initPriceChart() {
        databaseInstance.reference.child("$PRICECHART_REFERENCE_PATH/$tempDesignerId/??????")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    findViewById<TextView>(R.id.txt_designer_profile_price_title).text =
                        snapshot.key

                    // ?????? ?????? ?????? ?????? ???????????? view??? ???????????? ??????
                    snapshot.children.forEach {
                        val menuItem = it.getValue(StyleMenuItem::class.java) ?: return

                        // LayoutInflater??? ???????????? ?????? ?????? ?????? and ???????????? ??????
                        val menuViewBinding = ItemPriceMenuBinding.inflate(
                            layoutInflater,
                            findViewById(R.id.layout_designer_profile_menu),
                            true
                        )

                        menuViewBinding.textPriceMenuTitle.text = menuItem.menuName
                        menuViewBinding.textPriceMenuPrice.text = menuItem.menuPrice
                        menuViewBinding.textPriceMenuTime.text = menuItem.menuTime
                        //todo: ????????? ?????? ????????????
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    /**
     * ???????????? ?????? ???????????? ?????????
     */
    private fun initReviewImages() {
        // ???????????? ?????? ????????? ????????? ?????? ?????? parent layout
        val parentLayout =
            findViewById<ViewGroup>(R.id.layout_designer_profile_review_images)

        // ?????? ????????? ?????????
        reviewImagePathList.indices.forEach { i ->
            bindProfileImage(
                parentLayout,
                parentLayout.getChildAt(i) as ImageButton,
                reviewImagePathList[i],
                false
            )
        }
    }

    /**
     * ???????????? ?????? ?????? ?????????
     */
    private fun initReviewList(){
        val parentLayout = findViewById<ViewGroup>(R.id.layout_designer_profile_reviews)

        reviewList.indices.forEach { i ->
            initEachReviewItem(parentLayout, parentLayout.getChildAt(i), reviewList[i])
        }

    }

    /**
     * ?????? ?????? ???????????? ????????? ??? ?????????
     */
    private fun initEachReviewItem(
        parentView: ViewGroup,
        reviewItemView: View,
        userReview: UserReview
    ) {
        reviewItemView.apply {
            val profileImageView = this.findViewById<ImageView>(R.id.image_item_review) // ?????? ????????? ??????
            bindProfileImage(parentView, profileImageView, userReview.userImagePath, true)

            this.findViewById<TextView>(R.id.text_item_review_nickname).text =
                userReview.userName  // ?????? ??????
            this.findViewById<TextView>(R.id.text_item_review_description).text =
                userReview.description // ??????
            this.findViewById<TextView>(R.id.text_item_review_dates).text = userReview.dates // ??????
            this.findViewById<TextView>(R.id.text_item_review_rating).text =
                convertRatingToStar(userReview.rating) // ??????
        }
    }

    private fun addEditBtnsClickListener(){
        viewPager.setOnClickListener {
            Log.d("??????", "view page clicked")
            val intent = Intent(this, EditDesignerPortfolioActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_designer_profile_edit_profile).setOnClickListener{
            val intent = Intent(this, EditDesignerProfileActivity::class.java)
            startActivity(intent)
        }
    }
}