package com.example.mock_off_project.custom

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import com.example.mock_off_project.R
import com.example.mock_off_project.databinding.LinearItemBinding
import com.example.mock_off_project.model.Item
import com.example.mock_off_project.untils.Companion.Companion.CHECK
import com.example.mock_off_project.untils.Companion.Companion.DURATION_TRANSLATE
import com.example.mock_off_project.untils.Companion.Companion.DURATION_UPDATE_COLOR
import com.example.mock_off_project.untils.Companion.Companion.DURATION_UPDATE_COLOR_BACK
import com.example.mock_off_project.untils.Companion.Companion.DURATION_UPDATE_COLOR_BACK_FOCUS
import com.example.mock_off_project.untils.Companion.Companion.DURATION_UPDATE_COLOR_BACK_UN_FOCUS
import com.example.mock_off_project.untils.Companion.Companion.DURATION_UPDATE_COLOR_FOCUS
import com.example.mock_off_project.untils.Companion.Companion.DURATION_WIDTH
import com.example.mock_off_project.untils.Companion.Companion.INSERTED
import com.example.mock_off_project.untils.Delegate
import kotlin.math.abs


class LinearLayoutItem : LinearLayout {
    private var arrItem = ArrayList<ItemLayout>()
    private var currentItem = 0
    var listArr: List<Item> = arrayListOf()
    private lateinit var binding: LinearItemBinding
    private var x1 = 0f
    private var x2 = 0f
    private val MIN_DISTANCE = 150
    var passPosition: ((item: Item) -> Unit)? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initView(context: Context) {
        binding = LinearItemBinding.inflate(LayoutInflater.from(context), this, true)
        arrItem.add(binding.item)
        arrItem.add(binding.item1)
        arrItem.add(binding.item2)
        arrItem.add(binding.item3)
        arrItem.add(binding.item4)
        arrItem.add(binding.item5)
    }

    fun getList(list: List<Item>) {
        listArr = list
        initData()
    }


    //attach data to each item
    private fun initData() {
        for (i in listArr.indices) {
            for (a in arrItem.indices) {
                if (i == a) {
                    arrItem[a].setData(listArr[i])
                }
            }
            //default display item 0
            arrItem[currentItem].binding.colorItem.backgroundTintList =
                AppCompatResources.getColorStateList(context, listArr[currentItem].color)
            arrItem[currentItem].binding.item.backgroundTintList =
                AppCompatResources.getColorStateList(context, listArr[currentItem].colorBackGround)
            arrItem[currentItem].binding.linearLayoutItem.visibility = VISIBLE

        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                performClick()
                return true
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val deltaX: Float = x2 - x1
                if (abs(deltaX) > MIN_DISTANCE) {

                    //when right to left
                    if (x2 < x1) {
                        if (currentItem < arrItem.size - 1) {
                            translate(arrItem[currentItem + 1].width + arrItem[currentItem + 1].marginEnd)
                            rightToLeftUnFocus(currentItem)
                            currentItem++
                            rightToLeftFocus(currentItem)
                        }
                    }
                    //when left to right
                    else {
                        if (currentItem > 0) {
                            translate(-arrItem[currentItem - 1].width - arrItem[currentItem - 1].marginEnd)
                            leftToRightUnFocus(currentItem)
                            currentItem--
                            leftToRightFocus(currentItem)
                        }
                    }

                }
            }
        }

