package com.grappim.androHelper.ui.networkinfo.ip

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.grappim.androHelper.R
import com.grappim.androHelper.core.extensions.inflate
import com.grappim.androHelper.databinding.ItemNetworkInfoBinding

class NetworkInfoAdapter(

) : RecyclerView.Adapter<NetworkInfoAdapter.NetworkInfoViewHolder>() {

    private val items = mutableListOf<NetworkInfoItem>()

    inner class NetworkInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewBinding: ItemNetworkInfoBinding by viewBinding(ItemNetworkInfoBinding::bind)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkInfoViewHolder =
        NetworkInfoViewHolder(parent.inflate(R.layout.item_network_info))

    override fun onBindViewHolder(holder: NetworkInfoViewHolder, position: Int) {
        with(holder) {
            val networkItem = items[holder.bindingAdapterPosition]
            viewBinding.textLabel.setText(networkItem.label)
            viewBinding.textValue.text = networkItem.value
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(list: List<NetworkInfoItem>) {
        items.clear()
        items.addAll(items)
        notifyDataSetChanged()
    }
}

data class NetworkInfoItem(
    @StringRes val label: Int,
    val value: String
)