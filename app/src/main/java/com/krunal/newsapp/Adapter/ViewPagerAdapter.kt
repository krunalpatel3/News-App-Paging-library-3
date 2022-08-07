package com.krunal.newsapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

import com.krunal.newsapp.Globel.Utility.Companion.bindImage
import com.krunal.newsapp.databinding.LayoutImageLayoutBinding


class ViewPagerAdapter(private val context: Context) : PagerAdapter() {

    private val images = arrayOf(
        "https://blog.themediaant.com/wp-content/uploads/2020/01/Picture5-3.png",
        "https://gumlet.assettype.com/newslaundry%2F2022-04%2Fb0f8d217-9b52-48fe-8771-55859b629857%2F27d3b131_299e_41d1_81bc_b15b527a718d.jpg?auto=format%2Ccompress&w=1200",
        "https://drive.google.com/uc?export=download&id=1vHJ_sIX_vxcperCke8G2SHij6p97eNcN&confirm=t",
        "https://drive.google.com/uc?export=download&id=1Woo21FEkHGy9GM7tZv_RrsaQg2qFeZfr&confirm=t",
        "https://drive.google.com/uc?export=download&id=1mW-Kr0dIgfSC7sRxvrZt810Dfj_IApje&confirm=t"
    )

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = LayoutImageLayoutBinding.inflate(layoutInflater)
        bindImage(binding.imageView, images[position])
        container.addView(binding.root, 0)

        return binding.root
    }

    override fun getCount() = images.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
