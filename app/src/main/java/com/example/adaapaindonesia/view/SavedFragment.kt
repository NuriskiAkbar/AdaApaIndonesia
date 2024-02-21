package com.example.adaapaindonesia.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.adapter.SavedNewsAdapter
import com.example.adaapaindonesia.databinding.FragmentSavedBinding
import com.example.adaapaindonesia.model.SavedNews
import com.example.adaapaindonesia.sqlite.DatabaseHelper
import com.example.adaapaindonesia.sqlite.SavedNewsDatabase
import com.example.adaapaindonesia.viewmodel.SavedNewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

class SavedFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var savedNewsViewModel: SavedNewsViewModel
    private lateinit var savedNewsDatabase: SavedNewsDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            binding.rvSavednews.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            savedNewsDatabase = SavedNewsDatabase(requireActivity())
            val allData = savedNewsDatabase.getAllSavedNews().toMutableList()
            withContext(Dispatchers.Main){
                if (allData.isEmpty()){
                    Toast.makeText(requireActivity(), "ini datanya kosong yaa", Toast.LENGTH_SHORT).show()
                } else {
                    val adapter = SavedNewsAdapter(allData)
                    binding.rvSavednews.adapter = adapter

                    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            val sourcePosition = viewHolder.adapterPosition
                            val targetPosition = target.adapterPosition

                            Collections.swap(allData, sourcePosition, targetPosition)
                            adapter.notifyItemMoved(sourcePosition, targetPosition)

                            return true
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            savedNewsDatabase.delete(allData[viewHolder.adapterPosition].id)
                            adapter.deleteItem(viewHolder.adapterPosition)
                            Toast.makeText(requireActivity(), "Berita sudah terhapus", Toast.LENGTH_SHORT).show()
                        }

                    })
                    itemTouchHelper.attachToRecyclerView(binding.rvSavednews)
                }
            }
        }
    }
}