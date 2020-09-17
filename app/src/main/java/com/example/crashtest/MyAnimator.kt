package com.example.crashtest

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class MyAnimator : DefaultItemAnimator() {

    private val removingAnimations = HashSet<RecyclerView.ViewHolder>()

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        Logger.d(">> animateAdd [$holder]")
        if (holder != null) {
            val position = holder.adapterPosition
            val animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.anim_star_enter
            )
            animation.startOffset = position * animation.duration / 3
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    dispatchAddFinished(holder)
                }

                override fun onAnimationStart(animation: Animation?) {
                    dispatchAddStarting(holder)
                }
            })
            holder.itemView.startAnimation(animation)
        } else {
            dispatchAddFinished(holder)
        }
        return false
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        Logger.d(">> animateRemove [$holder]")
        if (holder != null) {
            removingAnimations.add(holder)
            return true
        } else {
            dispatchRemoveFinished(holder)
            return false
        }
    }

    override fun runPendingAnimations() {
        Logger.d(">> runPendingAnimations")

        removingAnimations.forEach { holder ->
            val position = holder.adapterPosition
            val animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.anim_star_enter
            )
            animation.startOffset = position * animation.duration
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    dispatchRemoveFinished(holder)
                    removingAnimations.remove(holder)
                }

                override fun onAnimationStart(animation: Animation?) {
                    dispatchRemoveStarting(holder)
                }

            })
            holder.itemView.clearAnimation()
            holder.itemView.startAnimation(animation)
        }

    }

    override fun isRunning(): Boolean {
        Logger.d(">> isRunning")
        return super.isRunning() || removingAnimations.isNotEmpty()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        Logger.d(">> endAnimation")
        super.endAnimation(item)
        item.itemView.clearAnimation()
        removingAnimations.remove(item)
    }

    override fun endAnimations() {
        Logger.d(">> endAnimations")
        removingAnimations.forEach {
            it.itemView.clearAnimation()
        }
        removingAnimations.clear()
    }

}