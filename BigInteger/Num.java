import java.util.*;

/**
 * Version : 1.0
 *
 * @author Tej Patel, Param Parikh
 */
public class Num implements Comparable<Num> {

    /**
     * defaultBase of number
     */
    static long defaultBase = 3037000499L;

    /**
     * base provided by user (if any)
     */
    long base = defaultBase;

    /**
     * array to store arbitrarily large integers
     */
    long[] arr;

    /**
     * boolean flag to represent negative numbers
     */
    boolean isNegative;

    /**
     * actual number of elements of array that are used; number is stored in
     * arr[0..len-1]
     */
    int len;

    /**
     * Constructor for creating array accepting arbitrarily large number in form
     * of string
     *
     * @param s string which stores arbitrarily large number
     */
    public Num(String s) {
        this.len = (int) Math.ceil(s.length() / Math.log10(this.base)); // Calculates the length
        this.arr = new long[this.len];
        if (s.charAt(0) == '-') {
            this.isNegative = true;
            this.convertToLongBase(new StringBuilder(s.substring(1)), 0);
        } else {
            this.convertToLongBase(new StringBuilder(s), 0);
        }
        this.clean();
    }

    /**
     * Constructor for creating array accepting arbitrarily large number in form
     * of long
     *
     * @param x long that stores the number
     */
    public Num(long x) {
        this(String.valueOf(x));
    }

    /**
     * Constructor for creating array accepting arbitrarily large number in form
     * of long
     *
     * @param x number represented as long
     * @param base the base that is used to create the array
     */
    private Num(long x, long base) {
        String s = String.valueOf(x);
        this.base = base;
        this.len = (int) Math.ceil(s.length() / Math.log10(this.base)); // Calculates the length
        this.arr = new long[this.len];
        if (s.charAt(0) == '-') {
            this.isNegative = true;
            this.convertToLongBase(new StringBuilder(s.substring(1)), 0);
        } else {
            this.convertToLongBase(new StringBuilder(s), 0);
        }
        this.clean();
    }

    /**
     * Constructor for creating array accepting arbitrarily large number in form
     * of string
     *
     * @param s String that stores the number
     * @param base the base that is used to create the array
     */
    private Num(String s, int base) {
        this.base = base;
        this.len = (int) Math.ceil(s.length() / Math.log10(this.base)); // Calculates the length
        this.arr = new long[this.len];
        if (s.charAt(0) == '-') {
            this.isNegative = true;
            this.convertToLongBase(new StringBuilder(s.substring(1)), 0);
        } else {
            this.convertToLongBase(new StringBuilder(s), 0);
        }
        this.clean();
    }

    /**
     * Constructor for creating array accepting arbitrarily large number in form
     * of string
     *
     * @param s String that stores the number
     * @param base the base that is used to create the array
     */
    private Num(String s, long base) {
        this.base = base;
        this.len = (int) Math.ceil(s.length() / Math.log10(this.base)); // Calculates the length
        this.arr = new long[this.len];
        if (s.charAt(0) == '-') {
            this.isNegative = true;
            this.convertToLongBase(new StringBuilder(s.substring(1)), 0);
        } else {
            this.convertToLongBase(new StringBuilder(s), 0);
        }
        this.clean();
    }

    /**
     * Constructor for creating object from array
     *
     * @param arr Array representing our number
     * @param isNegative Boolean that stores the sign of current number
     */
    private Num(long[] arr, boolean isNegative) {
        this.arr = arr;
        this.isNegative = isNegative;
        this.len = arr.length;
        this.clean();
    }

    /**
     * Converts a string into the default base using traditional way of
     * converting a number into default base using recursion.
     *
     * @param q StringBuilder object from which array is to be created
     * @param index current index of array where the number is to be stored
     */
    private void convertToLongBase(StringBuilder q, int index) {
        if (q.length() <= 0) {
            return;
        }
        if (q.length() < String.valueOf(base).length()) {
            this.arr[index] = Long.parseLong(q.toString());
            return;
        }
        StringBuilder dividend = new StringBuilder();
        int i = 0;
        long temp = 0;
        StringBuilder num = new StringBuilder();
        while (i < q.length()) {
            temp = (temp * 10) + (q.charAt(i) - '0');
            i++;
            if (temp >= base) {
                num.append(temp % base);
                dividend.append(temp / base);
                temp = temp % base;
                break;
            }
        }
        while (i < q.length()) {
            temp = (temp * 10) + (q.charAt(i) - '0');
            if (temp >= base) {
                num.append(temp % base);
                dividend.append(temp / base);
                temp = temp % base;
            } else {
                dividend.append('0');
            }
            i++;
        }
        this.arr[index] = temp;
        convertToLongBase(dividend, index + 1);
    }

