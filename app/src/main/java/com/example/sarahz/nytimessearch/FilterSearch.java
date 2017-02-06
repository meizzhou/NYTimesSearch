package com.example.sarahz.nytimessearch;

/**
 * Created by sarahz on 2/2/17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterSearch extends DialogFragment implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final String FRAG_TAG_DATE_PICKER = "Date Picker";

    @BindView(R.id.tvFilterDate)
    TextView tvFilterDate;
    @BindView(R.id.etFilterDate)
    EditText etFilterDate;
    @BindView(R.id.rowFilterDate)
    LinearLayout rowFilterDate;
    @BindView(R.id.tvFilterSortOrder)
    TextView tvFilterSortOrder;
    @BindView(R.id.spSortOrder)
    Spinner spSortOrder;
    @BindView(R.id.rowFilterSortOrder)
    LinearLayout rowFilterSortOrder;
    @BindView(R.id.tvNewsType)
    TextView tvNewsType;
    @BindView(R.id.cbArts)
    CheckBox cbArts;
    @BindView(R.id.cbFashionAndStyle)
    CheckBox cbFashionAndStyle;
    @BindView(R.id.cbSports)
    CheckBox cbSports;
    @BindView(R.id.rowCheckboxesNewsDesk)
    LinearLayout rowCheckboxesNewsDesk;
    @BindView(R.id.rowFilterNewsDesk)
    LinearLayout rowFilterNewsDesk;
    @BindView(R.id.btnSave)
    Button btnSave;

    public interface FilterSearchDialogListener {

        void onFinishFilterSearchDialog(String beginDate, String sortOrder, boolean isArts,
                                        boolean isFashionAndStyle, boolean isSports);
    }

    public FilterSearch() {
    }

    public static FilterSearch newInstance(String title, String beginDate,
                                                         String sortOrder, boolean isArts,
                                                         boolean isFashionAndStyle,
                                                         boolean isSports) {
        FilterSearch fragment = new FilterSearch();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("beginDate", beginDate);
        args.putString("sortOrder", sortOrder);
        args.putBoolean("isArts", isArts);
        args.putBoolean("isFashionAndStyle", isFashionAndStyle);
        args.putBoolean("isSports", isSports);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDatePickerEditText(view);
        setUpSortOrderSpinner(view);
        setUpCheckboxes(view);
        setUpSaveButton(view);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear,
                          int dayOfMonth) {
        etFilterDate.setText(new SimpleDateFormat("MM/dd/yy").format(new Date(year,
                monthOfYear, dayOfMonth)));
    }

    private void setUpDatePickerEditText(View view) {
        etFilterDate.setText(beginDate());
        etFilterDate.setInputType(InputType.TYPE_NULL);
        etFilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().
                        setOnDateSetListener(FilterSearch.this);
                cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        etFilterDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().
                            setOnDateSetListener(FilterSearch.this);
                    cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
                }
            }
        });
    }

    private String beginDate() {
        String beginDate = new SimpleDateFormat("MM/dd/yy").format(new Date());
        if (getArguments().getString("beginDate") != null) {
            SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat targetDateFormat = new SimpleDateFormat("MM/dd/yy");
            beginDate = convertDateFromTo(originalDateFormat, targetDateFormat,
                    getArguments().getString("beginDate"));
        }
        return beginDate;
    }

    private void setUpSortOrderSpinner(View view) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_order_arrays, android.R.layout.simple_spinner_dropdown_item);
        spSortOrder.setAdapter(spinnerAdapter);
        if (getArguments().getString("sortOrder") != null) {
            setSpinnerToValue(spSortOrder, getArguments().getString("sortOrder"));
        }
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }

    private void setUpCheckboxes(final View view) {
        if (getArguments().getBoolean("isArts")) {
            cbArts.setChecked(true);
        }
        if (getArguments().getBoolean("isFashionAndStyle")) {
            cbFashionAndStyle.setChecked(true);
        }
        if (getArguments().getBoolean("isSports")) {
            cbSports.setChecked(true);
        }
    }

    private void setUpSaveButton(View view) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });
    }

    private void onSave() {
        FilterSearchDialogListener listener = (FilterSearchDialogListener) getActivity();
        String sortOrderValue = spSortOrder.getSelectedItem().toString();
        boolean isArts = cbArts.isChecked();
        boolean isFashionAndStyle = cbFashionAndStyle.isChecked();
        boolean isSports = cbSports.isChecked();
        SimpleDateFormat originalDateFormat = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateValue = convertDateFromTo(originalDateFormat, targetDateFormat,
                etFilterDate.getText().toString());
        listener.onFinishFilterSearchDialog(dateValue, sortOrderValue, isArts, isFashionAndStyle,
                isSports);
//        Filter filter = new Filter(dateValue, sortOrderValue, isArts, isFashionAndStyle, isSports);
//        Intent i = new Intent(FilterSearch.this.getActivity(), SearchActivity.class);
//        i.putExtra("filter", filter);
//        startActivity(i);
        dismiss();
    }

    private String convertDateFromTo(DateFormat originalDateFormat, DateFormat targetDateFormat,
                                     String originalDate) {
        String targetDate = "";
        try {
            Date date = originalDateFormat.parse(originalDate);
            targetDate = targetDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetDate;
    }
}
