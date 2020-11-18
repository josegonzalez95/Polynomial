import java.util.StringTokenizer;


/**
 * @author Jose G. Gonzalez Massini
 */

public class TermImp implements Term {
    private double coefficient;
    private int exponent;

    public TermImp(double coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }
    

    @Override
    public double getCoefficient() {
        return this.coefficient;
    }

    @Override
    public int getExponent() {
        return this.exponent;
    }

    @Override
    public double evaluate(double x) {
        return this.coefficient * Math.pow(x, this.exponent);
    }
    
    // this method takes terms in form of string from the original string polynomial
    // and creates a new Term object and returns the Term created.
    public static Term stringToTerms(String str) {
        Term result = null;
        if (str.contains("x^")) {
            StringTokenizer stringToken = new StringTokenizer(str, "x^");
            List<String> list = new ArrayList<String>();
            while (stringToken.hasMoreElements()) {
                list.add((String) stringToken.nextElement());
            }
            if (list.size() == 0) {
                throw new IllegalArgumentException("Wrong");
            } else if (list.size() == 1) {
                Integer exponent = Integer.parseInt(list.get(0));
                result = new TermImp(1, exponent);
            } else {
                Double coefficient = Double.parseDouble(list.get(0));
                Integer exponent = Integer.parseInt(list.get(1));
                result = new TermImp(coefficient, exponent);
            }
        } else if (str.contains("x")) {
            StringTokenizer stringToken = new StringTokenizer(str, "x");
            List<String> list = new ArrayList<String>();
            while (stringToken.hasMoreElements()) {
                list.add((String) stringToken.nextElement());
            }
            if (list.size() == 0) {
                result = new TermImp(1.0, 1);
            } else {
                Double coefficient = Double.parseDouble(list.get(0));
                result = new TermImp(coefficient, 1);
            }
        } else {
            result = new TermImp(Double.parseDouble(str), 0);
        }
        return result;
    }


}
