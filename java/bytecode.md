You may already be aware of the fact that _Java_ code runs on the _Java Virtual Machine_. And if you are familiar with other 
languages, you may also be aware that other languages do not have their own _Virtual Machine_. Well, this is not entirely true, _C#_ has its own equivalent, but languages like _C_ or _Rust_, which we call _native_ languages, do not.

So why is that? We are here to day to discuss this exact question.

> [!NOTE]
> There are several things that make _Java_ code largely platform-independent. But we are here
> to talk about _bytecode_ specifically, and will omit irrelevant things.

## The Problem

If you've ever learned about the internals of how programming and code works, you might be familiar with the concept of _Machine Code_. This term
refers to the fact that computer processors (_CPUs_) do not run code as a text, but rather run code after it was transformed into _Machine Code_. And this
_Machine Code_ is in fact just a set of binary data representing what actions the _CPU_ has to perform.

But _Machine Code_ can be very different between two different _CPUs_. It is different enough that _Machine Code_ written for one may not work at all on another.

The _Machine Code_ for performing an adding of `1 + 1` on _Intel_'s _x86_ architecture and on _ARM_'s _aarch64_ looks quite different
```
// Intel x86: mov rax, 1
48 83 c0 01

// Arm AArch64: ADD X0, X1, X2
20 00 02 8b 
```

### CPU Architecture

A processor is basically a large set of logic circuits connected to each other and other components. It is capable of taking instructions and 
executing them one by one. Its architecture is the design and capabilities of a processor, which determines its performance and how it executes  software through its instruction set. So when a company makes a processor, they typically come up with their own architecture. And over time they are also likely to modify this architecture or switch to a new one entirely. 

Some examples for architectures include
- _Intel_'s _x86_/_x86-64_ - common on desktop machines
- _Arm_'s _armv7_ - common on smartphones
- _Arm_'s _aarch64_ - common on smartphones
- _Apple_'s _PowerPC_ - common on apple products
- And more

Then although processors seem very similar, internally, at the lowest levels, there are many differences. Both in what instructions they are capable to perform and how they do it.

If we look back at the _Machine Code_ example I gave above, we can see that not only was the _Machine Code_ different between the architecture, but
there was also a difference in the behaviour of the instructions:
- _x86_'s instruction was `mov rax, 1` which basically does `rax += 1`
- _aarch64_'s instruction was `add x0, x1, x2` which basically does `x0 = x1 + x2`

So the two instruction sets are very different from one another, and thus the code will be very different.

The lowest level text language is called _assembly_, which is what we saw next to the _Machine Code_. It is actually not a single language, but refers to multiple different subsets. What's common
between them is that they are all basically human-readable versions of _Machine Code_. So in _assembly_ if I want to perform addition I use the instruction named `add`, 
while in _Machine Code_ it is represented by a number (also known as _opcode_). Transforming _assembly_ into _Machine Code_ is done with the _assembler_ tool.

But of course, because of the difference of architectures and instruction sets, assembly would be written differently for each processor, and would result in different _Machine Code_. Meaning that I must write my code to suit the specific processor I am to use.

Here are some example of a function adding two numbers in various instruction sets
```
// original c code
int add(int num1, int num2) {
    return num1 + num2;
}

// x86-64
add(int, int):
  lea eax, [rdi + rsi]
  ret

// armv7-a
add(int, int):
  add r0, r1, r0
  bx lr

// aarch64
add(int, int):
  add w0, w1, w0
  ret

// AVR
add(int, int):
.L__stack_usage = 0
  add r24,r22
  adc r25,r23
  ret

// mips
add(int, int):
$func_begin0 = $tmp0
  jr $ra
  addu $2, $5, $4
```

### Higher Level Languages

Writing code in _assembly_ is better than writing in _Machine Code_, but even so it is not the easiest. So it is no suprise that higher level languages like _C_ came along, and eventually also _Java_.

When we take text code of a _native_ language like _C_ and want to transform it into _Machine Code_, it has to go through two processes:
- Compiling: transform the code into assembly instructions
- Assembling: transforms the assembly instructions into machine code

Because we know that the _assembly_ and _Machine Code_ are platform (hardware, OS) specific, we can immediatly guess that when compiling _native_ languages, the resulting binary can only run on a specific set of machines. And to get my code to work on other machines I'll have to compile more binaries, each for each machine. 

So although my _native_ code may be the same, the resulting binary is going to be very different.
```cpp
// c code
int main() {
    printf("Hello World\n");
    return 0;
}

// x86-64
main:
  push rax
  lea rdi, [rip + .Lstr]
  call puts@PLT
  xor eax, eax
  pop rcx
  ret
.Lstr:
  .asciz "Hello World"

// armv7-a
main:
  push {r11, lr}
  ldr r0, .LCPI0_0
.LPC0_0:
  add r0, pc, r0
  bl puts
  mov r0, #0
  pop {r11, pc}
.LCPI0_0:
  .long .Lstr-(.LPC0_0+8)
.Lstr:
  .asciz "Hello World"
```

## What Java Does

_Java_ has this slogen: write once, run everywhere. And part of the reason it is true, is that the creators of _Java_ found a way to compile _Java_ into a binary that is not specific to hardware (_cpu architecture_). And this is done with the help of the _Java Virtual Machine_.

Consider: what if instead of so many different _assemblies_ and _Machine Code_ we had one unified version for all machines? And that is what the _Java Bytecode_ is.

### Bytecode

The result of _Java_ code compilation is not an _assembly_ like we'd expect from _native_ languages, but rather it takes the format of the _Java Bytecode_.

