Our take on Java's BigInteger class. This Num class implements following operatoins on arbitrary long number.

Group Members
* Param Parikh
* Tej Patel

Public Methods
1. Num(String s)
2. Num(long x)
3. Num add(Num a, Num b)
4. Num subtract(Num a, Num b)
5. Num product(Num a, Num b)
6. Num power(Num a, long n)
7. Num divide(Num a , Num b)
8. Num mod(Num a, Num b)
9. Num squareRoot(Num a)
10. Int compareTo(Num other)
11. String toString()
12. Long Base()
13. Num convertBase(int _newBase)
14. Num by2()
15. Num evaluatePostfix(String[] expr)
16. Num evaluateInfix(String[] expr)

Few results:

1) Fib(524287) is nearly 1,08,000 digits long and our codes outputs it in 13sec using 230MB memory.
2) Pow(3,999999) is calculated in 60sec using 450MB memory.