    /**
     * Addition of 2 Num objects
     *
     * @param a First Num Object
     * @param b Second Num Object
     * @return object which stores addition of a and b
     */
    public static Num add(Num a, Num b) {
        if (a.base != b.base) {
            b = b.convertBase(a.base);
        }
        long[] result = new long[Math.max(a.len, b.len) + 1];
        long base = a.base;
        boolean isNegative;
        // case where sign change is not needed, both a and b have same sign.
        if (!(a.isNegative ^ b.isNegative)) {
            long carry = 0;
            int i = 0;
            while (i < a.arr.length && i < b.arr.length) {
                long total = a.arr[i] + b.arr[i] + carry;
                carry = total / base;
                long val = total % base;
                result[i] = val;
                i++;
            }

            while (i < a.arr.length) {
                long total = a.arr[i] + carry;
                carry = total / base;
                long val = total % base;
                result[i] = val;
                i++;
            }

            while (i < b.arr.length) {
                long total = b.arr[i] + carry;
                carry = total / base;
                long val = total % base;
                result[i] = val;
                i++;
            }

            if (carry > 0) {
                result[i] = carry;
            }
            isNegative = a.isNegative;
        } // Case where a and b have different sign, perform subtraction using
        // subhelper.
        else {
            int comparison = a.compareTo(b); // comparison returns -1 if a < b
            if (comparison < 0) {
                isNegative = b.isNegative;
                result = Num.subHelper(b, a);
            } else if (comparison > 0) {
                isNegative = a.isNegative;
                result = Num.subHelper(a, b);
            } else {
                return new Num(0, base);
            }
        }
        Num addition = new Num(result, isNegative);
        addition.base = a.base;
        addition.clean();
        return addition;
    }

    /**
     * Helper method for subtracting 2 Num objects, assumes a is greater than b
     *
     * @param a First Num Object
     * @param b Second Num Object
     * @return object which stores subtraction of a and b
     */
    public static long[] subHelper(Num a, Num b) {
        if (a.base != b.base) {
            b = b.convertBase(a.base);
        }
        long[] result = new long[Math.max(a.len, b.len) + 1];
        int i = 0;
        long base = a.base;
        long[] subArray = new long[a.arr.length];
        System.arraycopy(a.arr, 0, subArray, 0, a.arr.length);
        while (i < subArray.length && i < b.arr.length) {
            if (b.arr[i] > subArray[i]) {
                subArray[i] += a.base;
                int index = i + 1;
                if (index < subArray.length) {
                    while (index < subArray.length && subArray[index] == 0) {
                        subArray[index] = subArray[index] + base - 1;
                        index++;
                    }
                    subArray[index]--;
                }
                result[i] = subArray[i] - b.arr[i];
            } else {
                result[i] = subArray[i] - b.arr[i];
            }
            i++;

        }
        while (i < subArray.length) {
            result[i] = subArray[i];
            i++;
        }
        return result;
    }

    /**
     * Subtraction of 2 Num objects
     *
     * @param a First Num Object
     * @param b Second Num Object
     * @return object which stores subtraction of a and b
     */
    public static Num subtract(Num a, Num b) {
        if (a.base != b.base) {
            b = b.convertBase(a.base);
        }
        Num c = new Num(b.arr, !b.isNegative);
        c.base = a.base;
        return Num.add(a, c);
    }

