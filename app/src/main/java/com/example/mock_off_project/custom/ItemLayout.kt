package com.example.mock_off_project.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import com.example.mock_off_project.databinding.ItemLayoutBinding
import com.example.mock_off_project.model.Item

class ItemLayout : RelativeLayout {
    lateinit var binding: ItemLayoutBinding

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
        binding = ItemLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setData(item: Item) {
        binding.textView.text = item.name
        binding.tvMoney.text = getString("${item.value}")
        binding.icon.setImageResource(item.icon)
//        binding.item.backgroundTintList =
//            AppCompatResources.getColorStateList(context, item.colorBackGround)
//        binding.colorItem.backgroundTintList =
//            AppCompatResources.getColorStateList(context, item.color)
    }

    private fun getString(toString: String): String {
        return "$ $toString"
    }
}