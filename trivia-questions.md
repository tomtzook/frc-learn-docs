
## Java 1

1. `public` access modifier means that
    1. The field/methods are only accessible from inside the current class
    2. The field/method is accessible from any class/package
    3. The field/method is only accessible from inside the current package
    4. The field/method is only accessible from inheriting classses

2. Which of the following is a valid variable declaration
    1. `String a;`
    2. `Person;`
    3. `int 123b = 5;`
    4. `double num = 11.2;`

3. Access modifiers can be applied to
    1. Class members
    2. Classes
    3. Class mehods
    4. Local variables

4. Methods are
    1. Declaration of variables
    2. Statement of code
    3. Callable body of code
    4. Abstract Classes

5. `new` is used o create a new class
    1.True
    2. False

6. Which of the following is a valid method declaration
    1. `private void walk(int distance)`
    2. `public void methodName {}`
    3. `public toString()`
    4. `public void doStuff(int, int)`

7. Which of the following creates a new instance (object) of a class properly
    1. `int a = new int`
    2. `Person person = new Person();`
    3. `Person person = Person();`
    4. `Person person = new Person("Mathew");`

8. Local variables are
    1. Variables in a class
    2. Any variable
    3. Variable that only exist in a limited scope (in methods, if statemens, etc)
    4. Variables hat only exist for a specific instance of a class

9. The value of `a` printed will be 0
    ```java
    public void print() {
        int a;
        System.out.println(a);
    }
    ```
    1. True
    2. False

10. `int` variables can sore
    1. decimal numbers
    2. only positive integer numbers
    3. any integer number
    4. integer numbers up to approx 2 billion
   
11. Member variables have a copy for each class instance
    1. True
    2. False
   
12. The following converts a `double` to an `int`
    1. `int a = Integer.convert(15.2)`
    2. `int a = (int) 15.2`
    3. `int a = Math.ceil(15.2)`
    4. impossible
   
13. The default value of a Class-Type variable is
    1. 0
    2. null
    3. Nothing, no value
   
14. A constructor is used to
    1. Create a new instance
    2. To pass variables to a class
    3. To initialize a newely created instance
    4. Kinda useless actually
   
15. The following code will
    ```java
    Person person = null;
    person.hello();
    ```
    1. Call `hello` method
    2. Do nothing
    3. Cause program to crash
    4. Throw an exception

16. What is the _JDK_
    1. A set of tools for running Java code
    2. A set of tools for running and developing Java code
    3. A set of tools for the JVM
    4. Allows Intellij to use Gradle
   
17. Default acccess modifier for a method should be `protected`
    1. True
    2. False
   
18. Constructors must be `public`
    1. True
    2. False
   
19. The following code has an error
    ```java
    public static void main(String[] args) {
        Person person = new Person();
        System.out.println(person.age);
    }

    public class Person {
        private int age;
    }
    ```
    1. True
    2. False
   
20. What will be the values of `a` and `b` printed at the end of the program
    ```java
    int a = 21;
    int b = 1;

    if (a < 10 || (b--) > 0) {
        a = 5;
    }

    System.out.println(a);
    System.out.println(b);
    ```
    1. a = 21, b = 1
    2. a = 5, b = 0
    3. a = 5, b = 1
    4. a = 25, b = 0
   
21. Using `return` in a `void` method is possible
    1. True
    2. False
   
## Java 2

1. What is a _bit_
    1. 8 binary digits (0, 1)
    2. A binary digit (0, 1)
    3. A base 10 digit
    4. An integer
  
2. If we declare a constructor as `private` what will happen
    1. Nothing special, the class can be used normally
    2. Only code in the same package can create instances
    3. Compilation Error
    4. Creating a new instance is only possible within the same class

3. Variables defined as `final` can be modified
    1. True
    2. False
  
4. What is a _byte_
    1. Another name for bit
    2. An integer
    3. 8 bits
    4. KB

5. The purpose of the _RAM_ in a computer is
    1. For storing data (variables)
    2. For storing data (files)
    3. For running code (instructions)
    4. For communication (WIFI/Ethernet)

6. What is a `long` in Java
    1. An 8-byte integer data type
    2. A 4-byte integer data type
    3. An 8-byte floating point data type
    4. 8 bits
  
7. `Math.abs` returns the sign of a given number
    1. True
    2. False
  
8. In Java, how does a class inherit another class
    1. `class Student : Person`
    2. `class Student(Person)`
    3. `class Student extends Person`
    4. `class Student implements Person`

9. What is `a`
    ```java
    public void doSomething() {
        int a = 5;

        while (a > 5) {
    ...
    ```
    1. `a` is a member variable
    2. `a` is a local variable
    3. `a` is a condition
    4. `a` is a declaration

10. A Java file contain a type (Class, etc)
    1. True
    2. False

11. How to declare a _globally constant_ variable
    1. `public static final int VAR = 5;`
    2. `public final int VAR = 5;`
    3. `public static int VAR = 5;`
    4. `public int VAR = 5;`

