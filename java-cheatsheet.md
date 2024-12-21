
### Variables

```java
[ data_type ] variable_name;
[ data_type ] variable_name = initial_value;

// declaring an int variable named num with the value 5
int num = 5;

// declaring a double variable named value without initial value defined.
double value;
// assigning 22.4 into variable value
value = 22.4;
```

### Primitive Data Types

| name | used for | size |
|------|----------|------|
| `byte` | integers | 1 bytes |
| `short` | integers | 2 bytes |
| `int` | integers | 4 bytes |
| `long` | integers | 8 bytes |
| `float` | decimals | 4 bytes |
| `double` | decimals | 8 bytes |
| `char` | characters | 2 bytes |
| `boolean` | boolean | 1 bytes |

### Arithmetic Operators

| operation | name |
|-----------|------|
| `num1 + num2` | addition |
| `num1 - num2` | subtraction |
| `num1 * num2` | multiplication |
| `num1 / num2` | division |
| `num1 % num2` | modulus |
| `num1++` | post increment |
| `++num1` | pre increment |
| `num1--` | post decrement |
| `--num1` | pre decrement |

### Assignment Operators

| operation | full form | name |
|-----------|-----------|------|
| `num1 = value` | | assignment |
| `num1 += num2` | `num1 = num1 + num2` | addition |
| `num1 -= num2` | `num1 = num1 - num2` | subtraction |
| `num1 *= num2` | `num1 = num1 * num2` | multiplication |
| `num1 /= num2` | `num1 = num1 / num2` | division |
| `num1 %= num2` | `num1 = num1 % num2` | modulus |

### Comparison Operators

| operation | name |
|-----------|------|
| `num1 == num2` | equal to |
| `num1 != num2` | not equal |
| `num1 > num2` | greater than |
| `num1 < num2` | less than |
| `num1 >= num2` | greater than or equal |
| `num1 <= num2` | less than or equal |

### Logical Operators

| operation | name |
|-----------|------|
| `boolean1 && boolean 2` | logical AND |
| `boolean1 \|\| boolean2` | logical OR |
| `!boolean1` | logical NOT |

### Binary Operators

| operation | name |
|-----------|------|
| `num1 & num2` | bitwise AND |
| `num1 \| num2` | bitwise OR |
| `num1 ^ num2` | bitwise XOR |
| `~num1` | bitwise complement |
| `num1 >> num2` | right shift |
| `num1 << num2` | left shift |
| `num1 >>> num2` | unsigned right shift |

### Type Conversion

```java
// type casting
double val = 25.2;
int a = (int) val; // will be 25

// to string conversion
int number = 24;
String str = String.valueOf(number);
// or with string concat: "hello: " + number

// string to int
String str = "25";
int val = Integer.parseInt(str);

// string to double
String str = "25.5";
double val = Double.parseDouble(str);
```

### Literals

```java
// integers
int num1 = 34; // base 10 (decimal)
int num2 = 042; // base 8 (octal)
int num3 = 0x22; // base 16 (hexadecimal)
int num4 = 0b00100010; // base 2 (binary)
long value = 2211153122345L;

// decimals
float value = 155.4f;
double value = 155.4;
double value = 1.554e2; // exponent form

// char, String
char a = 'A';
char a = '\u0021';
char a = 67;
String b = "Hello World";
```

### If Statement

```java
if ( condition ) {
    statements
} else if ( condition ) {
    statements
} else {
    statements
}
```

Example
```java
if (num1 > 0) {
    System.out.println("Number is positive");
} else if (num1 < 0) {
    System.out.println("Number is negative");
} else {
    // num1 == 0
    System.out.println("Number is zero");
}
```

### Switch Statement

```java
switch ( condition ) {
    case value:
        statements
        break;
    case value2:
        statements
        break;
    default:
        statements
        break;
}
```

Example
```java
String monthName
switch (monthNumber) {
    case 1:
        monthName = "January";
        break;
    case 2:
        monthName = "February";
        break;
    default:
        monthName = "Unknown";
        break;
}
```

### For Loop

```java
for ( initialize ; condition ; increment ) {
    statements
}
```

Example
```java
// run from 0 to 9 (inclusive)
for (int i = 0; i < 10; i++) {

}

// run from 9 to 0 (inclusive)
for (int i = 9; i >= 0; i--) {

}
```

### While Loop

```java
while ( condition ) {
    statements
}
```

Example
```java
// run from 0 to 5
int num = 0;
while (num <= 5) {
    num++;
}

// example, infinite loop
while (true) {

}
```

### Do-While Loop

```java
do {
    statements
} while ( condition );
```

Example
```java
// example
int a = 0;
do {
    a++
} while(a < 5);
```

### 1D Arrays

