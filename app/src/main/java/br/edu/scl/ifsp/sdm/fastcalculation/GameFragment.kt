package br.edu.ifsp.sdm.fastcalculation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import br.edu.ifsp.scl.fastcalculation.CalculationGame
import br.edu.ifsp.scl.fastcalculation.ResultFragment
import br.edu.scl.ifsp.sdm.fastcalculation.Extras
import br.edu.scl.ifsp.sdm.fastcalculation.R
import br.edu.scl.ifsp.sdm.fastcalculation.Settings
import br.edu.scl.ifsp.sdm.fastcalculation.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var fragmentGameBinding: FragmentGameBinding
    private lateinit var settings: Settings
    private lateinit var calculationGame: CalculationGame
    private var currentRound: CalculationGame.Round? = null
    private var startRoundTime = 0L
    private var totalGameTime = 0L
    private var hits = 0
    private val roundDeadLineHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            totalGameTime += settings.roundsInterval
            play()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            settings = it.getParcelable(Extras.EXTRA_SETTINGS) ?: Settings()
        }
        calculationGame = CalculationGame(settings.rounds)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentGameBinding = FragmentGameBinding.inflate(inflater,container,false)

        val onClickListner = View.OnClickListener {
            val value = (it as Button).text.toString().toInt()
            if(value == currentRound?.answer){
                totalGameTime += System.currentTimeMillis() - startRoundTime
                hits++
            }else{
                totalGameTime += settings.roundsInterval
                hits--
            }
            roundDeadLineHandler.removeMessages(MSG_ROUND_DEADLINE)
            play()
        }
        fragmentGameBinding.apply {
            alternativeOneBt.setOnClickListener(onClickListner)
            alternativeTwoBt.setOnClickListener(onClickListner)
            alternativeThreeBt.setOnClickListener(onClickListner)
        }
        play()

        return fragmentGameBinding.root
    }

    companion object {
        private const val MSG_ROUND_DEADLINE = 0

        @JvmStatic
        fun newInstance(settings: Settings) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Extras.EXTRA_SETTINGS,settings)
                }
            }
    }

    private fun play(){
        currentRound = calculationGame.nextRound()
        if(currentRound != null){
            fragmentGameBinding.apply {
                "Round: ${currentRound!!.round}/${settings.rounds}".also {
                    roudTv.text = it
                }
                questionTv.text = currentRound!!.question
                alternativeOneBt.text = currentRound!!.alt1.toString()
                alternativeTwoBt.text = currentRound!!.alt2.toString()
                alternativeThreeBt.text = currentRound!!.alt3.toString()
            }
            startRoundTime = System.currentTimeMillis()
            roundDeadLineHandler.sendEmptyMessageDelayed(MSG_ROUND_DEADLINE, settings.roundsInterval)
        }else{
            with(fragmentGameBinding){
                roudTv.text = getString(R.string.points)
                val points = hits * 10f / (totalGameTime / 1000L)
                "%.1f".format(points).also {
                    questionTv.text = it

                    val fragment = ResultFragment()
                    val arguments = Bundle()
                    arguments.putString("points",it)
                    fragment.setArguments(arguments)
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.gameFl,fragment)?.commit()
                }
                alternativeOneBt.visibility = View.GONE
                alternativeTwoBt.visibility = View.GONE
                alternativeThreeBt.visibility = View.GONE



            }

        }

    }
}