12. What is the problem with the following code
    ```java
    public static void main(String[] args) {
        double number = HelperClass.calculate(5, 1.1);
        System.out.println(number);
    }

    public class HelperClass {

        private static double calculate(double a, double b) {
            return a + b;
        }
    }
    ```
    1. `calculate` cannot be accessed from main because it is in another class
    2. `calculate` can only be accessed from an instance of `HelperClass`
    3. `calculate` is private and cannot be accessed from main
    4. `calculate` needs to receive two `double` parameters and doesn't

13. What will be the value printed
    ```java
    int a = 11;
    System.out.println(a--);
    ```
    1. 11
    2. 9
    3. 10
    4. 12
   
14. What are exceptions
    1. A mechanism for reporting compile-time errors
    2. A type of data storing unique information for the JVM
    3. A special method for constructing instances of classes
    4. A mechanism for reporting run-time errors

15. What will be the value printed at the end of the program
    ```java
    public static void main(String[] args) {
        int a = 55;
        int c = 10;
        setValue(a, c);
        System.out.println(a);
    }

    static void setValue(int a, int b) {
        a = a / 2;
        b = a / b;
    }
    ```
    1. 10
    2. 55
    3. 2
    4. 27

16. Which of the following is a valid `char` definition
    1. `char a = 25.4;`
    2. `char a = 'A';`
    3. `char a = 5;`
    4. `char a = "A";`

17. Given that `Student` class `extends` `Person` class; the following is possible
    ```java
    Person person = new Student();
    ```
    1. True
    2. False

18. What will be the value printed
    ```java
    int a = 11 / 2;
    System.out.println(a);
    ```
    1. 11
    2. 5
    3. 5.5
    4. 2

19. What would the following code print
    ```java
    int a = 0;
    while ((a++) < 5) {
        System.out.println(a);
    }
    ```
    1. 0, 1, 2 ,3 4, 5
    2. 1, 2, 3, 4, 5
    3. 1, 2, 3, 4, 5, 6
    4. 0, 1, 2, 3, 4, 5, 6

20. What would the following code print
    ```java
    public static void main(String[] args) {
        Person person = new Person();
        person.age = 5;
        doSome(person);
        System.out.println(person.age);
    }

    private static void doSome(Person person) {
        person.age = 10;
    }
    ```
    1. 5
    2. 10
    3. Nothing, will cause runtime error
    4. Nothing, will not compile

21. What would the following code print
    ```java
    public static void main(String[] args) {
        Person person = new Person();
        person.age = 5;
        doSome(person);
        System.out.println(person.age);
    }

    private static void doSome(Person person) {
        person = new Person();
        person.age = 10;
    }
    ```
    1. 5
    2. 10
    3. Nothing, will cause runtime error
    4. Nothing, will not compile

22. The following class will have an error becausse it has not constructor
    ```java
    public class Taxi {
        private String owner;
        private String id;
        private int passengerCount;
        private int farePerKm;
    }
    ```
    1. True
    2. False

23. From where does a Java program start running
    1. From the `run` method
    2. From he `Main` class
    3. From the `main` method
    4. From any method we choose
   
24. In the following code, the statement `i < 10` runs 10 times
    ```java
    for (int i = 0; i < 10; i++) [

    }
    ```
    1. True
    2. False 

25. The keyword `this` refers to
    1. The current class instance
    2. All the class instances
    3. The current parent class instance
    4. he las object used by a JVM instruction

26. The following `String` concatenation will result in
    ```java
    int a = 95;
    int b = 23;
    String s = "(a:" + a + b + ": b)";
    System.out.println(a);
    ```
    1. `"(a: 95 23: b)"`
    2. `"(a: 9523: b)"`
    3. `"(a: 118: b)"`
    4. `"(a: 95 b: 23)"`

27. The following program will print
    ```java
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        doSome(person);
        System.out.println(Arrays.toString(arr));
    }

    private static void doSome(int[] arr) {
        arr[1] = 5;
    }
    ```
    1. 1, 2, 3
    2. 1, 5, 3
    3. 5, 2, 3
    4. 5, 5, 5
   
28. The following is a valid constructor declaration
    ```java
    class Person {
        private String name;

        public Constructor(String name) {
            this.name = name;
        }
    }
    ```
    1. True
    2. False

## Answers

### Java 1

1. 2 
2. 1, 4 
3. 1, 2, 3
4. 3
5. 2
6. 1
7. 2, 4
8. 3
9. 2
10. 4
11. 1
12. 2
13. 2
14. 3
15. 3, 4
16. 2
17. 2
18. 2
19. 1
20. 2
21. 1

### Java 2

1. 2
2. 4
3. 2
4. 3
5. 1
6. 1
7. 2
8. 3
9. 2
10. 1
11. 1
12. 3
13. 1
14. 4
15. 2
16. 2, 3
17. 1
18. 2
19. 2
20. 2
21. 1
22. 2
23. 3
24. 2
25. 1
26. 2
27. 2
28. 2
