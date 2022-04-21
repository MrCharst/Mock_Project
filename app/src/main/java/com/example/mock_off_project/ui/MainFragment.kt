package com.example.mock_off_project.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mock_off_project.R
import com.example.mock_off_project.databinding.FragmentMainBinding
import com.example.mock_off_project.model.Item
import com.example.mock_off_project.untils.Companion.Companion.CAFE
import com.example.mock_off_project.untils.Companion.Companion.CHECK
import com.example.mock_off_project.untils.Companion.Companion.CHECK_INSERTED
import com.example.mock_off_project.untils.Companion.Companion.GYM
import com.example.mock_off_project.untils.Companion.Companion.HOUSE
import com.example.mock_off_project.untils.Companion.Companion.INSERTED
import com.example.mock_off_project.untils.Companion.Companion.LOVE
import com.example.mock_off_project.untils.Companion.Companion.MAX
import com.example.mock_off_project.untils.Companion.Companion.OTHER
import com.example.mock_off_project.untils.Companion.Companion.TAXI
import com.example.mock_off_project.untils.Companion.Companion.TEXT_BIG
import com.example.mock_off_project.untils.Companion.Companion.TEXT_SMALL
import com.example.mock_off_project.untils.Companion.Companion.TITLE_BIG
import com.example.mock_off_project.untils.Companion.Companion.TITLE_SMALL
import com.example.mock_off_project.untils.Delegate
import com.example.mock_off_project.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private val itemViewModel: ItemViewModel by viewModels()
    private var currentTv = TITLE_SMALL
    private var arrItem: List<Item> = listOf()
    private var check = true
    private var checkUpdate = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Delegate.mainFragment = this
        initData()
        handlerLine()
        updateItem()
    }

    private fun updateItem() {
        binding.linearLayout.passPosition = { item ->
            binding.btnSave.setOnClickListener {
                itemViewModel.updateItem(
                    Item(
                        item.id,
                        item.icon,
                        item.color, item.name,
                        item.colorBackGround,
                        binding.tvDisplayMoney.text.toString().toInt()
                    )
                )
            }
        }


    }


    private fun initData() {
        val sharedPreference =
            requireActivity().getSharedPreferences(CHECK_INSERTED, Context.MODE_PRIVATE)
        if (sharedPreference.getString(CHECK, null) == null) {
            itemViewModel.insertItem(
                Item(
                    1,
                    R.drawable.ic_cafe,
                    R.color.color_cafe, CAFE,
                    R.color.cafe_background,
                    200
                )
            )
            itemViewModel.insertItem(
                Item(
                    2,
                    R.drawable.ic_gym,
                    R.color.color_gym, GYM,
                    R.color.gym_background,
                    500
                )
            )
            itemViewModel.insertItem(
                Item(
                    3,
                    R.drawable.ic_house,
                    R.color.color_house, HOUSE,
                    R.color.house_background,
                    300
                )
            )
            itemViewModel.insertItem(
                Item(
                    4,
                    R.drawable.ic_love,
                    R.color.color_love, LOVE,
                    R.color.love_background,
                    100
                )
            )
            itemViewModel.insertItem(
                Item(
                    5,
                    R.drawable.ic_taxi,
                    R.color.color_taxi, TAXI,
                    R.color.taxi_background,
                    700
                )
            )
            itemViewModel.insertItem(
                Item(
                    6,
                    R.drawable.ic_other,
                    R.color.color_other, OTHER,
                    R.color.other_background,
                    700
                )
            )
            val editor = sharedPreference.edit()
            editor.putString(CHECK, INSERTED)
            editor.apply()
        }
        itemViewModel.getAllItem().observe(viewLifecycleOwner, Observer {
            binding.linearLayout.getList(it)
            var sum = 0
            arrItem = it
            for (i in it) {
                sum += i.value
                binding.tvTotalMoney.text = sum.toString()
            }
            if (check) {
                binding.tvDisplayMoney.text = it[0].value.toString()
                binding.lineRuleHorizontal.smoothScrollTo(it[0].value, 0)
                check = false
            }
        })
    }
    private fun handlerLine() {
        binding.lineRuleHorizontal.setOnScrollChangeListener { view, l, t, oldl, oldt ->
            binding.tvDisplayMoney.text = l.toString()
            val scaleLine = AnimationUtils.loadAnimation(context, R.anim.scale_view)
            binding.view.startAnimation(scaleLine)
            ObjectAnimator.ofFloat(binding.tvDisplayMoney, View.ALPHA, 0.5f, 1f).setDuration(500)
                .start()
            animationOverLoad()
        }
    }

    private fun animationOverLoad() {
        val number = binding.tvDisplayMoney.text.toString().toInt()
        if (number <= MAX) {
            if (TITLE_SMALL != currentTv) {
                binding.tvTitleIn.text = TITLE_SMALL
                binding.titleIn.text = TEXT_SMALL
                animationTextView()
                binding.tvTitleOut.text = TITLE_BIG
                binding.titleOut.text = TEXT_BIG
                currentTv = TITLE_SMALL
            }

        } else {
            if (TITLE_BIG != currentTv) {
                binding.tvTitleIn.text = TITLE_BIG
                binding.titleIn.text = TEXT_BIG
                animationTextView()
                binding.tvTitleOut.text = TITLE_SMALL
                binding.titleOut.text = TEXT_SMALL
                currentTv = TITLE_BIG
            }
        }
    }

    private fun animationTextView() {
        val alphaTvTitleOut = ObjectAnimator.ofFloat(binding.tvTitleOut, View.ALPHA, 1f, 0f)
        val alphaTitleOut = ObjectAnimator.ofFloat(binding.titleOut, View.ALPHA, 1f, 0f)
        val alphaTvTitleIn = ObjectAnimator.ofFloat(binding.tvTitleIn, View.ALPHA, 0f, 1f)
        val alphaTitleIn = ObjectAnimator.ofFloat(binding.titleIn, View.ALPHA, 0f, 1f)
        val tranTvTitleOut = ObjectAnimator.ofFloat(binding.tvTitleOut, View.TRANSLATION_Y, 0f, 30f)
        val tranTitleOut = ObjectAnimator.ofFloat(binding.titleOut, View.TRANSLATION_Y, 0f, 30f)
        val tranTvTitleIn = ObjectAnimator.ofFloat(binding.tvTitleIn, View.TRANSLATION_Y, -30f, 0f)
        val tranTitleIn = ObjectAnimator.ofFloat(binding.titleIn, View.TRANSLATION_Y, -30f, 0f)
        val scaleTvTitleOut = ObjectAnimator.ofFloat(binding.tvTitleOut, View.SCALE_Y, 1f, 0.7f)
        val scaleTitleOut = ObjectAnimator.ofFloat(binding.titleOut, View.SCALE_Y, 1f, 0.7f)
        val animatorSetTV = AnimatorSet()
        animatorSetTV.apply {
            playTogether(
                alphaTvTitleOut,
                alphaTvTitleIn,
                tranTvTitleOut,
                tranTvTitleIn,
                scaleTvTitleOut
            )
            duration = 500
            start()
        }
        val animatorSetTitle = AnimatorSet()
        animatorSetTitle.apply {
            playTogether(alphaTitleOut, alphaTitleIn, tranTitleOut, tranTitleIn, scaleTitleOut)
            duration = 1000
            start()
        }
    }

}