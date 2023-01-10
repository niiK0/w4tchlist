package com.nico.w4tchlist.ui.lists

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nico.w4tchlist.CreateListActivity
import com.nico.w4tchlist.ListsFuns
import com.nico.w4tchlist.databinding.FragmentListsBinding

class ListsFragment : Fragment() {

    private var _binding: FragmentListsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val listsViewModel =
            ViewModelProvider(this).get(ListsViewModel::class.java)

        _binding = FragmentListsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listsFuns = ListsFuns()

        binding.rvListsList.layoutManager = LinearLayoutManager(this.context)
        binding.rvListsList.setHasFixedSize(true)

        val fab = binding.addList

        // Set an OnClickListener on the FAB to start the AddItemActivity
        fab.setOnClickListener {
            val intent = Intent(requireContext(), CreateListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}