```java
element_type[] variable_name; 
element_type[] variable_name = new element_type [ array_size ] ;
element_type[] variable_name = { value1, value2, value3, ... };

element_type name = variable_name [ index ];
variable_name [ index ] = value;

// example
// declaring and initializing a new empty array of type int and size 10. array will be stored in the variable arr
int[] arr = new int[10];
// declaring and initializing a new array of type double. array will be stored in the variable nums. array elements will have the values specified and be of size 3.
double[] nums = {22.1, 50, 34};

// saving element in array arr at index 0 into variable a
int a = arr[0];
// saving value 53 into element in array arr at index 0
arr[0] = 53;

// iteration, full array, from start to end, printing each value in the array
for (int i = 0; i < arr.length; i++) {
    int element = arr[i];
    System.out.println(element);
}
```

### Methods

```java
[modifiers] return-type name ( parameters ) {
    statements
}

// modifiers
[ access-modifier ] [ static / abstract ] [ synchronized ] [ final ]

// examples

// method named printName, returning nothing (void), no parameters, public access, static
public static void printName() {
    System.out.println("name");
}

// method named showNumbers, returning nothing (void), parameters a (int) and b (int), public access
public void showNumbers(int a, int b) {
    System.out.println("a=" + a + ", b=" + b);
}

// method named sum, returning int, parameters a (int) and b (int), public access
public int sum(int a, int b) {
    return a + b; // return from method the sum of a+b
}
```

The Main method
```java
public static void main(String[] args) {

}
```

Method Invocation
```java
method_name();
method_name( parameters );
return_type name = method_name( parameters );

// call void method named printName, no parameters
printName();
// call method sumNumbers, with parameters 11 (a) and 90 (b), returning an int
int sum = sumNumbers(11, 90);
```

### Math Functions

```java
// basic
double pi = Math.PI; // pi
double val = Math.abs(-15); // 15, absolute value
double val = Math.signum(-15); // -1, sign (-1, 0, 1)
double val = Math.max(12, 13); // 13, max of 2 numbers
double val = Math.min(12, 13); // 12, min of 2 numbers
double val = Math.sqrt(16); // square root  of number
double val = Math.pow(2, 4); // 2 to the power of 4

// round
double val = Math.ceil(15.2); // 16, round up
double val = Math.floor(15.2); // 15, round down
double val = Math.round(15.2); // 15, round to the closest

// trigo
double degrees = Math.toDegrees(Math.PI); // radians to degrees
double radians = Math.toRadians(180); // degrees to radians
double val = Math.sin(radians); // sine func
double val = Math.cos(radians); // cosine func
double val = Math.tan(radians); // tangent func
double radians = Math.asin(value); // arc sine func
double radians = Math.acos(value); // arc cosine func
double radians = Math.atan(value); // arc tangent func
```

### Classes

General Structure
```java
public class name {

    members

    constructors

    methods
}
```

Example structure
```java
public class name {

    private type var1;
    private type var2;

    public name ( type var1, type var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    public return_type method1 ( type param1, type param2 ) {
        statements
    }

    public return_type method2 ( params ) {
        statements
    }
}
```

Example
```java
public class Employee {

    private String name;
    private int age;
    private double salary;

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    public void raiseSalary(double percentage) {
        salary += salary * percentage;
    }
}
```

Object usage
```java
// creation
Class_Name var_name;
Class_Name var_name = new Class_Name();
Class_Name var_name = new Class_Name( parameters );

// declare variable for storing an instance of Person
Person person;
// declare variable for storing an instance of Person and assigning it a new instance of Person.
// the new instance receives the parameters "Barak Obama", 25 into the constructor 
Person person = new Person("Barak Obama", 25);

// access
type name = var_name.attribute;
var_name.attribute = value;

// save value of attribute age from instance in person variable, into variable a
int a = person.age;
// save value 55 into attribute age of the instance in person variable
person.age = 55;

// method invocation
var_name.method_name();
var_name.method_name( parameters );
return_type name = var_name.method_name( parameters );

// call method getName of the instance in variable person. method returns String
String name = person.getName();
// call method walkDistance of instance in variable person. method returns void, receives 5 into parameter. 
person.walkDistance(5);
```

### Modifiers

| modifier | meaning |
|----------|---------|
| `final` | Cannot be overriden or extended |
| `static` | Belongs to the class, rather than an instance |
| `abstract` (method) | Declares the method as abstract, requiring no body |
| `abstract` (class) | Declares the class as abstract, can contain abstract methods |
| `synchronized` | Holds monitor on class instance during method execution |
| `transient` | Attributes and methods are skipped when serial­izing the object containing them (via java object serial­iza­tion) |
| `volatile` | The value of the attribute is not cached on CPU |
	
Access Modifiers

| modifier | accessable to |
|----------|---------|
| `public` | Any code |
| `private` | Only code in the same class |
| `protected` | Only code in the same class, inheriting class or the same package |
| packag­e-p­rivate (default, set no modifier) | Only code in the same package