This _Bytecode_ is an intermidiate representation of _Java_ code, not unlike what _assembly_ is to _C_. But it is not hardware specific. So the _Bytecode_ will look exactly the same for all machines we'd run it for, meaning that we only need to compile once.

Let's look at an example
```
// Java code
public int add(int num1, int num2) {
    return num1 + num2;
}

// Bytecode
iload_1
iload_2
iadd
ireturn
```

Unfortunately this is not the end of the matter, because _CPUs_ cannot actually run _Java Bytecode_. It is an unknown to them. So for all the greatness of having the same _Bytecode_ for all machines, it doesn't just magically work, the proper _Machine Code_ is still necessary, and that's where the _JVM_ steps in.

When a _Java_ binary is executed (like a _jar_ file), it doesn't immediatly just run on the _CPU_ like a native program, but rather the _JVM_ is launched with it. The _JVM_ will then be resposible of executing the code. This is done by using a _Just In Time_ (JIT) compiler: when a set of _Bytecode_ is executed for the first time, the _JVM_ will transform the _Bytecode_ into the appropriate _Machine Code_ for the machine that _JVM_ is running on, and then give it to the processor for execution. 

We didn't get rid of _Machine Code_, we just changed when it is created. Because the target machine is always known when the binary runs (it runs on the target machine). And thus, the same _Java Bytecode_ will work for any machine. It is the _JVM_ that is made differently for each machine, not the code.

Despite all this, _bytecode_ is remarkably similar to assembly in structure and syntax. This was likely an intentional choice, simplifiying the work necessary to convert it into true _Machine Code_. And it was never meant to be actually manually written by a programmer, that's what the compiler is for.

There are several characteristics of _bytecode_ we should understand:
- It is instruction-based. Meaning that actual code is divided into single instructions, each performing a very specific action. Very much like any assembly language. The difference here is that these instructions aren't hardware-based and tied to any _CPU architecture_. This leads to
  - there being many instructions available. This is discouraged in hardware because of complexity. but for an intermidiate, hardware-independent language, it is not a serious limitation.
  - the instructions not being attached to specific hardware components like an _FPU_ (_Floating Point Unit_ responsible for performing `float` and `double` operations). Such attachments will only be made when translating to specific _machine code_.
  - lack of instructions dealing with low level processor operations, like _interrupts_, IO and registers. This of course also prevents using _Java_ code to interact with these directly, requiring the use of a lower level language.
- It is stack-oriented. All operations are done on the stack without the use of any registers. This makes sense as being hardware-independent makes it difficult to use registers which are very hardware-specific.
  - this is not to say that registers won't be used after compilation into _machine code_. It is just that as far as writing _bytecode_ is concerned, they are not a thing.
  - this also simplifies and shrinks the amount of instructions necessary, as everything works on the stack, so no need for instructions that also support multiple sources (like all the registers as well as memory).
  - It is important to understand that this is not entirely the same thing to when we discuss stack memory of a program. It is called the operand stack and used to do operations. The real stack will be used in the end, but is referenced in the resulting _Machine Code_.
- It retains the oop structure of the original code. This may be unexpected, but the _JVM_ is largely oriented on OOP.
  - when looking at full _bytecode_ of a class one can see that it is divided by methods and classes. We've only seen the contents of methods so far and not discussed the surrounding information
  - each `.java` file is basically compiled into a `.class` file which contains the _bytecode_.
 
You can find full list of instructions [here](https://en.wikipedia.org/wiki/List_of_Java_bytecode_instructions) and [here](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html)

#### Example Program

Let us analyze an example of _bytecode_ to understand what it does
```java
// Type your code here, or load an example.
class Main {
    static void func1() {
        int res = add(10, 12);
    }

    static int add(int num1, int num2) {
        return num1 + num2;
    }
}
```

```
class Main {
  Main();
       0: aload_0
       1: invokespecial #1 // Method java/lang/Object."<init>":()V
       4: return


  static void func1();
       0: bipush        10
       2: bipush        12
       4: invokestatic  #2 // Method add:(II)I
       7: istore_0
       8: return


  static int add(int, int);
       0: iload_0
       1: iload_1
       2: iadd
       3: ireturn
}
```

Take a look at the `func` method. It simply calls `add` with `10` and `12` as parameters and stores the return value. This single line is represented by 4 _bytecode_ instructions:
- `bipush 10`: push the value 10 unto the stack.
  - This places the value as the first parameter of the impending method call 
- `bipush 12`: push the value 12 unto the stack
  - This places the value as the second parameter of the impending method call
- `invokestatic #2`: call method `add` (represented by `#2`)
  - this executes the method
  - `invokestatic` is used specifically for `static` methods
  - because we are entering a new method, a transition is performed
    - the stack top is loaded into the parameters of the function (in this case `num1` and `num2` from the things we pushed earlier)
    - when we return from the method, the return value will be pushed unto the stack 
- `istore_0` load the return value from the stack into local variable
  - the return value from the method call is placed on the stack, and will be at the top
  - the instruction will pop this value from the top of the stack
  - `_0` in the instruction indicates that the value should be loaded unto local variable number `0`, whhich is `res` in our case. In _bytecode_ local variables are their own thing. In _Machine Code_ they would be stored on the stack or in a register.
 
Let's look at the `add` function, which is made up of a single line of addition.
- `iload_0`: push the value of local variable 0 (`num1`) unto the stack. 
- `iload_1`: push the value of local variable 1 (`num2`) unto the stack. 
- `iadd`: perform addition between the 2 top-most values in the stack, pop them, and push the result unto the stack
- `ireturn`: return from the function with an `int` from the top of the stack

You can try looking at _bytecode_ yourselves in [compiler explorer](https://godbolt.org/). Select _Java_ as the language, write you code and see the resulting _bytecode_.
