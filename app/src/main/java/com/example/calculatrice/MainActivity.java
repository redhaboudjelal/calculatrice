package com.example.calculatrice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView screen;
    private int op1 = 0;
    private int op2 = 0;
    private Ops operator = null;
    private boolean isOp1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screen = findViewById(R.id.screen);

        findViewById(R.id.btnPlus).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setOperator(v);
            }
        });

        findViewById(R.id.btnMoins).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setOperator(v);
            }
        });

        findViewById(R.id.btnFois).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setOperator(v);
            }
        });

        findViewById(R.id.btnDiv).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setOperator(v);
            }
        });


        for (int i = 0; i <= 9; i++) {
            final int digit = i;
            int buttonId = getResources().getIdentifier("btn" + i, "id", getPackageName());
            findViewById(buttonId).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addNumber(v, digit);
                }
            });
        }


        findViewById(R.id.btnEgal).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                compute();
            }
        });
    }

    public void setOperator(View v) {
        // Vérifier l'ID du bouton
        if (v.getId() == R.id.btnPlus) {
            operator = Ops.PLUS;
        } else if (v.getId() == R.id.btnMoins) {
            operator = Ops.MOINS;
        } else if (v.getId() == R.id.btnFois) {
            operator = Ops.FOIS;
        } else if (v.getId() == R.id.btnDiv) {
            operator = Ops.DIV;
        } else {
            // Afficher un message d'erreur si l'opérateur n'est pas reconnu
            Toast.makeText(this, "Opérateur non reconnu", Toast.LENGTH_LONG).show();
            return;
        }
        isOp1 = false;
        updateDisplay();
    }

    public void addNumber(View v, int digit) {
        try {
            Button button = (Button) v;
            String buttonText = button.getText().toString();
            int value = Integer.parseInt(buttonText);

            if (isOp1) {
                op1 = op1 * 10 + value;
                updateDisplay();
            } else {
                op2 = op2 * 10 + value;
                updateDisplay();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valeur erronée", Toast.LENGTH_LONG).show();
        }
    }

    public void compute() {
        if (!isOp1) {
            switch (operator) {
                case PLUS:
                    op1 = op1 + op2;
                    break;
                case MOINS:
                    op1 = op1 - op2;
                    break;
                case FOIS:
                    op1 = op1 * op2;
                    break;
                case DIV:
                    if (op2 != 0) {
                        op1 = op1 / op2;
                    } else {
                        Toast.makeText(this, "Division par zéro", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    return; // Ne rien faire s'il n'y a pas d'opérateur
            }
            op2 = 0;
            isOp1 = true;
            updateDisplay();
        }
    }

    private void updateDisplay() {
        screen.setText(String.valueOf(isOp1 ? op1 : op2));
    }

    public enum Ops {
        PLUS,
        MOINS,
        FOIS,
        DIV
    }
}
