package ru.abelov.compassview;

import java.util.ArrayList;

@Deprecated
public class GIConveyor {

    public final int m_deep = 10;
    public ArrayList<Value> m_values;

    public GIConveyor() {
        m_values = new ArrayList<Value>();
    }

    public void addValue(double value) {
        if (m_values.size() == m_deep) {
            m_values.remove(0);
        }
        m_values.add(new Value(value));
    }

    public double getValue() {
        double res = 0;
        for (int i = 0; i < m_values.size(); i++) {
            double val = m_values.get(0).getValue();
            if (val < 0) {
                val = val + 360;
            }
            res += val;
        }
        res = res / m_values.size();
        return res;
    }

    public class Value {
        private double m_value;

        Value() {
            m_value = 0;
        }

        Value(double value) {
            m_value = value;
        }

        double getValue() {
            return m_value;
        }
    }

}
