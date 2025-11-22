package pe.app.myapplication.appComida.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.app.myapplication.appComida.data.model.Meal
import pe.app.myapplication.appComida.databinding.ItemMealBinding

class MealAdapter(
    private var meals: List<Meal>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    inner class MealViewHolder(val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.binding.tvMealName.text = meal.strMeal
        Glide.with(holder.itemView.context).load(meal.strMealThumb).into(holder.binding.imgMeal)

        holder.itemView.setOnClickListener {
            onClick(meal.idMeal)
        }
    }

    override fun getItemCount(): Int = meals.size

    fun updateList(newList: List<Meal>) {
        meals = newList
        notifyDataSetChanged()
    }
}