        return super.onTouchEvent(event)
    }

    //animation translation all item
    private fun translate(translateUpdate: Int) {
        for (i in 0 until arrItem.size) {
            val translate = arrItem[0].x - arrItem[0].marginStart
            ObjectAnimator.ofFloat(
                arrItem[i],
                View.TRANSLATION_X,
                translate,
                translate - translateUpdate
            ).setDuration(DURATION_TRANSLATE).start()
        }
    }


    //animation update width when swipe
    private fun updateWith(currentItem: Int, widthCurrent: Int, widthUpdate: Int) {
        val index = currentItem
        val widthAnimator = ValueAnimator.ofInt(widthCurrent, widthUpdate)
        widthAnimator.addUpdateListener {
            arrItem[index].updateLayoutParams {
                width = it.animatedValue as Int
            }
        }
        widthAnimator.duration = DURATION_WIDTH
        widthAnimator.start()
    }


    //animation update color when swipe
    private fun updateColorItem(currentItem: Int, colorCurrent: Int, colorUpdate: Int) {
        val colorAnimator =
            ValueAnimator.ofArgb(
                ContextCompat.getColor(
                    context,
                    colorCurrent
                ), ContextCompat.getColor(context, colorUpdate)
            )
        colorAnimator.apply {
            addUpdateListener {
                if (currentItem == 0) {
                    arrItem[currentItem].binding.colorItem.backgroundTintList =
                        AppCompatResources.getColorStateList(
                            context, R.color.white
                        )
                } else {
                    val gradientDrawable =
                        (arrItem[currentItem].binding.colorItem.background as GradientDrawable).mutate()
                    (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                }
            }
            duration = DURATION_UPDATE_COLOR
            start()
        }
    }


    //animation update color back when swipe
    private fun updateColorBack(currentItem: Int, colorCurrentBack: Int) {
        val index = currentItem
        val colorAnimatorBack =
            ValueAnimator.ofArgb(
                ContextCompat.getColor(
                    context,
                    colorCurrentBack
                ), Color.TRANSPARENT
            )
        colorAnimatorBack.apply {
            addUpdateListener {
                val gradientDrawable =
                    (arrItem[index].binding.item.background as GradientDrawable).mutate()
                (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
            }
            duration = DURATION_UPDATE_COLOR_BACK
            start()
        }
    }


    //handler animation item un focus when swipe right to left
    private fun rightToLeftUnFocus(currentItem: Int) {
        updateWith(currentItem, arrItem[currentItem].width, arrItem[currentItem + 1].width)
        updateColorItem(currentItem, listArr[currentItem].color, R.color.white)
        updateColorBack(currentItem, listArr[currentItem].colorBackGround)
    }


    //pass item to MainFragment to update
    private fun passData() {
        Delegate.mainFragment.binding.tvDisplayMoney.text =
            listArr[currentItem].value.toString()
        Delegate.mainFragment.binding.lineRuleHorizontal.smoothScrollTo(
            listArr[currentItem].value,
            0
        )
        passPosition?.invoke(listArr[currentItem])
    }


    //handler animation item focus when swipe right to left
    private fun rightToLeftFocus(currentItem: Int) {
        passData()
        arrItem[currentItem].binding.linearLayoutItem.visibility = VISIBLE
        updateWith(currentItem, arrItem[currentItem].width, arrItem[currentItem - 1].width)
        updateColorItem(currentItem, R.color.white, listArr[currentItem].color)
        updateColorBackRightToLeftFocus(currentItem, listArr[currentItem].colorBackGround)
    }

    private fun updateColorBackRightToLeftFocus(currentItem: Int, colorUpdate: Int) {
        val colorAnimatorBack =
            ValueAnimator.ofArgb(
                Color.TRANSPARENT,
                ContextCompat.getColor(
                    context,
                    colorUpdate
                )
            )
        colorAnimatorBack.apply {
            addUpdateListener {
                val gradientDrawable =
                    (arrItem[currentItem].binding.item.background as GradientDrawable).mutate()
                (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
            }
            duration = DURATION_UPDATE_COLOR_FOCUS
            start()
        }
    }

    //handler animation item  focus when swipe left to right
    private fun leftToRightFocus(currentItem: Int) {
        passData()
        updateWith(currentItem, arrItem[currentItem].width, arrItem[currentItem + 1].width)
        arrItem[currentItem].binding.linearLayoutItem.visibility = VISIBLE
        updateColorItemLeftToRightFocus(currentItem, R.color.white, listArr[currentItem].color)
        updateColorBackLeftToRightFocus(currentItem, listArr[currentItem].colorBackGround)
    }

    private fun updateColorItemLeftToRightFocus(
        currentItem: Int,
        colorCurrent: Int,
        colorUpdate: Int
    ) {
        val colorAnimator =
            ValueAnimator.ofArgb(
                ContextCompat.getColor(context, colorCurrent),
                ContextCompat.getColor(
                    context,
                    colorUpdate
                )
            )
        colorAnimator.apply {
            addUpdateListener {
                if (currentItem == 0) {
                    arrItem[currentItem].binding.colorItem.backgroundTintList =
                        AppCompatResources.getColorStateList(
                            context, colorUpdate
                        )
                } else {
                    val gradientDrawable =
                        (arrItem[currentItem].binding.colorItem.background as GradientDrawable).mutate()
                    (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                }
            }
            duration = DURATION_UPDATE_COLOR_FOCUS
            start()
        }
    }

    private fun updateColorBackLeftToRightFocus(currentItem: Int, colorUpdate: Int) {
        val colorAnimatorBack =
            ValueAnimator.ofArgb(
                Color.TRANSPARENT,
                ContextCompat.getColor(
                    context,
                    colorUpdate
                )
            )
        colorAnimatorBack.apply {
            addUpdateListener {
                val gradientDrawable =
                    (arrItem[currentItem].binding.item.background as GradientDrawable).mutate()
                (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
            }
            duration = DURATION_UPDATE_COLOR_BACK_FOCUS
            start()
        }
    }

    //handler animation item  un focus when swipe left to right
    private fun leftToRightUnFocus(currentItem: Int) {
        updateWith(currentItem, arrItem[currentItem].width, arrItem[currentItem - 1].width)
        updateColorItem(currentItem, listArr[currentItem].color, R.color.white)
        updateColorBackLeftToRightUnFocus(currentItem, listArr[currentItem].colorBackGround)
    }

    private fun updateColorBackLeftToRightUnFocus(currentItem: Int, colorCurrent: Int) {
        val index: Int = currentItem
        val colorAnimatorBack =
            ValueAnimator.ofArgb(
                ContextCompat.getColor(
                    context,
                    colorCurrent
                ), Color.TRANSPARENT

            )
        colorAnimatorBack.apply {
            addUpdateListener {
                val gradientDrawable =
                    (arrItem[index].binding.item.background as GradientDrawable).mutate()
                (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
            }
            duration = DURATION_UPDATE_COLOR_BACK_UN_FOCUS
            start()
        }
    }

    override fun performClick(): Boolean {
        Log.d(CHECK, INSERTED)
        return super.performClick()
    }


}