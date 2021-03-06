package com.blooburn.owere.user.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.designer.activity.main.DesignerMainActivity
import com.blooburn.owere.user.activity.main.UserMainActivity
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


//회원가입 로그인 선택 화면
class MainActivity : AppCompatActivity() {

    var uids: DatabaseReference = databaseInstance.getReference("Uids");

    companion object {
        private const val TAG = "MainActivity"
    }

    // Firebase auth
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initial firebase auth
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Handler().postDelayed({
            updateUI(currentUser)
        }, 2000L)
    }

    private fun updateUI(account: FirebaseUser?) {
        //로그인이 되어있다면 다음 액티비티 실행
        // 로그인 되어있는 계정이 고객인지 디자이너인지에 따라 보여주는 액티비티가 다름.
        if (account != null) {
            // Uids 참조, 자식 노드의 키 값과 현재 유저의 uid 비교
            uids.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var agreement: Boolean = false
                    snapshot.children.forEach {
                        // 로그인이 되어있다면 다음 액티비티 실행 (= uid와 키 값 일치)
                        if (it.key == account.uid) {
                            agreement = true
                            val mode = it.value
                            // 고객 로그인
                            if (mode == "User") {
                                val intent: Intent = Intent(
                                    this@MainActivity,
                                    UserMainActivity::class.java
                                )
                                startActivity(intent)
                                // 디자이너 로그인
                            } else if (mode == "Designer") {
                                val intent: Intent = Intent(
                                    this@MainActivity,
                                    DesignerMainActivity::class.java
                                )
                                startActivity(intent)
                                // 미 가입자 로그인
                            } else if (mode == "None") {
                                val intent: Intent =
                                    Intent(
                                        this@MainActivity,
                                        MainActivityForUnknownUser::class.java
                                    )
                                startActivity(intent)
                            }
                        }
                    }
                    if (!agreement) {
                        // uid와 일치하는 키 값 없음 (= 처음 로그인 하는 유저)
                        val intent: Intent =
                            Intent(this@MainActivity, MainActivityForUnknownUser::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        } else {
            val intent: Intent = Intent(this, MainActivityForUnknownUser::class.java)
            startActivity(intent)
        }
    }
}
