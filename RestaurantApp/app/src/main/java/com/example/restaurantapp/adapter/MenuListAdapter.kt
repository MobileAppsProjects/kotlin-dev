package com.example.restaurantapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.databinding.MenuListRowBinding
import com.example.restaurantapp.models.Menus


class MenuListAdapter(val menuList: List<Menus?>?, val clickListener: MenuListClickListener): RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = MenuListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(menuList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return if(menuList == null)return 0 else menuList.size
    }

    inner class MyViewHolder(private val binding: MenuListRowBinding): RecyclerView.ViewHolder(binding.root) {
        var thumbImage: ImageView = binding.thumbImage
        val menuName: TextView = binding.menuName
        val menuPrice: TextView = binding.menuPrice
        val addToCartButton: TextView = binding.addToCartButton
        val addMoreLayout: LinearLayout = binding.addMoreLayout
        val imageMinus: ImageView = binding.imageMinus
        val imageAddOne: ImageView = binding.imageAddOne
        val tvCount: TextView = binding.tvCount

        fun bind(menus: Menus) {

            menuName.text = menus?.name ?: ""
            menuPrice.text = "Price: $ ${menus?.price ?: 0.0}"

            // Manejo del clic en el botón "Agregar al carrito"
            addToCartButton.setOnClickListener {
                menus?.totalInCart = 1
                clickListener.addToCartClickListener(menus)
                addMoreLayout.visibility = View.VISIBLE
                addToCartButton.visibility = View.GONE
                tvCount.text = menus?.totalInCart.toString()
            }

            // Maneja el clic en el botón de disminuir cantidad
            imageMinus.setOnClickListener {
                var total: Int = menus?.totalInCart ?: 0
                total--
                if (total > 0) {
                    menus?.totalInCart = total
                    clickListener.updateCartClickListener(menus)
                    tvCount.text = menus?.totalInCart.toString()
                } else {
                    menus?.let {
                        it.totalInCart = total
                        clickListener.removeFromCartClickListener(it)
                        addMoreLayout.visibility = View.GONE
                        addToCartButton.visibility = View.VISIBLE
                    }
                }
            }

            // Maneja el clic en el botón de aumentar cantidad
            imageAddOne.setOnClickListener {
                var total: Int = menus?.totalInCart ?: 0
                total++
                if (total <= 10) {
                    menus?.totalInCart = total
                    clickListener.updateCartClickListener(menus)
                    tvCount.text = total.toString()
                }
            }

            // Carga la imagen de menú usando Glide con caché
            Glide.with(thumbImage.context)
                .load(menus?.url)
                .centerCrop()
                .into(thumbImage)
        }

    }

    interface MenuListClickListener {
        fun addToCartClickListener(menu: Menus)
        fun updateCartClickListener(menu: Menus)
        fun removeFromCartClickListener(menu: Menus)
    }
}