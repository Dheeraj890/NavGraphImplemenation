package com.example.mynavigationtest.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mynavigationtest.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val PROGRESS_MAX=100
    private val PROGRESS_START=0
    private val JOB_TIME=4000
    private lateinit var job:CompletableJob
  lateinit  var textView: TextView
    lateinit var button: Button
    lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
         textView = root.findViewById(R.id.text_notifications)
        button = root.findViewById(R.id.button)
        progressBar = root.findViewById(R.id.progressBar)
        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })

        Toast.makeText(activity, arguments?.get("noteId").toString(),Toast.LENGTH_SHORT).show()

        button.setOnClickListener(View.OnClickListener {

            if(!::job.isInitialized){

                initJob()
            }
            progressBar.startJobOrCancel(job)

        })
        return root
    }



    fun ProgressBar.startJobOrCancel(job : Job){


        if(this.progress>0){

            println("${job} is already active. Cancelling ...")
            resetJob()
        }
        else{
            button.text="Cancel Job #1"
        val scope=    CoroutineScope(IO +job).launch {


            println("couroutine ${this} is activated with job ${job}")

            for(i in PROGRESS_MAX..PROGRESS_MAX)
            {

                delay((JOB_TIME/PROGRESS_MAX).toLong())
                this@startJobOrCancel.progress=i
            }
            updateJobCompleteTextView("Job is Complete")
            }
        }

    }

    fun updateJobCompleteTextView(text:String){


        GlobalScope.launch(
            Main

        ){

            textView.text=text
        }
    }

    private fun resetJob() {


        if(job.isActive||job.isCompleted){

            job.cancel(CancellationException(("Resetting Job")))


        }
        initJob()

    }

    fun initJob(){

updateJobCompleteTextView("")
        button.text="Start Job #1"
        job= Job()
        job.invokeOnCompletion {

            it?.message.let{

                var msg=it
                if(msg.isNullOrBlank()){

                    msg="Unknown cancellation Error"
                }

                println("${job} was cancelled. Reason : ${msg}")

                showToast(msg)
            }
        }



        progressBar.max=PROGRESS_MAX
        progressBar.progress=PROGRESS_START
    }


    fun showToast(text:String){

        GlobalScope.launch (Main){

            Toast.makeText(activity,text,Toast.LENGTH_SHORT).show()
        }


    }
}