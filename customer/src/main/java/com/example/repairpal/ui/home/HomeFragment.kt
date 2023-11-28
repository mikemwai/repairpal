package com.example.repairpal.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.repairpal.CarIssueActivity
import com.example.repairpal.R
import com.example.repairpal.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

    private var selectedItemId: Int = -1
    private lateinit var database: DatabaseReference
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        database = FirebaseDatabase.getInstance().reference

        val button7: Button = view.findViewById(R.id.button7)
        button7.setOnClickListener {
            saveSelectedItemToFirebase()
            showDataSavedAlert()
            val intent = Intent(requireContext(), CarIssueActivity::class.java)
            startActivity(intent)
        }

        val gridLayout: GridLayout = view.findViewById(R.id.grid1)
        for (i in 0 until gridLayout.childCount) {
            val cardView: View = gridLayout.getChildAt(i)
            cardView.setOnClickListener {
                handleItemClick(i)
            }
        }

        return view
    }

    private fun handleItemClick(itemId: Int) {
        if (selectedItemId != -1) {
            val previouslySelectedItem: View? = view?.findViewById(selectedItemId)
            previouslySelectedItem?.isSelected = false
        }

        val clickedItem: View? = view?.findViewById(itemId)
        clickedItem?.isSelected = true
        selectedItemId = itemId
    }

    private fun saveSelectedItemToFirebase() {
        if (selectedItemId != -1) {
            database.child("selectedItems").setValue(selectedItemId)
        }
    }

    private fun showDataSavedAlert() {
        val message = "The data has been saved."
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
