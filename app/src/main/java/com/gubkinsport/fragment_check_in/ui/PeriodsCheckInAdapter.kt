package com.gubkinsport.fragment_check_in.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.gubkinsport.R
import com.gubkinsport.data.models.sport_objects.ui_format.UiPeriod

class PeriodsCheckInAdapter(private val onCLickListener: OnPeriodClickListener): RecyclerView.Adapter<PeriodsCheckInAdapter.PeriodViewHolder>() {

    private val TAG_PERIODS_ADAPTER = "MyPeriodsCheckInAdapter"

    private var periodsList = emptyList<UiPeriod>()

    // Clicked position
    private var clickedPosition: Int? = null
    private var previousClickedPosition: Int? = null

    // Clicked holder
    private var clickedHolder: PeriodViewHolder? = null
    private var previousClickedHolder: PeriodViewHolder? = null

    // private var listOfHolders = mutableListOf<PeriodViewHolder>()

    fun setList(list: List<UiPeriod>){
        periodsList = list
        notifyDataSetChanged()
    }

    fun resetClickedItems(){
        clickedHolder?.setUnClickedColors(R.color.period_text_color, R.color.period_item, R.color.period_item_shadow_color)

        clickedHolder = null
        previousClickedHolder = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodViewHolder {
        return PeriodViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_period_rv, parent, false))
    }

    override fun onBindViewHolder(holder: PeriodViewHolder, position: Int) {

        /*if (!listOfHolders.contains(holder)){
            listOfHolders.add(holder)
        }*/

        /*if (position == clickedPosition){
            holder.setNewColors(R.color.white, R.color.black)
        } else {
            holder.setNewColors(R.color.black, R.color.white)
        }*/

        if (holder == clickedHolder) {
            holder.setClickedColors(R.color.white, R.color.black, R.color.white)
        }

        holder.itemView.setOnClickListener{

            Log.d(TAG_PERIODS_ADAPTER, "Вызван holder.itemView.setOnClickListener")

            previousClickedPosition = clickedPosition
            clickedPosition = position

            previousClickedHolder = clickedHolder
            clickedHolder = holder

            val black = holder.itemView.context.resources.getColor(R.color.black, null)
            val white = holder.itemView.context.resources.getColor(R.color.white, null)
            val gray = holder.itemView.context.resources.getColor(R.color.my_gray, null)

            previousClickedHolder?.setUnClickedColors(R.color.period_text_color, R.color.period_item, R.color.period_item_shadow_color)
            holder.setClickedColors(R.color.white, R.color.black, R.color.white)

            onCLickListener.onClick(periodsList[position])
        }

        holder.bind(periodsList[position])
    }

    override fun getItemCount(): Int {
        return periodsList.size
    }

    class PeriodViewHolder(newView: View): RecyclerView.ViewHolder(newView){

        private val TAG_PERIODS_ADAPTER = "MyPeriodViewHolder"

        private val textView = itemView.findViewById<TextView>(R.id.id_open_time_textview)
        private val linear = itemView.findViewById<LinearLayout>(R.id.id_item_period_linear)

        fun bind(newPeriod: UiPeriod){
            var newMinute = newPeriod.openMinute.toString()
            val newHour = newPeriod.openHour.toString()
            if (newMinute.length == 1){
                newMinute = "0$newMinute"
            }
            val uiTmeText = "$newHour:$newMinute"
            textView.text = uiTmeText
        }

        fun setClickedColors(@ColorRes textColor: Int, @ColorRes backColor: Int, @ColorRes shadowColor: Int){
            Log.d(TAG_PERIODS_ADAPTER, "Вызван setClickedColors со значениями: textColor = $textColor, backColor = $backColor")
            textView.setTextColor(itemView.context.resources.getColor(textColor, null))
            textView.setBackgroundColor(itemView.context.resources.getColor(backColor, null))
            textView.setShadowLayer(0f,
                0f,
                0f,
                itemView.context.resources.getColor(shadowColor, null))
        }

        fun setUnClickedColors(@ColorRes textColor: Int, @ColorRes backColor: Int, @ColorRes shadowColor: Int){
            Log.d(TAG_PERIODS_ADAPTER, "Вызван setUnClickedColors со значениями: textColor = $textColor, backColor = $backColor")
            textView.setTextColor(itemView.context.resources.getColor(textColor, null))
            textView.setBackgroundColor(itemView.context.resources.getColor(backColor, null))
            textView.setShadowLayer(0f,
                0f,
                0f,
                itemView.context.resources.getColor(shadowColor, null))
        }
    }

    interface OnPeriodClickListener{
        fun onClick(data: UiPeriod)
    }
}