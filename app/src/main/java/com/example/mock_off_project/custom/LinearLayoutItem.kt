package com.example.mock_off_project.custom

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
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

    private fun initData() {
        for (i in listArr.indices) {
            for (a in arrItem.indices) {
                if (i == a) {
                    arrItem[a].setData(listArr[i])
                }
            }
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
                    if (x2 < x1) {
                        val index: Int
                        if (currentItem < arrItem.size - 1) {
                            for (i in 0 until arrItem.size) {
                                val translate = arrItem[0].x - arrItem[0].marginStart
                                ObjectAnimator.ofFloat(
                                    arrItem[i],
                                    View.TRANSLATION_X,
                                    translate,
                                    translate - arrItem[currentItem + 1].width - arrItem[currentItem + 1].marginEnd
                                ).setDuration(700).start()
                            }

                            //update item tại vị trí thứ nhất khi kéo về bên trái
                            val widthCurrent = arrItem[currentItem].width
                            val widthUpdate = arrItem[currentItem + 1].width
                            index = currentItem
                            val widthAnimator = ValueAnimator.ofInt(widthCurrent, widthUpdate)
                            widthAnimator.addUpdateListener {
                                arrItem[index].updateLayoutParams {
                                    width = it.animatedValue as Int
                                }
                            }
                            widthAnimator.duration = 700
                            widthAnimator.start()

                            val colorAnimator =
                                ValueAnimator.ofArgb(
                                    ContextCompat.getColor(
                                        context,
                                        listArr[currentItem].color
                                    ), ContextCompat.getColor(context, R.color.white)
                                )
                            colorAnimator.apply {
                                addUpdateListener {
                                    if (index == 0) {
                                        arrItem[index].binding.colorItem.backgroundTintList =
                                            AppCompatResources.getColorStateList(
                                                context, R.color.white
                                            )
                                    } else {
                                        val gradientDrawable =
                                            (arrItem[index].binding.colorItem.background as GradientDrawable).mutate()
                                        (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                                    }
                                }
                                duration = 1500
                                start()
                            }

                            val colorAnimatorBack =
                                ValueAnimator.ofArgb(
                                    ContextCompat.getColor(
                                        context,
                                        listArr[currentItem].color
                                    ), Color.TRANSPARENT
                                )
                            colorAnimatorBack.apply {
                                addUpdateListener {
                                    val gradientDrawable =
                                        (arrItem[index].binding.item.background as GradientDrawable).mutate()
                                    (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                                }
                                duration = 1000
                                start()
                            }
                            currentItem++
                            Delegate.mainFragment.binding.tvDisplayMoney.text =
                                listArr[currentItem].value.toString()
                            Delegate.mainFragment.binding.lineRuleHorizontal.smoothScrollTo(
                                listArr[currentItem].value,
                                0
                            )
                            passPosition?.invoke(listArr[currentItem])
                            arrItem[currentItem].binding.linearLayoutItem.visibility = VISIBLE


                            val widthCurrent1 = arrItem[currentItem].width
                            val widthUpdate1 = arrItem[currentItem - 1].width
                            val widthAnimator1 = ValueAnimator.ofInt(widthCurrent1, widthUpdate1)
                            widthAnimator1.addUpdateListener {
                                arrItem[currentItem].updateLayoutParams {
                                    width = it.animatedValue as Int
                                }
                            }
                            widthAnimator1.duration = 700
                            widthAnimator1.start()


                            val colorAnimator1 =
                                ValueAnimator.ofArgb(
                                    ContextCompat.getColor(context, R.color.white),
                                    ContextCompat.getColor(context, listArr[currentItem].color)
                                )
                            colorAnimator1.apply {
                                addUpdateListener {
                                    val gradientDrawable =
                                        (arrItem[currentItem].binding.colorItem.background as GradientDrawable).mutate()
                                    (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                                }
                                duration = 1000
                                start()
                            }

                            val colorAnimatorBack1 =
                                ValueAnimator.ofArgb(
                                    Color.TRANSPARENT,
                                    ContextCompat.getColor(
                                        context,
                                        listArr[currentItem].colorBackGround
                                    )
                                )
                            colorAnimatorBack1.apply {
                                addUpdateListener {
                                    val gradientDrawable =
                                        (arrItem[currentItem].binding.item.background as GradientDrawable).mutate()
                                    (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                                }
                                duration = 1000
                                start()
                            }
                        }
                    } else {
                        val index: Int
                        if (currentItem > 0) {
                            for (i in 0 until arrItem.size) {
                                val translate = arrItem[0].x - arrItem[0].marginStart
                                ObjectAnimator.ofFloat(
                                    arrItem[i],
                                    View.TRANSLATION_X,
                                    translate,
                                    translate + arrItem[currentItem - 1].width + arrItem[currentItem - 1].marginEnd
                                ).setDuration(700).start()
                            }
                            index =currentItem
                            val widthCurrent = arrItem[currentItem].width
                            val widthUpdate = arrItem[currentItem-1].width
                            val widthAnimator = ValueAnimator.ofInt(widthCurrent, widthUpdate)
                            widthAnimator.addUpdateListener {
                                arrItem[index].updateLayoutParams {
                                    width = it.animatedValue as Int
                                }
                            }
                            widthAnimator.duration = 700
                            widthAnimator.start()

                            val colorAnimator =
                                ValueAnimator.ofArgb(
                                    ContextCompat.getColor(context, listArr[currentItem].color),
                                    ContextCompat.getColor(context, R.color.white)
                                )
                            colorAnimator.apply {
                                addUpdateListener {
                                        val gradientDrawable =
                                            (arrItem[index].binding.colorItem.background as GradientDrawable).mutate()
                                        (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)

                                }
                                duration = 1000
                                start()
                            }

                            val colorAnimatorBack =
                                ValueAnimator.ofArgb(
                                    ContextCompat.getColor(
                                        context,
                                        listArr[currentItem].colorBackGround
                                    )
                                    ,Color.TRANSPARENT

                                )
                            colorAnimatorBack.apply {
                                addUpdateListener {
                                    val gradientDrawable =
                                        (arrItem[index].binding.item.background as GradientDrawable).mutate()
                                    (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                                }
                                duration = 1000
                                start()
                            }
                            currentItem--
                            Delegate.mainFragment.binding.tvDisplayMoney.text =
                                listArr[currentItem].value.toString()
                            Delegate.mainFragment.binding.lineRuleHorizontal.smoothScrollTo(
                                listArr[currentItem].value,
                                0
                            )
                            passPosition?.invoke(listArr[currentItem])


                            val widthCurrent1 = arrItem[currentItem].width
                            val widthUpdate1 = arrItem[currentItem + 1].width
                            val widthAnimator1 = ValueAnimator.ofInt(widthCurrent1, widthUpdate1)
                            widthAnimator1.addUpdateListener {
                                arrItem[currentItem].updateLayoutParams {
                                    width = it.animatedValue as Int
                                }
                            }
                            widthAnimator1.duration = 700
                            widthAnimator1.start()

                            arrItem[currentItem].binding.linearLayoutItem.visibility = VISIBLE
                            val colorAnimator1 =
                                ValueAnimator.ofArgb(
                                    ContextCompat.getColor(context, R.color.white),
                                    ContextCompat.getColor(
                                        context,
                                        listArr[currentItem].color
                                    )
                                )
                            colorAnimator1.apply {
                                addUpdateListener {
                                  if (currentItem==0){
                                      arrItem[currentItem].binding.colorItem.backgroundTintList =
                                          AppCompatResources.getColorStateList(
                                              context, listArr[currentItem].color
                                          )
                                  }
                                    else{
                                      val gradientDrawable =
                                          (arrItem[currentItem].binding.colorItem.background as GradientDrawable).mutate()
                                      (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                                    }
                                }
                                duration = 1000
                                start()
                            }

                            val colorAnimatorBack1 =
                                ValueAnimator.ofArgb(Color.TRANSPARENT,ContextCompat.getColor(context,listArr[currentItem].colorBackGround))
                            colorAnimatorBack1.apply {
                                addUpdateListener {
                                    val gradientDrawable =
                                        (arrItem[currentItem].binding.item.background as GradientDrawable).mutate()
                                    (gradientDrawable as GradientDrawable).setColor(it.animatedValue as Int)
                                }
                                duration = 1000
                                start()
                            }
                        }
                    }

                }
            }
        }

        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        launchMissile()
        return super.performClick()
    }

    private fun launchMissile() {
        //Log.d("hihi", "Missile launched")
    }
}