    /**
     * Product of 2 Num objects in old school way
     *
     * @param a First Num Object
     * @param b Second Num Object
     * @return object which stores product of a and b
     */
    public static Num product(Num a, Num b) {
        if (a.base != b.base) {
            b = b.convertBase(a.base);
        }
        long[] result = new long[a.arr.length + b.arr.length + 1]; // stores the intermediate digits
        int j = 0;
        long base = a.base;
        while (j < b.len) {
            long carry = 0;
            int i = 0;
            long val2 = b.arr[j];
            int resIndex = j;
            while (i < a.len) {
                long val1 = a.arr[i] * val2;
                long total = val1 + carry + result[resIndex];
                carry = total / base;
                result[resIndex] = total % base;
                resIndex++;
                i++;
            }
            while (carry > 0) {
                long total = result[resIndex] + carry;
                carry = total / base;
                result[resIndex] = (result[resIndex] + total) % base;
                resIndex++;
            }
            j++;
        }
        // Xor for maintaining correct sign
        Num product = new Num(result, (a.isNegative ^ b.isNegative));
        product.base = a.base;
        product.clean();
        return product;
    }

    /**
     * Power of a Num object
     *
     * @param a Num Object
     * @param n long representing the power
     * @return object which stores power of a to n
     */
    public static Num power(Num a, long n) {
        if (n <= 0) {
            return new Num("1", a.base);
        }
        if (n == 1) {
            return a;
        }
        if (n % 2 == 0) {
            Num ret = power(Num.product(a, a), n / 2);
            ret.base = a.base;
            return ret;
        } else {
            Num ret = Num.product(a, power(Num.product(a, a), (n - 1) / 2));
            ret.base = a.base;
            return ret;
        }
    }

    /**
     * Division of 2 Num using binary search
     *
     * @param a First Num Object
     * @param b Second Num Object
     * @return object that stores division of a and b
     */
    public static Num divide(Num a, Num b) {
        if (a.base != b.base) {
            b = b.convertBase(a.base);
        }
        Num left = new Num(0, a.base);
        if (b.compareTo(left) == 0) {
            throw new ArithmeticException();
        }
        if (b.compareTo(new Num(1, a.base)) == 0) {
            return a;
        }
        Num right = new Num(a.arr, a.isNegative);
        right.base = a.base;
        while (left.compareTo(right) < 0) {
            Num middle = Num.add(left, right).by2();
            int compare = Num.product(middle, b).compareTo(a);
            if (compare == 0 || middle.compareTo(left) == 0) {
                middle.isNegative = (a.isNegative ^ b.isNegative);
                middle.clean();
                return middle;
            } else if (compare < 0) {
                left = middle;
            } else {
                right = middle;
            }
        }
        left.isNegative = (a.isNegative ^ b.isNegative);
        left.clean();
        return left;
    }

    /**
     * Mod of 2 Num
     *
     * @param a First Num Object
     * @param b Second Num Object
     * @return Num storing mod of a and b
     */
    public static Num mod(Num a, Num b) {
        if (b.isNegative) {
            throw new ArithmeticException();
        }
        if (a.base != b.base) {
            b = b.convertBase(a.base);
        }
        Num division = Num.divide(a, b);
        return Num.subtract(a, Num.product(division, b));
    }

    /**
     * Square root of Num using binary search
     *
     * @param a Num Object
     * @return Num storing square root of a
     */
    public static Num squareRoot(Num a) {
        if (a.isNegative) {
            throw new ArithmeticException();
        }
        Num left = new Num(0, a.base);
        Num right = new Num(a.arr, a.isNegative);
        right.base = a.base;
        while (left.compareTo(right) < 0) {
            Num middle = Num.add(left, right).by2();
            int compare = Num.product(middle, middle).compareTo(a);
            Num newLeft = left;
            Num newRight = right;
            if (compare == 0) {
                middle.clean();
                return middle;
            } else if (compare > 0) {
                right = middle;
            } else {
                left = middle;
            }
            if (newLeft.compareTo(left) == 0 && newRight.compareTo(right) == 0) {
                break;
            }
        }
        left.clean();
        return left;
    }

