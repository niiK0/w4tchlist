package com.nico.w4tchlist.ui.genres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nico.w4tchlist.GenresAdapter
import com.nico.w4tchlist.GetDataFuns
import com.nico.w4tchlist.databinding.FragmentGenresSelectBinding
import com.nico.w4tchlist.models.Genre

class GenresSelectFragment : Fragment() {

    private var _binding: FragmentGenresSelectBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel =
            ViewModelProvider(this).get(GenresSelectViewModel::class.java)

        _binding = FragmentGenresSelectBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        val data_funs = GetDataFuns()

        binding.rvGenresList.layoutManager = LinearLayoutManager(this.context)
        binding.rvGenresList.setHasFixedSize(true)

        data_funs.getGenres(){ genres : List<Genre> ->
            val adapter = GenresAdapter(genres)
            binding.rvGenresList.adapter = adapter
            adapter.setOnItemClickListener(object: GenresAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    val directions = GenresSelectFragmentDirections.actionGenresSelectToGenres(genres[position].id!!)
                    navController.navigate(directions)
                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}