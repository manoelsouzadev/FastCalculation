package br.edu.ifsp.scl.fastcalculation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import br.edu.scl.ifsp.sdm.fastcalculation.R
import br.edu.scl.ifsp.sdm.fastcalculation.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private lateinit var fragmentResultBinding: FragmentResultBinding
    private var arguments: Bundle? = getArguments()
    private var score = arguments?.getString("points")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentResultBinding = FragmentResultBinding.inflate(inflater,container,false)
        fragmentResultBinding.pointsTv.text = score
        
        fragmentResultBinding.playBt.setOnClickListener{
            (context as OnPlayGame).onPlayGame()
        }
        return fragmentResultBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.restartGameMi).isVisible = false
    }
}