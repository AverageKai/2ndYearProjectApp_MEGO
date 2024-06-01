package com.example.mego.expensemanagerapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mego.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //Floating action textview here

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //boolean conditions
    private boolean isOpen = false;

    //animation object for floating button

    private Animation FadOpen, FadClose;

    //Firebase code
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    //Dashboard income and expense values connect to data from expense and income fragments
    private TextView totalIncome;
    private TextView totalExpense;

    //Recycler views from income and expense
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;
    public DashboardFragment() {
        // Required empty public constructor
    }


    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);


        //Database firebase auth for isntance or user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        //Find data for seperate users in MEGO database ty stackoverflow
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        //dashboard values for icnoem and expenses
        totalIncome = myview.findViewById(R.id.income_set_results);
        totalExpense = myview.findViewById(R.id.expenses_set_results);

        //Recycler views
        mRecyclerIncome = myview.findViewById(R.id.recycler_income);
        mRecyclerExpense = myview.findViewById(R.id.recycler_expense);

        //button stuff below here
        //connections sa floating action buttons diri (ethan)
        fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myview.findViewById(R.id.expense_ft_btn);

        //connect floating text diri (ethan)
        fab_income_txt = myview.findViewById(R.id.income_ft_text);
        fab_expense_txt = myview.findViewById(R.id.expenses_ft_text);

        //Animation connect here
        FadOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if(isOpen){
                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);

                    isOpen = false;
                }else{
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);

                    isOpen = true;
                }
            }
        });

        //calculate total income using value listener
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int incomeSum = 0;

                //for loop sum +=
                for(DataSnapshot mysnap : snapshot.getChildren()){
                    Data data = mysnap.getValue(Data.class);
                    incomeSum += data.getAmount();

                    String stTotalValue = String.valueOf(incomeSum);

                    totalIncome.setText(stTotalValue +".00");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int expenseSum = 0;

                //for loop expense +=
                for(DataSnapshot mysnap : snapshot.getChildren()){
                    Data data = mysnap.getValue(Data.class);
                    expenseSum += data.getAmount();

                    String stTotalValue = String.valueOf(expenseSum);
                    totalExpense.setText(stTotalValue +".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Recycler view
        //Income
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);
        //Expense
        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);


        return myview;
    }
    //RAZZLE DAZZLE
    private void ftAnimation(){
        if(isOpen){
            fab_income_btn.startAnimation(FadClose);
            fab_expense_btn.startAnimation(FadClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadClose);
            fab_expense_txt.startAnimation(FadClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);

            isOpen = false;
        }else{
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);

            isOpen = true;
        }
    }

    private void addData(){
        //Button functionality for floating button(NGANO GA FLOATING BUTTONS KO PA CHUY2)
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }


    //Functionality for floatinb button income
    public void incomeDataInsert(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_insertdata_layout,null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        EditText editAmount = myview.findViewById(R.id.amount_edt);
        EditText edittype = myview.findViewById(R.id.type_edt);
        EditText editNote = myview.findViewById(R.id.note_edt);

        Button btnSave = myview.findViewById(R.id.btnSave);
        Button btnCancel = myview.findViewById(R.id.btnCancel);

        //Buttons from custom_insertdata_layout
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String type = edittype.getText().toString().trim();
                    String amount = editAmount.getText().toString().trim();
                    String note = editNote.getText().toString().trim();

                    if(TextUtils.isEmpty(type)){
                        edittype.setError("MISSING FIELD");
                        return;
                    }

                    if(TextUtils.isEmpty(amount)){
                        editAmount.setError("MISSING FIELD");
                        return;
                    }

                    int ourAmountInt = Integer.parseInt(amount);

                    if(TextUtils.isEmpty(note)){
                        editNote.setError("MISSING FIELD");
                        return;
                    }
                    ftAnimation();
                    //Inserting data into object data then pushing to database income
                    String id = mIncomeDatabase.push().getKey();
                    String mDate = DateFormat.getDateInstance().format(new Date());
                    Data data = new Data(ourAmountInt, type, note, id, mDate);
                    mIncomeDatabase.child(id).setValue(data);

                    Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Incompatible Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Functionality for floating button expense
    public void expenseDataInsert(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_insertdata_layout, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        EditText editAmount = myview.findViewById(R.id.amount_edt);
        EditText edittype = myview.findViewById(R.id.type_edt);
        EditText editNote = myview.findViewById(R.id.note_edt);

        Button btnSave = myview.findViewById(R.id.btnSave);
        Button btnCancel = myview.findViewById(R.id.btnCancel);

        //Buttons for custom_insertdata_layout

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String tmtype = edittype.getText().toString().trim();
                    String tmamount = editAmount.getText().toString().trim();
                    String tmnote = editNote.getText().toString().trim();

                    if(TextUtils.isEmpty(tmtype)){
                        edittype.setError("MISSING FIELD");
                        return;
                    }

                    if(TextUtils.isEmpty(tmamount)){
                        editAmount.setError("MISSING FIELD");
                        return;
                    }

                    int ourAmountInt = Integer.parseInt(tmamount);

                    if(TextUtils.isEmpty(tmnote)){
                        editNote.setError("MISSING FIELD");
                        return;
                    }
                    ftAnimation();

                    //Inserting data into object data then pushing to expense database
                    String id = mExpenseDatabase.push().getKey();
                    String mDate = DateFormat.getDateInstance().format(new Date());
                    Data data = new Data(ourAmountInt, tmtype, tmnote, id, mDate);
                    mExpenseDatabase.child(id).setValue(data);

                    Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }catch (Exception e){
                    Toast.makeText(getActivity(), "Incompatible Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    //Income dashboard

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> optionsIncome =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase, Data.class)
                        .setLifecycleOwner(this)
                        .build();
        //Read from Savings database
        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>( optionsIncome) {
            @NonNull
            @Override
            public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income, parent, false);
                return new IncomeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull IncomeViewHolder holder, int position, @NonNull Data model) {
                holder.setIncomeAmount(model.getAmount());
                holder.setIncomeType(model.getType());
                holder.setIncomeDate(model.getDate());
            }
        };

        //Read from expenses
        FirebaseRecyclerOptions<Data> optionsExpense =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data,ExpenseViewHolder>(optionsExpense) {
            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_expense, parent, false);
                return new ExpenseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Data model) {
                holder.setIncomeAmount(model.getAmount());
                holder.setIncomeType(model.getType());
                holder.setIncomeDate(model.getDate());
            }
        };
        mRecyclerIncome.setAdapter(incomeAdapter);
        incomeAdapter.startListening();
        mRecyclerExpense.setAdapter(expenseAdapter);
        expenseAdapter.startListening();

    }


    //Read income or savings  data idgaf what i call it anymore

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        View mIncomeView;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            mIncomeView = itemView;
        }

        public void setIncomeType(String type) {
            TextView mType = mIncomeView.findViewById(R.id.type_incomeDashboard);
            mType.setText(type);
        }

        public void setIncomeAmount(int amount) {
            TextView mAmount = mIncomeView.findViewById(R.id.amount_incomeDashboard);
            mAmount.setText(String.valueOf(amount));  // Use String.valueOf to convert int to String
        }

        public void setIncomeDate(String date) {
            TextView mDate = mIncomeView.findViewById(R.id.date_incomeDashboard);
            mDate.setText(date);
        }
    }

    //Read expense data
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        View mExpenseView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            mExpenseView = itemView;
        }

        public void setIncomeType(String type) {
            TextView mType = mExpenseView.findViewById(R.id.type_expenseDashboard);
            mType.setText(type);
        }

        public void setIncomeAmount(int amount) {
            TextView mAmount =mExpenseView.findViewById(R.id.amount_expenseDashboard);
            mAmount.setText(String.valueOf(amount));  // Use String.valueOf to convert int to String
        }

        public void setIncomeDate(String date) {
            TextView mDate = mExpenseView.findViewById(R.id.amount_expenseDashboard);
            mDate.setText(date);
        }
    }
}