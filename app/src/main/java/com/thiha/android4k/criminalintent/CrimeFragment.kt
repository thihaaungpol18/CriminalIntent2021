package com.thiha.android4k.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var solvedCheckBox: CheckBox
    private lateinit var crimeId: UUID
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var editingCrime: Crime
    private val crimeDetailVM: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    companion object {
        private const val TAG = "CrimeFragment"
        private const val DIALOG_DATE = "DATE_PICKER_DIALOG"
        private const val DIALOG_TIME = "TIME_PICKER_DIALOG"
        private const val REQUEST_CODE: Int = 0

        /*
         *static method like in JAVA
         * */
        fun newInstance(crimeId: UUID): CrimeFragment {
            val fragment = CrimeFragment()
            val args = Bundle()
            args.putSerializable("CRIME_ID", crimeId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        timeButton = view.findViewById(R.id.crime_time) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crimeId = arguments?.getSerializable("CRIME_ID") as UUID
        crimeDetailVM.loadCrime(crimeId)
    }

    override fun onStart() {
        super.onStart()

        titleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editingCrime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            editingCrime.isSolved = isChecked
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailVM.crimeLiveData.observe(viewLifecycleOwner) {
            it?.let { c ->
                editingCrime = c
                updateUI(c)
            }
        }
    }

    private fun updateUI(crime: Crime) {
        dateButton.text = Adapter.formatDate(crime.date)
        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_CODE)
                show(this@CrimeFragment.parentFragmentManager, DIALOG_DATE)
            }
        }
        titleField.setText(crime.title)
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            //Skip Animation Of CheckBox
            jumpDrawablesToCurrentState()
        }

        timeButton.text = Adapter.formatTime(crime.date)
        timeButton.setOnClickListener {
            TimePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_CODE)
                show(this@CrimeFragment.parentFragmentManager, DIALOG_TIME)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        saveCrime(editingCrime)
    }

    private fun saveCrime(crime: Crime) {
        crimeDetailVM.updateCrime(crime)
    }

    override fun onDateSelected(date: Date) {
        editingCrime.date = date
        saveCrime(editingCrime)
    }

    override fun onTimeSet(time: Date) {
        editingCrime.date = time
        saveCrime(editingCrime)
    }
}