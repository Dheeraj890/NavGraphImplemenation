package com.example.mynavigationtest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import com.example.mynavigationtest.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val RESULT_1="this is result 1"
    private val RESULT_2="this is result 2"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        root?.button_to_dashboard?.setOnClickListener(View.OnClickListener {

            // val action = HomeFragment.actionMessageListFragmentToMessageFragment(userRepository.getSignedInUser().userId)

            //it.findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
//Couroutine scope IO=For network and database operation,Main,Default=For heavy Computation

            CoroutineScope(IO).launch {


                fakeApiRequest()
            }

        })


        root?.button_to_notification?.setOnClickListener(View.OnClickListener {


            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_notifications)
        })
        return root
    }

    private suspend fun fakeApiRequest(){

        val result=getResultFromApi()
        println("debug ${result}")

        withContext(Main){

            var txt=text_home.text.toString()+"\n "+result
            text_home.text=txt

        }
        val result2=getResultFromApi2()
        withContext(Main){

            var txt=text_home.text.toString()+"\n "+result2
            text_home.text=txt

        }
    }

    private suspend fun getResultFromApi2():String{

        logThread("getResultFromApi2")
        delay(3000)
        return RESULT_2
    }

    private suspend fun getResultFromApi():String {

        logThread("getResultFromApi")
        delay(2000)
        return RESULT_1
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

}