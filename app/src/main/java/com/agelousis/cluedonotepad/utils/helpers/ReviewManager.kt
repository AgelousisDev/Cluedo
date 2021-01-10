package com.agelousis.cluedonotepad.utils.helpers

import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewManagerFactory

typealias ReviewSuccessBlock = () -> Unit
object ReviewManager {

    fun initialize(appCompatActivity: AppCompatActivity, reviewSuccessBlock: ReviewSuccessBlock) {
        val manager = ReviewManagerFactory.create(appCompatActivity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { requestInfo ->
            if (requestInfo.isSuccessful) {
                manager.launchReviewFlow(appCompatActivity, requestInfo.result).also {
                    it.addOnCompleteListener { reviewTask ->
                        if (reviewTask.isSuccessful)
                            reviewSuccessBlock()
                    }
                }
            }
        }
    }

}