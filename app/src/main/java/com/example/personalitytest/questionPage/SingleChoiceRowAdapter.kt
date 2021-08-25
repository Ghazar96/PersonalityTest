package com.example.personalitytest.questionPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.personalitytest.R

class SingleChoiceRowAdapter(
    private val items: List<String>,
    var checkBoxClicked: ((String) -> Unit)? = null
) :
    RecyclerView.Adapter<SingleChoiceRowAdapter.SingleChoiceRowViewHolder>() {

    private var selectedCheckBox: CheckBox? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChoiceRowViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_choice_row_layout, parent, false)
        return SingleChoiceRowViewHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChoiceRowViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    inner class SingleChoiceRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(message: String) {
            val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)

            checkBox.text = message
            checkBox.isChecked = false

            checkBox.setOnCheckedChangeListener { compoundButton, check ->
                if (check) {
                    selectedCheckBox?.isChecked = false
                    selectedCheckBox = checkBox
                } else {
                    selectedCheckBox = null
                }
                checkBoxClicked?.invoke(message)
            }
        }
    }
}
