package com.thiha.android4k.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        private const val DATE_ARGS = "date_argument"
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()

            args.putSerializable(DATE_ARGS, date)
            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }

        private const val TAG = "DatePickerFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val date = arguments?.getSerializable(DATE_ARGS) as Date
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            this,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val resultDate = GregorianCalendar(year, month, dayOfMonth).time
        targetFragment?.let {
            (it as Callbacks).onDateSelected(resultDate)
        }
    }

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

}