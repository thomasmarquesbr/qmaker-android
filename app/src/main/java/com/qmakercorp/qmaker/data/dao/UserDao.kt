package com.qmakercorp.qmaker.data.dao

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.qmakercorp.qmaker.utils.*

class UserDao {

    private val database = FirebaseFirestore.getInstance()

    fun getData(completion: (String, String, Plan) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection(USERS)
                    .document(user.uid)
                    .get()
                    .addOnSuccessListener { documentSnap ->
                        val data = documentSnap.data
                        data?.let {
                            if (!it.containsKey(NAME)) data[NAME] = user.displayName
                            if (!it.containsKey(EMAIL)) data[EMAIL] = user.email
                            if (!it.containsKey(PLAN)) data[PLAN] = Plan.BASIC.name
                            saveData(user.uid, it[NAME] as String, it[EMAIL] as String, Plan.valueOf(it[PLAN] as String))
                            completion(it[NAME] as String, it[EMAIL] as String, Plan.valueOf(it[PLAN] as String))
                        } ?: run { 
                            saveData(user.uid, user.displayName, user.email, Plan.BASIC)
                        }
                    }
        }
    }

    private fun saveData(userId: String, name: String?, email: String?, plan: Plan) {
        safeLet(name, email) { n, e ->
            val data = mapOf(NAME to n,
                    EMAIL to e,
                    PLAN to plan.name)
            database.collection(USERS)
                    .document(userId)
                    .set(data, SetOptions.merge());
        }
    }

}