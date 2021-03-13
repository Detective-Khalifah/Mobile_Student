package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleCalculatorFragment extends Fragment {

    private static final String LOG_TAG = SimpleCalculatorFragment.class.getName();
    private static StringBuilder expression = new StringBuilder(), result;
    private static int[] buttonsArr = {
            R.id.bt_one, R.id.bt_two, R.id.bt_three, R.id.bt_four, R.id.bt_five, R.id.bt_six,
            R.id.bt_seven, R.id.bt_eight, R.id.bt_nine, R.id.bt_lePar, R.id.bt_rePar,
            R.id.bt_minus, R.id.bt_plus, R.id.bt_plus_minus, R.id.bt_division, R.id.bt_multiplication,
            R.id.bt_equal, R.id.bt_decimal
    };
    private TextView tvExpression, tvResult;
    private Button btnDelete;

    public SimpleCalculatorFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple_calculator, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvExpression = (TextView) view.findViewById(R.id.tv_expression);
        tvResult = (TextView) view.findViewById(R.id.tv_result);

        for (int buttonId : buttonsArr) {
            Button button = (Button) view.findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    touched(v);
                }
            });
        }

        btnDelete = (Button) view.findViewById(R.id.bt_del);

        if (savedInstanceState != null) {
            expression = new StringBuilder(savedInstanceState.getString("expression_string"));
            savedInstanceState.getString("result_string");
        }

        displayResult();
    }

    /**
     * Ascertain the {@link @param view} item clicked, and trigger the appropriate response.
     *
     * @param view item clicked
     */
    public void touched (View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_decimal:
                appendChar("deciPoint");
                break;
            case R.id.bt_minus:
                appendChar("minus");
                break;
            case R.id.bt_plus:
                appendChar("plus");
                break;
            case R.id.bt_multiplication:
                appendChar("multi");
                break;
            case R.id.bt_division:
                appendChar("divi");
                break;
            case R.id.bt_plus_minus:
                appendChar("plusMinus");
                break;
            case R.id.bt_equal:
                appendChar("equals?");
                break;
            case R.id.bt_zero:
                appendChar("0");
                break;
            case R.id.bt_one:
                appendChar("1");
                break;
            case R.id.bt_two:
                appendChar("2");
                break;
            case R.id.bt_three:
                appendChar("3");
                break;
            case R.id.bt_four:
                appendChar("4");
                break;
            case R.id.bt_five:
                appendChar("5");
                break;
            case R.id.bt_six:
                appendChar("6");
                break;
            case R.id.bt_seven:
                appendChar("7");
                break;
            case R.id.bt_eight:
                appendChar("8");
                break;
            case R.id.bt_nine:
                appendChar("9");
                break;
            case R.id.bt_lePar:
                appendChar("(");
                break;
            case R.id.bt_rePar:
                appendChar(")");
                break;
        }
    }

    /**
     * Checks the expression to determine what arithmetic symbols are at the beginning or end;
     * check it there's a decimal point anywhere in the expression or otherwise;
     * and decide behavior of the recent button tapped/clicked.
     *
     * @param btn identifier for numeric/symbol button clicked.
     */
    private void appendChar (String btn) {
        char firstChar = 'a', lastChar = 'z';
        int lastPosition = 0;
        if (expression.length() > 1) {
            lastPosition = expression.length() - 1;
            firstChar = expression.charAt(0);
            lastChar = expression.charAt(lastPosition);
        }
        String eq = String.valueOf(expression);
        switch (btn) {
            case "deciPoint":
                // check if a decimal point is contained in the equation
                // If there's one, replace it at the end; otherwise, append '.' to end of expression
                if (eq.contains("."))
                    expression.deleteCharAt(expression.indexOf("."));
                expression.append('.');
                displayResult();
                break;
            case "divi":
                if (eq.endsWith("/"))
                    break;
                else if (lastChar == '+' || lastChar == '-' || lastChar == '*')
                    expression.replace(lastPosition, expression.length(), "/");
                else
                    expression.append("/");
                tvExpression.setText(expression);
                break;
            case "minus":
                if (eq.endsWith("-"))
                    break;
                else if (lastChar == '+' || lastChar == '*' || lastChar == '/')
                    expression.replace(lastPosition, expression.length(), "-");
                else
                    expression.append('-');
                tvExpression.setText(expression);
                break;
            case "multi":
                if (eq.endsWith("*"))
                    break;
                else if (lastChar == '+' || lastChar == '-' || lastChar == '/')
                    expression.replace(lastPosition, expression.length(), "*");
                else
                    expression.append("*");
                tvExpression.setText(expression);
                break;
            case "plus":
                if (eq.endsWith("+")) {
                    break;
                } else if (lastChar == '-' || lastChar == '*' || lastChar == '/')
                    expression.replace(lastPosition, expression.length(), "+");
                else {
                    expression.append("+");
                }
                tvExpression.setText(expression);
                break;
            case "plusMinus":
                if (firstChar == '-') {
                    eq = String.valueOf(expression.deleteCharAt(0));
                    expression = new StringBuilder(eq);
                } else {
                    expression = new StringBuilder("-").append(eq);
                }
                displayResult();
                break;
            case "equals?":
                if (eq.endsWith("*") || eq.endsWith("+") || eq.endsWith("-") || eq.endsWith("/"))
                    eq = String.valueOf(expression.deleteCharAt(lastPosition));
                displayResult();
                break;
            case "(":
                break;
            case ")":
                break;
            default:
                expression.append(btn);
                displayResult();
        }
    }

    /**
     * This method evaluates the expression on #tvExpression and displays it on #tvResult
     */
    private void displayResult () {
        String evalResult; // evaluated result

        // Mozilla Rhino
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
            evalResult = String.valueOf(engine.eval(String.valueOf(expression)));

            tvExpression.setText(expression);
            tvResult.setText(evalResult);
        } catch (ScriptException se) {
            tvExpression.setText(se.getLocalizedMessage());
        }

    }

}