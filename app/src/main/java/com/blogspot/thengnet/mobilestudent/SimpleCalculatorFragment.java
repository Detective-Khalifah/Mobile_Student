package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private static StringBuilder expression = new StringBuilder();
    private static int[] buttonsArr = {
            R.id.bt_zero, R.id.bt_one, R.id.bt_two, R.id.bt_three, R.id.bt_four, R.id.bt_five,
            R.id.bt_six, R.id.bt_seven, R.id.bt_eight, R.id.bt_nine, R.id.bt_lePar, R.id.bt_rePar,
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
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("expression_string", String.valueOf(expression));
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

        // Event handlers for "DEL" button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            /**
             * Delete character at last position of expression, if expression not empty
             *
             * @param v
             */
            @Override
            public void onClick (View v) {
                if (String.valueOf(expression).equals("") || expression == null)
                    return;
                expression.deleteCharAt(expression.length() - 1); // delete character at last index
                displayExpression(); // show expression after deleting character at last index
            }
        });
        btnDelete.setOnLongClickListener(new View.OnLongClickListener() {
            /**
             * Delete all characters in the expression
             *
             * @param v -- the #btnDelete button
             * @return
             */
            @Override
            public boolean onLongClick (View v) {
                if (!String.valueOf(expression).equals("") || expression != null)
                    expression = null;
                displayExpression();
                displayResult();
                return false;
            }
        });

        if (savedInstanceState != null) {
            expression = new StringBuilder(savedInstanceState.getString("expression_string"));
            displayExpression();
            displayResult();
        }

        displayExpression();
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
        if (expression != null) {
            if (expression.length() > 1) {
                lastPosition = expression.length() - 1;
                firstChar = expression.charAt(0);
                lastChar = expression.charAt(lastPosition);
            }
        } else {
            expression = new StringBuilder();
        }
        String eq = String.valueOf(expression);
        switch (btn) {
            case "deciPoint":
                // check if a decimal point is contained in the equation
                // If there's one, replace it at the end; otherwise, append '.' to end of expression
                if (eq.contains("."))
                    expression.deleteCharAt(expression.indexOf("."));
                expression.append('.');
                displayExpression();
                break;
            case "divi":
                if (eq.endsWith("/"))
                    break;
                else if (lastChar == '+' || lastChar == '-' || lastChar == '*')
                    expression.replace(lastPosition, expression.length(), "/");
                else
                    expression.append("/");
                displayExpression();
                break;
            case "minus":
                if (eq.endsWith("-"))
                    break;
                else if (lastChar == '+' || lastChar == '*' || lastChar == '/')
                    expression.replace(lastPosition, expression.length(), "-");
                else
                    expression.append('-');
                displayExpression();
                break;
            case "multi":
                if (eq.endsWith("*"))
                    break;
                else if (lastChar == '+' || lastChar == '-' || lastChar == '/')
                    expression.replace(lastPosition, expression.length(), "*");
                else
                    expression.append("*");
                displayExpression();
                break;
            case "plus":
                if (eq.endsWith("+")) {
                    break;
                } else if (lastChar == '-' || lastChar == '*' || lastChar == '/')
                    expression.replace(lastPosition, expression.length(), "+");
                else {
                    expression.append("+");
                }
                displayExpression();
                break;
            case "plusMinus":
                // If expression is empty, simply skip using plus-minus sign the process of finding
                // last sign, and return to last point/stack
                if (eq.length() == 0)
                    return;

                // If a "-" sign already exists at the beginning, skip the process of finding last
                // sign, and return to last point/stack
                if (eq.charAt(0) == '-')
                    return;
                int lastSignIndex = checkSign();

                Log.v(LOG_TAG, "lastSignIndex: " + lastSignIndex);

                // Add a "-" sign to the part of expression after a sign (+, -, / or *) if one exists
                if (lastSignIndex != -1) {
                    String rem = expression.substring(lastSignIndex + 1, expression.length());

                    // When there are no numbers after the last sign, return to stack
                    if (!rem.equals(""))
                        return;

                    // When there already exists a plus-minus expression segment
                    // TODO: Use a regex here; would be easier to find functions already having
                    //  "(-function)" at different segments to avoid repeating at same
                    //  segment, and toggle deletion of "(-) without function part, if one already
                    //  exits. Also find out the plus-minus code; that'd make things easier without
                    //  confusing minus sign
                    if (eq.contains("(-")) {
                        Log.v(LOG_TAG, "expression in eq.contains(\"(-\")) check:: " + expression);
                        return;
                    }
                    expression = new StringBuilder(eq.substring(0, lastSignIndex) + eq.charAt(lastSignIndex) + "(-" + rem + ")");
                } else {
                    // add a "-" sign at the beginning and append the expression if no sign is found
                    expression = new StringBuilder("-").append(eq);
                }

                displayExpression();
                break;
            case "equals?":
                if (eq.endsWith("*") || eq.endsWith("+") || eq.endsWith("-") || eq.endsWith("/"))
                    expression.deleteCharAt(lastPosition);
                displayResult();
                break;
            // TODO: Add logic and layout tricks to handle "dangling parentheses"
            case "(":
                expression.append("(");
                displayExpression();
                break;
            case ")":
                expression.append(")");
                displayExpression();
                break;
            case "0":
                if (expression != null) {
                    if (!(expression.length() == 1 && expression.charAt(0) == '0'))
                        expression.append(btn);
                } else {
                    expression.append(btn);
                }
                displayExpression();
                break;
            default:
                if (expression != null)
                    expression.append(btn);
                displayExpression();
        }
    }

    /**
     * Traverse the expression from right, and return the index (if found) of the first
     * arithmetic sign encountered
     *
     * @return the sign's index (if one exists)
     */
    private int checkSign () {
        int lastSignIndex = -1;

        for (int z = expression.length() - 1; z > 1; z--) {
            switch (expression.charAt(z)) {
                case '-':
                case '+':
                case '/':
                case '*':
                    lastSignIndex = z;
                    break;
                default:
            }

            if (lastSignIndex != -1)
                break;
        }

        return lastSignIndex;

    }

    /**
     * Utility method that displays the expression's current state in the #tvExpression
     * {@link TextView}
     */
    private void displayExpression () {
        if (expression != null)
            tvExpression.setText(expression);
    }

    /**
     * This method evaluates the expression on #tvExpression and displays it on #tvResult
     */
    private void displayResult () {
        String evalResult; // evaluated result

        /**
         * Do not evaluate expression if it's empty!
         * Simply set the expression & result #TextViews to the null String ""
         */
        if (String.valueOf(expression).equals("") || expression == null) {
            tvExpression.setText("");
            tvResult.setText("");
            return;
        }

        // Mozilla Rhino
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
            evalResult = String.valueOf(engine.eval(String.valueOf(expression)));

            // Remove ".0" if that's the last string at end of evaluated expression
            // TODO: Check type of number and use it to determine result display format
            if (evalResult.substring(evalResult.length() - 2, evalResult.length()).equals(".0"))
                evalResult = evalResult.substring(0, evalResult.length() - 2);

            tvResult.setText(evalResult);

            // reset #expression to the calculated#evalResult, so further calculation can be made using it.
            expression = new StringBuilder(evalResult);
        } catch (ScriptException se) {
            tvResult.setText(getString(R.string.warning_syntax_error));
        }

    }

}