    /**
     * Compares 2 Num objects. Sign is ignored, only magnitude is compared
     *
     * @param other Num object to be compared with current Num
     * @return +1 if current num is greater than other num, 0 if equal and -1 if
     * other is bigger
     */
    @Override
    public int compareTo(Num other) {
        if (other.base != this.base) {
            other = other.convertBase(this.base);
        }
        if (this.len > other.len) {
            return 1;
        } else if (this.len < other.len) {
            return -1;
        } else {
            for (int i = this.len - 1; i >= 0; i--) {
                if (this.arr[i] == other.arr[i]) {
                    continue;
                }
                if (this.arr[i] > other.arr[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 0;
        }
    }

    /**
     * Prints the array of Num
     */
    public void printList() {
        System.out.println(this.base + ": " + Arrays.toString(this.arr));
    }

    /**
     * Converts current number into base 10
     *
     * @return String containing current number in base 10
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        LinkedList<Long> number = new LinkedList<>();

        for (long a : arr) {
            number.addFirst(a);
        }

        while (number.size() > 0) {
            long reminder = 0;
            int size = number.size();
            while (size-- > 0) {
                number.add((reminder * base + number.peek()) / 10);
                reminder = (reminder * base + number.peek()) % 10;
                number.removeFirst();
            }

            while (!number.isEmpty() && number.peek() == 0) {
                number.pop();
            }

            sb.append(reminder);
        }

        if (isNegative) {
            sb.append("-");
        }
        return sb.reverse().toString();
    }

    /**
     * @return Base of current Num
     */
    public long base() {
        return base;
    }

    /**
     * Converts number equal to "this" number, in base=newBase
     *
     * @param _newBase value of new base
     * @return Num equal to current number in _newBase
     */
    public Num convertBase(int _newBase) {
        Num result = new Num(toString(), _newBase);
        result.isNegative = this.isNegative;
        return result;
    }

    /**
     * Converts number equal to "this" number, in base=newBase
     *
     * @param _newBase
     * @return Num equal to current number in _newBase
     */
    private Num convertBase(long _newBase) {
        Num result = new Num(toString(), _newBase);
        result.isNegative = this.isNegative;
        return result;
    }

    /**
     * Divides the Num by 2 using binary search
     *
     * @return Num that stores half the value
     */
    public Num by2() {
        Num half = new Num(0, this.base);
        Num two = new Num(2, this.base);

        while (Num.product(half, two).compareTo(this) < 0) {
            Num pow = new Num(1, this.base);
            Num newHalf = new Num(half.arr, half.isNegative);
            newHalf.base = this.base;
            while (Num.product(Num.add(half, pow), two).compareTo(this) < 0) {
                half = Num.add(half, pow);
                pow = Num.product(pow, two);
            }
            if (half.compareTo(newHalf) == 0) {
                break;
            }
        }
        Num one = new Num(1, this.base);
        if (Num.product(two, Num.add(half, one)).compareTo(this) <= 0) {
            half = Num.add(half, one);
        }
        half.isNegative = this.isNegative;
        half.clean();
        return half;
    }

    /**
     * Removes extra zeros at end of array. (in beginning of a number)
     */
    private void clean() {
        if (this.len > 1) {
            int length = this.len;
            int i = this.len - 1;
            while (i >= 1 && this.arr[i] == 0) {
                length--;
                i--;
            }
            long[] array = new long[length];
            for (i = 0; i < length; i++) {
                array[i] = this.arr[i];
            }
            this.len = length;
            this.arr = array;
        } else {
            if (this.arr[0] == 0) {
                this.isNegative = false;
            }
        }
    }

    /**
     * Evaluate an expression in postfix and return resulting number Each string
     * is one of: "*", "+", "-", "/", "%", "^", "0", or a number: [1-9][0-9]*.
     * There is no unary minus operator.
     *
     * @param expr expression to evaluate
     * @return Num storing the evaluation
     */
    public static Num evaluatePostfix(String[] expr) {
        Stack<Num> operands = new Stack<>();

        for (String e : expr) {
            Token t = tokenizer(e.trim());

            switch (t) {
                case NUMBER:
                    operands.push(new Num(e));
                    break;
                case OPERATOR:
                    Num b = operands.pop();
                    Num a = operands.pop();
                    Num result;

                    if (e.equals("+")) {
                        result = Num.add(a, b);
                    } else if (e.equals("-")) {
                        result = Num.subtract(a, b);
                    } else if (e.equals("*")) {
                        result = Num.product(a, b);
                    } else if (e.equals("/")) {
                        result = Num.divide(a, b);
                    } else if (e.equals("%")) {
                        result = Num.mod(a, b);
                    } else {
                        long b_long = Long.parseLong(b.toString());
                        result = Num.power(a, b_long);
                    }
                    operands.push(result);
                    break;
                default:
                    break;
            }
        }
        if (operands.isEmpty()) {
            return new Num(0);
        }
        System.out.println("Evaluated to: " + Arrays.toString(operands.peek().arr));
        return operands.pop();
    }

    /**
     * Evaluate an expression in infix and return resulting number Each string
     * is one of: "*", "+", "-", "/", "%", "^", "0", or a number: [1-9][0-9]*.
     * There is no unary minus operator.
     *
     * @param expr expression to evaluate
     * @return Num storing the evaluation
     * @throws Exception bad operands
     */
    public static Num evaluateInfix(String[] expr) throws Exception {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        Map<String, Rule> precedence = new HashMap<>();
        precedence.put("+", new Rule(11, Associativity.LEFT));
        precedence.put("-", new Rule(11, Associativity.LEFT));
        precedence.put("*", new Rule(12, Associativity.LEFT));
        precedence.put("/", new Rule(12, Associativity.LEFT));
        precedence.put("%", new Rule(12, Associativity.LEFT));
        precedence.put("^", new Rule(14, Associativity.RIGHT));

        for (String e : expr) {
            Token t = tokenizer(e.trim());

            switch (t) {
                case NUMBER:
                    output.add(e);
                    break;
                case OPERATOR:
                    if (!precedence.containsKey(e)) {
                        throw new Exception("Operation not supported yet.");
                    }

                    Rule e_rule = precedence.get(e);
                    Rule top_rule;
                    while (!operators.empty() && !operators.peek().equals("(")) {
                        top_rule = precedence.get(operators.peek());
                        if ((e_rule.precedence < top_rule.precedence)
                                || ((e_rule.precedence == top_rule.precedence) && (top_rule.associativity == Associativity.LEFT))) {
                            output.add(operators.pop());
                        } else {
                            break;
                        }
                    }
                    operators.push(e);
                    break;
                case OPEN:
                    operators.push("(");
                    break;
                case CLOSE:
                    while (!operators.empty() && !operators.peek().equals("(")) {
                        output.add(operators.pop());
                    }
                    if (operators.empty()) {
                        throw new Exception("Bad expression");
                    }
                    operators.pop();
                    break;
                case ERROR:
                default:
                    throw new Exception("Invalid token in expression");
            }
        }

        while (!operators.empty()) {
            output.add(operators.pop());
        }

        expr = new String[output.size()];
        int index = 0;
        for (String o : output) {
            expr[index++] = o;
        }
        System.out.println("RPN: " + Arrays.toString(expr));
        return evaluatePostfix(expr);
    }

    /**
     * Tokens that can be used to distinguish various symbols of expression
     */
    private enum Token {
        NUMBER, OPERATOR, OPEN, CLOSE, ERROR
    };

    /**
     * Associativity of operator
     */
    private enum Associativity {
        LEFT, RIGHT
    };

    /**
     * Returns appropriate tokens from string
     *
     * @param s String that is to be evaluated
     * @return Token
     */
    private static Token tokenizer(String s) {
        if (s.matches("\\d+")) {
            return Token.NUMBER;
        }
        if (s.equals("(")) {
            return Token.OPEN;
        }
        if (s.equals(")")) {
            return Token.CLOSE;
        }

        String[] operators = {"+", "-", "*", "/", "%", "^"};
        for (String op : operators) {
            if (s.equals(op)) {
                return Token.OPERATOR;
            }
        }

        return Token.ERROR;

    }

    private static class Rule {

        int precedence;
        Associativity associativity;

        Rule(int _precedence, Associativity _associativity) {
            precedence = _precedence;
            associativity = _associativity;
        }
    }

    /**
     * Driver function
     *
     * @param args user input from console
     * @throws Exception Bad input
     */
    public static void main(String[] args) throws Exception {
        Num x = new Num(999);
        Num y = new Num("8");
        Num z = Num.add(x, y);
        System.out.println(z);
        Num a = Num.power(x, 8);
        System.out.println(a);
        if (z != null) {
            z.printList();
        }
    }
}
