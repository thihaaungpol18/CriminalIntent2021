package com.thiha.android4k.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    companion object {
        private const val DATE_ARGS = "DATE_ARGUMENT"

        fun newInstance(date: Date): TimePickerFragment {
            val args = Bundle()
            args.putSerializable(DATE_ARGS, date)
            val fragment = TimePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        calendar.time = arguments?.getSerializable(DATE_ARGS) as Date
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            this,
            hour,
            minute,
            false
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val beforeDate = arguments?.getSerializable(DATE_ARGS) as Date
        beforeDate.apply {
            targetFragment?.let {
                (it as Callbacks).onTimeSet(Date(year, month, date, hourOfDay, minute))
            }
        }
    }

    interface Callbacks {
        fun onTimeSet(time: Date)
    }
}