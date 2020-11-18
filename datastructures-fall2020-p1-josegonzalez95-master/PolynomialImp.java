
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


/**
 * @author Jose G. Gonzalez Massini
 */

public class PolynomialImp implements Polynomial {


    private ArrayList<Term> terms;

    public PolynomialImp(String P) {
        terms = new ArrayList<Term>();
        stringToPolynomial(P);
//        for (int i = 0; i < this.terms.size(); ++i) {
//            System.out.print("[" + this.terms.get(i).getCoefficient() + "|");
//            System.out.print(this.terms.get(i).getExponent() + "]");
//        }
    }


    // the strategy is to iterate in the two arrays simultaneously, this and P2
    // we use a boolean to know when a term in the array this has no one to be added with

    // Each term of P2 once added is removed from the list, if a term is not removed from the list it means that they had no one to add to, so it is added to result

    /**
     * Addition method
     *
     * @param P2 polynomial to be added
     * @return new polynomial after addition
     */
    @Override
    public Polynomial add(Polynomial P2) {
        PolynomialImp temp = (PolynomialImp) P2;
        PolynomialImp result = new PolynomialImp("");
        for (Term t : this.terms) {
            boolean notSummed = true;
            for (Term t1 : temp.terms) {
                if (t.getExponent() == t1.getExponent()) {
                    result.addTerm(addTerms(t, t1));
                    notSummed = false;
                    temp.terms.remove(t1);
                }
            }
            if (notSummed) {
                result.addTerm(t);
            }
        }
        for (Term t2 : temp.terms) {
            result.addTerm(t2);
        }
        return result;
    }

    /**
     * Subtract method
     *
     * @param P2 polynomial to be subtracted
     * @return new polynomial after subtraction
     */
    @Override
    public Polynomial subtract(Polynomial P2) {
        return this.add(P2.multiply(-1));
    }

    /**
     * Multiply two polynomials
     *
     * @param P2 Polynomial to be multiply
     * @return resulting polynomial after multiplication
     */
    @Override
    public Polynomial multiply(Polynomial P2) {
        PolynomialImp temp = (PolynomialImp) P2;
        PolynomialImp temp1 = new PolynomialImp("");
        PolynomialImp result = new PolynomialImp("");


        for (Term t : this.terms) {
            for (Term t1 : temp.terms) {
                temp1.addTerm(multiplyTerms(t, t1));
            }
        }

        ArrayList<Integer> indexToDelete = new ArrayList<Integer>(); // list to store index to be deleted from temp1
        while (temp1.terms.size() > 0) {
            indexToDelete = new ArrayList<Integer>();

            Term a = temp1.terms.get(0);
            Term b = temp1.terms.get(0);
            indexToDelete.add(0);
            for (int i = 1; i < temp1.terms.size(); i++) {
                b = addTerms(b, temp1.terms.get(i));
                if (b == null) {
                    b = a;
                } else {
                    a = b;
                    indexToDelete.add(i);
                }
            }
            result.addTerm(a);
            for (int j = 0; j < indexToDelete.size(); j++) {
                temp1.terms.remove(indexToDelete.get(j) - j);
            }
        }
        return result;
    }


    /**
     * Multiply by a scalar value
     *
     * @param c value to be multiply and return
     * @return new polynomial after scalar product
     */
    @Override
    public Polynomial multiply(double c) {
        PolynomialImp result = new PolynomialImp("");
        for (Term t : this.terms) {
            t = new TermImp(c * t.getCoefficient(), t.getExponent());
            result.addTerm(t);
        }
        return result;
    }

    /**
     * @return the derivative of the polynomial as a new polynomial
     */
    @Override
    public Polynomial derivative() {
        PolynomialImp result = new PolynomialImp("");
        Term t;
        for (int i = 0; i < this.terms.size(); ++i) {
            t = new TermImp(this.terms.get(i).getExponent() * this.terms.get(i).getCoefficient(),
                    this.terms.get(i).getExponent() - 1);
            result.addTerm(t);
        }
        return result;
    }

    /**
     * @return new polynomial as the indefinite integral of current polynomial
     */
    @Override
    public Polynomial indefiniteIntegral() {
        PolynomialImp result = new PolynomialImp("");
        Term dummyConstant = new TermImp(1.00, 0);
        Term t;
        for (int i = 0; i < this.terms.size(); ++i) {
            t = new TermImp((this.terms.get(i).getCoefficient()) / (this.terms.get(i).getExponent() + 1),
                    this.terms.get(i).getExponent() + 1);
            result.addTerm(t);
        }
        result.addTerm(dummyConstant);
        return result;
    }

