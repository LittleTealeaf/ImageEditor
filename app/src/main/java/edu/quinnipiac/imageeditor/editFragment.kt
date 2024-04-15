package edu.quinnipiac.imageeditor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.quinnipiac.imageeditor.databinding.FragmentEditBinding
import androidx.navigation.findNavController

class editFragment : Fragment() {
    private var _binding:FragmentEditBinding? = null
    private val binding get ()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding = FragmentEditBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_editFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}