    /**
     * Evaluate the integral of the polynomial
     *
     * @param a = lower bound of the integral
     * @param b = upper bound of the integral
     * @return the result of evaluating the integral
     */
    @Override
    public double definiteIntegral(double a, double b) {
        return this.indefiniteIntegral().evaluate(b) - this.indefiniteIntegral().evaluate(a);
    }

    /**
     * @return the degree of the polynomial
     */
    @Override
    public int degree() {
        return this.terms.get(0).getExponent();
    }

    /**
     * @param x value to be evaluated on each term
     * @return double value of the evaluating result
     */
    @Override
    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < this.terms.size(); ++i) {
            result += this.terms.get(i).evaluate(x);
        }
        return result;
    }


    public String toString() {
        String result = null;
        if (!this.terms.isEmpty()) {
            result = String.format("%.2f", this.terms.get(0).getCoefficient()) + "x^" + this.terms.get(0).getExponent();
            for (int i = 1; i < this.terms.size(); ++i) {
                if (this.terms.get(i).getExponent() == 0) {
                    result += "+" + String.format("%.2f", this.terms.get(i).getCoefficient());
                } else if (this.terms.get(i).getExponent() > 1) {
                    result += "+" + String.format("%.2f", this.terms.get(i).getCoefficient()) + "x^" + this.terms.get(i).getExponent();
                } else {
                    result += "+" + String.format("%.2f", this.terms.get(i).getCoefficient()) + "x";
                }
            }
        } else {
            result = "0.00x^0";
        }
        return result;
    }

    /**
     * @return true if the polynomials are equal
     */
    @Override
    public boolean equals(Polynomial P2) {
        return P2.toString().equals(this.toString());
    }

    // non-member method that takes two terms as parameters and adds if both of them
    // together have the same exponent they are added and return as a new term
    // if not they cant be added it return null.
    private Term addTerms(Term t, Term t1) {
        if (t.getExponent() == t1.getExponent()) {
            return new TermImp(t1.getCoefficient() + t.getCoefficient(), t1.getExponent());
        }
        return null;
    }

    //non-member method that takes two terms as parameters and multiplies them.
    private Term multiplyTerms(Term t, Term t1) {
        return new TermImp(t1.getCoefficient() * t.getCoefficient(), t1.getExponent() + t.getExponent());
    }

    //Non-member method that converts the string into polynomial and add each term to array this.
    private void stringToPolynomial(String P) {
        StringTokenizer stringToken = new StringTokenizer(P, "+");
        String str = null;
        Term term = null;
        while (stringToken.hasMoreElements()) {
            str = (String) stringToken.nextElement();
            term = TermImp.stringToTerms(str);
            this.addTerm(term);
        }
    }

    //Non-member method that organize the terms in array this in descending order and takes care
    // of not adding terms with coefficient of 0.
    private void addTerm(Term term) {
        if (term.getCoefficient() != 0) {
            if (this.terms.isEmpty()) {
                this.terms.add(term);
            } else {
                boolean needsToBeAdded = true;
                for (int i = 0; i < this.terms.size(); ++i) {
                    if (term.getExponent() > this.terms.get(i).getExponent()) {
                        this.terms.add(term, i);
                        needsToBeAdded = false;
                        break;
                    }
                }
                if (needsToBeAdded) {
                    this.terms.add(term);
                }
            }
        } else {
            if (this.terms.isEmpty() && term.getExponent() == 0) {
                this.terms.add(term);
            }
        }
    }

    @SuppressWarnings("hiding")
	public class PolynomialImpIterator<Term> implements Iterator<Term> {

        private int currentPosition;

        public PolynomialImpIterator() {
            this.currentPosition = 0;
        }

        @Override
        public boolean hasNext() {
            return this.currentPosition < terms.size();
        }

        @Override
        public Term next() {
            if (this.hasNext()) {
                @SuppressWarnings("unchecked")
				Term result = (Term) terms.get(this.currentPosition++);
                return result;
            } else {
                throw new NoSuchElementException();
            }
        }
    }


    @Override
    public Iterator<Term> iterator() {
        return new PolynomialImpIterator<>();
    }
}
